package com.mycompany.game;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import javax.swing.table.DefaultTableCellRenderer;

public class ColorWorkingTable extends DefaultTableCellRenderer {

    
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);
        l.setHorizontalAlignment(JLabel.CENTER);
        String dato = String.valueOf(value);
                
        if (dato.equals("Working")) {
            l.setForeground(ColorSueldoTable.VERDE_SUELDO);
            

        } else {
            l.setForeground(ColorSueldoTable.ROJO_SUELDO);
        }

        return l;
    }

}
