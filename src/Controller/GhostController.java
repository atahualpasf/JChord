/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Node;
import java.util.List;

/**
 * Clase controlladora para el nodo fantasma.
 *
 * @author Atahualpa Silva F.
 * @link https://github.com/atahualpasf
 * <br> 
 * @author Andrea L. Contreras D.
 * @link https://github.com/andrecontdi
 */
public class GhostController {

    /**
     * Contabiliza y muestra la cantidad de nodos conectados al anillo.
     *
     * @param ring Lista que contiene los nodos conectados al anillo.
     */
    public static void showStatusNetwork(List<Node> ring) {
        Util.showMessage(2, 3, GhostController.class.getSimpleName(), "Nodos en el anillo: " + ring.size());
        ring.forEach(node -> {
            Util.showMessage(2, 3, GhostController.class.getSimpleName(), node.toString());
        });
    }
}
