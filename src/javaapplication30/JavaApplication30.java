/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication30;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import operaciones.conversor;

/**
 *
 * @author David
 */
public class JavaApplication30 {

   

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        conversor nuevo= new conversor();
        nuevo.binary("Hola");
        //nuevo.ascii("01001100 01101111 01101100 01100001");
        nuevo.redundancia();
        nuevo.deteccion();
        //nuevo.archivoAB();
        
    }

}
