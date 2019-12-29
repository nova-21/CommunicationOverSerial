/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import operaciones.Conversor;
import vistas.Vista;

/**
 *
 * @author David
 */
public class RecibirHilo implements Runnable {

    static ServerSocket serverSocket;
    static Socket socket;
    static DataInputStream entrada;
    static DataOutputStream salida;
    private Conversor convertir = new Conversor();
    private ArrayList<String> mensajesCorrectos = new ArrayList<String>();
    private Vista view;

    public void servidor(Vista view, ServerSocket serverSocket) {
        this.view = view;
        this.serverSocket=serverSocket;
        new Thread(new Runnable(){
                @Override
                public void run(){
                    
        
        try {
            
            
            socket = serverSocket.accept();
            entrada = new DataInputStream(socket.getInputStream());
            salida = new DataOutputStream(socket.getOutputStream());
            String mensaje = "";
            char caracterIngreso = 0;
            while (caracterIngreso != 'a') {

                mensaje = view.jTextArea4.getText();

                String verificar = "";

                for (int i = 0; i < 27; i++) {
                    caracterIngreso = entrada.readChar();
                    if (caracterIngreso == 'a') {
                        break;

                    }

                    mensaje = mensaje + caracterIngreso;
                    verificar = verificar + caracterIngreso;
                    view.jTextArea4.setText(mensaje);
                }

                if (caracterIngreso == 'a') {
                    break;
                }

                String desentramado = convertir.desentramado(verificar);

                if (convertir.deteccion(desentramado) == 1) {
                    salida.writeInt(1);
                    mensajesCorrectos.add(desentramado);
                    view.jTextArea5.append(desentramado + "   Correcto\n");
                } else {
                    salida.writeInt(0);
                    view.jTextArea5.append(desentramado + "   Incorrecto\n");
                }
                System.out.println(desentramado);
                view.jTextArea4.setText(view.jTextArea4.getText() + "\n");
            }

        } catch (IOException ex) {
            Logger.getLogger(controlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("fin");
        
        try {
            entrada.close();
            salida.close();
            //socket.close();
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(RecibirHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
        mostrar();
                }
            }).start();
        

        
    }

    private synchronized void mostrar() {
        ArrayList<String> binarios = new ArrayList<String>();
        for (String mensaje : mensajesCorrectos) {

            binarios.add(mensaje.substring(0, mensaje.length() - 3));
        }
        for (String mensaje : binarios) {
            view.jTextArea6.append(convertir.ascii(mensaje));

        }
    }

    @Override
    public void run() {

    }

}
