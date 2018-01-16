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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase controladora del JChord
 *
 * @author Atahualpa Silva F.
 * @link https://github.com/atahualpasf
 * <br> 
 * @author Andrea L. Contreras D.
 * @link https://github.com/andrecontdi
 */
public class JChordController {

    private static Socket connection = null;
    private static ObjectOutputStream outputObject = null;
    private static ObjectInputStream inputObject = null;

    /**
     * Metodo que se utiliza para insertar un nodo en el anillo.
     *
     */
    public static void joinRing() {
        if (Data.getMyNode() == null) {
            try {
                openConnection(Util.GHOST_IP, Util.GHOST_PORT);
                Node me = new Node(Util.getMyIp(), Util.MY_PORT);
                me.setLocalFiles(loadArchives());
                System.out.println(me);
                me.setKey(Util.hashCode(me));
                StandardObject request = new StandardObject(me, true)
                        .buildProtocol(Arrays.asList("1", "JOIN"));
                outputObject.writeObject(request);
                request = (StandardObject) inputObject.readObject();
                System.out.println(request);
                if (request.isSuccess()) {
                    me = (Node) request.getObject();
                    Data.setMyNode(me);
                }
                connection.close();
                loadArchives();
                if (me.getPredecessor() != null) {
                    updateNodeRelations(me, true, true);
                }
                if (me.getSuccessor() != null) {
                    updateNodeRelations(me, true, false);
                }
            } catch (IOException ex) {
                Util.showMessage(3, 3, GhostHandlerController.class.getSimpleName(), "IOException " + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                Util.showMessage(3, 3, GhostHandlerController.class.getSimpleName(), "ClassNotFoundException " + ex.getMessage());
            }
        } else {
            Util.showMessage(4, 2, JChordController.class.getSimpleName(), "Your session is already active.");
        }
    }

    /**
     * Metodo que se utiliza para que un nodo abandone el anilo.
     *
     */
    public static void leaveRing() {
        if (Data.getMyNode() != null) {
            try {
                openConnection(Util.GHOST_IP, Util.GHOST_PORT);
                Node me = Data.getMyNode();
                StandardObject request = new StandardObject(me, true)
                        .buildProtocol(Arrays.asList("2", "LEAVE"));
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
                Util.showMessage(3, 3, GhostHandlerController.class.getSimpleName(), "IOException " + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                Util.showMessage(3, 3, GhostHandlerController.class.getSimpleName(), "ClassNotFoundException " + ex.getMessage());
            }
        } else {
            Util.showMessage(4, 2, JChordController.class.getSimpleName(), "Please login first.");
        }
    }

    /**
     * Metodo para realizar la carga de la lista de archivos.
     *
     * @return Lista de archivos
     */
    private static List<Archive> loadArchives() {
        List<Archive> archiveList = new ArrayList<>();
        File[] files = new File(Util.getLocalDirPath()).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    Archive archive = new Archive(Util.hashCode(file.getName()), file.getName());
                    archiveList.add(archive);
                }
            }
        }
        return archiveList;
    }

    /**
     * Metodo para mostrar os archivos locales
     *
     */
    public static void showLocalArchives() {
        if (Data.getMyNode() != null) {
            Data.getMyNode().getLocalFiles().forEach((archive) -> {
                System.out.println(archive);
            });
        } else {
            Util.showMessage(4, 2, JChordController.class.getSimpleName(), "Please login first.");
        }
    }

    /**
     * Metodo para realizar la busqueda de los archivos.
     *
     */
    public static void lookupArchive() {
        if (Data.getMyNode() != null && Data.getMyNode().getPredecessor() != null && Data.getMyNode().getSuccessor() != null) {
            String filename = KeyIn.inString("|\t\t   FILE NAME ->");
            Integer keyFile = Util.hashCode(filename);
            Archive archiveToLookup = new Archive(keyFile, filename);
            Node me = new Node(Data.getMyNode(), true);
            System.out.println(keyFile);
            if (!Data.getMyNode().getLocalFiles().contains(archiveToLookup)) {
                if (!Data.getMyNode().getRemoteFilesTable().containsKey(archiveToLookup)) {
                    try {
                        Util.showMessage(2, 3, JChordController.class.getSimpleName(), "I do not know who has the archive, but I inform you who could know.");
                        Node nodeToAsk;
                        try {
                            nodeToAsk = Data.getMyNode().getFingerTable().lowerEntry(keyFile).getValue();
                        } catch (NullPointerException e) {
                            nodeToAsk = Data.getMyNode().getFingerTable().lastEntry().getValue();
                        }
                        Util.showMessage(3, 3, JChordController.class.getSimpleName(), "Node to ask: " + nodeToAsk);
                        StandardObject request = new StandardObject(me, true)
                                .buildProtocol(Arrays.asList("4", "LOOKUP"));
                        Socket connection = new Socket(nodeToAsk.getIp(), nodeToAsk.getPort());
                        connection.setSoTimeout(Util.SOCKET_TIMEOUT);
                        ObjectOutputStream objectToSend = new ObjectOutputStream(connection.getOutputStream());
                        objectToSend.writeObject(request);
                        request = new StandardObject(archiveToLookup, true);
                        objectToSend.writeObject(request);
                    } catch (IOException ex) {
                        Util.showMessage(3, 3, GhostHandlerController.class.getSimpleName(), "IOException " + ex.getMessage());
                    }
                } else {
                    try {
                        Util.showMessage(2, 3, JChordController.class.getSimpleName(), "I tell who has the archive to send it.");
                        List<Node> listNode = Data.getMyNode().getRemoteFilesTable().get(archiveToLookup);
                        Random randomIndex = new Random();
                        Node nodeSelected = listNode.get(randomIndex.nextInt(listNode.size()));
                        StandardObject request = new StandardObject(me, true)
                                .buildProtocol(Arrays.asList("6", "SENDFILE"));
                        Socket connection = new Socket(nodeSelected.getIp(), nodeSelected.getPort());
                        connection.setSoTimeout(Util.SOCKET_TIMEOUT_DOWNLOAD);
                        ObjectOutputStream objectToSend = new ObjectOutputStream(connection.getOutputStream());
                        objectToSend.writeObject(request);
                        request = new StandardObject(archiveToLookup, true);
                        objectToSend.writeObject(request);
                    } catch (IOException ex) {
                        Util.showMessage(3, 3, GhostHandlerController.class.getSimpleName(), "IOException " + ex.getMessage());
                    }
                }
            } else {
                Util.showMessage(2, 3, JChordController.class.getSimpleName(), "Sorry but you already have the file " + Util.ANSI_CYAN + archiveToLookup.getName());
            }
        } else {
            if (Data.getMyNode() == null) {
                Util.showMessage(4, 2, JChordController.class.getSimpleName(), "Please login first.");
            } else {
                Util.showMessage(4, 2, JChordController.class.getSimpleName(), "You are only in the ring, try later..");
            }
        }
    }

    /**
     * Metodo para actualizar las relaciones de cada nodo con respecto a los
     * demas nodos del anillo
     *
     * @param me Nodo
     * @param isJoin Indicador de que el nodo se esta uniendo al anillo.
     * @param isPredecessor Indicador de si el nodo es predecesor.
     */
    private static void updateNodeRelations(Node me, boolean isJoin, boolean isPredecessor) {
        try {
            String operation = (isJoin) ? "1" : "2";
            String command = (isJoin) ? "JOIN" : "LEAVE";
            String type = (isPredecessor) ? "PREDECESSOR" : "SUCCESSOR";
            Node nodeToUpdate = (isPredecessor) ? me.getPredecessor() : me.getSuccessor();
            openConnection(nodeToUpdate.getIp(), nodeToUpdate.getPort());
            StandardObject request = new StandardObject(me, true)
                    .buildProtocol(Arrays.asList(operation, command, type));
            outputObject.writeObject(request);
            connection.close();
        } catch (IOException ex) {
            Util.showMessage(3, 3, GhostHandlerController.class.getSimpleName(), "IOException " + ex.getMessage());
        }
    }

    /**
     * Metodo para actualizar solo un nodo.
     *
     * @param me Nodo.
     */
    private static void updateOnlyOne(Node me) {
        try {
            Node nodeToUpdate = me.getPredecessor();
            openConnection(nodeToUpdate.getIp(), nodeToUpdate.getPort());
            StandardObject request = new StandardObject(me, true)
                    .buildProtocol(Arrays.asList("21", "LEAVE", "ONLYONE"));
            outputObject.writeObject(request);
            connection.close();
        } catch (IOException ex) {
            Util.showMessage(3, 3, GhostHandlerController.class.getSimpleName(), "IOException " + ex.getMessage());
        }
    }

    /**
     *
     *
     * @param ip
     * @param port
     */
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
