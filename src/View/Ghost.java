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
 * Clase para manejar el nodo fantasma.
 *
 * @author Atahualpa Silva F.
 * @link https://github.com/atahualpasf
 * <br> 
 * @author Andrea L. Contreras D.
 * @link https://github.com/andrecontdi
 */
public class Ghost {

    private static Menu ghostMenu;

    /**
     * Método que se encarga de inicializar y mantener activo el nodo fantasma.
     *
     * @param args
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
            ghostMenu.putAction("SHOW STATUS", () -> {
                GhostController.showStatusNetwork(ring);
            });
            while (wannaRun) {
                // TODO some error checking.
                System.out.println(ghostMenu.generateText());
                option = KeyIn.inInt("|\t\t   YOUR OPTION ->");
                System.out.println("");
                ghostMenu.executeAction(option);
                Util.smallCls();
            }
        } catch (InterruptedException ex) {
            Util.showMessage(3, 3, Ghost.class.getSimpleName(), ex.getMessage());            
        }

    }
}

/**
 * Esta clase representa al demonio que permanece escuchando.
 *
 * @author Atahualpa Silva F.
 * @link https://github.com/atahualpasf
 * <br> 
 * @author Andrea L. Contreras D.
 * @link https://github.com/andrecontdi
 */
class DemonGhost extends Thread {

    private static ServerSocket ghostServer;
    private static Socket ghostClient;
    private static List<Node> ring;

    /**
     * Constructor de la clase.
     *
     * @param ring Anillo en ejecución.
     */
    public DemonGhost(List<Node> ring) {
        DemonGhost.ring = ring;
    }

    /**
     * Método que corre el servidor fantasma.
     *
     */
    @Override
    public void run() {
        try {
            ghostServer = new ServerSocket(Util.GHOST_PORT, 8, InetAddress.getByName(Util.getMyIp()));
            Util.showServerMessage("GHOST", true);
            Util.showMessage(2, 1, this.getClass().getSimpleName(), ghostServer.getInetAddress().getHostAddress() + ":" + ghostServer.getLocalPort());
            while (true) {
                ghostClient = ghostServer.accept();
                Util.showMessage(2, 2, this.getClass().getSimpleName(), ghostClient.getInetAddress().getHostAddress() + ":" + ghostClient.getPort());
                ghostClient.setSoTimeout(Util.SOCKET_TIMEOUT_DOWNLOAD);
                new GhostHandler(ghostClient, ring).start();
            }
        } catch (UnknownHostException ex) {
            Util.showMessage(3, 3, this.getClass().getSimpleName(), ex.getMessage());
        } catch (IOException ex) {
            Util.showMessage(3, 3, this.getClass().getSimpleName(), ex.getMessage());
        }
    }
}
