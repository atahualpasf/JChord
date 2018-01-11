/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import Controller.JChordGhostController;
import Controller.Util;
import Model.StandardObject;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class GhostHandler extends Thread {
    private final Socket ghostClient;
    private StandardObject clientRequest = null;
    private ObjectInputStream objectFromClient = null;
    private ObjectOutputStream objectToClient = null;

    public GhostHandler(Socket ghostClient) {
        this.ghostClient = ghostClient;
    }
    
    @Override
    public void run()   
    {
        try {
            objectFromClient = new ObjectInputStream(ghostClient.getInputStream());
            objectToClient = new ObjectOutputStream(ghostClient.getOutputStream());
            while (true) {
                clientRequest = (StandardObject) objectFromClient.readObject();
                String protocol[] = clientRequest.getProtocol().split(Util.DELIMETER);
                if (protocol[1] != null) {
                    switch(protocol[1]) {
                        case "JOIN":
                            objectToClient.writeObject("ENTRO");
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
        catch(Exception e)
        {
            System.out.print(Util.ANSI_RED + "EXCEPTION: ");
            System.out.println(Util.ANSI_RESET + e.getMessage());
        }
    }
}
