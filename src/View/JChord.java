/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Data;
import Controller.JChordController;
import Controller.Util;
import java.io.File;

/**
 * Clase que se encarga de iniciar el servidor JChord y también de mandar a
 * crear el menú y recibir la selección del usuario.
 *
 * @author Atahualpa Silva F.
 * @link https://github.com/atahualpasf
 * <br> 
 * @author Andrea L. Contreras D.
 * @link https://github.com/andrecontdi
 */
public class JChord {

    private static Menu nodeMenu;

    /**
     * @param args The command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, InterruptedException {
        Util.calculateMaxNodes();
        Server jChordServer = new Server();
        jChordServer.start();
        new File(Util.getLocalDirPath()).mkdirs();
        new File(Util.getDownloadDirPath()).mkdirs();
        Thread.sleep(300);

        boolean wannaRun = true;
        int option;
        nodeMenu = new Menu("MAIN ");
        nodeMenu.putAction("JOIN RING", () -> {
            JChordController.joinRing();
        });
        nodeMenu.putAction("SHOW LOCAL FILES", () -> {
            JChordController.showLocalArchives();
        });
        nodeMenu.putAction("SEARCH FILE", () -> {
            JChordController.lookupArchive();
        });
        nodeMenu.putAction("SHOW INFO", () -> {
            Util.prettyFormat(Data.getMyNode());
        });
        nodeMenu.putAction("LEAVE RING", () -> {
            JChordController.leaveRing();
        });
        while (wannaRun) {
            // TODO some error checking.
            System.out.println(nodeMenu.generateText());
            option = KeyIn.inInt("|\t\t   YOUR OPTION ->");
            System.out.println("");
            nodeMenu.executeAction(option);
            Util.smallCls();
        }
    }

}
