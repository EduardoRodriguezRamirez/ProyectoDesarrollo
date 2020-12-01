/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erd.parser;

/**
 *
 * @author HP
 */
public class Fabrica {
    
    public Cardinalidades getConexion(String motor){
        if(motor==null){
            return null;
        }
        if(motor.equalsIgnoreCase("Max11")){
            return new Max1a1();
        }else if(motor.equalsIgnoreCase("Max1N")){
            return new Max1aN();
        }else if(motor.equalsIgnoreCase("MaxNN")){
            return new MaxNaN();
        }
        return null;
    }
}
