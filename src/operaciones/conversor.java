/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operaciones;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

/**
 *
 * @author David
 */
public class conversor {

    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private int divisor=10110;
    ArrayList<String> mensajes;
    ArrayList<String> tramas;

    public void binary(String me) {
        String text = me;
        byte[] bytes = text.getBytes(UTF_8);
        mensajes= new ArrayList();
        
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            StringBuilder fila = new StringBuilder();
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                fila.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            
            mensajes.add(fila.toString());            
            binary.append(' ');
        }
        
        for(Object men:mensajes){
            System.out.println(men);
        }
        /*CRC32 re= new CRC32();
        re.update(bytes);
        
        System.out.println(re.getValue());*/
        
        System.out.println("'" + text + "' to binary: " + binary);
        //System.out.println("bytes= " + bytes[0]);
        //System.out.println("text again= " + new String(bytes, UTF_8));
        //System.out.println("Hola");
    }
    
    public void redundancia(){
        
        int contador=0;
        for(String men:mensajes){
            int dividend=Integer.parseInt(men+"0000");
            String quotient = Integer.toBinaryString((dividend/divisor));
            String remainder = Integer.toBinaryString((dividend%divisor));
            mensajes.set(contador, mensajes.get(contador)+remainder);
            System.out.println("\nquotient is: "+quotient+" and remainder is: "+remainder);
            contador++;
        }
        
        for(Object men:mensajes){
            System.out.println(men);
        }
    }
    
    public void entramado() {
        tramas = new ArrayList();
        String code = "01111110";
        String trama = "";
        for(int i=0; i<mensajes.size(); i++) {
            trama = "";
            trama += code;
            trama += mensajes.get(i);
            trama += code;
            
            System.out.println("Trama " + (i+1) + ": " + trama + "\n");
            tramas.add(trama);
        }
    }
    
    public void deteccion(){
        
    }

    public void ascii(String s) {
        String s2 = "";
        char nextChar;

        for (int i = 0; i <= s.length() - 8; i += 9) //this is a little tricky.  we want [0, 7], [9, 16], etc (increment index by 9 if bytes are space-delimited)
        {
            nextChar = (char) Integer.parseInt(s.substring(i, i + 8), 2);
            s2 += nextChar;
        }
        
        CRC32 re= new CRC32();
        re.update(s2.getBytes());
        
        System.out.println(re.getValue());
        
        System.out.println(s2);
        
    }

    public void toAscii(byte[] bytes) {
        String texto = new String(bytes, UTF_8);
        File nueva = new File("./nuevo/sdf.ex");
    }

    public void archivoAB() throws IOException {
        File file = new File("cosa.txt");
        byte[] fileData = new byte[(int)file.length()];
        FileInputStream in = new FileInputStream(file);
        in.read(fileData);
        in.close();
        StringBuilder binary = new StringBuilder();
        for (byte b : fileData) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            binary.append(' ');
        }
        
        System.out.println(binary);
        
        
        System.out.println(fileData[0]);
    }
    
    public void archivoRecibir(byte[] fileData) throws FileNotFoundException, IOException{
        File file2= new File("nuevo.txt");
        FileOutputStream out= new FileOutputStream(file2);
        out.write(fileData);
        out.close();
    }
}
