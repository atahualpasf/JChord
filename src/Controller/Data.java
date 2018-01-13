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
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class Data {
    private static Node me = null;
    private static List<Archive> files = new ArrayList<>();

    public static Node getMyNode() {
        return me;
    }
    
    public static void setMyNode(Node me) {
        Data.me = me;
    }

    public static List<Archive> getFiles() {
        return files;
    }

    public static void setFiles(List<Archive> files) {
        Data.files = files;
    }
}
