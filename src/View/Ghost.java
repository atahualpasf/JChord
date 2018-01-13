/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Util;
import Model.Node;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author atahu
 */
public class Ghost {
    private static ServerSocket ghostServer;
    private static Socket ghostClient;
    private static List<Node> ring;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        try {
            ghostServer = new ServerSocket(Util.GHOST_PORT, 8, InetAddress.getByName(Util.getMyIp()));
            Util.showServerMessage("GHOST", true);
            System.out.println(Util.ANSI_BLUE + "INFORMACIÓN:" + Util.ANSI_RESET + " Servidor -> " + ghostServer.getInetAddress().getHostAddress() + ":" + ghostServer.getLocalPort());
            ring = new ArrayList<>();
            while (true) 
            {
                ghostClient = ghostServer.accept();
                System.out.println(Util.ANSI_BLUE + "INFORMACIÓN:" + Util.ANSI_RESET + " Cliente -> " + ghostClient.getInetAddress().getHostAddress() + ":" + ghostClient.getPort());
                new GhostHandler(ghostClient, ring).start();
            }
        } catch (IOException ex) {
            System.out.print(Util.ANSI_RED + "EXCEPTION: ");
            System.out.println(Util.ANSI_RESET + ex.getMessage());
            Util.pause();
        }
        
    }   
}
