
package erd.parser;

import javax.swing.table.AbstractTableModel;


public class MyTableModel extends AbstractTableModel {
    private final String[] columnNames;
    Object[][] data;
    MyTableModel(Object[][] datos, String[] Columnas) {
        this.columnNames=Columnas;
        this.data=datos;
    }
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }
    @Override
     public boolean isCellEditable(int row, int col) {
        if (col==0 || (col==5) || col==6) {
            return false;
        } else {
            return true;
        }
    }
    @Override
     public Class getColumnClass(int c) {
         Class claz = String.class;
        switch(c){
            case 0:
                claz = String.class;
                break;
            case 1:
                claz = String.class;
                break;
            case 2:
                claz = Integer.class;
                break;
            case 3:
                claz = Integer.class;
                break;   
            case 4:
                claz = Boolean.class;
                break;
            case 5:
                claz = Boolean.class;
                break;  
            case 6:
                claz = Boolean.class;
                break;  
        }
        return claz;   
    }

     public void setValueAt(Object value, int row, int col) {     
        data[row][col] = value;        
        fireTableCellUpdated(row, col);    
    }
}
