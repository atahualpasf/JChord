/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import Controller.GhostHandlerController;
import Controller.Util;
import Model.Node;
import Model.StandardObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class GhostHandler extends Thread {
    private final Socket ghostClient;
    private StandardObject clientRequest = null;
    private StandardObject serverReply = null;
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
                String protocol[] = clientRequest.getProtocol().split(Util.CMD_DELIMETER);
                if (protocol[1] != null) {
                    switch(protocol[1]) {
                        case "JOIN":
                            GhostHandlerController.joinRing(clientRequest, ring, objectToClient);
                            Thread.sleep(Util.THREAD_TIMEOUT);
                            GhostHandlerController.notifyRingNodes(ring);
                            break;
                        case "LEAVE":
                            GhostHandlerController.leaveRing(clientRequest, ring, objectToClient);
                            Thread.sleep(Util.THREAD_TIMEOUT);
                            GhostHandlerController.notifyRingNodes(ring);
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
        } catch (InterruptedException ex) {
            Logger.getLogger(GhostHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
