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
public class Max1a1 implements Cardinalidades {
    
    @Override
    public void Cardinalidad(JSONObject c1, JSONObject c2, String nombre, Table Tabla,String Name, ArrayList<String> AtributosRel, ERDParser e) {
        
        String min1 = c1.getString("min");
        String min2 = c2.getString("min");
        String otra = "";

        //Si la participacion es obligatoria
        if (min1.equals("1") && min2.equals("1")) {
            
            //Encuentra cual de los 2 es el nombre de la tabla desconocida
            if (!nombre.equalsIgnoreCase(c1.getString("entidad"))) {
                
                otra = c2.getString("entidad");
                
            } else {
                
                otra = c1.getString("entidad");
                
            }         

            //Se obtienen los atributos de esa tabla
            ArrayList<String> atributos = e.obtenerAtributos(otra);
            
            //Se agregan a la tabla los atributos
            for (int i = 0; i < atributos.size(); i++) {

                if (atributos.get(i).contains("*")) {
                    
                    Tabla.setFK(atributos.get(i));
                    
                }
                
                Tabla.add(atributos.get(i));
                
            }
            
            for (int i = 0; i < AtributosRel.size(); i++) {
                
                Tabla.add(AtributosRel.get(i));
                
            }
        }
        
        if ((min1.equals("1") && !min2.equals("1"))) {
            
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
        
        if ((!min1.equals("1") && min2.equals("1"))) {
            
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
        }
        
        if (!min1.equals("1") && !min2.equals("1")) {
            
                if (c1.getString("entidad").equalsIgnoreCase(nombre)) {
                    
                    ArrayList<String> foreing = e.ObtenerForaneas(c2.getString("entidad"));
                    
                    for (int i = 0; i < foreing.size(); i++) {
                        
                        Tabla.setFK(foreing.get(i));
                        Tabla.add(foreing.get(i));
                        
                    }
                    
                    for (int i = 0; i < AtributosRel.size(); i++) {
                        
                        Tabla.add(AtributosRel.get(i));
                        
                    }
                    
                } else {
                    
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
                }
        }
    }
}
