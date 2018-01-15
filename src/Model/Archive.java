/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que crea el objeto archivo.
 * 
 * @author Atahualpa Silva F.
 * @link https://github.com/atahualpasf
 * <br> 
 * @author Andrea L. Contreras D.
 * @link https://github.com/andrecontdi
 */
public class Archive implements Serializable, Comparable<Archive> {

    private Integer key;
    private String name;

    /**
     * Constructor de la clase.
     * 
     * @param key Clave del archivo.
     * @param name Nombre del archivo.
     */
    public Archive(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * Método para obtener la clave del archivo.
     * 
     * @return Clave del archivo.
     */
    public Integer getKey() {
        return key;
    }

    /**
     * Método para setear la clave del archivo.
     * 
     * @param key Clave del archivo.
     */
    public void setKey(Integer key) {
        this.key = key;
    }

    /**
     * Método para obtener el nombre del archivo.
     * 
     * @return Nombre del archivo. 
     */
    public String getName() {
        return name;
    }

    /**
     * Método para setear el nombre del archivo.
     * 
     * @param name Nombre del archivo.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Archive other = (Archive) obj;
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Archive{" + "key=" + key + ", name=" + name + '}';
    }

    @Override
    public int compareTo(Archive a) {
        return this.key.compareTo(a.getKey());
    }
}
