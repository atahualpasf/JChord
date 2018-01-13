/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import Controller.Util;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class Server extends Thread {
    private ServerSocket nodeServer;
    private Socket nodeClient;
    
    public Server() {};
    
    @Override
    public void run () {
        try {
            nodeServer = new ServerSocket(Util.MY_PORT, 8, InetAddress.getByName(Util.getMyIp()));
            Util.showServerMessage(String.valueOf(Util.MY_PORT), true);
            while (true) 
            {
                nodeClient = nodeServer.accept();
                System.out.println(Util.ANSI_BLUE + "INFORMACIÓN:" + Util.ANSI_RESET + " Cliente -> " + nodeClient.getInetAddress().getHostAddress() + ":" + nodeClient.getPort());
                new ServerHandler(nodeClient).start();
            }
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
