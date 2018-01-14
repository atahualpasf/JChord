/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.GhostController;
import Controller.Util;
import Model.Node;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class Ghost {
    private static Menu ghostMenu;
    /**
     * 
     * @author Atahualpa Silva F. <https://github.com/atahualpasf>
     * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            Util.calculateMaxNodes();
            List<Node> ring = new ArrayList<>();
            DemonGhost demonGhost = new DemonGhost(ring);
            demonGhost.start();
            Thread.sleep(300);
            
            boolean wannaRun = true;
            int option;
            ghostMenu = new Menu("GHOST");
            ghostMenu.putAction("SHOW STATUS", () -> {GhostController.showStatusNetwork(ring);});
            while (wannaRun) {
                // TODO some error checking.
                System.out.println(ghostMenu.generateText());
                option = KeyIn.inInt("|\t\t   YOUR OPTION ->");
                System.out.println("");
                ghostMenu.executeAction(option);
                Util.smallCls();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Ghost.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }   
}

class DemonGhost extends Thread {
    private static ServerSocket ghostServer;
    private static Socket ghostClient;
    private static List<Node> ring;

    public DemonGhost(List<Node> ring) {
        DemonGhost.ring = ring;
    }
    
    @Override
    public void run () {        
        try {
            ghostServer = new ServerSocket(Util.GHOST_PORT, 8, InetAddress.getByName(Util.getMyIp()));
            Util.showServerMessage("GHOST", true);
            System.out.println(Util.ANSI_BLUE + "INFORMACIÓN:" + Util.ANSI_RESET + " Servidor -> " + ghostServer.getInetAddress().getHostAddress() + ":" + ghostServer.getLocalPort());
            while (true)
            {
                ghostClient = ghostServer.accept();
                System.out.println(Util.ANSI_BLUE + "INFORMACIÓN:" + Util.ANSI_RESET + " Cliente -> " + ghostClient.getInetAddress().getHostAddress() + ":" + ghostClient.getPort());
                new GhostHandler(ghostClient, ring).start();
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(DemonGhost.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.print(Util.ANSI_RED + "EXCEPTION: ");
            System.out.println(Util.ANSI_RESET + ex.getMessage());
        }
    }
}