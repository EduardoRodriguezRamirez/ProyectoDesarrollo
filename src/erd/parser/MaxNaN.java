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
public class MaxNaN implements Cardinalidades{

    @Override
    public void Cardinalidad(JSONObject c1, JSONObject c2, String nombre, Table Tabla,String Name, ArrayList<String> AtributosRel, ERDParser e) {
        
            Table t = new Table(Name);
            
            ArrayList<String> llaves1 = e.ObtenerForaneas(c1.getString("entidad"));
            ArrayList<String> llaves2 = e.ObtenerForaneas(c2.getString("entidad"));
            
            for (int i = 0; i < llaves1.size(); i++) {
                
                t.setFK(llaves1.get(i));
                t.setPK(llaves1.get(i));
                t.add(llaves1.get(i));
                
            }
            for (int i = 0; i < llaves2.size(); i++) {
                
                t.setFK(llaves2.get(i));
                t.setPK(llaves2.get(i));
                t.add(llaves2.get(i));
                
            }
            for (int i = 0; i < AtributosRel.size(); i++) {
                
                t.add(AtributosRel.get(i));
                
            }
            
            e.tablas.add(t);
    }
    
}
