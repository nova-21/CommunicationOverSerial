/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

//import static controlador.EnviarHilo.socket;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
//import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import operaciones.Conversor;
import vistas.Vista;

/**
 *
 * @author David
 */
public class RecibirHilo implements Runnable {

    private ServerSocket serverSocket;
    //private Socket socket;
    //private  DataInputStream entrada;
    //private DataOutputStream salida;
    private Conversor convertir = new Conversor();
    private ArrayList<String> mensajesCorrectos = new ArrayList<String>();
    private Vista view;

    public void servidor(Vista view, ServerSocket sa) {
        this.view = view;
        this.serverSocket = sa;
        //this.socket=socket;

        new Thread(new Runnable() {
            //private ServerSocket serverSocket;
            @Override
            public void run() {

                //System.out.print(socket.getPort());
                //serverSocket = new ServerSocket(4000);
                //Socket socket = serverSocket.accept();
                
                //DataInputStream entrada = new DataInputStream(socket.getInputStream());
                //DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
                String mensaje = "";
                String trama="";
                char caracterIngreso = 0;
                while (caracterIngreso != 'a') {
                    
                    mensaje = view.txtTramas.getText();
                    
                    //String verificar = "";
                    
                    trama=leer();
                    
                    /*for (int i = 0; i < 27; i++) {
                        
                        
                        
                        
                        caracterIngreso = leer().charAt(0);
                        if (caracterIngreso == 'a') {
                            break;
                            
                        }
                        
                        mensaje = mensaje + caracterIngreso;
                        verificar = verificar + caracterIngreso;
                        view.txtTramas.setText(mensaje);
                    }*/
                    
                        mensaje = mensaje + trama;
                        //verificar = verificar + caracterIngreso;
                        view.txtTramas.setText(mensaje);
                    
                    if (caracterIngreso == 'a') {
                        break;
                    }
                    
                    String desentramado = convertir.desentramado(trama);
                    try {
                        sleep(600);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RecibirHilo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if (convertir.deteccion(desentramado) == 1) {
                        responder("1");
                        mensajesCorrectos.add(desentramado);
                        view.txtVR.append(desentramado + "   Correcto\n");
                    } else {
                        responder("0");
                        view.txtVR.append(desentramado + "   Incorrecto\n");
                    }
                    
                    System.out.println(desentramado);
                    view.txtTramas.setText(view.txtTramas.getText() + "\n");
                }
                //entrada.close();
                //salida.close();
                //socket.close();
                //serverSocket.close();

                System.out.println("fin");

                mostrar();
            }
        }).start();

    }
    
    
    private String leer(){
        String trama="";
        try {
            
        Process p2 = Runtime.getRuntime().exec("minimodem --rx-one  110 -A -c 2");
            //p = Runtime.getRuntime().exec(b);
            BufferedReader br = new BufferedReader(
                new InputStreamReader(p2.getInputStream()));
            trama = br.readLine();
            System.out.println("line: " + trama);
            //p2.waitFor();
            //System.out.println ("exit: " + p2.exitValue());
            System.out.println("antes de destruir");
            p2.destroy();
        } catch (IOException ex) {
            Logger.getLogger(RecibirHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return trama;
        
        
    }
    
    private void responder(String trama){
        
        String comando="echo "+trama+"|minimodem --tx 110 -A";
        
        String[] b=new String[] { "/bin/bash",  "-c", comando };
        
        Process p;
        
        try {
            p = Runtime.getRuntime().exec(b);
            //p = Runtime.getRuntime().exec(b);
            BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream()));
            p.waitFor();
            //System.out.println ("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {}
        
    }

    private synchronized void mostrar() {
        ArrayList<String> binarios = new ArrayList<String>();
        for (String mensaje : mensajesCorrectos) {

            binarios.add(mensaje.substring(0, mensaje.length() - 3));
        }
        for (String mensaje : binarios) {
            view.txtRecibido.append(convertir.ascii(mensaje));

        }
    }

    @Override
    public void run() {

    }

}
