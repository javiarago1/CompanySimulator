package com.mycompany.game;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ColorSueldoTable extends DefaultTableCellRenderer {

    final static Color VERDE_SUELDO = new java.awt.Color(25, 111, 61);
    final static Color NARANJA_SUELDO = new java.awt.Color(147, 81, 22);
    final static Color ROJO_SUELDO = new java.awt.Color(135, 54, 0);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);
        label.setHorizontalAlignment(JLabel.CENTER);
        String dato = value.toString();
        dato = dato.substring(0, dato.length() - 2);
        int num = Integer.parseInt(dato);
        Color colorSueldo = colorSueldo(num);
        label.setForeground(colorSueldo);
        GenerarEmpleados.contratos.get(row).setColorSueldo(colorSueldo);
        return label;
    }

    protected static Color colorSueldo(int num) {
        if (num >= 0 && num < 50) {
            return VERDE_SUELDO;
        } else if (num >= 50 && num < 80) {
            return NARANJA_SUELDO;
        } else {
            return ROJO_SUELDO;
        }
    }
}
