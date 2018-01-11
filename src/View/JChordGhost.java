/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Util;
import static Controller.Util.ANSI_BLUE;
import static Controller.Util.ANSI_RESET;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author atahu
 */
public class JChordGhost {
    private static ServerSocket ghostServer;
    private static Socket ghostClient;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        try {
            ghostServer = new ServerSocket(Util.GHOST_PORT, 8, InetAddress.getByName(Util.getMyIp()));
            showServerMessage(true);
            System.out.println(ANSI_BLUE + "INFORMACIÓN:" + ANSI_RESET + " Servidor -> " + ghostServer.getInetAddress().getHostAddress() + ":" + ghostServer.getLocalPort());
            while (true) 
            {
                ghostClient = ghostServer.accept();
                System.out.println(ANSI_BLUE + "INFORMACIÓN:" + ANSI_RESET + " Cliente -> " + ghostClient.getInetAddress().getHostAddress() + ":" + ghostClient.getPort());
                new GhostHandler(ghostClient).start();
            }
        } catch (IOException ex) {
            System.out.print(Util.ANSI_RED + "EXCEPTION: ");
            System.out.println(Util.ANSI_RESET + ex.getMessage());
            Util.pause();
        }
        
    }
    
    private static void showServerMessage(boolean start) {
        System.out.println(" ---------------  ");
        System.out.println("|     " + Util.ANSI_BLUE + "GHOST" + Util.ANSI_RESET + "     |");
        System.out.println("|  -----------  |");
        System.out.println("| |           | |");
        if (start)
            System.out.println("| |  ^     ^  | |");
        else
            System.out.println("| |  X     X  | |");
        System.out.println("| |     -     | |");
        if (start)
            System.out.println("| |    ---    | |");
        else
            System.out.println("| |     O     | |");
        System.out.println("|  -----------  |");
        if (start)
            System.out.println("|" + Util.ANSI_BLUE + " SERVER" + Util.ANSI_RESET + ":" + Util.ANSI_GREEN + "   ON" + Util.ANSI_RESET + "  |");
        else
            System.out.println("|" + Util.ANSI_BLUE + " SERVER" + Util.ANSI_RESET + ":" + Util.ANSI_RED + "  OFF" + Util.ANSI_RESET + "  |");
        System.out.println("| --      ----- |");
        System.out.println("| " + Util.ANSI_BLUE + Util.getMyIp() + Util.ANSI_RESET + " |");
        System.out.println(" --------------- ");
    }    
}
