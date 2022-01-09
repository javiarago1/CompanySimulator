package com.mycompany.game;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ColorSueldoTable extends DefaultTableCellRenderer {
        final static Color verdeSueldo = new java.awt.Color(25, 111, 61);
        final Color naranjaSueldo = new java.awt.Color(147, 81, 22);
        final static Color rojoSueldo = new java.awt.Color(135, 54, 0);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);
        label.setHorizontalAlignment( JLabel.CENTER );
        String dato = value.toString();
        dato = dato.substring(0, dato.length() - 2);
        int num = Integer.parseInt(dato);
     
            if (num >= 0 && num < 50) {
                label.setForeground(verdeSueldo);
                GenerarEmpleados.contratos.get(row).setColorSueldo(verdeSueldo);
            } else if (num >= 50 && num < 80) {
                label.setForeground(naranjaSueldo);
                GenerarEmpleados.contratos.get(row).setColorSueldo(naranjaSueldo);
            } else {
                label.setForeground(rojoSueldo);
                GenerarEmpleados.contratos.get(row).setColorSueldo(rojoSueldo);
            }   
        return label;
    }

}
