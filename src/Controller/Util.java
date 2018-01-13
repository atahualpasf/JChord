/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import Model.Node;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Objects;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class Util {
    // Constantes de aplicación
    public static Integer MAX_NODES = null;
    private static final Integer M_BITS = 8;
    private static String MY_IP = null;
    public static final Integer MY_PORT = 4447;
    public static final String GHOST_IP = "192.168.0.110";
    public static final Integer GHOST_PORT = 5555;
    public static final String DELIMETER = ":";
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
    
    public static int hashCode(Node node) {
        int hash = 1;
        hash = hash * Objects.hashCode(node.getIp());
        hash = hash * Objects.hashCode(node.getPort());
        hash = (hash & 0x7FFFFFFF) % Util.MAX_NODES;
        return hash;
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
    
    public static void showServerMessage(String name, boolean start) {
        name = (name.length() < 5) ? " " + name : name;
        System.out.println(" ---------------  ");
        System.out.println("|     " + Util.ANSI_BLUE + name + Util.ANSI_RESET + "     |");
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
