/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import Model.Node;
import Model.StandardObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class GhostHandlerController {
    public static void joinRing(StandardObject clientRequest, List<Node> ring, ObjectOutputStream objectToClient) {
        try {
            StandardObject serverReply;
            Node newNode = (Node) clientRequest.getObject();
            ring.add(newNode);
            
            Collections.sort(ring, (a,b) -> a.getKey().compareTo(b.getKey()));
            int sizeOfRing = ring.size();
            // El anillo estaba vacío, ahora está únicamente el nuevo | PRE y SUC son vacíos
            if (sizeOfRing == 1) {
                serverReply = new StandardObject(newNode, true);
            } else {
                int newIndex = ring.indexOf(newNode);
                Node updatedNode = new Node(newNode);
                updatedNode.setPredecessor(ring.get((newIndex == 0) ? sizeOfRing-1 : newIndex-1));
                updatedNode.setSuccessor(ring.get((newIndex+1 == sizeOfRing) ? 0 : newIndex+1));
                serverReply = new StandardObject(updatedNode, true);
            }
            objectToClient.writeObject(serverReply);
        } catch (IOException ex) {
            Logger.getLogger(GhostHandlerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void leaveRing(StandardObject clientRequest, List<Node> ring, ObjectOutputStream objectToClient) {
        try {
            StandardObject serverReply;
            Node oldNode = (Node) clientRequest.getObject();
            ring.remove(oldNode);
            String command = null;
            if (ring.size() == 1) {
                command = "ONLYONE";
            } else if (ring.isEmpty()) {
                command = "EMPTY";
            }
            serverReply = new StandardObject(command, oldNode, true);
            objectToClient.writeObject(serverReply);
        } catch (IOException ex) {
            Logger.getLogger(GhostHandlerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
