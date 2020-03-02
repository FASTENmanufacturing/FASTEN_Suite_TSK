/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fasten.wp4.optimizator.tactical.simulator;

/**
 *
 * @author narci
 */
public class DataTreatment {
    
    public static StringBuilder getStringSpaced(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {

            result.append(s.charAt(i));
            if (i != s.length() -1 && Character.isUpperCase(s.charAt(i + 1))) {
                result.append(" ");
            }
        }
        return result;
    }
}
