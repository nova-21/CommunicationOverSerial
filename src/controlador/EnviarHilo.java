/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

//import static controlador.RecibirHilo.entrada;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
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

        String cadena = "";

        try {

            for (int contadorTramas = 0; contadorTramas < textoEntramado.size(); contadorTramas++) {

                view.txtEnvio.append("Enviando trama " + contadorTramas + ".\n");

                String men = textoEntramado.get(contadorTramas);
                if (randomNum == contadorTramas && error == 1 && perdida == 0) {

                    for (int car = 0; car < 13; car++) {
                        cadena = cadena + (men.charAt(car));

                    }
                    if (men.charAt(13) == '0') {
                        cadena = cadena + '1';

                    } else {
                        cadena = cadena + '0';

                    }

                    for (int car = 14; car < men.length(); car++) {
                        cadena = cadena + men.charAt(car);

                    }
                    error = 0;
                } else if (randomNum == contadorTramas && error == 0 && perdida == 1) {
                    perdida = 0;
                } else {
                    for (Character car : men.toCharArray()) {
                        cadena = cadena + car;

                    }
                }
                
                enviar(cadena);
                sleep(600);
                cadena = "";
                view.txtEnvio.append("Trama " + contadorTramas + " enviada.\n");

                try {
                     ack();
                    


                } catch (Exception e) {

                    contadorTramas--;

                    view.txtEnvio.append("Sin respuesta, retransmitiendo...\n");
                    continue;
                }

                switch (respuesta) {
                    case 0:

                        contadorTramas--;
                        view.txtEnvio.append("Trama enviada contiene error, retransmitiendo...\n");
                        break;
                    case 1:

                        view.txtEnvio.append("Trama " + contadorTramas + " confirmada.\n");
                        break;
                    default:
                        contadorTramas--;
                        break;
                }

                respuesta = -1;

            }

            enviar("000000000000000000000000000");

            JOptionPane.showMessageDialog(null, "Mensaje enviado con exito.");

        } catch (IOException ex) {

            Logger.getLogger(EnviarHilo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void ack() throws IOException, InterruptedException {
        Process p2;
        p2 = Runtime.getRuntime().exec("minimodem --rx-one 110 -A -c 2");

        BufferedReader br = new BufferedReader(
                new InputStreamReader(p2.getInputStream()));
        
        respuesta = Integer.parseInt(br.readLine());
        p2.waitFor(1, TimeUnit.SECONDS);
        
        p2.destroyForcibly();

    }

    public void enviar(String cadena) throws IOException, InterruptedException {

        Process p;
        String comando = "echo " + cadena + "|minimodem --tx 110 -A";

        String[] b = new String[]{"/bin/bash", "-c", comando};

        p = Runtime.getRuntime().exec(b);
        p.waitFor(1, TimeUnit.SECONDS);

        p.destroyForcibly();

        

    }

    @Override
    public void run() {

    }

}
