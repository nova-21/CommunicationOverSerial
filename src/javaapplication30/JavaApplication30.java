/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication30;

import controlador.controlador;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import operaciones.Conversor;
import vistas.Vista;

/**
 *
 * @author David
 */
public class JavaApplication30 {

   

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Vista view= new Vista();
        view.setVisible(true);
        controlador control= new controlador(view);
        
        
        
        Conversor nuevo= new Conversor();
        //nuevo.binary("Uno dos tres");
        //nuevo.ascii("01001100 01101111 01101100 01100001");
        //nuevo.redundancia();
        
        //System.out.println("Entramado");
        //nuevo.entramado();
        
        //System.out.println("Desentramado");
        //nuevo.desentramado();
        
        //nuevo.deteccion();
        //nuevo.archivoAB();
        
    }

}
