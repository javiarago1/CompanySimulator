package com.mycompany.game;

import static com.mycompany.game.Game.abstractModelEmpleados;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import javax.swing.table.DefaultTableCellRenderer;

public class ColorFinTable extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                row, column);
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);
        l.setHorizontalAlignment(JLabel.CENTER);
        CrearEmpleados ex = GenerarEmpleados.empleados.get(row);

        if (!ex.isSwitcher_hilo() && ex.isFinContrato()) {
            int velocidad = ex.calculateLight();
            ex.setSwitcher_hilo(true);
            Runnable runner = () -> {
                try {
                    Thread.sleep(velocidad);
                    ex.setSwitcher_alerta(true);
                    abstractModelEmpleados.fireTableCellUpdated(row, column);
                    Thread.sleep(velocidad);
                    ex.setSwitcher_alerta(false);
                    abstractModelEmpleados.fireTableCellUpdated(row, column);
                    ex.setSwitcher_hilo(false);
                } catch (InterruptedException e) {
                }
            };
            Thread hilo = new Thread(runner);
            hilo.start();

        }
        if (ex.isSwitcher_alerta() && ex.isFinContrato()) {
            l.setForeground(new Color(182, 185, 87));
            return l;
        } else {
            l.setForeground(new Color(187, 187, 187));
            return l;
        }

    }
}
