package com.mycompany.game;

import java.awt.Font;
import java.time.LocalDate;
import javax.swing.ImageIcon;

public abstract class CrearTecnico extends CrearEmpleados {

    
    
    public CrearTecnico(String DNI, String genero, String nombre,
            String apellido, String nivel, int duracion, int horas, int sueldo,
            double rendimiento, String rendimientoRango,
            LocalDate fechaNacimiento, int edad, ImageIcon foto,
            String nacionalidad, String nacionalidadColor, String procedencia,
            Font fuenteFirma, String horario, int felicidad) {
        super(DNI, genero, nombre, apellido, nivel, duracion, horas, sueldo,
                rendimiento, rendimientoRango, fechaNacimiento, edad, foto,
                nacionalidad, nacionalidadColor, procedencia, fuenteFirma,
                horario, felicidad);
    }

    
    //public abstract void ();
}
