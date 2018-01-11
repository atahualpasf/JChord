/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import Controller.Util;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class StandardObject implements Serializable {
    private String protocol = null;
    private Object object = null;
    private boolean success;

    public StandardObject(String protocol, Object object) {
        this.protocol = protocol;
        this.object = object;
    }
    
    public StandardObject(String protocol, Object object, boolean success) {
        this.protocol = protocol;
        this.object = object;
        this.success = success;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    
    public StandardObject buildProtocol(List<String> parameters) {
        parameters.forEach((data) -> {
            if (protocol == null)
                protocol = data;
            else
                protocol = protocol + Util.DELIMETER + data;
        });
        return this;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
    
     public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "StandardObject{" + "protocol=" + protocol + ", object=" + object + ", success=" + success + '}';
    }
}
