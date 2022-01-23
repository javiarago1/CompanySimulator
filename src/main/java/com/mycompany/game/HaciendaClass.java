package com.mycompany.game;

public class HaciendaClass {

    protected static int impuestoBeneficios = 15;
    protected static int impuestoSueldos=10;

    protected static int calcularImpuestoEmpresario() {
        int a = 20 * GenerarEmpleados.empleados.size();
        return a;
    }

    protected static int calcularImpuestoBeneficios(int beneficios) {
        if (beneficios<=0){
            return 0;
        }
        else {
            return beneficios * impuestoBeneficios;
        }
    }

    
    protected static int calcularTasa(){
        int numEmpleados = GenerarEmpleados.empleados.size();
        if (numEmpleados<=10){
            return 50;
        }
        else if (numEmpleados<=20){
            return 100;
        }
        else {
            return 200;
        }
    }
}
