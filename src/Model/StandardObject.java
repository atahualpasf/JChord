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
 * Clase que se encarga de el manejo del protocolo de comunicación entre cliente
 * y servidor.
 *
 * @author Atahualpa Silva F.
 * @link https://github.com/atahualpasf
 * <br> 
 * @author Andrea L. Contreras D.
 * @link https://github.com/andrecontdi
 */
public class StandardObject implements Serializable {

    private String protocol = null;
    private Object object = null;
    private boolean success;

    /**
     * Constructor de la clase. Solo para protocolo,
     *
     * @param protocol Protocolo a utilizar.
     * @param success Indicador de operación exitosa o fallida.
     */
    public StandardObject(String protocol, boolean success) {
        this.protocol = protocol;
        this.success = success;
    }

    /**
     * Constructor de la clase. Solo para objetos.
     *
     * @param object Objeto genérico.
     * @param success Indicador de operación exitosa o fallida.
     */
    public StandardObject(Object object, boolean success) {
        this.object = object;
        this.success = success;
    }

    /**
     * Constructor de la clase. Para ambos, protocolo y objeto.
     *
     * @param protocol Protocolo a utilizar.
     * @param object Objeto genérico.
     * @param success Indicador de operación exitosa o fallida.
     */
    public StandardObject(String protocol, Object object, boolean success) {
        this.protocol = protocol;
        this.object = object;
        this.success = success;
    }

    /**
     * Método para obtener el protocolo.
     *
     * @return Prorocolo obtenido.
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Método para setear el protocolo.
     *
     * @param protocol Protocolo seteado.
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * Método que se encaga de construir el protocolo según una lista de
     * paramétros.
     *
     * @param parameters Parametros para el armado del protocolo.
     * @return Objeto generito que representa el protocolo.
     */
    public StandardObject buildProtocol(List<String> parameters) {
        parameters.forEach((data) -> {
            if (protocol == null) {
                protocol = data;
            } else {
                protocol = protocol + Util.CMD_DELIMETER + data;
            }
        });
        return this;
    }

    /**
     * Método para obtener un objeto.
     *
     * @return Objeto generico.
     */
    public Object getObject() {
        return object;
    }

    /**
     * Método para setear un objeto.
     *
     * @param object Objectb seteado.
     */
    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * Método para indicar que una operación o petición fue exitosa.
     *
     * @return Indicador de operación exitosa o fallida.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Método para setear el valor de boolean success.
     *
     * @param success Indicador de operación exitosa o fallida.
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "StandardObject{" + "protocol=" + protocol + ", object=" + object + ", success=" + success + '}';
    }
}
