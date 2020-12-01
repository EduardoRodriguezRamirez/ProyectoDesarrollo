
package erd.parser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class JTabla extends JFrame implements ActionListener{
    JTabla(MyTableModel model, String Nombre, ERDParser es){
    this.setSize(500,500);
    tabla(model);
    this.setTitle(Nombre);
    nom=Nombre;
    puente=es;
     this.add(panel1);
    }
    ERDParser puente;
    String nom;
    JScrollPane scrollPane;
    JTable table;
    JTextArea jtext;
    private JMenuBar menubar;
    private JMenu menu1;
    private JMenuItem menuitem1, menuitem2;
    JPanel panel1, panel2;
    MyTableModel modelo;
    
    public void tabla(MyTableModel model) {  

    modelo=model;
    table = new JTable(model); 
    scrollPane = new JScrollPane(table);
    table.setFillsViewportHeight(true); 
    scrollPane.setVisible(true);
    
    jtext= new JTextArea("aaaaaaaaaaaaaaaaaaa");
    jtext.setVisible(true);
    
    menubar = new JMenuBar();
    setJMenuBar(menubar);
    
    menu1 = new JMenu("Script");
    menubar.add(menu1);
    
    menuitem1 = new JMenuItem("Consulta");
    menuitem1.addActionListener(this);
    
    menuitem2 = new JMenuItem("Tabla");
    menuitem2.addActionListener(this);
    
    menu1.add(menuitem1);
    menu1.add(menuitem2);
    
    panel1 = new JPanel();
    panel1.add(scrollPane);
    panel1.setVisible(true);
    
    panel2 = new JPanel();
    panel2.add(jtext);
    panel2.setVisible(false);
    
    addCheckBox(5,table);
    addCheckBox(4,table);
    addCheckBox(6,table);
    addComboBox(1,table);
    SimpleTableDemo St = new SimpleTableDemo(table);
    }
    public void addCheckBox(int Column, JTable table){
    TableColumn tc = table.getColumnModel().getColumn(Column);
    tc.setCellEditor(table.getDefaultEditor(Boolean.class));
    tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }
    
    public void addComboBox(int Column, JTable table){
    TableColumn tc = table.getColumnModel().getColumn(Column);
    JComboBox CB= new JComboBox();
    CB.addItem("CHAR");
    CB.addItem("BOOLEAN");
    CB.addItem("CHARACTER VARYING");
    CB.addItem("DATE");
    CB.addItem("INTEGER");
    CB.addItem("NAME");
    CB.addItem("NUMERIC");
    CB.addItem("TEXT");
    tc.setCellEditor(new DefaultCellEditor(CB));
    }

    @Override
    public void actionPerformed(ActionEvent e) { 
        
        if(e.getSource() == menuitem1){
            this.add(panel2);   
            if(panel1.isVisible()){
                panel1.setVisible(false);               
                
            }
            panel2.setVisible(true);
            String consulta=Consulta();
            jtext.setText(quitarComa(consulta));
            
        }
        if(e.getSource() == menuitem2){
           
            if(panel2.isVisible()){
                panel2.setVisible(false);
                
            }
            panel1.setVisible(true);
        }
        
        
    }
    
    public String Consulta(){
        
            Object [][] datos=modelo.data;
            
            String consulta="CREATE TABLE "+nom+"(";
            String primarias="\nPRIMARY KEY (";
            String foraneas="";
            
            //try{
            for(int i=0;i<datos.length;i++){
                
               String nombre =(String)datos[i][0];
               Object foreing=datos[i][6];     
               Object primary=datos[i][5];
               
               
               if(foreing==null){
                   foreing=false;
               }
                        
               if(primary==null){
                   primary=false;
               }
               
               if((boolean)primary==true){
                   
                   primarias=primarias+nombre+",";
                   
               }
               
               if((boolean)foreing==true){
                   
                   foraneas=foraneas+"\nFOREIGN KEY ("+nombre+") REFERENCES "+encontrarLlave(nombre)+" ("+nombre+"),";
                   continue;
               }
               
               String Tipo=(String)datos[i][1];
                System.out.println(((boolean)foreing)==false);
               if(Tipo==null && ((boolean)foreing)==false){
                   JOptionPane.showMessageDialog(rootPane, "Agregue un tipo de variable para cada atributo");
                   break;
               }
               
               Object Longitud=datos[i][2];
               
               /*if(Longitud==null && ((boolean)foreing)==false){
                  if(Tipo.equalsIgnoreCase("char")){
                   JOptionPane.showMessageDialog(rootPane, "DEBE AGREGAR LONGITUD A TIPO CHAR");
                   continue;
                  }
               }*/
               
               String pres=datos[i][3]+"";
             
               Object Null1= datos[i][4];
               
               if(Null1==null){
                   Null1=false;
               }
                                         
               String atri="";
               
               atri="\n"+nombre+" "+Tipo;
               
               if((Tipo.equalsIgnoreCase("char")  && Longitud!=null)|| (Tipo.equalsIgnoreCase("character varying") && Longitud!=null) || (Tipo.equalsIgnoreCase("numeric" ) && Longitud!=null)){
                   atri=atri+"("+Longitud+") ";
               }
               if((boolean)Null1==true){
                   atri=atri+" NOT NULL";
               }
                             
               atri=atri+",";
               consulta=consulta+atri;
               
            }
            //}catch(Exception a){
            //    System.out.println(a);
            //}
            primarias=quitarComa(primarias);
            consulta=consulta+primarias+","+foraneas;
            return consulta;
    }
    
    public String encontrarLlave(String llave){
        return puente.llaveprimaria(llave);
    }
    public String quitarComa(String consulta){
        String consulta2="";
        for(int i=0;i<consulta.length()-1;i++){
            consulta2=consulta2+consulta.charAt(i);
        }
        return consulta2+")";
    }
}
