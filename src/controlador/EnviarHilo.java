/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

//import static controlador.RecibirHilo.entrada;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import vistas.Vista;

/**
 *
 * @author David
 */
public class EnviarHilo implements Runnable {

    //static Socket socket;
    //static DataInputStream entrada;
    //static DataOutputStream salida;
    private int error = -1;
    InputStream entrada;
    OutputStream salida;
    private int respuesta = -1;
    private int delay=10;

    public void transporte(Vista view, ArrayList<String> textoEntramado, int error, int perdida, int DELAY, InputStream entrada, OutputStream salida) throws InterruptedException, IOException {
        int randomNum = ThreadLocalRandom.current().nextInt(1, textoEntramado.size()-1);
        this.error = error;
        int controlTotal = 0;
        this.delay=DELAY;
        
        this.entrada= new DataInputStream(entrada);
        this.salida = new DataOutputStream(salida);
        
        try {

            for (int contadorTramas = 0; contadorTramas < textoEntramado.size(); contadorTramas++) {

                view.txtEnvio.append("Enviando trama " + contadorTramas + ".\n");

                String men = textoEntramado.get(contadorTramas);
                
                if (randomNum == contadorTramas && error == 1 && perdida == 0) {

                    for (int car = 0; car < Variables.POSICION_ERROR_ENTRAMADO; car++) {
                        salida.write(men.charAt(car));
                        sleep(delay);
                    }
                    if (men.charAt(Variables.POSICION_ERROR_ENTRAMADO) == '0') {
                        salida.write('1');
                        sleep(delay);
                    } else {
                        salida.write('0');
                        sleep(delay);
                    }

                    for (int car = Variables.POSICION_ERROR_ENTRAMADO + 1; car < men.length(); car++) {
                        salida.write(men.charAt(car));
                        sleep(delay);
                    }
                    respuesta=0;
                    view.txtEnvio.append("Trama " + contadorTramas + " enviada.\n");
                    
                    if(view.ckHamm.isSelected()){
                        
                    }else{
                       view.txtEnvio.append("Trama enviada contiene error, retransmitiendo...\n");
                       contadorTramas--; 
                    }
                    
                    error = 0;
                    
                } else if (randomNum == contadorTramas && error == 0 && perdida == 1) {
                    sleep(2000);
                    contadorTramas--;
                    perdida = 0;
                    view.txtEnvio.append("No hay respuesta, retransmitiendo...\n");
                    continue;
                } else {
                    for (Character car : men.toCharArray()) {
                        salida.write(car);
                        
                        sleep(delay);
                    }
                    view.txtEnvio.append("Trama " + contadorTramas + " enviada.\n");
                }
                
                sleep(delay);

                 
                //respuesta = entrada.read();
                
                
                

                if (respuesta == '0') {
                    //controlTotal++;
                    contadorTramas--;
                    view.txtEnvio.append("Trama enviada contiene error, retransmitiendo...\n");

                } else if (respuesta == '1') {
                    //controlTotal = 0;
                    view.txtEnvio.append("Trama " + contadorTramas + " confirmada.\n");

                }

                /*if (controlTotal == 5) {
                    respuesta = -1;
                    break;
                }*/
                respuesta = -1;

            }
            sleep(500);
            salida.write('a');
            
            JOptionPane.showMessageDialog(null, "Mensaje enviado con exito.");

            entrada.close();
            salida.close();
            
        } catch (SocketException ex) {
            JOptionPane.showMessageDialog(null, "No existe respuesta, conexión perdida.");
            Logger.getLogger(controlador.class.getName()).log(Level.SEVERE, null, ex);
            try {
                entrada.close();
                salida.close();
                
            } catch (IOException ex1) {
                Logger.getLogger(EnviarHilo.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (IOException ex) {

            Logger.getLogger(EnviarHilo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    
    
    @Override
    public void run() {

    }

}
