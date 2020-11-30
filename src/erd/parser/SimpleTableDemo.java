/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erd.parser;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author HP
 */
public class SimpleTableDemo implements TableModelListener {
    public SimpleTableDemo(JTable table){
        table.getModel().addTableModelListener(this);
    }
    @Override
    public void tableChanged(TableModelEvent e) {
            
            
    }
    
}
