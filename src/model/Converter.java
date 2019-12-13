/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.nio.charset.Charset;
import java.util.ArrayList;

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
    
}
