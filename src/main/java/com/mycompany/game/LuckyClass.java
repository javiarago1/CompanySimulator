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
        int a =  r.nextInt(15-10)+10;
        System.out.println(a);
        return a;
    }

}
