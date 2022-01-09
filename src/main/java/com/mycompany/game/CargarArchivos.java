package com.mycompany.game;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

public class CargarArchivos {
    protected static ArrayList<String> dniValidos = new ArrayList<>();
    protected static ArrayList<String> arrayOrigin = new ArrayList<>();
    protected static ArrayList<String> arrayHombre;
    protected static ArrayList<String> arrayMujer;
    protected static ArrayList<String> arrayApellidos;
    protected static Font reloj_Font, bolsa_Font, contract_Font, header_Font, titulos_Font,miFirma_Font;
    protected static ArrayList<Font> arrayFuentesRandom = new ArrayList<>();
    protected static ArrayList<ImageIcon> ArrayHombreJoven = new ArrayList<>();
    protected static ArrayList<ImageIcon> ArrayMujerJoven = new ArrayList<>();
    protected static ArrayList<ImageIcon> ArrayHombreMayor = new ArrayList<>();
    protected static ArrayList<ImageIcon> ArrayMujerMayor = new ArrayList<>();
    private static String[] archivosPath;
    protected static File f;
    protected static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

    protected static void cargarImagenes() {

        f = new File("src/main/java/images/images_avatar/jovenes_hombres/");
        archivosPath = f.list();
        for (String e : archivosPath) {
            ArrayHombreJoven.add(new javax.swing.ImageIcon(
                    "src/main/java/images/images_avatar/jovenes_hombres/" + e));
        }
        f = new File("src/main/java/images/images_avatar/jovenes_mujeres/");
        archivosPath = f.list();
        for (String e : archivosPath) {
            ArrayMujerJoven.add(new javax.swing.ImageIcon(
                    "src/main/java/images/images_avatar/jovenes_mujeres/" + e));
        }
        f = new File("src/main/java/images/images_avatar/mayores_hombres/");
        archivosPath = f.list();
        for (String e : archivosPath) {
            ArrayHombreMayor.add(new javax.swing.ImageIcon(
                    "src/main/java/images/images_avatar/mayores_hombres/" + e));
        }
        f = new File("src/main/java/images/images_avatar/mayores_mujeres/");
        archivosPath = f.list();
        for (String e : archivosPath) {
            ArrayMujerMayor.add(new javax.swing.ImageIcon(
                    "src/main/java/images/images_avatar/mayores_mujeres/" + e));
        }

    }

    protected static void cargarFuentesRandom() {
        f = new File("src/main/java/fonts/random_fonts/");
        archivosPath = f.list();
        for (String e : archivosPath) {
            try {
                arrayFuentesRandom.add(Font.createFont(Font.TRUETYPE_FONT,
                        new File("src/main/java/fonts/random_fonts/" + e)).deriveFont(
                                18f));
                ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(
                        "src/main/java/fonts/random_fonts/" + e)));
            } catch (FontFormatException | IOException ex) {
            }

        }

    }

    protected static void cargarFuentes() {
        try {
            //
            reloj_Font = Font.createFont(Font.TRUETYPE_FONT, new File(
                    "src/main/java/fonts/unique_fonts/DIGITAL-MONO.ttf")).deriveFont(
                    45f);

            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(
                    "src/main/java/fonts/unique_fonts/DIGITAL-MONO.ttf")));

            ////////////
            bolsa_Font = Font.createFont(Font.TRUETYPE_FONT, new File(
                    "src/main/java/fonts/unique_fonts/graf.ttf")).deriveFont(
                    22f);

            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(
                    "src/main/java/fonts/unique_fonts/graf.ttf")));
            ////////////
            header_Font = Font.createFont(Font.TRUETYPE_FONT, new File(
                    "src/main/java/fonts/unique_fonts/CaviarDreams.ttf")).deriveFont(
                    16f);

            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(
                    "src/main/java/fonts/unique_fonts/CaviarDreams.ttf")));

            ////////
            contract_Font = Font.createFont(Font.TRUETYPE_FONT, new File(
                    "src/main/java/fonts/unique_fonts/Roboto-Thin.ttf")).deriveFont(
                    20f);

            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(
                    "src/main/java/fonts/unique_fonts/Roboto-Thin.ttf")));
            
            titulos_Font = Font.createFont(Font.TRUETYPE_FONT, new File(
                    "src/main/java/fonts/unique_fonts/Walkway Expand SemiBold.ttf")).deriveFont(
                    20f);

            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(
                    "src/main/java/fonts/unique_fonts/Walkway Expand SemiBold.ttf")));
            
            miFirma_Font = Font.createFont(Font.TRUETYPE_FONT, new File(
                    "src/main/java/fonts/unique_fonts/Sallim.ttf")).deriveFont(
                    20f);

            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(
                    "src/main/java/fonts/unique_fonts/Sallim.ttf")));
            
            

            ///////
            UIManager.put("TableHeader.font", CargarArchivos.header_Font);
        } catch (FontFormatException | IOException ex) {
        }

    }

    protected static void cargarNombres() {

        try {
            File source = new File("src/main/java/names/male.txt");
            Scanner s = new Scanner(source, "UTF-8");
            arrayHombre = new ArrayList<>();
            while (s.hasNext()) {
                arrayHombre.add(s.nextLine());
            }

            source = new File("src/main/java/names/female.txt");
            s = new Scanner(source, "UTF-8");
            arrayMujer = new ArrayList<>();
            while (s.hasNext()) {
                arrayMujer.add(s.nextLine());
            }

            source = new File("src/main/java/names/surname.txt");
            s = new Scanner(source, "UTF-8");
            arrayApellidos = new ArrayList<>();
            while (s.hasNext()) {
                arrayApellidos.add(s.nextLine());
            }
            source = new File("src/main/java/names/origin.txt");
            s = new Scanner(source, "UTF-8");
            arrayOrigin = new ArrayList<>();
            while (s.hasNext()) {
                arrayOrigin.add(s.nextLine());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GenerarEmpleados.class.getName()).log(Level.SEVERE,
                    null, ex);
        }

    }
}
