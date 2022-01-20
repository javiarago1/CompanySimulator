
package com.mycompany.game;

import java.awt.Font;
import java.time.LocalDate;
import javax.swing.ImageIcon;

public class CrearArquitecto extends CrearTecnico{
    public CrearArquitecto(String DNI, String genero, String nombre, String apellido, String nivel, int duracion, int horas, int sueldo, double rendimiento, String rendimientoRango, LocalDate fechaNacimiento, int edad, ImageIcon foto, String nacionalidad, String nacionalidadColor, String procedencia, Font fuenteFirma, String horario, int felicidad) {
        super(DNI, genero, nombre, apellido, nivel, duracion, horas, sueldo, rendimiento, rendimientoRango, fechaNacimiento, edad, foto, nacionalidad, nacionalidadColor, procedencia, fuenteFirma, horario, felicidad);
    }
  
}
