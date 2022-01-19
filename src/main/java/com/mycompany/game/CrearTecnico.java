package com.mycompany.game;

import java.awt.Font;
import java.time.LocalDate;
import javax.swing.ImageIcon;

public abstract class CrearTecnico extends CrearEmpleados {
    protected double rendimientoTempInicial;
    protected double rendimiento;
    protected String rendimientoRango;
    
    public CrearTecnico(String DNI, String genero, String nombre,
            String apellido, String nivel, int duracion, int horas, int sueldo,
            double rendimiento, String rendimientoRango,
            LocalDate fechaNacimiento, int edad, ImageIcon foto,
            String nacionalidad, String nacionalidadColor, String procedencia,
            Font fuenteFirma, String horario, int felicidad) {
        super(DNI, genero, nombre, apellido, nivel, duracion, horas, sueldo,
                fechaNacimiento, edad, foto,
                nacionalidad, nacionalidadColor, procedencia, fuenteFirma,
                horario, felicidad);
        this.rendimiento = rendimiento;
        this.rendimientoRango=rendimientoRango;
        this.rendimientoTempInicial=rendimiento;
        
    }

 
    public double getRendimientoXminuto() {
        return rendimiento / 40;
    }

    @Override
    public Object getAbstractRendimientoTemp(){
        return rendimientoTempInicial;
    }
    
    @Override 
    public String getAbstractRangoRendimiento (){
        return this.rendimientoRango;
    }
    
    @Override
    public Object getAbstractRendimiento() {
        return rendimiento;
    }

    @Override
    public void checkWorkingHorario(int horaReal, int minutoReal, int diaReal) {
        super.checkWorkingHorario(horaReal, minutoReal, diaReal);
        if (switcher_rendimiento) {
            Game.abstractModelEmpleados.fireTableCellUpdated(
                    GenerarEmpleados.empleados.indexOf(this), 1);
            switcher_rendimiento = false;
        }
        if (this.felicidad != this.felicidadTempInicial) {
            double op = this.felicidadTempInicial - this.felicidad;
            this.felicidadTempInicial = felicidad;
            rendimientoTempInicial=this.rendimiento;
            this.rendimiento = (Double.parseDouble(GenerarEmpleados.df.format(
                    ((100 - op) / 100) * this.rendimiento).replace(",", ".")));
            Game.abstractModelEmpleados.fireTableCellUpdated(
                    GenerarEmpleados.empleados.indexOf(this), 1);
            switcher_rendimiento = true;
        }

    }
}
