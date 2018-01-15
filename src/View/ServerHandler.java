/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import Controller.Data;
import Controller.Util;
import Model.Archive;
import Model.Node;
import Model.StandardObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class ServerHandler extends Thread {
    private final Socket nodeClient;
    private StandardObject clientRequest = null;
    private StandardObject serverReply = null;
    private ObjectInputStream objectFromClient = null;
    private ObjectOutputStream objectToClient = null;
    
    public ServerHandler(Socket nodeClient) {
        this.nodeClient = nodeClient;
    }
    
    @Override
    public void run()   
    {
        try {
            objectFromClient = new ObjectInputStream(nodeClient.getInputStream());
            objectToClient = new ObjectOutputStream(nodeClient.getOutputStream());
            while (true) {
                clientRequest = (StandardObject) objectFromClient.readObject();
                System.out.println(clientRequest);
                String protocol[] = clientRequest.getProtocol().split(Util.CMD_DELIMETER);
                if (protocol[1] != null) {
                    switch(protocol[1]) {
                        case "JOIN":
                            Node newNode = new Node((Node) clientRequest.getObject());
                            switch(protocol[2]) {
                                case "PREDECESSOR":
                                    Data.getMyNode().setSuccessor(newNode);
                                    break;
                                case "SUCCESSOR":
                                    Data.getMyNode().setPredecessor(newNode);
                                    break;
                            }
                            break;
                        case "LEAVE":
                            Node oldNode = (Node) clientRequest.getObject();
                            switch(protocol[2]) {
                                case "PREDECESSOR":
                                    Data.getMyNode().setSuccessor(oldNode.getSuccessor());
                                    break;
                                case "SUCCESSOR":
                                    Data.getMyNode().setPredecessor(oldNode.getPredecessor());
                                    break;
                                case "ONLYONE":
                                    Data.getMyNode().clearPredecessor();
                                    Data.getMyNode().clearSuccessor();
                                    break;
                            }
                            break;
                        case "FIXFINGERS":
                            TreeMap<Integer,Node> fingerTable = (TreeMap<Integer,Node>) clientRequest.getObject();
                            TreeMap<Archive,List<Node>> filesTable = (TreeMap<Archive,List<Node>>) ((StandardObject) objectFromClient.readObject()).getObject();
                            Data.getMyNode().setFingerTable(fingerTable);
                            Data.getMyNode().setRemoteFilesTable(filesTable);
                            break;
                        case "LOOKUP":
                            Node nodeToReply = (Node) clientRequest.getObject();
                            Archive archiveToLookup = (Archive) ((StandardObject) objectFromClient.readObject()).getObject();
                            if (Data.getMyNode().getKey() < archiveToLookup.getKey()) {
                                System.out.println("Es menor");
                                try {
                                    Node nodeToAsk = Data.getMyNode().getFingerTable().lowerEntry(archiveToLookup.getKey()).getValue();
                                    Socket connection = new Socket(nodeToAsk.getIp(), nodeToAsk.getPort());
                                    connection.setSoTimeout(Util.SOCKET_TIMEOUT);
                                    ObjectOutputStream outputObject = new ObjectOutputStream(connection.getOutputStream());
                                    StandardObject request = new StandardObject(nodeToReply, true)
                                            .buildProtocol(Arrays.asList("4","LOOKUP"));
                                    outputObject.writeObject(request);
                                    request = new StandardObject(archiveToLookup, true);
                                    outputObject.writeObject(request);
                                    connection.shutdownOutput();
                                } catch (IOException ex) {
                                    Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                System.out.println("Soy el que tal");
                                if (Data.getMyNode().getLocalFiles().contains(archiveToLookup) || 
                                        Data.getMyNode().getRemoteFilesTable().containsKey(archiveToLookup)) {
                                    System.out.println("Yo lo tengo menor o yo te informo");
                                    //SendFile sendFile = new SendFile(nodeToReply, archiveToLookup);
                                    //sendFile.start();
                                } else {
                                    StandardObject request = new StandardObject(archiveToLookup,true)
                                            .buildProtocol(Arrays.asList("5","FILENOTFOUND"));
                                    Socket connection = new Socket(nodeToReply.getIp(), nodeToReply.getPort());
                                    ObjectOutputStream outputObject = new ObjectOutputStream(connection.getOutputStream());
                                    outputObject.writeObject(request);
                                    connection.shutdownOutput();
                                }
                            }
                            break;
                        case "FILENOTFOUND":
                            Archive archive = (Archive) clientRequest.getObject();
                            System.out.print(Util.ANSI_BLUE + "INFORMACIÓN:" + Util.ANSI_RESET + " Cliente -> " + nodeClient.getInetAddress().getHostAddress() + ":" + nodeClient.getPort() + " dice: ");
                            System.out.println("Disculpe pero el archivo " + Util.ANSI_BLUE + archive.getName() + Util.ANSI_RESET +
                                    " no se encuentra en el anillo.");
                            break;
                        case "SENDFILE":
                            System.out.println("Entro a SENDFILE");
                            Node nodeWannaFile = (Node) clientRequest.getObject();
                            Archive archiveToSend = (Archive) ((StandardObject) objectFromClient.readObject()).getObject();
                            StandardObject request = new StandardObject(archiveToSend,true)
                                .buildProtocol(Arrays.asList("7","DOWNLOAD"));
                            Socket socket = new Socket(nodeWannaFile.getIp(),nodeWannaFile.getPort());
                            socket.setSoTimeout(Util.SOCKET_TIMEOUT_DOWNLOAD);
                            ObjectOutputStream objectToSend = new ObjectOutputStream(socket.getOutputStream());
                            ObjectInputStream objectToReceive = new ObjectInputStream(socket.getInputStream());
                            objectToSend.writeObject(request);
                            SendFile sendFile = new SendFile(socket, objectToSend, objectToReceive, nodeWannaFile, archiveToSend);
                            sendFile.start();
                            break;
                        case "DOWNLOAD":
                            System.out.println("Entro a DOWNLOAD");
                            Archive archiveToDownload = (Archive) clientRequest.getObject();
                            ReceiveFile receiverFile = new ReceiveFile(this.nodeClient, objectFromClient, archiveToDownload);
                            receiverFile.start();
                            break;
                        default:
                            System.out.println("Nei");
                            break;
                    }
                }
            }
        }
        catch (ClassNotFoundException e) {
            System.out.print(Util.ANSI_RED + "EXCEPTION: ClassNotFoundException ");
            System.out.println(Util.ANSI_RESET + e.getMessage());
        } catch(IOException e) {
            System.out.print(Util.ANSI_RED + "EXCEPTION: IOException ");
            System.out.println(Util.ANSI_RESET + e.getMessage());
        }
    }
}
