/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erd.parser;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class RelationTableModel extends DefaultTableModel {
    public RelationTableModel(Object[][] datos, Object[] columnas){
        super(datos, columnas);
    }
    
    @Override
    public boolean isCellEditable(int renglon, int columna){
        return columna > 0;
    }
    
    @Override
    public Class<?> getColumnClass(int columnaI){
        Class claz = String.class;
        switch(columnaI){
            case 0:
                claz = String.class;
                break;
            case 1:
                claz = String.class;
                break;
            case 2:
                claz = String.class;
                break;
            case 3:
                claz = String.class;
                break;   
            case 4:
                claz = String.class;
                break;
            case 5:
                claz = String.class;
                break;      
        }
        return claz;    
    }
}
