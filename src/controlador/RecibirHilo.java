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
import java.util.concurrent.TimeUnit;
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
    
    private Conversor convertir = new Conversor();
    private ArrayList<String> mensajesCorrectos = new ArrayList<String>();
    private Vista view;

    public void servidor(Vista view) {
        this.view = view;

        new Thread(new Runnable() {

            @Override
            public void run() {

                String mensaje = "";
                String trama = "";
                char caracterIngreso = 0;
                while (caracterIngreso != 'a') {

                    mensaje = view.txtTramas.getText();

                    trama = leer();

                    mensaje = mensaje + trama;

                    view.txtTramas.setText(mensaje);

                    if (caracterIngreso == 'a') {
                        break;
                    }

                    String desentramado = convertir.desentramado(trama);
                    
                    if (convertir.deteccion(desentramado) == 1) {
                        responder("1");
                        view.txtVR.append(desentramado + "   Correcto\n");
                        mensajesCorrectos.add(desentramado);
                    } else {
                        responder("0");
                        view.txtVR.append(desentramado + "   Incorrecto\n");
                    }

                    
                    view.txtTramas.setText(view.txtTramas.getText() + "\n");
                }

                System.out.println("fin");

                mostrar();
            }
        }).start();

    }

    private String leer() {
        String trama = "";
        try {

            Process p2 = Runtime.getRuntime().exec("minimodem --rx-one  110 -A -c 2");
            try {
                p2.waitFor();
            } catch (InterruptedException ex) {
                Logger.getLogger(RecibirHilo.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p2.getInputStream()));
            
            trama = br.readLine();
            
            p2.destroy();
        } catch (IOException ex) {
            Logger.getLogger(RecibirHilo.class.getName()).log(Level.SEVERE, null, ex);
        }

        return trama;

    }

    private void responder(String trama) {

        String comando = "echo " + trama + "|minimodem --tx 110 -A";

        String[] b = new String[]{"/bin/bash", "-c", comando};

        Process p;

        try {
            p = Runtime.getRuntime().exec(b);
            p.waitFor();
            p.destroy();
        } catch (Exception e) {
        }

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
