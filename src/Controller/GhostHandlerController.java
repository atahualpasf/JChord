/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import Model.Archive;
import Model.Node;
import Model.StandardObject;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
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
                updatedNode.setPredecessor(new Node(ring.get((newIndex == 0) ? sizeOfRing-1 : newIndex-1), true));
                updatedNode.setSuccessor(new Node(ring.get((newIndex+1 == sizeOfRing) ? 0 : newIndex+1), true));
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
            Collections.sort(ring, (a,b) -> a.getKey().compareTo(b.getKey()));
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
    
    public static void notifyRingNodes(List<Node> ring) {
        StandardObject ghostRequest;
        try {
            for ( Node node : ring ) {
                
                TreeMap<Integer,Node> fingerTable = buildFingerTable(ring, node);
                ghostRequest = new StandardObject(fingerTable, true)
                        .buildProtocol(Arrays.asList("3","FIXFINGERS"));
                Socket nodeToNotify = new Socket(node.getIp(), node.getPort());
                nodeToNotify.setSoTimeout(Util.SOCKET_TIMEOUT);
                ObjectOutputStream objectToClient = new ObjectOutputStream(nodeToNotify.getOutputStream());
                objectToClient.writeObject(ghostRequest);
                
                TreeMap<Archive,List<Node>> filesTable = buildFilesTable(ring, node);
                ghostRequest = new StandardObject(filesTable, true);
                objectToClient.writeObject(ghostRequest);
            }
        } catch (IOException ex) {
            Logger.getLogger(GhostHandlerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static TreeMap<Integer,Node> buildFingerTable(List<Node> ring, Node nodeToAddFT) {
        TreeMap<Integer,Node> fingerTable = new TreeMap<>();
        int base = 2;
        for ( int i = 1 ; i <= Util.M_BITS ; i++) {
            int key = (nodeToAddFT.getKey() + (int) Math.pow(base, i-1)) % Util.MAX_NODES;
            Node nodeToPoint = null;
            for ( Node node : ring ) {
                if (node.getKey() >= key) {
                    nodeToPoint = new Node(node, true);
                    break;
                }
            }            
            if (nodeToPoint == null)
                nodeToPoint = new Node(ring.get(0), true);
            
            fingerTable.put(key, nodeToPoint);
        }        
        return fingerTable;
    }
    
    private static TreeMap<Archive,List<Node>> buildFilesTable(List<Node> ring, Node nodeToAddFT) {
        TreeMap<Archive,List<Node>> filesTable = new TreeMap<>();
        int sizeOfRing = ring.size();
        if (sizeOfRing > 1) {
            int newIndex = ring.indexOf(nodeToAddFT);
            int keyNode = nodeToAddFT.getKey();
            int keyPredecessor = ring.get((newIndex == 0) ? sizeOfRing-1 : newIndex-1).getKey();
            ring.forEach((node) -> {
                node.getLocalFiles().forEach((archive) -> {
                    int keyArchive = archive.getKey();
                    if (keyPredecessor > keyNode) {
                        if (keyArchive > keyPredecessor || keyArchive <= keyNode) {
                            addToFilesTable(filesTable, archive, node);
                        }
                    } else {                        
                        if (keyArchive > keyPredecessor && keyArchive <= keyNode) {
                            addToFilesTable(filesTable, archive, node);
                        }
                    }
                });
            });
            return filesTable;
        }
        return null;
    }
    
    private static void addToFilesTable(TreeMap<Archive,List<Node>> filesTable, Archive archive, Node node) {
        if (!filesTable.containsKey(archive)) {
            Node tempNode = new Node(node, true);
            List<Node> listNode = new ArrayList<>();
            listNode.add(tempNode);
            filesTable.put(archive, listNode);
        } else {
            Node tempNode = new Node(node, true);
            filesTable.get(archive).add(tempNode);
        } 
    }
}
