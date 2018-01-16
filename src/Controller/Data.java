/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Node;

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
}
