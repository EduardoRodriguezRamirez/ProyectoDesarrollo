/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erd.parser;

import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author HP
 */
public interface Cardinalidades {
    public void Cardinalidad(JSONObject c1, JSONObject c2, String nombre, Table Tabla, String Name,ArrayList<String> AtributosRel, ERDParser e);   
}
