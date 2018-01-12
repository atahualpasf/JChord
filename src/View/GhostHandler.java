/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import Controller.Util;
import Model.Node;
import Model.StandardObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class GhostHandler extends Thread {
    private final Socket ghostClient;
    private StandardObject clientRequest = null;
    private StandardObject serverRequest = null;
    private ObjectInputStream objectFromClient = null;
    private ObjectOutputStream objectToClient = null;
    private List<Node> ring = null;

    public GhostHandler(Socket ghostClient, List<Node> ring) {
        this.ghostClient = ghostClient;
        this.ring = ring;
    }
    
    @Override
    public void run()   
    {
        try {
            objectFromClient = new ObjectInputStream(ghostClient.getInputStream());
            objectToClient = new ObjectOutputStream(ghostClient.getOutputStream());
            while (true) {
                clientRequest = (StandardObject) objectFromClient.readObject();
                System.out.println(clientRequest);
                String protocol[] = clientRequest.getProtocol().split(Util.DELIMETER);
                if (protocol[1] != null) {
                    switch(protocol[1]) {
                        case "JOIN":
                            Node newNode = (Node) clientRequest.getObject();
                            ring.add(newNode);
                            
                            Collections.sort(ring, (a,b) -> a.getKey().compareTo(b.getKey()));
                            int sizeOfRing = ring.size();
                            // El anillo estaba vacío, ahora está únicamente el nuevo | PRE y SUC son vacíos
                            if (sizeOfRing == 1) {
                                serverRequest = new StandardObject(newNode, true);
                            } else {
                                int newIndex = ring.indexOf(newNode);
                                newNode.setPredecessor(ring.get((newIndex-1 < 0) ? newIndex : newIndex-1));
                                newNode.setSuccessor(ring.get((newIndex+1 > sizeOfRing) ? newIndex : newIndex+1));
                                serverRequest = new StandardObject(newNode, true);
                            }
                            objectToClient.writeObject(serverRequest);
                            //objectToClient.writeObject("ENTRO");
                            break;
                        case "LEAVE":
                            //JChordGhostController.leaveRing();
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
