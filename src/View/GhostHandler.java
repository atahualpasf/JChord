/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import Controller.JChordGhostController;
import Controller.Util;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class GhostHandler extends Thread {
    private Socket ghostClient;
    private String clientOption = null;
    private BufferedReader dataFromClient = null;
    private DataOutputStream dataToClient = null;
    private ObjectInputStream objectFromClient = null;
    private ObjectOutputStream objectToClient = null;

    public GhostHandler(Socket ghostClient) {
        this.ghostClient = ghostClient;
    }
    
    @Override
    public void run()   
    {
        try
        {
            dataFromClient = new BufferedReader(new InputStreamReader(ghostClient.getInputStream()));
            dataToClient = new DataOutputStream(ghostClient.getOutputStream());
            objectFromClient = new ObjectInputStream(ghostClient.getInputStream());
            objectToClient = new ObjectOutputStream(ghostClient.getOutputStream());
            //JChordGhostController.initOutlets(ghostClient,dataFromClient,dataToClient,objectFromClient,objectToClient);
            System.out.println("Ghost client");
            while (true) {
                clientOption = dataFromClient.readLine();
                if (clientOption != null) {
                    switch(clientOption) {
                        case "1":
                            //JChordGhostController.joinRing();
                            break;
                        case "2":
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
