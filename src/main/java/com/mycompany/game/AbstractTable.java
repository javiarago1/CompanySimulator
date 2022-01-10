package com.mycompany.game;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class AbstractTable extends AbstractTableModel {

    private final String[] tableHeader = {"Trabajo", "Rendimiento", "Estado"};
    private final List<CrearEmpleados> listEmpleados;

    public AbstractTable(List<CrearEmpleados> listaEmpleados) {
        this.listEmpleados = listaEmpleados;

    }

    @Override
    public int getRowCount() {
        return listEmpleados.size();
    }

    @Override
    public int getColumnCount() {
        return tableHeader.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0 -> {
                return listEmpleados.get(rowIndex).getNivel();
            }

            case 1 -> {
                return listEmpleados.get(rowIndex).getRendimiento() + " €/h";
            }

            case 2 -> {
                return listEmpleados.get(rowIndex).getStrWorking();
            }

        }
        return null;

    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);
        super.fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public void fireTableRowsInserted(int firstRow, int lastRow) {
        super.fireTableRowsInserted(firstRow, lastRow);
    }
    
    @Override
    public String getColumnName(int column) {
        return tableHeader[column];
    }

}
