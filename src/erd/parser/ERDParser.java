/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erd.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author rnavarro
 */
public class ERDParser {

    /**
     * @param args the command line arguments
     */
    Object [][] Atributos ;
    public static void main(String[] args) throws FileNotFoundException{
        ERDParser e = new ERDParser();
        e.tablas();

    }
    public void tablas() throws FileNotFoundException{
        FileReader fp = new FileReader("university-erd.json");

        JSONTokener tokenizer = new JSONTokener(fp);

        JSONObject JSONDoc = new JSONObject(tokenizer);

        JSONArray names = JSONDoc.names();
        
        System.out.println(names);

        JSONArray entidades = JSONDoc.getJSONArray("entidades");

        Iterator it = entidades.iterator();
        
        
        String [] Columnas={"Nombre","Tipo Dato","Longitud","Precision","No Nulo?","Llave Primaria"};
        ArrayList<String> ar= new ArrayList<>();
        
        while (it.hasNext()) {

            JSONObject entidad = (JSONObject) it.next();
            
            String NombreTabla = entidad.getString("nombre");
            
            JSONArray atributos = entidad.getJSONArray("atributos");
            
            Iterator attribIt = atributos.iterator();
            
            int k=0;
            ArrayList<Integer> PK = new ArrayList<>();
            while (attribIt.hasNext()) {
                JSONObject atributo = (JSONObject) attribIt.next();
                ar.add(k, atributo.getString("nombre"));                   
                        
                if (atributo.getInt("tipo") == 1) {
                    PK.add(k);
                    System.out.println("Llave primaria: "+atributo.getString("nombre")+" Numero: "+k);
                }
                k++;
            }
            Object [][] datos= new Object [ar.size()][Columnas.length];
            
            for(int i=0;i<ar.size();i++){
                datos[i][0]=ar.get(i);
                if(PK.contains(i)){
                    datos[i][5]=true;
                    datos[i][4]=true;
                }
            }                
            JTabla jt = new JTabla(new MyTableModel(datos,Columnas),NombreTabla);
            jt.setVisible(true);
            //ArrayList<String> FK= verR(JSONDoc, NombreTabla); 
            ar.clear();
            
        }        
        JSONArray debiles = JSONDoc.getJSONArray("debiles");
        Iterator itdeb = debiles.iterator();
        ArrayList<String> ar2= new ArrayList<>();
        while(itdeb.hasNext()){
            JSONObject debil = (JSONObject) itdeb.next();

            JSONArray atributos2 = debil.getJSONArray("atributos");
            
            Iterator itat = atributos2.iterator();
            int k=0;
            ArrayList<Integer> PK = new ArrayList<>();
            while(itat.hasNext()){
                JSONObject atributo2 = (JSONObject) itat.next();
                
                ar2.add(k, atributo2.getString("nombre"));
                if (atributo2.getInt("tipo") == 1) {
                    PK.add(k);
                    System.out.println("Llave primaria: "+atributo2.getString("nombre")+" Numero: "+k);
                }
                k++;                     
            }
            Object [][] datos2= new Object[ar2.size()+1][Columnas.length];
            datos2[0][0]=PKDe(debil.getString("fuerte"));
            datos2[0][5]=true;
            datos2[0][4]=true;
            for(int i=0;i<ar2.size();i++){
                datos2[i+1][0]=ar2.get(i);
                if(PK.contains(i)){
                    datos2[i+1][5]=true;
                    datos2[i+1][4]=true;
                }
            }
            RelationTableModel model = new RelationTableModel(datos2,Columnas);
            JTabla jt = new JTabla(new MyTableModel(datos2, Columnas),debil.getString("nombre"));
            jt.setVisible(true);
            
        }   
    }
    public String PKDe(String entidad2) throws FileNotFoundException{
        String PK="";
        FileReader fp = new FileReader("university-erd.json");
        
        JSONTokener tokenizer = new JSONTokener(fp);
        
        JSONObject JSONDoc = new JSONObject(tokenizer);
        
        JSONArray names = JSONDoc.names(); 
        
        System.out.println(names);
        
        JSONArray entidades = JSONDoc.getJSONArray("entidades");
        
        Iterator it = entidades.iterator();
        while (it.hasNext()) {
            JSONObject entidad = (JSONObject) it.next();
            
            String NombreTabla = entidad.getString("nombre");
            
            JSONArray atributos = entidad.getJSONArray("atributos");
            
            Iterator attribIt = atributos.iterator();
            if(NombreTabla.equalsIgnoreCase(entidad2)){
            while (attribIt.hasNext()) {
                JSONObject atributo = (JSONObject) attribIt.next();
                
                if (atributo.getInt("tipo") == 1) {
                    PK=atributo.getString("nombre");
                }
            }
            }
        }          
        return PK;
    }
    public int NumTablas(JSONObject JSONDoc){
        JSONArray entidades = JSONDoc.getJSONArray("entidades");
        JSONArray debiles = JSONDoc.getJSONArray("debiles");
        JSONArray relaciones = JSONDoc.getJSONArray("relaciones");
        
        int tablas = entidades.length();
        tablas=tablas+debiles.length();   
        
        Iterator it = relaciones.iterator();

        while (it.hasNext()) {
            JSONObject rel = (JSONObject) it.next();


            JSONArray cards = rel.getJSONArray("cardinalidades");

            int n = cards.length();
            if(n>2){
                tablas++;
            }else{
                JSONObject e1 = cards.getJSONObject(0);
                JSONObject e2 = cards.getJSONObject(1);
                if(!e1.getString("max").equals("1") && !e2.getString("max").equals("1")){
                    tablas++;
                }       
        }
        }
        
        return tablas;
    }
    public ArrayList<String> verR(JSONObject JSONDoc, String Name){
        JSONArray relations = JSONDoc.getJSONArray("relaciones");
        ArrayList<String> a = new ArrayList<>();
        Iterator it = relations.iterator();

        while (it.hasNext()) {
            JSONObject rel = (JSONObject) it.next();

            System.out.println(rel.getString("nombre") );

            JSONArray cards = rel.getJSONArray("cardinalidades");

            int n = cards.length();
            if(n>2){
                
            }else{
                JSONObject e1 = cards.getJSONObject(0);
                JSONObject e2 = cards.getJSONObject(1);
                int caso;
                if(e1.getString("entidad").equalsIgnoreCase(Name) || e2.getString("entidad").equalsIgnoreCase(Name) ){
                    if((e1.getString("max").equals("1") && !e2.getString("max").equals("1"))||(!e1.getString("max").equals("1") && e2.getString("max").equals("1"))){
                        caso=1;
                    }
                    if(e1.getString("max").equals("1") && e2.getString("max").equals("1")){
                        if(e1.getString("min").equals("1") && e2.getString("min").equals("1")){
                            caso=2;
                        }else{
                            caso=3;
                        }
                        switch(caso){
                            case 1:
                                if(e1.getString("entidad").equalsIgnoreCase(Name)){
                                    if(!e1.getString("max").equals("1")){
                                        e2.getString("entidad");
                                    }
                                }else{
                                
                                }
                                break;
                            case 3:
                                break;
                        
                        }
                        
                        
                }
                
            /*for (int i = 0; i < n; i++) {
                JSONObject e1 = cards.getJSONObject(i);

                if(e1.getString("entidad").equals(Name)){
                    if(n>2){
                        
                    }
                }
                System.out.printf("\t%s (%s,%s)\n", e1.getString("entidad"),
                        e1.getString("min"),
                        e1.getString("max"));

            }*/
            }
        }
    }
        return null;
    }
}
