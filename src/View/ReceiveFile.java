/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import Controller.Util;
import Model.Archive;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class ReceiveFile extends Thread {
    
    private Socket socket;
    private Archive archive;
    private long tamañoArchivo;
    private long descargadoDelArchivo;
    private long byteInicio;
    private FileOutputStream out = null;
    private ObjectInputStream dis=null;
    
    public ReceiveFile(Socket socket, ObjectInputStream dis, Archive archive) {
        this.socket = socket;
        this.archive = archive;
        this.byteInicio = 0;
        this.dis = dis;
    }
    
    /**
     * Devuelve si la descarga termino
     * @return true si la descarga termino
     */
    public boolean descargaTerminada(){ 
        return tamañoArchivo==descargadoDelArchivo; 
    }
    
    /**
     * Devuelve la cantidad de bytes descargados hasta ahora
     * @return cantidad de bytes descargados
     */
    public long descargadoDelArchivo() {
        return descargadoDelArchivo;
    }
    
    public long getByteInicio() {
        return byteInicio;
    }
    
    public void Stop()throws IOException{
        if (socket != null) {socket.close();};
        if (out != null) {out.close();};
        if (dis!=null)  {dis.close(); ;}
        this.stop();
    }
    
    @Override
    public void run() {
        System.out.println("ENTRÓ AQUI EN EL RUN DEL RECEIVER");
        
        byte[] bytes = new byte[512]; // 1/2 kb

        int count;

        try {
            try {  
                if (byteInicio==0){
                    out = new FileOutputStream(Util.getDownloadDirPath() + Util.getOsDirSeparator() + 
                        archive.getName());
                } else {
                    out = new FileOutputStream(Util.getDownloadDirPath() + Util.getOsDirSeparator() + 
                            archive.getName(), true); 
                }

            } catch (FileNotFoundException ex) {
                System.out.println("No se encontró el archivo.");
            }
            tamañoArchivo = dis.readLong();
            descargadoDelArchivo = this.byteInicio;

            int  display=10;
            while ((count = dis.read(bytes)) > 0) {
                if ( (descargadoDelArchivo*100/tamañoArchivo)>display){
                    System.out.println(this.archive.getName()+" "+(descargadoDelArchivo*100/tamañoArchivo)+"%");
                    display=display+10;
                }
             //    System.out.println(this.nombreLibro+" "+(contador/length)+"%");
                descargadoDelArchivo= descargadoDelArchivo +count;
                out.write(bytes, 0, count);
            }
            System.out.println(this.archive.getName()+" "+(descargadoDelArchivo*100/tamañoArchivo)+"%");
        } catch (IOException e) {
            System.out.println("Upps se callo la descarga. ");
        }finally{
            try{
            if (socket != null) {socket.close();};
            if (out != null) {out.close();};
            if (dis!=null)  {dis.close(); ;}
            this.stop();
            }catch (IOException e){}
        }

        System.out.println(" Descarga terminada: "+this.archive.getName());        
    }
}