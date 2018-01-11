/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import Controller.Util;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class Node implements Serializable
{
    private Integer key;
    private String myIp;
    private Integer myPort;

    public Node(String myIp, Integer myPort) 
    {
        this.myIp = myIp;
        this.myPort = myPort;
        this.setKey(this.hashCode());
    }

    public Integer getKey() 
    {
        return key;
    }

    public void setKey(Integer key) 
    {
        this.key = key;
    }

    public String getMyIp() 
    {
        return myIp;
    }

    public void setMyIp(String myIp) 
    {
        this.myIp = myIp;
    }

    public Integer getMyPort() 
    {
        return myPort;
    }

    public void setMyPort(Integer myPort) 
    {
        this.myPort = myPort;
    }

    @Override
    public int hashCode() 
    {
        int hash = 1;
        hash = hash * Objects.hashCode(this.myIp);
        hash = hash * Objects.hashCode(this.myPort);
        hash = (hash & 0x7FFFFFFF) % Util.MAX_NODES;
        return hash;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (obj == null) 
        {
            return false;
        }
        if (getClass() != obj.getClass()) 
        {
            return false;
        }
        final Node other = (Node) obj;
        if (!Objects.equals(this.key, other.key)) 
        {
            return false;
        }
        return true;
    }
}
