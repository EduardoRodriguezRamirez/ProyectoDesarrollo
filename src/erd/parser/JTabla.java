
package erd.parser;

import java.util.*;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class JTabla extends JFrame {
    JTabla(MyTableModel model, String Nombre){
    this.setSize(500,500);
    tabla(model);
    this.setTitle(Nombre);
    this.add(scrollPane);
    
    }
    JScrollPane scrollPane;
    JTable table;
    public void tabla(MyTableModel model) {          
    table = new JTable(model); 
    scrollPane = new JScrollPane(table);
    table.setFillsViewportHeight(true); 
    scrollPane.setVisible(true); 
    addCheckBox(5,table);
    addCheckBox(4,table);
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
    CB.addItem("BIT");
    CB.addItem("BOOLEAN");
    CB.addItem("BYTEA");
    CB.addItem("CHARACTER VARYING");
    CB.addItem("CHARACTER");
    CB.addItem("DATE");
    CB.addItem("INTEGER");
    CB.addItem("JSON");
    CB.addItem("NAME");
    CB.addItem("NUMERIC");
    CB.addItem("TEXT");
    CB.addItem("REAL");
    tc.setCellEditor(new DefaultCellEditor(CB));
    }
    
}
