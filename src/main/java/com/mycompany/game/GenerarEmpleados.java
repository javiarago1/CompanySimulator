package com.mycompany.game;

import java.awt.Font;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

public class GenerarEmpleados {

    public static int contador = 0;

    private final static Random r = new Random();
    private static int low, high, num;
    private static int tempFoto;
    protected static final DecimalFormat df = new DecimalFormat("0.00");

    private static double tempSalarium, tempA, tempC;

    private static int tempSueldo, tempHoras, tempEdad, tempDias;
    private static String tempGenero, tempNacionalidad;
    private static LocalDate tempLocalNacimiento;

    protected static List<CrearEmpleados> empleados = new ArrayList<>();
    protected static ArrayList<CrearEmpleados> contratos = new ArrayList<>();

    protected static void generarContratos() {
        contratos.add(new CrearProgramador(generarDNI(), generarGenero(),
                generarNombre(), generarApellido(),
                "Programador", generadorDias(), generadorHoras(),
                generarSueldo(), generarRendimiento(),
                generarRendimientoEstimado(),
                generarFechaNacimiento(), generarEdad(), generarFoto(),
                generarNacionalidad(), generarNacionalidadColor(),
                generarProcedencia(), generarFuenteFirma(), generarHorario(),GenerarFelicidad()));

    }

    private static String generarDNI() {
        low = 0;
        high = 23;
        char[] array = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};
        char letra = array[r.nextInt(high - low) + low];
        low = 10000000;
        high = 100000000;
        num = r.nextInt(high - low) + low;
        String DNI = String.valueOf(num) + letra;
        
        CargarArchivos.dniValidos.add(DNI);

