package erd.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ERDParser {
    
    //Todas las variable globales
    JSONArray entidades;
    JSONArray debiles;
    JSONArray relaciones;
    ArrayList<Table> tablas = new ArrayList<>();
    String[] Columnas = {"Nombre", "Tipo Dato", "Longitud", "Precision", "No Nulo?", "Llave Primaria", "Llave Foranea"};
    Object[][] Atributos;

    //Metodo constructor
    ERDParser(String a) throws FileNotFoundException {
        FileReader fp = new FileReader(a);
        JSONTokener tokenizer = new JSONTokener(fp);
        JSONObject JSONDoc = new JSONObject(tokenizer);
        entidades = JSONDoc.getJSONArray("entidades");
        debiles = JSONDoc.getJSONArray("debiles");
        relaciones = JSONDoc.getJSONArray("relaciones");
    }

    //Main
    public static void main(String[] args) throws FileNotFoundException {
        ERDParser e = new ERDParser("university-erd.json");
        e.tablas();
        e.Debiles();
        e.crearTablas();
    }

    //Ciclo para crear las tablas
    public void crearTablas() {
        for (int i = 0; i < tablas.size(); i++) {
            hacerT(tablas.get(i));
        }
    }

    //Hacer la tabla con el modelo que le pasaron, inserta de un ArrayList a un Array
    public void hacerT(Table TablaA) {
        Object[][] datos = new Object[TablaA.atributes.size()][Columnas.length];
        
        for (int i = 0; i < TablaA.atributes.size(); i++) {
            
            if (TablaA.isFK(TablaA.atributes.get(i))) {
                datos[i][6] = true;
            } else {
                datos[i][6] = false;
            }
            
            datos[i][0] = TablaA.atributes.get(i);
            
            if (TablaA.isPK(TablaA.atributes.get(i))) {
                datos[i][4] = true;
                datos[i][5] = true;
            }else{
                datos[i][5] = false;
            }

        }
        
        JTabla jt = new JTabla(new MyTableModel(datos, Columnas), TablaA.name, this);
        
        jt.setVisible(true);
    }
    
    //Metodo para encontrar la entidad que corresponde a alguna llave primaria
    public String llaveprimaria(String llave) {
        Iterator it = entidades.iterator();
        String NombreEntidad = "";
        
        while (it.hasNext()) {
            
            JSONObject entidad = (JSONObject) it.next();
            String NombreTabla = entidad.getString("nombre");               
                
                JSONArray atributos = entidad.getJSONArray("atributos");
                Iterator attribIt = atributos.iterator();

                while (attribIt.hasNext()) {
                    
                    JSONObject atributo = (JSONObject) attribIt.next();
                    
                    if ((atributo.getInt("tipo") == 1) && atributo.getString("nombre").equalsIgnoreCase(llave)) {
                        
                        NombreEntidad = NombreTabla;
                        
                    }

                }
            
        }
        return NombreEntidad;
    }

    //Crear la tablas apartir de las entidades, despues crear las tablas de las
    //relaciones
    public void tablas() throws FileNotFoundException {
        Iterator it = entidades.iterator();
        
        while (it.hasNext()) {

            JSONObject entidad = (JSONObject) it.next();
            String NombreTabla = entidad.getString("nombre");

                Table TablaA = new Table(NombreTabla);
                JSONArray atributos = entidad.getJSONArray("atributos");
                Iterator attribIt = atributos.iterator();

                while (attribIt.hasNext()) {

                    JSONObject atributo = (JSONObject) attribIt.next();
                    TablaA.add(atributo.getString("nombre"));

                    if (atributo.getInt("tipo") == 1) {
                        
                        TablaA.setPK(atributo.getString("nombre"));

                    }
                }
                
                contieneRelaciones(NombreTabla, TablaA);
                tablas.add(TablaA);
                
        }
    }

    //Verifica el tipo de relaciones que tiene la entidad de antes
    public void contieneRelaciones(String nombre, Table tabla) {
        Iterator it = relaciones.iterator();

        while (it.hasNext()) {
            
            JSONObject rel = (JSONObject) it.next();
            boolean pasa = true;
            JSONArray cards = rel.getJSONArray("cardinalidades");
            String Nombre = rel.getString("nombre");
            JSONArray atributos = rel.getJSONArray("atributos");
            ArrayList<String> AtributosRel = new ArrayList<>();
            
            for (int i = 0; i < tablas.size(); i++) {
                
                if (tablas.get(i).name.equalsIgnoreCase(Nombre)) {
                    
                    pasa = false;
                    
                }
            }
         
            for (int i = 0; i < atributos.length(); i++) {
                
                JSONObject a = atributos.getJSONObject(i);
                AtributosRel.add(a.getString("nombre"));
                
            }
            
            if (pasa) {
                
                if (cards.length() == 2) {
                    
                    JSONObject e1 = cards.getJSONObject(0);
                    JSONObject e2 = cards.getJSONObject(1);
                    
                    if (e1.getString("entidad").equals(nombre) || e2.getString("entidad").equals(nombre)) {
                        
                        String c1 = e1.getString("max");
                        String c2 = e2.getString("max");

                        if (c1.equals("1") && c2.equals("1")) {
                            
                            Max11(e1, e2, nombre, tabla, AtributosRel);
                            
                        }
                        
                        if ((c1.equals("1") && !c2.equals("1")) || (!c1.equals("1") && c2.equals("1"))) {
                            
                            Max1N(e1, e2, nombre, tabla, AtributosRel);
                            
                        }
                        
                        if (!c1.equals("1") && !c2.equals("1")) {
                            
                            MaxNN(e1, e2, nombre, tabla, Nombre, AtributosRel);
                            
                        }
                    }
                    
                } else {
                    
                    multiple(cards, Nombre, AtributosRel);
                    
                }
            }
        }
    }
    
    //Se obtienen las entidades debiles despues de las entidades fuertes y relaciones
    public void Debiles() throws FileNotFoundException {
        Iterator itdeb = debiles.iterator();
        
        while (itdeb.hasNext()) {
            
            JSONObject debil = (JSONObject) itdeb.next();
            Table TablaA = new Table(debil.getString("nombre"));
            JSONArray atributos2 = debil.getJSONArray("atributos");
            Iterator itat = atributos2.iterator();

            while (itat.hasNext()) {
                
                JSONObject atributo2 = (JSONObject) itat.next();

                TablaA.add(atributo2.getString("nombre"));

                if (atributo2.getInt("tipo") == 1) {
                    
                    TablaA.setPK(atributo2.getString("nombre"));
                    
                }

            }
                     
            ArrayList<String> LlavesForaneas=obtenerF(debil.getString("fuerte"));
            
            for(int i=0;i<LlavesForaneas.size();i++){
                
                TablaA.add(LlavesForaneas.get(i));
                TablaA.setPK(LlavesForaneas.get(i));
                TablaA.setFK(LlavesForaneas.get(i));
                
            }
            
            
            tablas.add(TablaA);
        }
    }

    //En caso de tener multiples entidades una relacion se hace este proceso
    public void multiple(JSONArray cards, String Name, ArrayList<String> AtributosRel) {
        
        Table t = new Table(Name);
        
        for (int i = 0; i < cards.length(); i++) {
            
            JSONObject e1 = cards.getJSONObject(i);
            ArrayList<String> atributos = obtenerF(e1.getString("entidad"));
            
                for (int j = 0; j < atributos.size(); j++) {
                    
                    t.add(atributos.get(j));
                    t.setFK(atributos.get(j));
                    t.setPK(atributos.get(j));
                    
                }
        }
        
        for (int i = 0; i < AtributosRel.size(); i++) {
            
            t.add(AtributosRel.get(i));
            System.out.println(AtributosRel.get(i));
            
        }
        
        tablas.add(t);
        
    }

    //En caso de que la relacion sea de 1 a 1
    public void Max11(JSONObject c1, JSONObject c2, String nombre, Table Tabla, ArrayList<String> AtributosRel) {
        
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
            ArrayList<String> atributos = obtenerAtributos(otra);
            
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
                
                ArrayList<String> foreing = obtenerF(c2.getString("entidad"));
                
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
                
                ArrayList<String> foreing = obtenerF(c1.getString("entidad"));
                
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
                    
                    ArrayList<String> foreing = obtenerF(c2.getString("entidad"));
                    
                    for (int i = 0; i < foreing.size(); i++) {
                        
                        Tabla.setFK(foreing.get(i));
                        Tabla.add(foreing.get(i));
                        
                    }
                    
                    for (int i = 0; i < AtributosRel.size(); i++) {
                        
                        Tabla.add(AtributosRel.get(i));
                        
                    }
                    
                } else {
                    
                    if (c2.getString("entidad").equalsIgnoreCase(nombre)) {
                        
                        ArrayList<String> foreing = obtenerF(c1.getString("entidad"));
                        
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
    
    //En caso de que la relacion sea de 1 a muchos
    public void Max1N(JSONObject c1, JSONObject c2, String nombre, Table Tabla, ArrayList<String> AtributosRel) {
        
        String max1 = c1.getString("max");
        String max2 = c2.getString("max");
        
        if (max1.equalsIgnoreCase("1") && !max2.equalsIgnoreCase("1")) {
            
            if (c2.getString("entidad").equalsIgnoreCase(nombre)) {
                
                ArrayList<String> foreing = obtenerF(c1.getString("entidad"));
                
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
                
                ArrayList<String> foreing = obtenerF(c2.getString("entidad"));
                
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

    //En caso de que la relacion sea de muchos a muchos
    public void MaxNN(JSONObject c1, JSONObject c2, String nombre, Table Tabla, String Name, ArrayList<String> AtributosRel) {
        
            Table t = new Table(Name);
            ArrayList<String> llaves1 = obtenerF(c1.getString("entidad"));
            ArrayList<String> llaves2 = obtenerF(c2.getString("entidad"));
            
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
            
            tablas.add(t);
            
    }
    
    //Se obtienen los atributos de una entidad deseada con el nombre de la misma entidad
    public ArrayList<String> obtenerAtributos(String nombret) {
        
        Iterator it = entidades.iterator();
        ArrayList<String> atributos2 = new ArrayList<>();
        
        while (it.hasNext()) {
            
            JSONObject ent = (JSONObject) it.next();
            
            if (ent.getString("entidad").equalsIgnoreCase(nombret)) {
                
                JSONArray atributos = ent.getJSONArray("atributos");
                Iterator attribIt = atributos.iterator();
                
                while (attribIt.hasNext()) {
                    
                    JSONObject atributo = (JSONObject) attribIt.next();
                    
                    if (atributo.getInt("tipo") != 1) {
                        
                        atributos2.add(atributo.getString("nombre"));
                        
                    } else {
                        
                        atributos2.add(atributo.getString("nombre") + "*");
                        
                    }
                }

            }

        }
        
        return atributos2;
        
    }
    
    //Devuelve las llaves primarias de una entidad cualquiera
    public ArrayList<String> obtenerF(String nombret) {
        
        Iterator it = entidades.iterator();
        ArrayList<String> atributos2 = new ArrayList<>();
        
        while (it.hasNext()) {
            
            JSONObject ent = (JSONObject) it.next();
            
            if (ent.getString("nombre").equalsIgnoreCase(nombret)) {
                
                JSONArray atributos = ent.getJSONArray("atributos");
                Iterator attribIt = atributos.iterator();
                
                while (attribIt.hasNext()) {
                    
                    JSONObject atributo = (JSONObject) attribIt.next();
                    
                    if (atributo.getInt("tipo") == 1) {
                        
                        atributos2.add(atributo.getString("nombre"));
                        
                    }

                }

            }

        }
        return atributos2;
    }

    //Devuelve una llave principal de una entidad cualquiera
    public String PKDe(String entidad2) throws FileNotFoundException {
        String PK = "";
        Iterator it = entidades.iterator();
        
        while (it.hasNext()) {
            
            JSONObject entidad = (JSONObject) it.next();
            String NombreTabla = entidad.getString("nombre");
            JSONArray atributos = entidad.getJSONArray("atributos");
            Iterator attribIt = atributos.iterator();
            
            if (NombreTabla.equalsIgnoreCase(entidad2)) {
                
                while (attribIt.hasNext()) {
                    
                    JSONObject atributo = (JSONObject) attribIt.next();
                    
                    if (atributo.getInt("tipo") == 1) {
                        
                        PK = atributo.getString("nombre");
                        
                    }
                }
            }
        }
        
        return PK;
        
    }
}
