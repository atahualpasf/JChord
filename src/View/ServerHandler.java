/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import Controller.Data;
import Controller.Util;
import Model.Node;
import Model.StandardObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
                String protocol[] = clientRequest.getProtocol().split(Util.DELIMETER);
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
                        case "3":
                            //JChordGhostController.saveArchives();
                            break;
                        default:
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
