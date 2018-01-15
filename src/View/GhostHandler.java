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

/**
 * Clase que se encarga de manejar las operaciones que puede realizar el nodo
 * fantasma.
 * 
 * @author Atahualpa Silva F.
 * @link https://github.com/atahualpasf
 * <br> 
 * @author Andrea L. Contreras D.
 * @link https://github.com/andrecontdi
 */
public class GhostHandler extends Thread {
    private final Socket ghostClient;
    private StandardObject clientRequest = null;
    private StandardObject serverReply = null;
    private ObjectInputStream objectFromClient = null;
    private ObjectOutputStream objectToClient = null;
    private List<Node> ring = null;

    /**
     * Constructor de la clase.
     * 
     * @param ghostClient Socket que identifica al cliente que se quiere
     * conectar.
     * @param ring Anillo al que se quiere conectar.
     */
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
            Util.showMessage(3, 3, GhostHandler.class.getSimpleName(), "ClassNotFoundException " + e.getMessage()); 
        } catch(IOException e) {
            Util.showMessage(3, 3, GhostHandler.class.getSimpleName(), "IOException " + e.getMessage());
        } catch (InterruptedException ex) {
            Util.showMessage(3, 3, GhostHandler.class.getSimpleName(), "InterruptedException " + ex.getMessage());
        }
    }
}
