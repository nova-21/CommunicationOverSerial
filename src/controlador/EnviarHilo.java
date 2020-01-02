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

    private int respuesta = -1;

    public void transporte(Vista view, ArrayList<String> textoEntramado, int error, int perdida) throws InterruptedException, IOException {
        int randomNum = ThreadLocalRandom.current().nextInt(0, textoEntramado.size());
        this.error = error;
        int controlTotal = 0;

        Socket socket = new Socket("192.168.1.12", 4000);
        DataInputStream entrada = new DataInputStream(socket.getInputStream());
        DataOutputStream salida = new DataOutputStream(socket.getOutputStream());

        try {

            for (int contadorTramas = 0; contadorTramas < textoEntramado.size(); contadorTramas++) {

                view.txtEnvio.append("Enviando trama " + contadorTramas + ".\n");

                String men = textoEntramado.get(contadorTramas);
                if (randomNum == contadorTramas && error == 1 && perdida == 0) {

                    for (int car = 0; car < 13; car++) {
                        salida.writeChar(men.charAt(car));
                        sleep(100);
                    }
                    if (men.charAt(13) == '0') {
                        salida.writeChar('1');
                        sleep(100);
                    } else {
                        salida.writeChar('0');
                        sleep(100);
                    }

                    for (int car = 14; car < men.length(); car++) {
                        salida.writeChar(men.charAt(car));
                        sleep(100);
                    }
                    error = 0;
                } else if (randomNum == contadorTramas && error == 0 && perdida == 1) {
                    perdida = 0;
                } else {
                    for (Character car : men.toCharArray()) {
                        salida.writeChar(car);
                        sleep(100);
                    }
                }
                view.txtEnvio.append("Trama " + contadorTramas + " enviada.\n");
                sleep(100);

                try {
                    socket.setSoTimeout(5000);
                    respuesta = entrada.readInt();
                    socket.setSoTimeout(0);
                } catch (SocketTimeoutException e) {
                    socket.setSoTimeout(0);
                    //controlTotal++;
                    contadorTramas--;

                    view.txtEnvio.append("Sin respuesta, retransmitiendo...\n");
                    continue;
                }

                if (respuesta == 0) {
                    //controlTotal++;
                    contadorTramas--;
                    view.txtEnvio.append("Trama enviada contiene error, retransmitiendo...\n");

                } else if (respuesta == 1) {
                    //controlTotal = 0;
                    view.txtEnvio.append("Trama " + contadorTramas + " confirmada.\n");

                }

                /*if (controlTotal == 5) {
                    respuesta = -1;
                    break;
                }*/
                respuesta = -1;

            }

            salida.writeChar('a');
            JOptionPane.showMessageDialog(null, "Mensaje enviado con exito.");

            entrada.close();
            salida.close();
            socket.close();

        } catch (SocketException ex) {
            JOptionPane.showMessageDialog(null, "No existe respuesta, conexión perdida.");
            Logger.getLogger(controlador.class.getName()).log(Level.SEVERE, null, ex);
            try {
                entrada.close();
                salida.close();
                socket.close();
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
