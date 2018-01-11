/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import Model.Node;
import Model.StandardObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
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
    
    public static void joinRing(String myIp, Integer myPort) throws ClassNotFoundException {
        try {
            connection = new Socket(Util.GHOST_IP, Util.GHOST_PORT);
            outputObject = new ObjectOutputStream(connection.getOutputStream());
            inputObject = new ObjectInputStream(connection.getInputStream());
            Node me = new Node(myIp, myPort);
            me.setKey(Util.hashCode(me));
            StandardObject standardObject = new StandardObject(null, me, true)
                    .buildProtocol(Arrays.asList("1","JOIN"));
            outputObject.writeObject(standardObject);
            System.out.println((String) inputObject.readObject());
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(JChordController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