        return DNI;
    }

    private static String generarGenero() {
        char[] arrayGeneros = {'H', 'H', 'H', 'H', 'H', 'M', 'M', 'M', 'M', 'M', 'S'};
        high = 11;
        low = 0;
        char x = arrayGeneros[r.nextInt(high - low) + low];
        switch (x) {
            case 'H' -> {
                tempGenero = "Hombre";
                return tempGenero;
            }
            case 'M' -> {
                tempGenero = "Mujer";
                return tempGenero;
            }
            default -> {
                tempGenero = "Sin genero";
                return tempGenero;
            }

        }
    }

    private static String generarApellido() {

        high = 25849;
        low = 0;
        num = r.nextInt(high - low) + low;
        return CargarArchivos.arrayApellidos.get(num);
    }

    private static String generarHombre() {
        low = 0;
        high = 24584;
        num = r.nextInt(high - low) + low;
        return CargarArchivos.arrayHombre.get(num);

    }

    private static String generarMujer() {
        low = 0;
        high = 24756;
        num = r.nextInt(high - low) + low;
        return CargarArchivos.arrayMujer.get(num);
    }

    private static String generarNombre() {

        switch (tempGenero) {

            case "Hombre" -> {
                return generarHombre();
            }
            case "Mujer" -> {
                return generarMujer();
            }
            default -> {
                boolean opt = r.nextBoolean();
                if (opt) {
                    return generarHombre();
                } else {
                    return generarMujer();
                }
            }
        }
    }

    private static ImageIcon generarFoto() {
        high = CargarArchivos.ArrayHombreJoven.size();
        low = 0;

        do {
            num = r.nextInt(high - low) + low;
        } while (tempFoto == num);
        tempFoto = num;

        switch (tempGenero) {

            case "Hombre" -> {
                if (tempEdad < 45) {

                    return CargarArchivos.ArrayHombreJoven.get(num);

                } else {
                    return CargarArchivos.ArrayHombreMayor.get(num);

                }
            }
            case "Mujer" -> {
                if (tempEdad < 45) {
                    return CargarArchivos.ArrayMujerJoven.get(num);

                } else {
                    return CargarArchivos.ArrayMujerMayor.get(num);

                }
            }
            default -> {
                ImageIcon nouser = new javax.swing.ImageIcon(
                        "");
                return nouser;
            }
        }
    }

    private static int generarSueldo() {
        low = 1200;
        high = 2600;
        num = r.nextInt(high - low) + low;

        double a = num;
        double b = tempHoras;
        double c = tempDias;

        tempSalarium = (a / ((30 / c) * (8 / b))) / c;

        // System.out.println(b + " horas");
        //System.out.println("Sueldo a pagar x hora:" + tempSalarium / b);
        tempSueldo = (int) tempSalarium;

        return tempSueldo;
    }

    private static double generarRendimiento() {
        double b = tempSalarium / tempHoras;
        double a = b - (0.4 * b);
        tempA = Double.parseDouble(df.format(a).replace(',', '.'));
        double c = b + (2 * b);
        tempC = Double.parseDouble(df.format(c).replace(',', '.'));
        double precision = 100;
        double genera = Math.floor(
                Math.random() * (a * precision - c * precision) + c * precision) / (1 * precision);
        String temp = df.format(genera);
        genera = Double.parseDouble(temp.replace(',', '.'));
        //System.out.println("Rendimiento: " + genera);
        return genera;
    }

    private static String generarRendimientoEstimado() {
        //System.out.println(String.valueOf(tempA) + "€/h - " + String.valueOf(
        //     tempC) + "€/h");
        return String.valueOf(tempA) + " €/h  -  " + String.valueOf(tempC) + " €/h";
    }

    private static int generadorHoras() {
        int[] array_horas = {8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 6, 6, 6, 7, 4, 4, 4, 9, 9, 12};
        high = 20;
        low = 0;
        num = r.nextInt(high - low) + low;
        tempHoras = array_horas[num];
        return tempHoras;
    }

    private static String fixMinutes(int temp) {
        if (temp < 10) {
            return "0" + temp;
        }
        return String.valueOf(temp);

    }

    private static String generarHorario() {
        int finalJornada = 20;
        String tempMinutos = "00";
        String tempInicial;
        int horaInicial;
        int horaFinal;
        int temp;

        int horaMaxima = finalJornada - tempHoras;
        System.out.println(tempHoras);
        high = horaMaxima + 1;
        low = 8;
        horaInicial = r.nextInt(high - low) + low;
        if (horaInicial < 10) {
            tempInicial = " " + horaInicial;
        } else {
            tempInicial = String.valueOf(horaInicial);
        }
        if (horaInicial != horaMaxima) {
            high = 60;
            low = 1;
            temp = r.nextInt(high - low) + low;
            tempMinutos = fixMinutes(temp);
        }
        horaFinal = horaInicial + tempHoras;

       //return tempInicial + ":" + tempMinutos + " - " + (horaFinal) + ":" + tempMinutos;
         return " "+8 + ":" + "0"+5 + " - " + "10" + ":" + "00";

    }

    private static int generadorDias() {
        high = 12;
        low = 3;
        num = r.nextInt(high - low) + low;
        tempDias = num;
        //return tempDias;
        return 1;
    }

    protected static int timerContrato() {
        low = 5000;
        high = 8000;
        num = r.nextInt(high - low) + low;
        return num;
    }

    private static LocalDate generarFechaNacimiento() {
        low = 5000;
        high = 24000;
        num = r.nextInt(high - low) + low;
        LocalDate fecha_nacimiento = LocalDate.now().minusDays(num);
        tempLocalNacimiento = fecha_nacimiento;
        return tempLocalNacimiento;
    }

    private static String generarNacionalidad() {
        String[] arrayPaises = {"ES", "ES", "ES", "ES", "ES", "FR", "PT"};
        high = arrayPaises.length;
        low = 0;
        num = r.nextInt(high - low) + low;
        tempNacionalidad = arrayPaises[num];
        return tempNacionalidad;
    }

    private static int generarEdad() {
        Period p = Period.between(tempLocalNacimiento, LocalDate.now());
        tempEdad = p.getYears();
        return tempEdad;
    }

    private static String generarProcedencia() {
        high = 10;
        low = 1;
        num = r.nextInt(high - low) + low;
        if (num >= 7) {
            return CargarArchivos.arrayOrigin.get(13);
        }
        high = CargarArchivos.arrayOrigin.size();
        low = 0;
        do {
            num = r.nextInt(high - low) + low;
        } while (num == 13);
        return CargarArchivos.arrayOrigin.get(num);
    }

    private static Font generarFuenteFirma() {
        high = CargarArchivos.arrayFuentesRandom.size();
        low = 0;
        num = r.nextInt(high - low) + low;
        // System.out.println(CargarArchivos.arrayFuentesRandom.get(num));
        return CargarArchivos.arrayFuentesRandom.get(num);

    }
    
    private static int GenerarFelicidad(){
        high = 91;
        low = 60;   
        num = r.nextInt(high -low)+low;    
        return num;
    }

    private static String generarNacionalidadColor() {
        generarProcedencia();
        String finalNacionalidad = "";
        String colorA = "";
        String colorB = "";
        String colorC = "";
        String a, b, c;
        int numColores = 3;
        switch (tempNacionalidad) {
            case "ES" -> {
                colorA = "#C70318";
                colorB = "#FCFF00";
                colorC = colorA;

                if (tempGenero.equals("Hombre") || tempGenero.equals(
                        "Sin genero")) {
                    finalNacionalidad = "Español";
                } else {
                    finalNacionalidad = "Española";
                }
            }
            case "FR" -> {

                colorA = "#001E96";
                colorB = "#FFFFFF";
                colorC = "#EE2436";
                if (tempGenero.equals("Hombre") || tempGenero.equals(
                        "Sin genero")) {
                    finalNacionalidad = "Francés";
                } else {
                    finalNacionalidad = "Francesa";
                }
            }
            case "PT" -> {
                colorA = "#006600";
                colorB = "#FE0000";
                colorC = colorB;

                if (tempGenero.equals("Hombre") || tempGenero.equals(
                        "Sin genero")) {
                    finalNacionalidad = "Portgués";
                } else {
                    finalNacionalidad = "Portuguesa";
                }
            }

        }

        int divisionBandera;

        int lengthFinal;

        int totalCaracteres = finalNacionalidad.length();
        divisionBandera = totalCaracteres / numColores;

        switch (totalCaracteres % numColores) {

            case 0 -> {
                a = finalNacionalidad.substring(0, divisionBandera);
                b = finalNacionalidad.substring(a.length(),
                        a.length() + divisionBandera);
                lengthFinal = a.length() + b.length();
                c = finalNacionalidad.substring(lengthFinal,
                        lengthFinal + divisionBandera);
                return "<html><p style='font-size:10px'><font color=" + colorA + ">" + a + "</font><font color=" + colorB + ">" + b + "</font><font color=" + colorC + ">" + c + "</font></p></html>";
            }
            case 1 -> {
                a = finalNacionalidad.substring(0, divisionBandera + 1);
                b = finalNacionalidad.substring(a.length(),
                        a.length() + divisionBandera);
                lengthFinal = a.length() + b.length();
                c = finalNacionalidad.substring(lengthFinal,
                        lengthFinal + divisionBandera);
                return "<html><p style='font-size:10px'><font color=" + colorA + ">" + a + "</font><font color=" + colorB + ">" + b + "</font><font color=" + colorC + ">" + c + "</font></p></html>";
            }
            default -> {
                a = finalNacionalidad.substring(0, divisionBandera + 1);
                b = finalNacionalidad.substring(a.length(),
                        a.length() + divisionBandera);
                lengthFinal = a.length() + b.length();
                c = finalNacionalidad.substring(lengthFinal,
                        lengthFinal + divisionBandera + 1);
                return "<html><p style='font-size:10px'><font color=" + colorA + ">" + a + "</font><font color=" + colorB + ">" + b + "</font><font color=" + colorC + ">" + c + "</font></p></html>";
            }
        }

    }

}
