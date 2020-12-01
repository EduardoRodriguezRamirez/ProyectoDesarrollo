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
public class Max1aN implements Cardinalidades {

    @Override
    public void Cardinalidad(JSONObject c1, JSONObject c2, String nombre, Table Tabla,String Name, ArrayList<String> AtributosRel, ERDParser e) {
        
        String max1 = c1.getString("max");
        String max2 = c2.getString("max");
        
        if (max1.equalsIgnoreCase("1") && !max2.equalsIgnoreCase("1")) {
            
            if (c2.getString("entidad").equalsIgnoreCase(nombre)) {
                
                ArrayList<String> foreing = e.ObtenerForaneas(c1.getString("entidad"));
                
                for (int i = 0; i < foreing.size(); i++) {
                    
                    Tabla.setFK(foreing.get(i));
                    Tabla.add(foreing.get(i));
                    
                }
                
                for (int i = 0; i < AtributosRel.size(); i++) {
                    
                    Tabla.add(AtributosRel.get(i));
                    
                }
            }
            
        } else {
            
            if (c1.getString("entidad").equalsIgnoreCase(nombre)) {
                
                ArrayList<String> foreing = e.ObtenerForaneas(c2.getString("entidad"));
                
                for (int i = 0; i < foreing.size(); i++) {
                    
                    Tabla.setFK(foreing.get(i));
                    Tabla.add(foreing.get(i));
                    
                }
                
                for (int i = 0; i < AtributosRel.size(); i++) {
                    
                    Tabla.add(AtributosRel.get(i));
                    
                }
            }
        }
    }
    
}
