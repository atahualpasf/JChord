/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class Util {
    // Constantes de aplicación
    public static Integer MAX_NODES = null;
    private static final Integer M_BITS = 8;
    public static final Integer GHOST_PORT = 5555;
    private static String MY_IP = null;
    public static final Integer MY_PORT = 4444;
    // Constantes para mostrar colores en la consola
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void calculateMaxNodes() {
        if (MAX_NODES == null) {
            int i = 2;
            MAX_NODES = (int) Math.pow(i, M_BITS);
        }
    }
    
    public static void cls() {
        for(int i = 0; i < 25; i++) {
            System.out.println();
        }
    }
    
    public static void pause() {
        System.out.println("Presione cualquier tecla para continuar...");
        new java.util.Scanner(System.in).nextLine();
    }
    
    public static String getMyIp() {
        if (MY_IP == null) {
            try {
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                        .getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface ni = (NetworkInterface) networkInterfaces
                            .nextElement();
                    Enumeration<InetAddress> nias = ni.getInetAddresses();
                    while(nias.hasMoreElements()) {
                        InetAddress ia= (InetAddress) nias.nextElement();
                        if (!ia.isLinkLocalAddress() 
                         && !ia.isLoopbackAddress()
                         && ia instanceof Inet4Address) {
                            MY_IP = ia.getHostAddress();
                        }
                    }
                }
            } catch (SocketException e) {
                System.out.println("unable to get current IP " + e.getMessage());
            }
        }
        return MY_IP;
    }
}