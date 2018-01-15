/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Archive;
import Model.Node;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que se encarga de a obtención y seteo de la información de los nodos y
 * listas de archivo.
 *
 * @author Atahualpa Silva F.
 * @link https://github.com/atahualpasf
 * <br> 
 * @author Andrea L. Contreras D.
 * @link https://github.com/andrecontdi
 */
public class Data {

    private static Node me = null;
    private static List<Archive> files = new ArrayList<>();

    /**
     * Obtiene la información de un nodo.
     *
     * @return Nodo obtenido.
     */
    public static Node getMyNode() {
        return me;
    }

    /**
     * Setea información en un nodo.
     *
     * @param me Nodo a setear.
     */
    public static void setMyNode(Node me) {
        Data.me = me;
    }

    /**
     * Obtiene los archivos que se encuentran en el nodo.
     *
     * @return Lista que contiene los archivos.
     */
    public static List<Archive> getFiles() {
        return files;
    }

    /**
     * Setea los archivos de una lista.
     *
     * @param files Lista que contiene los archivos y se necesita setear.
     */
    public static void setFiles(List<Archive> files) {
        Data.files = files;
    }
}
