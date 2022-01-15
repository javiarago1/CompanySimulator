package com.mycompany.game;

import java.util.Random;

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
        int a = r.nextInt(50 - 40) + 40;
        System.out.println(a);
        return a;
    }

    private static int extraerDatos(String var) {
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

    private static int generarProbabilidadElementos(int elementoSolicitado,
            int elementoAnterior, int tipo) {
        int temp;
        if (tipo == 0) {
            temp = ((elementoSolicitado * 50) / elementoAnterior) * 25 / 100;
            return smallChecker(temp);
        } else {
            int restar = 0;
            if (elementoSolicitado > elementoAnterior) {
                restar = (elementoSolicitado - elementoAnterior) * 10;
            } else if (elementoAnterior > elementoSolicitado) {
                restar = -(elementoAnterior - elementoSolicitado) * 10;
            }
            temp = ((elementoAnterior * 50) / elementoSolicitado) * 25 / 100 - restar;
            return smallChecker(temp);
        }
    }

    protected static void probabilidadRenovarContrato(String duracionString,
            String horasString, String sueldoString, CrearEmpleados ex) {
        int duracion = extraerDatos(duracionString);
        int horas = extraerDatos(horasString);
        int sueldo = extraerDatos(sueldoString);

        int probDuracion = generarProbabilidadElementos(duracion,
                ex.getDuracion(), 0);
        int probHoras = generarProbabilidadElementos(horas, ex.getHoras(), 1);
        int probSueldo = generarProbabilidadElementos(sueldo, ex.getSueldo(), 0);

        int probFelicidad = generarProbabilidadElementos(ex.getFelicidad(), 80,
                0);

        System.out.println(
                "Probabilidad duracion: " + probDuracion + " Probabilidad horas" + probHoras + " Probabilidad sueldo " + probSueldo + " felicidad del momento" + probFelicidad);

        int probabilidad_final = probDuracion + probHoras + probSueldo + probFelicidad;
        System.out.println(probabilidad_final);
        boolean val = new Random().nextInt(1, 101) <= probabilidad_final;
        System.out.println(val);

    }

}
