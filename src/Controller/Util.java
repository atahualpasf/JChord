/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class Util {
    // Constantes de aplicación
    public static Integer MAX_NODES = null;
    private static final Integer M_BITS = 8;
    private static final Integer GHOST_PORT = 5555;
    private static final Integer MY_PORT = 4444;
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
}
