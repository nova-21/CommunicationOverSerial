/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class controlador implements ActionListener {

    private Vista view;
    private int error = 0;
    private int perdida = 0;
    private ServerSocket serverSocket;
    private Socket socket;
    private Conversor convertir = new Conversor();
    private ArrayList<String> textoBinario = new ArrayList<String>();
    private ArrayList<String> textoRedundancia = new ArrayList<String>();
    private ArrayList<String> textoEntramado = new ArrayList<String>();

    public controlador(Vista view) {
        

        this.view = view;
        this.view.btnEnviar.addActionListener(this);
        this.view.btnLimpiar.addActionListener(this);
        try {
            serverSocket = new ServerSocket(3000);
        } catch (IOException ex) {
            Logger.getLogger(controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        (new Thread(new Runnable(){
            //private ServerSocket serverSocket;
                @Override
                public void run(){
        servidor();
                }})).start();

    }

    private synchronized void enviar() {
        convertirBinario();
        agregarRedundancia();
        entramar();
        transporte();
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
                enviar.transporte(textoEntramado,socket, error, perdida);
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
        recibir.servidor(this.view, serverSocket, socket);
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

            new Thread(new Runnable() {
                @Override
                public void run() {
                    enviar();
                }
            }).start();

        }

        if (e.getSource().equals(view.btnLimpiar)) {
            servidor();
        }
    }

}
