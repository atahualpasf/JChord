/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import Model.Node;
import Model.StandardObject;
import Model.Archive;
import View.KeyIn;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class JChordController {
    private static Socket connection = null;
    private static ObjectOutputStream outputObject = null;
    private static ObjectInputStream inputObject = null;
    
    public static void joinRing() {
        if (Data.getMyNode() == null) {
            try {
                openConnection(Util.GHOST_IP, Util.GHOST_PORT);
                Node me = new Node(Util.getMyIp(), Util.MY_PORT);
                me.setLocalFiles(loadArchives());
                System.out.println(me);
                me.setKey(Util.hashCode(me));
                StandardObject request = new StandardObject(me, true)
                        .buildProtocol(Arrays.asList("1","JOIN"));
                outputObject.writeObject(request);
                request = (StandardObject) inputObject.readObject();
                System.out.println(request);
                if (request.isSuccess()) {
                    me = (Node) request.getObject();
                    Data.setMyNode(me);
                }
                connection.close();
                loadArchives();
                if (me.getPredecessor() != null) updateNodeRelations(me, true, true);
                if (me.getSuccessor() != null) updateNodeRelations(me, true, false);
            } catch (IOException ex) {
                Logger.getLogger(JChordController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(JChordController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println(Util.ANSI_RED + "ERROR:" + Util.ANSI_RESET + " Cliente -> Su sesión ya está activa.");
        }        
    }
    
    public static void leaveRing() {
        if (Data.getMyNode() != null) {
            try {
                openConnection(Util.GHOST_IP, Util.GHOST_PORT);
                Node me = Data.getMyNode();
                StandardObject request = new StandardObject(me, true)
                        .buildProtocol(Arrays.asList("2","LEAVE"));
                outputObject.writeObject(request);
                request = (StandardObject) inputObject.readObject();
                System.out.println(request);
                if (request.isSuccess()) {
                    Data.setMyNode(null);
                }
                connection.close();
                if (request.getProtocol() == null) {
                    updateNodeRelations(me, false, true);
                    updateNodeRelations(me, false, false);
                } else if (request.getProtocol().equalsIgnoreCase("ONLYONE")) {
                    updateOnlyOne(me);
                }
            } catch (IOException ex) {
                Logger.getLogger(JChordController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(JChordController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println(Util.ANSI_RED + "ERROR:" + Util.ANSI_RESET + " Cliente -> Por favor inicie sesión primero.");
        }
    }
    
    private static List<Archive> loadArchives() {
        List<Archive> archiveList = new ArrayList<>();
        File[] files = new File(Util.getLocalDirPath()).listFiles();
        if (files != null){
            for (File file : files){
                if (file.isFile()){
                    Archive archive = new Archive(Util.hashCode(file.getName()), file.getName());
                    archiveList.add(archive);
                }
            }
        }
        return archiveList;
    }
    
    public static void showLocalArchives() {
        if (Data.getMyNode() != null) {
            Data.getMyNode().getLocalFiles().forEach((archive) -> {
                System.out.println(archive);
            });
        } else {
            System.out.println(Util.ANSI_RED + "ERROR:" + Util.ANSI_RESET + " Cliente -> Por favor inicie sesión primero.");
        }
    }
    
    public static void lookupArchive() {
        if (Data.getMyNode() != null && Data.getMyNode().getPredecessor() != null && Data.getMyNode().getSuccessor() != null) {
            try {
                String filename = KeyIn.inString("|\t\t   NOMBRE DE ARCHIVO ->");
                Integer keyFile = Util.hashCode(filename);
                Archive archiveToLookup = new Archive(keyFile, filename);
                Node me = new Node(Data.getMyNode());
                Node nodeToAsk = Data.getMyNode().getFingerTable().lowerEntry(keyFile).getValue();
                openConnection(nodeToAsk.getIp(), nodeToAsk.getPort());
                StandardObject request = new StandardObject(me, true)
                        .buildProtocol(Arrays.asList("4","LOOKUP"));
                outputObject.writeObject(request);
                request = new StandardObject(archiveToLookup, true);
                outputObject.writeObject(request);
                connection.close();
            } catch (IOException ex) {
                Logger.getLogger(JChordController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (Data.getMyNode() == null)
                System.out.println(Util.ANSI_RED + "ERROR:" + Util.ANSI_RESET + " Cliente -> Por favor inicie sesión primero.");
            else
                System.out.println(Util.ANSI_RED + "ERROR:" + Util.ANSI_RESET + " Cliente -> Solo se encuentra usted en el anillo, intente luego.");
        }
    }
    
    private static void updateNodeRelations(Node me, boolean isJoin, boolean isPredecessor) {
        try {
            String operation = (isJoin) ? "1" : "2";
            String command = (isJoin) ? "JOIN" : "LEAVE";
            String type = (isPredecessor) ? "PREDECESSOR" : "SUCCESSOR";
            Node nodeToUpdate = (isPredecessor) ? me.getPredecessor() : me.getSuccessor() ;
            openConnection(nodeToUpdate.getIp(), nodeToUpdate.getPort());
            StandardObject request = new StandardObject(me, true)
                    .buildProtocol(Arrays.asList(operation,command,type));
            outputObject.writeObject(request);
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(JChordController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void updateOnlyOne(Node me) {
        try {
            Node nodeToUpdate = me.getPredecessor();
            openConnection(nodeToUpdate.getIp(), nodeToUpdate.getPort());
            StandardObject request = new StandardObject(me, true)
                    .buildProtocol(Arrays.asList("21","LEAVE","ONLYONE"));
            outputObject.writeObject(request);
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(JChordController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void openConnection(String ip, Integer port) {
        try {
            connection = new Socket(ip, port);
            connection.setSoTimeout(Util.SOCKET_TIMEOUT);
            outputObject = new ObjectOutputStream(connection.getOutputStream());
            inputObject = new ObjectInputStream(connection.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(JChordController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
