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
 * Clase que contiene los métodos generales que se utilizan durante toda la
 * ejecución del programa.
 *
 * @author Atahualpa Silva F.
 * @link https://github.com/atahualpasf
 * <br> 
 * @author Andrea L. Contreras D.
 * @link https://github.com/andrecontdi
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
    public static Integer MAX_NODES = null;
    public static final Integer M_BITS = 8;
    private static String MY_IP = null;
    public static final Integer MY_PORT = 4444;
    public static final String GHOST_IP = "192.168.0.105";
    public static final Integer GHOST_PORT = 5555;
    public static final String CMD_DELIMETER = ":";
    public static Integer THREAD_TIMEOUT = 200;
    public static Integer SOCKET_TIMEOUT = 10000;
    public static Integer SOCKET_TIMEOUT_DOWNLOAD = 900000;
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

    /**
     * Obtiene el nombre del sistema operativo en el que se esta ejecutando el
     * programa.
     *
     * @return Nombre del sistema operativo.
     */
    private static String getOsName() {
        if (OS == null) {
            OS = System.getProperty("os.name");
        }
        return OS;
    }

    /**
     * Indica si el sistema operativo es Windows o Linux.
     *
     * @return TRUE si es Windows, FALSE si es Linux.
     */
    private static boolean isWindows() {
        return getOsName().startsWith("Windows");
    }

    /**
     * Método que obtiene la ruta del directorio local donde se encuentran los
     * archivos dispobibles del nodo.
     *
     * @return Ruta del directorio.
     */
    public static String getLocalDirPath() {
        return (isWindows()) ? LW_FILES_PATH : LL_FILES_PATH;
    }

    /**
     * Método que obtiene la ruta del directorio de descargas donde se colocaran
     * los archivos que descargue el usuario.
     *
     * @return Ruta del directorio.
     */
    public static String getDownloadDirPath() {
        return (isWindows()) ? DW_FILES_PATH : DL_FILES_PATH;
    }

    /**
     * Método que se encarga de obtener el separador que utilliza cada OS.
     *
     * @return Separador a utilizar.
     */
    public static String getOsDirSeparator() {
        return (isWindows()) ? W_DIR_SEPARATOR : L_DIR_SEPARATOR;
    }

    /**
     * Método que se encarga de calcular la cantidad máxima de nodos en el
     * anillo.
     *
     */
    public static void calculateMaxNodes() {
        if (MAX_NODES == null) {
            int i = 2;
            MAX_NODES = (int) Math.pow(i, M_BITS);
        }
    }

    /**
     * Método que se encarga de imprimir en formato Gson para que sea más fácil
     * de leer la información de cada nodo.
     *
     * @param obj Nodo del que se necesita leer la información.
     * @return JSON
     */
    public static String prettyFormat(Object obj) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(obj);
        System.out.println(json);
        return json;
    }

    /**
     * Método utilizado para obtener la IP de equipo donde se este ejecutando el
     * programa.
     *
     * @return IP del equipo.
     */
    public static String getMyIp() {
        if (MY_IP == null) {
            try {
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                        .getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface ni = (NetworkInterface) networkInterfaces
                            .nextElement();
                    Enumeration<InetAddress> nias = ni.getInetAddresses();
                    while (nias.hasMoreElements()) {
                        InetAddress ia = (InetAddress) nias.nextElement();
                        if (!ia.isLinkLocalAddress()
                                && !ia.isLoopbackAddress()
                                && ia instanceof Inet4Address) {
                            MY_IP = ia.getHostAddress();
                        }
                    }
                }
            } catch (SocketException e) {
                Util.showMessage(3, 3, Util.class.getSimpleName(), "Unable to get current IP " + e.getMessage());
            }
        }
        return MY_IP;
    }

    /**
     * Método que se encarga de hacer el has de la IP + el puerto.
     *
     * @param node Nodo del que se necesita el hash.
     * @return Hash del nodo.
     */
    public static int hashCode(Node node) {
        int hash = 1;
        hash = hash * Objects.hashCode(node.getIp());
        hash = hash * Objects.hashCode(node.getPort());
        hash = (hash & 0x7FFFFFFF) % Util.MAX_NODES;
        return hash;
    }

    /**
     * Método que se encarga de realizar el hash del nombre del archivo.
     *
     * @param fileName Nombre del archivo
     * @return Hash del nommbre del archivo.
     */
    public static int hashCode(String fileName) {
        int hash = 1;
        hash = hash * Objects.hashCode(fileName);
        hash = (hash & 0x7FFFFFFF) % Util.MAX_NODES;
        return hash;
    }

    /**
     * Metodo que se encarga de limpiar la pantalla, durante la ejecución.
     *
     */
    public static void cls() {
        for (int i = 0; i < 25; i++) {
            System.out.println();
        }
    }

    /**
     * Metodo que se encarga de limpiar la pantalla, durante la ejecución.
     *
     */
    public static void smallCls() {
        for (int i = 0; i < 3; i++) {
            System.out.println();
        }
    }

    /**
     * Método que se encarga de colocar en pausa el programa.
     *
     */
    public static void pause() {
        System.out.println("Presione cualquier tecla para continuar...");
        new java.util.Scanner(System.in).nextLine();
    }

    /**
     * Método que se encarga de imprimir en pantalla un mensaje sobre el status
     * del servidor.
     *
     * @param name Nombre del servidor.
     * @param start Status del servidor, encendido o apagado.
     */
    public static void showServerMessage(String name, boolean start) {
        name = (name.length() < 5) ? " " + name : name;
        System.out.println(" ---------------  ");
        System.out.println("|     " + ANSI_CYAN + name + ANSI_RESET + "     |");
        System.out.println("|  -----------  |");
        System.out.println("| |           | |");
        if (start) {
            System.out.println("| |  ^     ^  | |");
        } else {
            System.out.println("| |  X     X  | |");
        }
        System.out.println("| |     -     | |");
        if (start) {
            System.out.println("| |    ---    | |");
        } else {
            System.out.println("| |     O     | |");
        }
        System.out.println("|  -----------  |");
        if (start) {
            System.out.println("|" + " SERVER" + ":" + ANSI_GREEN + "   ON" + ANSI_RESET + "  |");
        } else {
            System.out.println("|" + " SERVER" + ":" + ANSI_RED + "  OFF" + ANSI_RESET + "  |");
        }
        System.out.println("| --      ----- |");
        System.out.println("| " + ANSI_CYAN + Util.getMyIp() + ANSI_RESET + " |");
        System.out.println(" --------------- ");
    }

    /**
     * Método para realizar impresiones de forma genérica.
     *
     * @param status Status del mensaje:
     * <br>1 Exitoso.
     * <br>2 Información.
     * <br>3 Excepción.
     * <br>4 Error.
     * @param server Indica de quien es la información a imprimir:
     * <br>1 Servidor.
     * <br>2 Cliente.
     * <br>3 Ninguno de los anteriores.
     * @param origin Clase de donde proviene la información/mensaje.
     * @param message Mesanje a mostrar.
     */
    public static void showMessage(int status, int server, String origin, String message) {
        if (status == 1) {
            if (server == 1) {
                System.out.println(ANSI_GREEN + "SUCCESS:" + ANSI_RESET + " Server -> " + message);
            }
            if (server == 2) {
                System.out.println(ANSI_GREEN + "SUCCESS:" + ANSI_RESET + " Client -> " + message);
            }
            if (server == 3) {
                System.out.println(ANSI_GREEN + "SUCCESS:" + ANSI_RESET + " " + message);
            }
        }
        if (status == 2) {
            if (server == 1) {
                System.out.println(ANSI_CYAN + "INFORMATION from " + origin + ":" + ANSI_RESET + " Server -> " + message);
            }
            if (server == 2) {
                System.out.println(ANSI_CYAN + "INFORMATION from " + origin + ":" + ANSI_RESET + " Client -> " + message);
            }
            if (server == 3) {
                System.out.println(ANSI_CYAN + "INFORMATION from " + origin + ":" + ANSI_RESET + " " + message);
            }
        }
        if (status == 3) {
            if (server == 1) {
                System.out.println(ANSI_YELLOW + "EXCEPTION from " + origin + ":" + ANSI_RESET + " Server -> " + message);
            }
            if (server == 2) {
                System.out.println(ANSI_YELLOW + "EXCEPTION from " + origin + ":" + ANSI_RESET + " Client -> " + message);
            }
            if (server == 3) {
                System.out.println(ANSI_YELLOW + "EXCEPTION from " + origin + ":" + ANSI_RESET + " " + message);
            }
        }
        if (status == 4) {
            if (server == 1) {
                System.out.println(ANSI_RED + "ERROR from " + origin + ":" + ANSI_RESET + " Server -> " + message);
            }
            if (server == 2) {
                System.out.println(ANSI_RED + "ERROR from " + origin + ":" + ANSI_RESET + " Client -> " + message);
            }
            if (server == 3) {
                System.out.println(ANSI_RED + "ERROR from " + origin + ":" + ANSI_RESET + " " + message);
            }
        }
    }
}
