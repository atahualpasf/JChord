/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import Controller.Data;
import Controller.Util;
import Model.Archive;
import Model.Node;
import Model.StandardObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class SendFile extends Thread {
    
    private Socket socket;
    private ObjectOutputStream dataOutputStream;
    private ObjectInputStream dataInputStream;
    private Node node;
    private Archive archive;
    //private ArrayList<DatosLibro> ListaLibros ;
    private int indiceLibro;
    //private ArrayList<DatosCliente> ListaClientes;
    private long byteInicio;
    
    public SendFile(Socket nodeSocket, ObjectOutputStream dataOutputStream, ObjectInputStream dataInputStream, Node node, Archive archive) {
        this.socket = nodeSocket;
        this.node = node;
        this.dataOutputStream = dataOutputStream;
        this.dataInputStream = dataInputStream;
        //this.ListaLibros=ListaLibros;
        //this.ListaClientes=ListaClientes;
        this.archive = archive;
        this.byteInicio = 0;
    }
    
    public void Close() {
        try {
            if (socket != null) {socket.close();};
            if (dataInputStream!=null)  {dataInputStream.close(); ;}
            if (dataOutputStream!=null) {dataOutputStream.close();}
        } catch (IOException ex) {
            Logger.getLogger(SendFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        // accion = dataInputStream.readUTF();
        //byteInicio=dataInputStream.readLong();
        //DatosCliente ClienteActual= new DatosCliente(this.idUsuario,0);
        /*int indiceCliente= ListaClientes.indexOf(ClienteActual);
        if (indiceCliente==-1){
        ListaClientes.add(ClienteActual );
        }else {
        ClienteActual=ListaClientes.get(indiceCliente);
        }*/
        try {
            System.out.println("El usuario " + node.getIp() + ":" + node.getPort() + " solicita " +
                    archive.getName() + " de " + Data.getMyNode().getIp() + ":" + Data.getMyNode().getPort());
            //this.indiceLibro=ListaLibros.indexOf(new DatosLibro(nombreLibro,autor));
            //ListaLibros.get(this.indiceLibro).sumarDescargando();

            //String pathLibro=this.path+"/"/*+(ListaLibros.get(this.indiceLibro).getNombreArchivo())*/;
            //dataOutputStream.writeUTF(ListaLibros.get(this.indiceLibro).getNombreArchivo());
            //System.out.println("Indice del libro "+indiceLibro);

            File file = new File(Util.getLocalDirPath() + Util.getOsDirSeparator() + archive.getName());
            System.out.println(file.getAbsolutePath());

            // Get the size of the file
            long length = file.length();
            long sendedFromFile = byteInicio;
            byte[] bytes = new byte[ 512]; // 1/2 kb


            FileInputStream in = new FileInputStream(file);
            in.skip(byteInicio);
            dataOutputStream.writeLong(length);
            //DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            // DataOutputStream out = dataOutputStream;
            int count;
            while ((count = in.read(bytes)) > 0) {
                //   System.out.println(""+count);
                dataOutputStream.write(bytes, 0, count);
                sendedFromFile = sendedFromFile + count;
            }
            System.out.println("Enviado: " + sendedFromFile + " Tamano: "+length);
            if (sendedFromFile == length){
            //ListaLibros.get(this.indiceLibro).sumarDescarga();

            //ClienteActual.sumarCliente();
            }
            // System.out.println("  ");
        } catch (IOException ex) {
            Logger.getLogger(SendFile.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Se callo la conexion: " + Data.getMyNode() + ".");
            Close();
        } finally {
                Close();
        }
        System.out.println("Se desconecto "+ Data.getMyNode() + ".");
    }  
}