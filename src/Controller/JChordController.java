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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class JChordController {
    private static Socket connection = null;
    private static ObjectOutputStream outputObject = null;
    private static ObjectInputStream inputObject = null;
    
    public static void joinRing(String myIp, Integer myPort) {
        try {
            if (Data.getMyNode() == null) {
                openConnection(Util.GHOST_IP, Util.GHOST_PORT);
                Node me = new Node(myIp, myPort);
                me.setKey(Util.hashCode(me));
                StandardObject request = new StandardObject(me, true)
                        .buildProtocol(Arrays.asList("1","JOIN"));
                outputObject.writeObject(request);
                request = (StandardObject) inputObject.readObject();
                System.out.println(request);
                if (request.isSuccess()) {
                    Data.setMyNode((Node) request.getObject());
                }
                connection.close();
                Node predecessor = Data.getMyNode().getPredecessor();
                Node successor = Data.getMyNode().getSuccessor();
                if (predecessor != null) joinNode(predecessor, true);
                if (successor != null) joinNode(successor, false);
            } else {
                System.out.println(Util.ANSI_RED + "ERROR:" + Util.ANSI_RESET + " Cliente -> Su sesión ya está activa.");
            }
        } catch (IOException ex) {
            Logger.getLogger(JChordController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JChordController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void joinNode(Node nodeToJoin, boolean isPredecessor) {
        try {
            openConnection(nodeToJoin.getIp(), nodeToJoin.getPort());
            Node me = new Node(Data.getMyNode());
            StandardObject request = (isPredecessor) ? 
                    new StandardObject(me, true)
                    .buildProtocol(Arrays.asList("1","JOIN","PREDECESSOR")) : 
                    new StandardObject(me, true)
                    .buildProtocol(Arrays.asList("1","JOIN","SUCCESSOR"));
            outputObject.writeObject(request);
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(JChordController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void openConnection(String ip, Integer port) {
        try {
            connection = new Socket(ip, port);
            outputObject = new ObjectOutputStream(connection.getOutputStream());
            inputObject = new ObjectInputStream(connection.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(JChordController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
