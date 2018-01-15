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
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class JChord {
    private static Menu nodeMenu;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, InterruptedException {
        // TODO code application logic here
        Util.calculateMaxNodes();
        Server jChordServer = new Server();
        jChordServer.start();
        new File(Util.getLocalDirPath()).mkdirs();
        new File(Util.getDownloadDirPath()).mkdirs();
        Thread.sleep(300);
        
        boolean wannaRun = true;
        int option;
        nodeMenu = new Menu("MAIN ");
        nodeMenu.putAction("JOIN RING", () -> {JChordController.joinRing();});
        nodeMenu.putAction("LEAVE RING", () -> {JChordController.leaveRing();});
        nodeMenu.putAction("SHOW LOCAL FILES", () -> {JChordController.showLocalArchives();});
        nodeMenu.putAction("SEARCH FILE", () -> {JChordController.lookupArchive();});
        nodeMenu.putAction("SHOW INFO", () -> {Util.prettyFormat(Data.getMyNode());});
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
