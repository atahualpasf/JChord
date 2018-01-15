/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Clase que crea el objeto nodo.
 *
 * @author Atahualpa Silva F.
 * @link https://github.com/atahualpasf
 * <br> 
 * @author Andrea L. Contreras D.
 * @link https://github.com/andrecontdi
 */
public class Node implements Serializable {

    private Integer key;
    private String ip;
    private Integer port;
    private Node successor = null;
    private Node predecessor = null;
    private TreeMap<Integer, Node> fingerTable = new TreeMap<>();
    private TreeMap<Archive, List<Node>> remoteFilesTable = new TreeMap<>();
    private List<Archive> localFiles = new ArrayList<>();

    /**
     * Constructor de la clase, solo para usar la clave.
     *
     * @param key Clave del nodo.
     */
    public Node(Integer key) {
        this.key = key;
    }

    /**
     * Constructor de la clase, para usar con IP y Puerto.
     *
     * @param myIp IP del nodo.
     * @param myPort Puerto del nodo.
     */
    public Node(String myIp, Integer myPort) {
        this.ip = myIp;
        this.port = myPort;
    }

    /**
     * Constructor de la clase, para usar con ip, puerto, clave y archivos
     * locales.
     *
     * @param node Nodo construido.
     */
    public Node(Node node) {
        this.ip = node.getIp();
        this.port = node.getPort();
        this.key = node.getKey();
        this.localFiles = node.getLocalFiles();
    }

    /**
     * Constructor de la clase, para usar con ip, puerto y clave.
     *
     * @param node Nodo construido
     * @param notFiles Indicador de uso de archivos.
     */
    public Node(Node node, boolean notFiles) {
        this.ip = node.getIp();
        this.port = node.getPort();
        this.key = node.getKey();
    }

    /**
     * Método para obtener la clave de un nodo.
     *
     * @return Clave del nodo.
     */
    public Integer getKey() {
        return key;
    }

    /**
     * Método para setear la clave de un nodo.
     * 
     * @param key Clave del nodo.
     */
    public void setKey(Integer key) {
        this.key = key;
    }

    /**
     * Método para obtener la IP de un nodo.
     * 
     * @return IP del nodo.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Método para setear la IP de un nodo. 
     * 
     * @param ip IP del nodo.
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Método para obtener el puerto de un nodo.
     * 
     * @return Puerto del nodo.
     */
    public Integer getPort() {
        return port;
    }

    /**
     * Método para setear el puerto de un nodo.
     * 
     * @param port Puerto del nodo.
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * Método para obtener el sucesor de un nodo.
     * 
     * @return Sucesor del nodo.
     */
    public Node getSuccessor() {
        return successor;
    }

    /**
     * Metodo para setear el sucesor de un nodo.
     * 
     * @param successor Sucesor del nodo.
     */
    public void setSuccessor(Node successor) {
        if (!successor.equals(this)) {
            this.successor = successor;
        }
    }

    /**
     * Método para eliminar el sucesor.
     * 
     */
    public void clearSuccessor() {
        this.successor = null;
    }

    /**
     * Método para obtener el predecesor de un nodo.
     * 
     * @return Predecesor del nodo.
     */
    public Node getPredecessor() {
        return predecessor;
    }

    /**
     * Método para setear el predecesor de un nodo.
     * 
     * @param predecessor Predecesor del nodo.
     */
    public void setPredecessor(Node predecessor) {
        if (!predecessor.equals(this)) {
            this.predecessor = predecessor;
        }
    }

    /**
     * Método para eliminar el predecesor de un nodo.
     * 
     */
    public void clearPredecessor() {
        this.predecessor = null;
    }

    /**
     * Método para eliminar las relaciones de un nodo con su predecesor
     * y sucesor.
     * 
     */
    public void cleanRelations() {
        this.predecessor = null;
        this.successor = null;
    }

    /**
     * Método para obtener la tabla de finger de un nodo. 
     * 
     * @return Tabla de finger del nodo.
     */
    public TreeMap<Integer, Node> getFingerTable() {
        return fingerTable;
    }

    /**
     * Método para setear la tabla de finger de un nodo.
     * 
     * @param FingerTable Tabla de finger.
     */
    public void setFingerTable(TreeMap<Integer, Node> FingerTable) {
        this.fingerTable = FingerTable;
    }

    /**
     * Método para obtener los archivos remotos de un nodo.
     * 
     * @return Tabla de archivos remotos del nodo.
     */
    public TreeMap<Archive, List<Node>> getRemoteFilesTable() {
        return remoteFilesTable;
    }

    /**
     * Método para setear la tabla de archivos remotos de un nodo.
     * 
     * @param remoteFilesTable Tabla de archivos remotos del nodo
     */
    public void setRemoteFilesTable(TreeMap<Archive, List<Node>> remoteFilesTable) {
        this.remoteFilesTable = remoteFilesTable;
    }

    /**
     * Método para obtener la lista de archivos locales de un nodo
     * 
     * @return Lista de archivos locales del nodo.
     */
    public List<Archive> getLocalFiles() {
        return localFiles;
    }

    /**
     * Método para setear los archivos locales.
     * 
     * @param localFiles Lista de archivos locales.
     */
    public void setLocalFiles(List<Archive> localFiles) {
        this.localFiles = localFiles;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Node{" + "key=" + key + ", ip=" + ip + ", port=" + port + ", successor=" + successor + ", predecessor=" + predecessor + ", fingerTable=" + fingerTable + ", remoteFilesTable=" + remoteFilesTable + ", localFiles=" + localFiles + '}';
    }
}
