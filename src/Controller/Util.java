/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import Model.Node;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    // Constantes de os
    private static String OS = null;
    private static final String LW_FILES_PATH = "SD\\LOCAL";
    private static final String DW_FILES_PATH = "SD\\DOWNLOAD";    
    private static final String LL_FILES_PATH = "SD/LOCAL";
    private static final String DL_FILES_PATH = "SD/DOWNLOAD";
    private static final String W_DIR_SEPARATOR = "\\";
    private static final String L_DIR_SEPARATOR = "/";
    
    // Constantes de aplicación
    public static Integer THREAD_TIMEOUT = 200;
    public static Integer SOCKET_TIMEOUT = 10000;
    public static Integer MAX_NODES = null;
    public static final Integer M_BITS = 8;
    private static String MY_IP = null;
    public static final Integer MY_PORT = 4446;
    public static final String GHOST_IP = "192.168.0.110";
    public static final Integer GHOST_PORT = 5555;
    public static final String CMD_DELIMETER = ":";
    public static final String SUBCMD_DELIMETER = ";";
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
    public static final int MENU_TITLE_LENGTH = 14;
    
    private static String getOsName() {
       if(OS == null) { OS = System.getProperty("os.name"); }
       return OS;
    }
    
    private static boolean isWindows() {
       return getOsName().startsWith("Windows");
    }
    
    public static String getLocalDirPath() {
        return (isWindows()) ? LW_FILES_PATH : LL_FILES_PATH;
    }
    
    public static String getDownloadDirPath() {
        return (isWindows()) ? DW_FILES_PATH : DL_FILES_PATH;
    }
    
    public static String getOsDirSeparator() {
        return (isWindows()) ? W_DIR_SEPARATOR : L_DIR_SEPARATOR;
    }

    public static void calculateMaxNodes() {
        if (MAX_NODES == null) {
            int i = 2;
            MAX_NODES = (int) Math.pow(i, M_BITS);
        }
    }
    
    public static String prettyFormat(Node node) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(node);
        System.out.println(json);
      return json;
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
    
    public static int hashCode(String fileName) {
        int hash = 1;
        hash = hash * Objects.hashCode(fileName);
        hash = (hash & 0x7FFFFFFF) % Util.MAX_NODES;
        return hash;
    }
    
    public static void cls() {
        for(int i = 0; i < 25; i++) {
            System.out.println();
        }
    }
    
    public static void smallCls() {
        for(int i = 0; i < 3; i++) {
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
