/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import Model.Node;
import java.util.List;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class GhostController {
    public static void showStatusNetwork(List<Node> ring) {
        System.out.println("NODOS EN EL ANILLO: " + ring.size());
        ring.forEach(node->{
            System.out.println(node);
        });
    }
}
