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
import java.math.BigInteger;
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
    private String divisor = "1011";

    ArrayList<String> mensajes;

    ArrayList<String> recibidos;

    ArrayList<String> mensajeRec = new ArrayList();

    public void binary(String me) {
        String text = me;
        byte[] bytes = text.getBytes(UTF_8);
        mensajes = new ArrayList();

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

        /*for (Object men : mensajes) {
            System.out.println(men);
        }*/
        System.out.println("'" + text + "' to binary: " + binary);
        //System.out.println("bytes= " + bytes[0]);
        //System.out.println("text again= " + new String(bytes, UTF_8));
        //System.out.println("Hola");
    }

    static String div(String code, String gen) {
        int pointer = gen.length();
        String result = code.substring(0, pointer);
        String rem = "";
        for (int i = 0; i < gen.length(); i++) {
            if (result.charAt(i) == gen.charAt(i)) {
                rem += "0";
            } else {
                rem += "1";
            }
        }
        while (pointer < code.length()) {
            if (rem.charAt(0) == '0') {
                rem = rem.substring(1, rem.length());
                rem = rem + String.valueOf(code.charAt(pointer));
                pointer++;
            }
            result = rem;
            rem = "";
            if (result.charAt(0) == '0') {
                for (int i = 0; i < gen.length(); i++) {
                    if (result.charAt(i) == '1') {
                        rem += 1;
                    } else {
                        rem += 0;
                    }
                }
            } else {
                for (int i = 0; i < gen.length(); i++) {
                    if (result.charAt(i) == gen.charAt(i)) {
                        rem += "0";
                    } else {
                        rem += "1";
                    }
                }
            }
        }
        return rem.substring(1, rem.length());
    }

    public void redundancia() {

        int contador = 0;
        for (String men : mensajes) {
            String dividend = men + "000";
            //System.out.println(divisor);
            //System.out.println(dividend);
            String remainder = div(dividend.toString(), divisor.toString());
            mensajes.set(contador, mensajes.get(contador) + remainder);
            //System.out.println("remainder is: " + remainder);
            contador++;

        }
        System.out.println("Mensajes con redundancia");
        for (Object men : mensajes) {
            System.out.println(men);
        }
    }

    public void deteccion() {
        recibidos = mensajes;

        for (String men : recibidos) {
            String dividend = men;
            String remainder = div(dividend, divisor);
            //System.out.println(remainder);
            if (Integer.parseInt(remainder) == 0) {
                mensajeRec.add("Correcto");
            } else {
                mensajeRec.add("Retransmitir");
            }

            //System.out.println("Remainder is: " + remainder);
            //contador++;
        }

        System.out.println("Deteccion de errores");
        for (String men : mensajeRec) {
            System.out.println(men);
        }

    }

    public void ascii(String s) {
        String s2 = "";
        char nextChar;

        for (int i = 0; i <= s.length() - 8; i += 9) //this is a little tricky.  we want [0, 7], [9, 16], etc (increment index by 9 if bytes are space-delimited)
        {
            nextChar = (char) Integer.parseInt(s.substring(i, i + 8), 2);
            s2 += nextChar;
        }

        CRC32 re = new CRC32();
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
        byte[] fileData = new byte[(int) file.length()];
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

    public void archivoRecibir(byte[] fileData) throws FileNotFoundException, IOException {
        File file2 = new File("nuevo.txt");
        FileOutputStream out = new FileOutputStream(file2);
        out.write(fileData);
        out.close();
    }
}
