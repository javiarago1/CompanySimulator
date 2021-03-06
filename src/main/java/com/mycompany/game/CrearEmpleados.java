package com.mycompany.game;

import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public abstract class CrearEmpleados {

    private Color colorSueldo;
    protected boolean traslado, seguro, trabajandoDeb, trabajando;
    private Font fuenteFirma;
    private String DNI, nombre, apellido, nivel, fechaNacimiento, genero, nacionalidad, nacionalidadColor, procedencia, firmaEmpleado, firmaEmpresa, horario;
    private ImageIcon foto;
    private LocalDate DatoLocal;
    private int horas, duracion, edad, sueldo;
    protected int felicidad;

    // Variable guardar renegociación
    private int duracionPosible, jornadaPosible, sueldoPosible;
    //

    //actividad 
    private ArrayList<String> actividadArray = new ArrayList<>();

    //
    protected boolean switcher_rendimiento, switcher_alerta, switcher_hilo;

    private boolean jornadaEntra, jornadaSale;

    private int fechaIncorporacion, fechaFinalizacion;

    protected boolean finContrato;
    private boolean empleadoRepeticion = true;

    protected int horaFinal, minutoFinal, horaHorario, minutoHorario, tempDay;
    private int contadorInternoTrabajando, contadorInternoTrabajandoFinal;
    private int contadorInternoFelicidad, contadorInternoFelicidadFinal;
    private int contadorInternoRenovar;
    protected int felicidadTempInicial;

    public CrearEmpleados(String DNI, String genero, String nombre,
            String apellido,
            String nivel, int duracion, int horas, int sueldo,
            LocalDate fechaNacimiento,
            int edad, ImageIcon foto, String nacionalidad,
            String nacionalidadColor, String procedencia, Font fuenteFirma,
            String horario, int felicidad) {
        this.felicidadTempInicial = felicidad;
        this.felicidad = felicidad;
        this.horario = horario;
        this.fuenteFirma = fuenteFirma;
        this.nacionalidad = nacionalidad;
        this.procedencia = procedencia;
        this.foto = foto;
        this.genero = genero;
        this.fechaNacimiento = establishFecha(fechaNacimiento);
        this.DatoLocal = fechaNacimiento;
        this.edad = edad;
        this.DNI = DNI;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nivel = nivel;
        this.sueldo = sueldo;
        this.horas = horas;
        this.duracion = duracion;
        this.nacionalidadColor = nacionalidadColor;
        this.contadorInternoTrabajandoFinal = LuckyClass.probabilidadTiempoFelicidadYTrabajo();
        this.contadorInternoFelicidadFinal = LuckyClass.probabilidadTiempoFelicidadYTrabajo();
        extractHorario();
    }

    protected final void extractHorario() {
        horaFinal = Integer.parseInt(horario.substring(8, 10));
        minutoFinal = Integer.parseInt(horario.substring(11, 13));

        if (Character.isWhitespace(horario.charAt(0))) {
            horaHorario = Character.getNumericValue(horario.charAt(1));
        } else {
            horaHorario = Integer.parseInt(horario.substring(0, 2));
        }
        minutoHorario = Integer.parseInt(horario.substring(3, 5));

    }

    public void checkWorkingHorario(int horaReal, int minutoReal, int diaReal) {
        this.trabajandoDeb = ((horaReal >= horaHorario && minutoReal >= minutoHorario || horaReal > horaHorario) && (horaReal < horaFinal || horaReal == horaFinal && minutoReal < minutoFinal) && (diaReal >= fechaIncorporacion && diaReal < fechaFinalizacion));

        this.finContrato = ((horaReal > horaFinal || horaReal == horaFinal && minutoReal >= minutoFinal) && (fechaFinalizacion - diaReal == 1) || (fechaFinalizacion - diaReal <= 0));

        if (!trabajandoDeb && trabajando) {
            trabajando = false;
            if (!jornadaSale) {
                jornadaSale = true;
                this.actividadArray.add(
                        Game.getDataTime() + "  -  Ha terminado su jornada.");
                Game.llenarListaEventos(this);
            }

            Game.abstractModelEmpleados.setValueAt(getStrWorking(),
                    GenerarEmpleados.empleados.indexOf(this), 2);

        } else if (tempDay != diaReal && trabajandoDeb) {
            trabajando = true;
            if (!jornadaEntra) {
                jornadaEntra = true;
                this.actividadArray.add(
                        Game.getDataTime() + "  -  Ha comenzado su jornada.");
                Game.llenarListaEventos(this);
            }

            Game.abstractModelEmpleados.setValueAt(getStrWorking(),
                    GenerarEmpleados.empleados.indexOf(this), 2);
            tempDay = diaReal;
        }

    }

    public abstract Object getAbstractRendimientoTemp();

    public abstract Object getAbstractRendimiento();

    public abstract String getAbstractRangoRendimiento();

    public String getEstadoContrato(int currentDate) {
        if (finContrato) {
            return "\u2022 " + "El contrato ha finalizado";
        } else if (fechaIncorporacion > currentDate) {
            return "\u2022 " + "Fecha de incorporación: día " + fechaIncorporacion;
        } else {
            int restante = fechaFinalizacion - currentDate;
            String wordString = "días";
            if (restante <= 1) {
                wordString = wordString.substring(0, wordString.length() - 1);
            }
            return "\u2022 " + "Tiempo restate de contrato: " + restante + " " + wordString;
        }
    }

    public ArrayList<String> getActividadArray() {
        return actividadArray;
    }

    public void setActividadArray(ArrayList<String> actividadArray) {
        this.actividadArray = actividadArray;
    }

    public int getDuracionPosible() {
        return duracionPosible;
    }

    public void setDuracionPosible(int duracionPosible) {
        this.duracionPosible = duracionPosible;
    }

    public int getJornadaPosible() {
        return jornadaPosible;
    }

    public void setJornadaPosible(int jornadaPosible) {
        this.jornadaPosible = jornadaPosible;
    }

    public int getSueldoPosible() {
        return sueldoPosible;
    }

    public void setSueldoPosible(int sueldoPosible) {
        this.sueldoPosible = sueldoPosible;
    }

    public boolean isEmpleadoRepeticion() {
        return empleadoRepeticion;
    }

    public void setEmpleadoRepeticion(boolean empleadoRepeticion) {
        this.empleadoRepeticion = empleadoRepeticion;
    }

    public boolean isFinContrato() {
        return finContrato;
    }

    public void setFinContrato(boolean finContrato) {
        this.finContrato = finContrato;
    }

    public boolean isSwitcher_hilo() {
        return switcher_hilo;
    }

    public void setSwitcher_hilo(boolean switcher_hilo) {
        this.switcher_hilo = switcher_hilo;
    }

    public boolean isSwitcher_alerta() {
        return switcher_alerta;
    }

    public void setSwitcher_alerta(boolean switcher_alerta) {
        this.switcher_alerta = switcher_alerta;
    }

    public int getContadorInternoRenovar() {
        return contadorInternoRenovar;
    }

    public void setContadorInternoRenovar(int contadorInternoRenovar) {
        this.contadorInternoRenovar = contadorInternoRenovar;
    }

    public int getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(int fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public int getFechaIncorporacion() {
        return fechaIncorporacion;
    }

    public void setFechaIncorporacion(int fechaIncorporacion) {
        this.fechaIncorporacion = fechaIncorporacion;
    }

    public int getContadorInternoTrabajando() {
        return contadorInternoTrabajando;
    }

    public void setContadorInternoTrabajando(int contadorInternoTrabajando) {
        this.contadorInternoTrabajando = contadorInternoTrabajando;
    }

    public int getContadorInternoTrabajandoFinal() {
        return contadorInternoTrabajandoFinal;
    }

    public void setContadorInternoTrabajandoFinal(
            int contadorInternoTrabajandoFinal) {
        this.contadorInternoTrabajandoFinal = contadorInternoTrabajandoFinal;
    }

    public int getContadorInternoFelicidad() {
        return contadorInternoFelicidad;
    }

    public void setContadorInternoFelicidad(int contadorInternoFelicidad) {
        this.contadorInternoFelicidad = contadorInternoFelicidad;
    }

    public int getContadorInternoFelicidadFinal() {
        return contadorInternoFelicidadFinal;
    }

    public void setContadorInternoFelicidadFinal(
            int contadorInternoFelicidadFinal) {
        this.contadorInternoFelicidadFinal = contadorInternoFelicidadFinal;
    }

    public boolean isSwitcher() {
        return switcher_rendimiento;
    }

    public void setSwitcher(boolean switcher) {
        this.switcher_rendimiento = switcher;
    }

    public int getFelicidadTempInicial() {
        return felicidadTempInicial;
    }

    public void setFelicidadTempInicial(int felicidadTempInicial) {
        this.felicidadTempInicial = felicidadTempInicial;
    }

    public String getStrWorking() {
        if (trabajando) {
            return "Working";
        }
        return "Not working";
    }

    public boolean isTrabajando() {
        return trabajando;
    }

    public void setTrabajando(boolean trabajando) {
        this.trabajando = trabajando;
    }

    public int getFelicidad() {
        return felicidad;
    }

    public void setFelicidad(int felicidad) {
        this.felicidad = felicidad;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public boolean isTrabajandoDeb() {
        return trabajandoDeb;
    }

    public void setTrabajandoDeb(boolean trabajando) {
        this.trabajandoDeb = trabajando;
    }

    public boolean getTraslado() {
        return traslado;
    }

    public void setTraslado(boolean traslado) {
        this.traslado = traslado;
    }

    public boolean getSeguro() {
        return seguro;
    }

    public void setSeguro(boolean seguro) {
        this.seguro = seguro;
    }

    public int getPrecioSeguro() {
        return this.duracion * Game.precioSeguro;
    }

    public String getFirmaEmpleado() {
        return firmaEmpleado;
    }

    public void setFirmaEmpleado(String firmaEmpleado) {
        this.firmaEmpleado = firmaEmpleado;
    }

    public String getFirmaEmpresa() {
        return firmaEmpresa;
    }

    public void setFirmaEmpresa(String firmaEmpresa) {
        this.firmaEmpresa = firmaEmpresa;
    }

    public String getProcedencia() {
        return procedencia;
    }

    public Font getFuenteFirma() {
        return fuenteFirma;
    }

    public void setFuenteFirma(Font fuenteFirma) {
        this.fuenteFirma = fuenteFirma;
    }

    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }

    public String getOriginName() {
        return this.procedencia.substring(0, this.procedencia.indexOf(":"));
    }

    public int getOriginPrice() {
        return Integer.parseInt(this.procedencia.substring(
                this.procedencia.indexOf(":") + 2,
                this.procedencia.length() - 1));
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getNacionalidadColor() {
        return nacionalidadColor;
    }

    public void setNacionalidadColor(String nacionalidadColor) {
        this.nacionalidadColor = nacionalidadColor;
    }

    public Color getColorSueldo() {
        return colorSueldo;
    }

    public void setColorSueldo(Color colorSueldo) {
        this.colorSueldo = colorSueldo;
    }

    public ImageIcon getFoto() {
        return foto;
    }

    public void setFoto(ImageIcon foto) {
        this.foto = foto;
    }

    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public LocalDate getDatoLocal() {
        return DatoLocal;
    }

    public void setDatoLocal(LocalDate DatoLocal) {
        this.DatoLocal = DatoLocal;
    }

    private String establishFecha(LocalDate fechaNacimiento) {
        return fechaNacimiento.format(DateTimeFormatter.ofLocalizedDate(
                FormatStyle.LONG));
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String funcion) {
        this.nivel = funcion;
    }

    public int getSueldo() {
        return sueldo;
    }

    public void setSueldo(int sueldo) {
        this.sueldo = sueldo;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

}
