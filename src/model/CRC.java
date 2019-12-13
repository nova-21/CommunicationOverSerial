/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author Jose
 */
public class CRC {
    
    public static ArrayList<String> entramar(ArrayList<String> binary) {
        ArrayList<String> tramas = new ArrayList();
        String code = "01111110";
        String trama = "";
        for(int i=0; i<binary.size(); i++) {
            trama = "";
            trama += code;
            trama += binary.get(i);
            trama += code;
            
            System.out.println("Trama " + (i+1) + ": " + trama + "\n");
            tramas.add(trama);
        }
        
        return tramas;
    }
    
    public static ArrayList<String> addRedundancy(ArrayList<String> binary) {
        ArrayList<String> binaryModified = new ArrayList();
        int divisor=10110;
        int dividend = 0;
        String quotient = "";
        String remainder = "";
        for(int i=0; i<binary.size(); i++) {
            dividend = Integer.parseInt((binary.get(i) + "0000"));
            quotient = Integer.toBinaryString((dividend / divisor));
            remainder = Integer.toBinaryString((dividend % divisor));
            System.out.println("\nquotient is: "+quotient+" and remainder is: "+remainder);
            binaryModified.add(binary.get(i) + remainder);
        }
        
        return binaryModified;
    }
}
