package com.mycompany.game;

import java.awt.event.ActionEvent;
import java.util.Random;
import javax.swing.Timer;

public class LuckyClass {

    private static final Random r = new Random();

    protected static void probabilidadTrabajando(CrearEmpleados ex) {
        // 90 - ex.getFelicidad()
        boolean val = new Random().nextInt(1, 101) >= 50;
        if (val) {
            ex.setTrabajando(true);

        } else {
            ex.setTrabajando(false);

        }
        System.out.println(val);

    }

    protected static int probabilidadTiempoFelicidadYTrabajo() {
        return r.nextInt(10 - 5) + 5;

    }

    protected static int probabilidadTiempoIncorporacion() {
        return r.nextInt(3 - 1) + 1;

    }

    protected static int azarTiempoRenovar() {
        int a = r.nextInt(10 - 5) +5;
        System.out.println(a);
        return a;
    }

    protected static int extraerDatos(String var) {
        return Integer.valueOf(var.substring(0, var.indexOf(' ')));
    }

    private static int smallChecker(int temp) {
        if (temp >= 25) {
            return 25;
        } else if (temp <= 0) {
            return 0;
        }
        return temp;
    }

    private static int aumentadorDes(int elementoSolicitado,
            int elementoAnterior, int modificadorAumento,
            int modificadorDisminucion) {
        if (elementoSolicitado > elementoAnterior) {
            return (elementoSolicitado - elementoAnterior) * modificadorDisminucion;
        } else if (elementoAnterior > elementoSolicitado) {
            return -(elementoAnterior - elementoSolicitado) * modificadorAumento;
        }
        return 0;
    }

    private static int generarProbabilidadElementos(int elementoSolicitado,
            int elementoAnterior, int tipo) {
        int temp;
        switch (tipo) {
            case 0 -> {
                temp = ((elementoSolicitado * 50) / elementoAnterior) * 25 / 100;
                return smallChecker(temp);
            }
            case 1 -> {
                temp = ((elementoSolicitado * 50) / elementoAnterior) * 25 / 100 - aumentadorDes(
                        elementoAnterior, elementoSolicitado, 3, 3);
                return smallChecker(temp);
            }
            default -> {
                temp = ((elementoAnterior * 50) / elementoSolicitado) * 25 / 100 - aumentadorDes(
                        elementoSolicitado, elementoAnterior, 5, 5);
                return smallChecker(temp);
            }
        }
    }

    protected static int probabilidadRenovarContrato(String duracionString,
            String horasString, String sueldoString, CrearEmpleados ex) {

        //Datos obtenidos
        int duracion = extraerDatos(duracionString);
        int horas = extraerDatos(horasString);
        int sueldo = extraerDatos(sueldoString);

        //Probabilidad calculadad a partir de los datos
        int probDuracion = generarProbabilidadElementos(duracion,
                ex.getDuracion(), 0);
        int probSueldo = generarProbabilidadElementos(sueldo, ex.getSueldo(), 1);
        int probHoras = generarProbabilidadElementos(horas, ex.getHoras(), 2);
        int probFelicidad = ex.getFelicidad() * 25 / 100;

        System.out.println(
                "Probabilidad duracion: " + probDuracion + " Probabilidad horas" + probHoras + " Probabilidad sueldo " + probSueldo + " felicidad del momento" + probFelicidad);

        int probabilidad_final = probDuracion + probHoras + probSueldo + probFelicidad;
        return probabilidad_final;

    }

    private static void datosCambiados(CrearEmpleados ex) {
        int horas = ex.getJornadaPosible();
        int duracion = ex.getDuracionPosible();
        int sueldo = ex.getSueldoPosible();
        ex.setFinContrato(false);
        if (horas != ex.getHoras()) {
            ex.setHoras(horas);
            ex.setHorario(GenerarEmpleados.generarHorario(horas));
            ex.extractHorario();
        }
        if (duracion != ex.getDuracion()) {
            ex.setDuracion(duracion);
            ex.setFechaIncorporacion(Game.dias_reloj + 1);
            ex.setFechaFinalizacion(ex.getFechaIncorporacion() + duracion);
        }
        if (sueldo != ex.getSueldo()) {
            ex.setSueldo(sueldo);
        }

    }

    protected static void procesoAceptacion(int probabilidad, CrearEmpleados ex) {
        int numLista = GenerarEmpleados.empleados.indexOf(ex);
        GenerarEmpleados.empleados.remove(ex);
        Game.abstractModelEmpleados.fireTableRowsDeleted(numLista, numLista);
        boolean val = r.nextInt(1, 101) <= probabilidad;
        int tiempoEspera = r.nextInt(5000 - 3000) + 3000;
        Timer timer_proceso = new Timer(tiempoEspera, (ActionEvent e) -> {
            if (val) {
                datosCambiados(ex);
                GenerarEmpleados.empleados.add(ex);
                Game.abstractModelEmpleados.fireTableDataChanged();
            } else {
                Game.removerEmpleadoLista(ex);
            }
            ((Timer) e.getSource()).stop();
        });
        timer_proceso.setRepeats(false);
        timer_proceso.start();

    }

}
