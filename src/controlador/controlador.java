/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import operaciones.Conversor;
import vistas.Vista;

/**
 *
 * @author David
 */
public class controlador implements ActionListener {

    private Vista view;
    private int error = 0;
    private int perdida = 0;
    private int DELAY = 1;

    private Conversor convertir = new Conversor();
    private ArrayList<String> textoBinario = new ArrayList<String>();
    private ArrayList<String> textoRedundancia = new ArrayList<String>();
    private ArrayList<String> textoEntramado = new ArrayList<String>();
    InputStream entrada;
    OutputStream salida;
    String path = "";

    public controlador(Vista view) {

        this.view = view;
        this.view.btnEnviar.addActionListener(this);
        this.view.btnLimpiar.addActionListener(this);
        this.view.ckCRC.addActionListener(this);
        this.view.ckHamm.addActionListener(this);
        this.view.btnArchivo.addActionListener(this);
        try {
            connect();
        } catch (Exception ex) {
            Logger.getLogger(controlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        (new Thread(new Runnable() {
            //private ServerSocket serverSocket;
            @Override
            public void run() {
                servidor();
            }
        })).start();

    }

    private synchronized void enviar() {
        if(view.ckImagen.isSelected()){
           convertirBinarioArchivo();
        }else{
           convertirBinario(); 
        }
        
        agregarRedundancia();
        entramar();
        transporte();
    }
    
    private synchronized void convertirBinarioArchivo(){
        //File fileInput = new File (path);

        //String filePathOut = "/Users/David/Desktop/Test_out.png";

        //ArrayBytesFile file = new ArrayBytesFile(fileInput);
       // byte[] bFile = file.getBFile();
        ArrayList<String> tramas= new ArrayList();
        String trama="";
        try {
                      byte[] fileContent = Files.readAllBytes(Paths.get(this.path));
            for(byte o:fileContent){
                textoBinario.add(Integer.toBinaryString((o & 0xFF) + 0x100).substring(1));

        }
        } catch (IOException ex) {
           
        }
    }

    void connect() throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("COM4");
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                entrada = serialPort.getInputStream();
                salida = serialPort.getOutputStream();

            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    private synchronized void convertirBinario() {
        textoBinario = convertir.binary(this.view.txtAreaMensajeSalida.getText());
        try {
            sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void agregarRedundancia() {
        textoRedundancia = convertir.redundancia(textoBinario);
        for (Object men : textoRedundancia) {
            this.view.txtAreaRedundancia.append((String) men + "\n");
        }
        try {
            sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void entramar() {
        textoEntramado = convertir.entramado(textoRedundancia);
        for (Object men : textoEntramado) {
            this.view.txtAreaEntramado.append((String) men + "\n");
        }
        try {
            sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void transporte() {
        EnviarHilo enviar = new EnviarHilo();
        Thread hilo = new Thread(enviar);
        hilo.start();
        try {
            try {
                enviar.transporte(view, textoEntramado, error, perdida, DELAY, this.entrada, this.salida);
            } catch (IOException ex) {
                Logger.getLogger(controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void servidor() {
        RecibirHilo recibir = new RecibirHilo();
        Thread hilo = new Thread(recibir);
        hilo.start();
        recibir.servidor(this.view, this.entrada, this.salida);
    }

    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(view.btnEnviar)) {
            
            if (this.view.ckError.isSelected()) {
                error = 1;

            } else {
                error = 0;
            }

            if (this.view.ckPerdida.isSelected()) {
                perdida = 1;
            } else {
                perdida = 0;
            }

            this.DELAY = this.view.sliderVelocidad.getValue();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    enviar();
                }
            }).start();

        } else if (e.getSource().equals(view.btnArchivo)) {

            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            int returnValue = jfc.showOpenDialog(null);
            // int returnValue = jfc.showSaveDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                path = selectedFile.getAbsolutePath();
                view.txtArchivo.setText(path);
            }
        }

        if (e.getSource().equals(view.ckCRC)) {

            this.view.ckHamm.setSelected(false);
        } else if (e.getSource().equals(view.ckHamm)) {
            this.view.ckCRC.setSelected(false);
        }

        if (e.getSource().equals(view.btnLimpiar)) {
            servidor();
        }
    }

}
