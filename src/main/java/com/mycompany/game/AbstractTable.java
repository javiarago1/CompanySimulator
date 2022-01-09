package com.mycompany.game;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class AbstractTable extends AbstractTableModel {

    
    private String [] tableHeader = {"Trabajo","Rendimiento","Estado"};
    private List<CrearEmpleados> listEmpleados;

    
    
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

    public void añadirRow() {
        fireTableRowsInserted(listEmpleados.size() - 1, listEmpleados.size() - 1);
    }

    @Override
    public String getColumnName(int column) {
        return tableHeader[column];
    }
    
    

}
