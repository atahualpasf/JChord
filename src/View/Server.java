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

/**
 * Clase que se encarga de crear los hijos de los servidores o nodos.
 *
 * @author Atahualpa Silva F.
 * @link https://github.com/atahualpasf
 * <br> 
 * @author Andrea L. Contreras D.
 * @link https://github.com/andrecontdi
 */
public class Server extends Thread {

    private ServerSocket nodeServer;
    private Socket nodeClient;

    /**
     * Constructor de la clase.
     * 
     */
    public Server() {
    }

    ;
    
    @Override
    public void run() {
        try {
            nodeServer = new ServerSocket(Util.MY_PORT, 8, InetAddress.getByName(Util.getMyIp()));
            Util.showServerMessage(String.valueOf(Util.MY_PORT), true);
            while (true) {
                nodeClient = nodeServer.accept();
                Util.showMessage(2, 2, SendFile.class.getSimpleName(), nodeClient.getInetAddress().getHostAddress() + ":" + nodeClient.getPort());
                new ServerHandler(nodeClient).start();
            }

        } catch (UnknownHostException ex) {
            Util.showMessage(3, 3, ReceiveFile.class.getSimpleName(), "UnknownHostException " + ex.getMessage());
        } catch (IOException ex) {
            Util.showMessage(3, 3, ReceiveFile.class.getSimpleName(), "IOException " + ex.getMessage());
        }
    }
}
