/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class Node implements Serializable {
    private Integer key;
    private String myIp;
    private Integer myPort;
    private Node successor = null;
    private Node predecessor = null;

    public Node(String myIp, Integer myPort) {
        this.myIp = myIp;
        this.myPort = myPort;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getMyIp() {
        return myIp;
    }

    public void setMyIp(String myIp) {
        this.myIp = myIp;
    }

    public Integer getMyPort() {
        return myPort;
    }

    public void setMyPort(Integer myPort) {
        this.myPort = myPort;
    }

    public Node getSuccessor() {
        return successor;
    }

    public void setSuccessor(Node successor) {
        if (!successor.equals(this))
            this.successor = successor;
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Node predecessor) {
        if (!predecessor.equals(this))
            this.predecessor = predecessor;
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
        return "Node{" + "key=" + key + ", myIp=" + myIp + ", myPort=" + myPort + ", successor=" + successor + ", predecessor=" + predecessor + '}';
    }

    
}
