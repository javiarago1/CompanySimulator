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
    
    protected static int azarTiempoRenovar(){
        int a =  r.nextInt(50-40)+40;
        System.out.println(a);
        return a;
    }
    
    protected static boolean probabilidadRenovarContrato(int duracion,int horas,int sueldo, CrearEmpleados ex){
        int probDuracion = Math.abs((duracion*100)/ex.getDuracion()-100);
        int probHoras = Math.abs((horas*100)/ex.getHoras()-100);
        int probSueldo = Math.abs((sueldo*100)/ex.getSueldo()-100);
        
        int probabilidad_final = probDuracion+probHoras+probSueldo+ex.getProbabilidadContratado(); 
        return new Random().nextInt(1,101)<=probabilidad_final;
    }

}
