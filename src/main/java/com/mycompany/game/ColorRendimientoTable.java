package com.mycompany.game;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ColorRendimientoTable extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);
        l.setHorizontalAlignment(JLabel.CENTER);
        CrearEmpleados ex = GenerarEmpleados.empleados.get(row);
        if (ex.isSwitcher()) {

            if (ex.getRendimientoTempInicial() > ex.getRendimiento()) {
                l.setForeground(ColorSueldoTable.ROJO_SUELDO);
            } else {
                l.setForeground(ColorSueldoTable.VERDE_SUELDO);
            }
        } else {
            l.setForeground(new java.awt.Color(187, 187, 187));
        }
        return l;

    }

}
