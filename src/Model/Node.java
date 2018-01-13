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
    private String ip;
    private Integer port;
    private Node successor = null;
    private Node predecessor = null;

    public Node(String myIp, Integer myPort) {
        this.ip = myIp;
        this.port = myPort;
    }
    
    public Node(Node node) {
        this.ip = node.getIp();
        this.port = node.getPort();
        this.key = node.getKey();
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Node getSuccessor() {
        return successor;
    }

    public void setSuccessor(Node successor) {
        if (!successor.equals(this))
            this.successor = successor;
    }
    
    public void clearSuccessor() {
        this.successor = null;
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Node predecessor) {
        if (!predecessor.equals(this))
            this.predecessor = predecessor;
    }
    
    public void clearPredecessor() {
        this.predecessor = null;
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
        return "Node{" + "key=" + key + ", myIp=" + ip + ", myPort=" + port + ", successor=" + successor + ", predecessor=" + predecessor + '}';
    }

    public void cleanRelations() {
        this.predecessor = null;
        this.successor = null;
    }
}
