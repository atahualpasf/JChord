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
                            Data.getMyNode().setFingerTable(fingerTable);
                            break;
                        case "LOOKUP":
                            Node nodeToReply = (Node) clientRequest.getObject();
                            Archive archiveToLookup = (Archive) ((StandardObject) objectFromClient.readObject()).getObject();
                            if (Data.getMyNode().getKey() < archiveToLookup.getKey()) {
                                System.out.println("Es menor");
                                try {
                                    Node nodeToAsk = Data.getMyNode().getFingerTable().lowerEntry(archiveToLookup.getKey()).getValue();
                                    Socket connection = new Socket(nodeToAsk.getIp(), nodeToAsk.getPort());
                                    ObjectOutputStream outputObject = new ObjectOutputStream(connection.getOutputStream());
                                    ObjectInputStream inputObject = new ObjectInputStream(connection.getInputStream());
                                    StandardObject request = new StandardObject(nodeToReply, true)
                                            .buildProtocol(Arrays.asList("4","LOOKUP"));
                                    outputObject.writeObject(request);
                                    request = new StandardObject(archiveToLookup, true);
                                    outputObject.writeObject(request);
                                    connection.close();
                                } catch (IOException ex) {
                                    Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                System.out.println("Soy el que tal");
                            }
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
