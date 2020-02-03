/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

//import static controlador.EnviarHilo.socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.Thread.sleep;
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
    InputStream leer;
    OutputStream salida;
    String filePathOut = "/Users/David/Desktop/2.txt";
    int TAMANO=45;
    

    public void servidor(Vista view, InputStream entrada, OutputStream salida) {
        this.view = view;
        this.leer = entrada;
        this.salida = salida;
        
        byte[] buffer = new byte[1024];
        //this.socket=socket;

        new Thread(new Runnable() {
            //private ServerSocket serverSocket;
            @Override
            public void run() {
                

                try {
                    
                    String mensaje = "";
                    char caracterIngreso = 0;
                    while (caracterIngreso != 'a') {

                        mensaje = view.txtTramas.getText();

                        String verificar = "";

                        for (int i = 0; i < TAMANO; i++) {

                            caracterIngreso = (char) leer.read();
                            if (caracterIngreso == 'a') {
                                System.out.println("break");
                                break;

                            }

                            mensaje = mensaje + caracterIngreso;
                            verificar = verificar + caracterIngreso;

                            view.txtTramas.setText(mensaje);
                        }

                        if (caracterIngreso == 'a') {
                            System.out.println("fin");
                            
                            break;
                            
                        }

                        String desentramado = convertir.desentramado(verificar);
                        
                        

                        if (verificar.charAt(0) == '1' && verificar.charAt(1) == ' ') {
                            // System.out.println("ack"); No detecta
                        } else {
                            if (convertir.deteccion(desentramado) == 1) {
                                try {
                                    sleep(500);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(RecibirHilo.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                salida.write('1');
                                mensajesCorrectos.add(desentramado);
                                view.txtVR.append(desentramado + "   Correcto\n");
                            } else {
//                                try {
//                                    sleep(500);
//                                } catch (InterruptedException ex) {
//                                    Logger.getLogger(RecibirHilo.class.getName()).log(Level.SEVERE, null, ex);
//                                }
                                if (view.ckHamm.isSelected()) {
                                    try {
                                        sleep(500);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(RecibirHilo.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    salida.write('1');
                                    String corregido = hamming(desentramado);
                                    mensajesCorrectos.add(corregido);

                                    view.txtVR.append(desentramado + "   Incorrecto    " + corregido + "   Corregido \n");
                                } else {
                                    try {
                                        sleep(500);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(RecibirHilo.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    salida.write('0');
                                    view.txtVR.append(desentramado + "   Incorrecto\n");
                                }

                            }
                            System.out.println(desentramado);
                            view.txtTramas.setText(view.txtTramas.getText() + "\n");
                        }

                    }

                } catch (SocketException ex) {
                    Logger.getLogger(controlador.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(mensajesCorrectos.size());
                System.out.println("fin");

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
            view.txtRecibido.append(convertir.ascii(mensaje));

        }
        
        System.out.println(binarios.size());

        if (view.ckImagen.isSelected()) {
            //byte[] bFile=mensajesCorrectos.toArray();
            byte[] bFile= new byte[binarios.size()];
            int contador=0;
            for (String o : binarios) {
                int bits = (int) Long.parseLong(o, 2);
                //System.out.println(bits);
                bFile[contador]=Byte.parseByte(""+bits,10) ;
                contador++;
            }

            ArrayBytesFile fileOutput = new ArrayBytesFile(filePathOut, bFile);
            System.out.println("File: " + filePathOut);
            fileOutput.writeFile();
        }
    }

    private String hamming(String trama) {
        String corregida = "";
        for (int car = 0; car < 5; car++) {
            corregida = corregida + trama.charAt(car);

        }

        if (trama.charAt(5) == '0') {
            corregida = corregida + '1';

        } else {
            corregida = corregida + '0';

        }

        for (int car = 6; car < trama.length(); car++) {
            corregida = corregida + trama.charAt(car);

        }
        return corregida;
    }

    @Override
    public void run() {

    }

}
