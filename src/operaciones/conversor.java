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

/**
 *
 * @author David
 */
public class conversor {

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    public void binary(String me) {
        String text = me;
        byte[] bytes = text.getBytes(UTF_8);
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            binary.append(' ');
        }
        System.out.println("'" + text + "' to binary: " + binary);
        System.out.println("bytes= " + bytes[0]);
        System.out.println("text again= " + new String(bytes, UTF_8));
        System.out.println("Hola");
    }

    public void ascii(String s) {
        String s2 = "";
        char nextChar;

        for (int i = 0; i <= s.length() - 8; i += 9) //this is a little tricky.  we want [0, 7], [9, 16], etc (increment index by 9 if bytes are space-delimited)
        {
            nextChar = (char) Integer.parseInt(s.substring(i, i + 8), 2);
            s2 += nextChar;
        }
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
