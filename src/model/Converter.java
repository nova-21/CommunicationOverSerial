/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.zip.CRC32;

/**
 *
 * @author Jose
 */
public class Converter {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    
    public static ArrayList<String> toBinary(String msg) {
        ArrayList<String> binary = new ArrayList();
        byte[] bytes = msg.getBytes(UTF_8);
        StringBuilder row = null;
        
        int val = 0;
        for (byte b : bytes) {
            row = new StringBuilder();
            val = b;
            for (int i = 0; i < 8; i++) {
                row.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            
            binary.add(row.toString());            
        }
        
        return binary;        
    }
    
    public static String fromBinary(ArrayList<String> binary) {
        StringBuilder sBinary = new StringBuilder();
        for(String b: binary) {
            sBinary.append(b);
            sBinary.append(' ');
        }
        
        return fromBinaryImplementation(sBinary.toString());
    }
    
    public static String fromBinary(String binary) {
        return fromBinaryImplementation(binary);
    }
    
    private static String fromBinaryImplementation(String binary) {
        String text = "";
        char nextChar = 0;
        
        for(int i=0; i<=binary.length() - 8; i+=9) { //this is a little tricky.  we want [0, 7], [9, 16], etc (increment index by 9 if bytes are space-delimited)
            nextChar = (char) Integer.parseInt(binary.substring(i, i + 8), 2);
            text += nextChar;
        }
        
        CRC32 re= new CRC32();
        re.update(text.getBytes());
        System.out.println(re.getValue());
        System.out.println(text);
        
        return text;
    }
    
    public static ArrayList<Byte> toAscii(String msg) {
        ArrayList<Byte> codes = new ArrayList();
        for(int i=0; i<msg.length(); i++) 
            codes.add((byte) msg.charAt(i));
        
        return codes;
    }
    
}
