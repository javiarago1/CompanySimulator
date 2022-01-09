package com.mycompany.game;

import java.util.Random;

public class LuckyClass {

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

        Random r = new Random();
        return r.nextInt(10 - 5) + 5;

    }

}
