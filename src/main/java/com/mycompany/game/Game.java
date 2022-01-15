package com.mycompany.game;

import com.formdev.flatlaf.FlatDarkLaf;

import java.awt.Color;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;

import javax.swing.table.DefaultTableCellRenderer;

import javax.swing.table.DefaultTableModel;

public final class Game extends javax.swing.JFrame {

    protected static final AbstractTable abstractModelEmpleados = new AbstractTable(
            GenerarEmpleados.empleados);

    protected static Point locationMain;

    DecimalFormat formateador = new DecimalFormat("#,###.##");
    private double dineroEmpresa = 500;
    private double tempTotal, tempDineroEmpresa;

    protected static int selectedRow;

    private int minutos_reloj = 0;
    private int horas_reloj = 10;
    private int dias_reloj = 1;
    private Timer Writer;
    private Timer timer_contrato;
    private Timer timer_borrar;
    private Timer timer_añadir;
    private Timer timer_reloj;
    private Timer timer_dinero;

    protected static boolean contratoIsVisible;

    private int contadorLabel, contadorContrato;
    private static int tiempo = 1000;

    private ArrayList<CrearEmpleados> arrayEliminados;
    private final ArrayList<JLabel> ArrayLabel = new ArrayList<>();
    private final ArrayList<String> ArrayStrings = new ArrayList<>();

    public void defect() {
        MenuPanel1.setVisible(true);
        MenuPanel2.setVisible(false);
        MenuPanel3.setVisible(false);
    }

    private void controlDinero() {
        timer_dinero = new Timer(1500, (ActionEvent e) -> {
            SwingWorker<Void, Double> worker_dinero = new SwingWorker<Void, Double>() {
                @Override
                protected Void doInBackground() throws Exception {
                    tempTotal = 0;
                    for (int i = 0; i < GenerarEmpleados.tecnicos.size(); i++) {
                        if (GenerarEmpleados.tecnicos.get(i).isTrabajando()) {
                            tempTotal += GenerarEmpleados.tecnicos.get(i).getRendimientoXminuto();
                        }
                    }
                    dineroEmpresa += tempTotal;
                    double doubleDinero = dineroEmpresa;
                    publish(doubleDinero);
                    Thread.sleep(500);
                    return null;
                }

                @Override
                protected void process(List<Double> chunks) {
                    Double valor = chunks.get(chunks.size() - 1);
                    dineroEmpresaLabel.setText(
                            formateador.format(valor) + " €");
                    if (valor > tempDineroEmpresa) {
                        dineroEmpresaLabel.setForeground(Color.GREEN);
                    } else if (valor < tempDineroEmpresa) {
                        dineroEmpresaLabel.setForeground(Color.RED);
                    } else {
                        dineroEmpresaLabel.setForeground(Color.WHITE);
                    }
                    tempDineroEmpresa = dineroEmpresa;
                }

                @Override
                protected void done() {
                    dineroEmpresaLabel.setForeground(Color.WHITE);
                }

            };
            worker_dinero.execute();

        });
        timer_dinero.start();
    }

    private void añadirRow() {
        timer_añadir = new Timer(1000, (ActionEvent e) -> {
            if (GenerarEmpleados.contratos.size() < 8) {
                GenerarEmpleados.generarContratos();

                CrearEmpleados elemento = GenerarEmpleados.contratos.get(
                        GenerarEmpleados.contratos.size() - 1);

                Object[] row = new Object[]{
                    getDataTime(),
                    elemento.getNivel(),
                    elemento.getSueldo() + " €",
                    elemento.getOriginName(),
                    elemento.getHoras() + " horas",};

                TablaModelContratos.addRow(row);
                tiempo = GenerarEmpleados.timerContrato();
                timer_añadir.setDelay(100);

            }
        });

        timer_añadir.start();
    }

    private String getDataTime() {
        String tempHora = LabelHoras.getText() + LabelMinutos.getText();
        return tempHora;
    }

    private void borrarRow() {
        timer_borrar = new Timer(30000, (ActionEvent e) -> {
            if (!GenerarEmpleados.contratos.isEmpty()) {
                Game.TablaModelContratos.removeRow(0);
                GenerarEmpleados.contratos.remove(0);
                timer_borrar.setDelay(GenerarEmpleados.timerContrato());
            }
        });
        timer_borrar.setInitialDelay(60000);
        timer_borrar.start();
    }

    private void relojTimer() {
        String temp = String.valueOf("0" + minutos_reloj);
        Game.LabelMinutos.setText(temp);
        temp = String.valueOf("" + horas_reloj + ":");
        Game.LabelHoras.setText(temp);
        timer_reloj = new Timer(1000, (ActionEvent e) -> {
            minutos_reloj++;
            if (minutos_reloj == 60) {
                horas_reloj++;
                minutos_reloj = 0;
                Game.LabelMinutos.setText("0");
                if ((horas_reloj >= 8) && (horas_reloj <= 9)) {
                    Game.LabelHoras.setText(String.valueOf(
                            "0" + horas_reloj + ":"));
                } else {
                    Game.LabelHoras.setText(String.valueOf(horas_reloj + ":"));
                }
            }
            if ((minutos_reloj >= 0) && (minutos_reloj <= 9)) {
                Game.LabelMinutos.setText("0" + String.valueOf(minutos_reloj));
            } else {
                Game.LabelMinutos.setText(String.valueOf(minutos_reloj));
            }
            if (horas_reloj == 11) {
                dias_reloj++;
                LabelDias.setText("Dia: " + dias_reloj);
                horas_reloj = 10;
                LabelHoras.setText("0" + String.valueOf(horas_reloj) + ":");
                minutos_reloj = 0;
                LabelMinutos.setText(String.valueOf("0" + minutos_reloj));
            }
            restarCapacidad();
        });
        timer_reloj.setRepeats(true);
        timer_reloj.setCoalesce(true);
        timer_reloj.start();

    }

    private void restarCapacidad() {
        arrayEliminados = new ArrayList<>();
        for (CrearEmpleados ex : GenerarEmpleados.empleados) {
            ex.checkWorkingHorario(horas_reloj, minutos_reloj, dias_reloj);
            if (ex.isTrabajando()) {

                ex.setContadorInternoTrabajando(
                        ex.getContadorInternoTrabajando() + 1);
                if (ex.getContadorInternoTrabajando() == ex.getContadorInternoTrabajandoFinal()) {
                    //LuckyClass.probabilidadTrabajando(ex);
                    abstractModelEmpleados.fireTableCellUpdated(
                            GenerarEmpleados.empleados.indexOf(ex), 2);
                    ex.setContadorInternoTrabajando(0);
                    ex.setContadorInternoTrabajandoFinal(
                            LuckyClass.probabilidadTiempoFelicidadYTrabajo());
                }
                ex.setContadorInternoFelicidad(
                        ex.getContadorInternoFelicidad() + 1);
                if (ex.getContadorInternoFelicidad() == ex.getContadorInternoFelicidadFinal()) {
                    System.out.println("he restado xd");
                    ex.setFelicidad(ex.getFelicidad() + 1);
                    ex.setContadorInternoFelicidad(0);
                    ex.setContadorInternoFelicidadFinal(
                            LuckyClass.probabilidadTiempoFelicidadYTrabajo());
                }

            } else if (ex.isFinContrato()) {
                renovarMet(ex);
            }
        }
        if (arrayEliminados.size() > -1) {
            GenerarEmpleados.empleados.removeAll(arrayEliminados);
        }
    }

    private void rellenarFieldsRenovacion() {
        FieldRenovarDuracion.setText(GenerarEmpleados.empleados.get(
                TablaEmpleados.getSelectedRow()).getDuracion() + " días");
        FieldRenovarJornada.setText(GenerarEmpleados.empleados.get(
                TablaEmpleados.getSelectedRow()).getHoras() + " horas");
        FieldRenovarSueldo.setText(GenerarEmpleados.empleados.get(
                TablaEmpleados.getSelectedRow()).getSueldo() + " €");

    }

    private void renovarMet(CrearEmpleados ex) {

        if (ex.isEmpleadoRepeticion()) {
            ex.setContadorInternoRenovar(LuckyClass.azarTiempoRenovar());
        }

        ex.setContadorInternoRenovar(ex.getContadorInternoRenovar() - 1);
        if (GenerarEmpleados.empleados.indexOf(ex) == TablaEmpleados.getSelectedRow() && !AnimationPanelEmpleados.isVisible()) {
            RenovarBoton.setText(
                    "Renovar contrato (" + ex.getContadorInternoRenovar() + ")");
            if (!PanelRenovacion.isVisible()) {
                PanelRenovacion.setVisible(true);
                rellenarFieldsRenovacion();
                PanelDatosDefecto.setVisible(false);
            }
        }
        ex.setEmpleadoRepeticion(false);
        if (ex.getContadorInternoRenovar() <= 0) {
            PanelRenovacion.setVisible(false);
            int num = GenerarEmpleados.empleados.indexOf(ex);
            if (ex instanceof CrearTecnico crearTecnico) {
                GenerarEmpleados.tecnicos.remove(crearTecnico);
            }
            arrayEliminados.add(ex);
            abstractModelEmpleados.fireTableRowsDeleted(num, num);
            RenovarBoton.setText("Renovar contrato");

        }

    }

    private void llenarArrayLabel() {
        ArrayLabel.add(DniLabel);
        ArrayLabel.add(NameLabel);
        ArrayLabel.add(FechaLabel);
        ArrayLabel.add(GeneroLabel);
        ArrayLabel.add(TrabajoLabel);
        ArrayLabel.add(SueldoLabel);
        ArrayLabel.add(HorasLabel);
        ArrayLabel.add(DuracionLabel);
        ArrayLabel.add(RendimientoLabel);
        ArrayLabel.add(FirmaTrabajadorLabel);

    }

    private void cargarModels() {
        TablaModelContratos = (DefaultTableModel) TablaContratos.getModel();

    }

    private void misComps() {
        llenarArrayLabel();
        defect();
        añadirRow();
        borrarRow();
        relojTimer();
        controlDinero();
        cargarModels();
    }

    private void cargarArchivos() {
        CargarArchivos.cargarFuentes();
        CargarArchivos.cargarFuentesRandom();
        CargarArchivos.cargarImagenes();
        CargarArchivos.cargarNombres();

    }

    public Game() {
        cargarArchivos();
        initComponents();
        misComps();
        this.setLocationRelativeTo(null);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelIzquierdo = new javax.swing.JPanel();
        VerEmpelados = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        ContrarEmpleados = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        PanelSuperior = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        LabelHoras = new javax.swing.JLabel();
        LabelMinutos = new javax.swing.JLabel();
        dineroEmpresaLabel = new javax.swing.JLabel();
        LabelDias = new javax.swing.JLabel();
        MenuPanel1 = new javax.swing.JPanel();
        AnimationPanelEmpleados = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        PanelDatosPersonales1 = new javax.swing.JPanel();
        unLabelName1 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        NameLabel1 = new javax.swing.JLabel();
        DniLabel1 = new javax.swing.JLabel();
        unLabelDni1 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        unFechaLabel2 = new javax.swing.JLabel();
        unNacionalidadLabel1 = new javax.swing.JLabel();
        GeneroLabel1 = new javax.swing.JLabel();
        FechaLabel1 = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator17 = new javax.swing.JSeparator();
        PanelFoto1 = new javax.swing.JPanel();
        LabelFoto1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        unGeneroLabel1 = new javax.swing.JLabel();
        NacionalidadLabel1 = new javax.swing.JLabel();
        PanelDatosConntrato1 = new javax.swing.JPanel();
        HorasLabel1 = new javax.swing.JLabel();
        TrabajoLabel1 = new javax.swing.JLabel();
        unTrabajoLabel1 = new javax.swing.JLabel();
        unFechaLabel3 = new javax.swing.JLabel();
        jSeparator18 = new javax.swing.JSeparator();
        unLabelName3 = new javax.swing.JLabel();
        DuracionLabel1 = new javax.swing.JLabel();
        unLabelSueldo1 = new javax.swing.JLabel();
        SueldoLabel1 = new javax.swing.JLabel();
        jSeparator19 = new javax.swing.JSeparator();
        jSeparator20 = new javax.swing.JSeparator();
        unRendimientoLabel1 = new javax.swing.JLabel();
        RendimientoLabel1 = new javax.swing.JLabel();
        jSeparator21 = new javax.swing.JSeparator();
        CerrarContrato = new javax.swing.JButton();
        DatosPersonales1 = new javax.swing.JLabel();
        SeguroCheckBox1 = new javax.swing.JCheckBox();
        TrasladoCheckBox1 = new javax.swing.JCheckBox();
        unFirmaRepresentanteLabel1 = new javax.swing.JLabel();
        unFirmaTrabajadorLabel1 = new javax.swing.JLabel();
        FirmaRepresentante1 = new javax.swing.JTextField();
        jSeparator22 = new javax.swing.JSeparator();
        FirmaTrabajadorLabel1 = new javax.swing.JLabel();
        TransLayerEmpleados = new javax.swing.JLabel();
        PaneEmpleados = new javax.swing.JScrollPane();
        TablaEmpleados = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        PanelInfoEmpleado = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        LabelFechas = new javax.swing.JLabel();
        unHorarioLabel = new javax.swing.JLabel();
        ProgressBarFelicidad = new javax.swing.JProgressBar();
        HorarioLabel = new javax.swing.JLabel();
        PanelRenovacion = new javax.swing.JPanel();
        RenovarBoton = new javax.swing.JButton();
        EliminarBoton = new javax.swing.JButton();
        CheckBoxSeguroRenovacion = new javax.swing.JCheckBox();
        PanelRenovarFields = new javax.swing.JPanel();
        FieldRenovarDuracion = new javax.swing.JTextField();
        FieldRenovarJornada = new javax.swing.JTextField();
        FieldRenovarSueldo = new javax.swing.JTextField();
        jSeparator23 = new javax.swing.JSeparator();
        PanelDatosDefecto = new javax.swing.JPanel();
        BotonSancionar = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        BotonDespedir = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox<>();
        jSeparator24 = new javax.swing.JSeparator();
        jSeparator25 = new javax.swing.JSeparator();
        jSeparator26 = new javax.swing.JSeparator();
        VerContratoEmpleados = new javax.swing.JButton();
        MenuPanel2 = new javax.swing.JPanel();
        AnimationPanel = new javax.swing.JPanel();
        PanelDatosPersonales = new javax.swing.JPanel();
        unLabelName = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        NameLabel = new javax.swing.JLabel();
        DniLabel = new javax.swing.JLabel();
        unLabelDni = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        unFechaLabel = new javax.swing.JLabel();
        unNacionalidadLabel = new javax.swing.JLabel();
        GeneroLabel = new javax.swing.JLabel();
        FechaLabel = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        PanelFoto = new javax.swing.JPanel();
        LabelFoto = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        unGeneroLabel = new javax.swing.JLabel();
        NacionalidadLabel = new javax.swing.JLabel();
        PanelDatosConntrato = new javax.swing.JPanel();
        HorasLabel = new javax.swing.JLabel();
        TrabajoLabel = new javax.swing.JLabel();
        unTrabajoLabel = new javax.swing.JLabel();
        unFechaLabel1 = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        unLabelName2 = new javax.swing.JLabel();
        DuracionLabel = new javax.swing.JLabel();
        unLabelSueldo = new javax.swing.JLabel();
        SueldoLabel = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        jSeparator15 = new javax.swing.JSeparator();
        unRendimientoLabel = new javax.swing.JLabel();
        RendimientoLabel = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JSeparator();
        ContratarButton = new javax.swing.JButton();
        DescartarButton = new javax.swing.JButton();
        DatosPersonales = new javax.swing.JLabel();
        SeguroCheckBox = new javax.swing.JCheckBox();
        TrasladoCheckBox = new javax.swing.JCheckBox();
        unFirmaRepresentanteLabel = new javax.swing.JLabel();
        unFirmaTrabajadorLabel = new javax.swing.JLabel();
        FirmaRepresentante = new javax.swing.JTextField();
        jSeparator13 = new javax.swing.JSeparator();
        FirmaTrabajadorLabel = new javax.swing.JLabel();
        TransLayer = new javax.swing.JLabel();
        TituloContratos = new javax.swing.JLabel();
        PaneContratos = new javax.swing.JScrollPane();
        TablaContratos = new javax.swing.JTable();
        ShowContractButton = new javax.swing.JButton();
        MenuPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelIzquierdo.setBackground(new java.awt.Color(58, 72, 77));
        PanelIzquierdo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        VerEmpelados.setBackground(new java.awt.Color(58, 72, 77));
        VerEmpelados.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        VerEmpelados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VerEmpeladosMouseClicked(evt);
            }
        });
        VerEmpelados.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon("src/main/java/images/general/workers.png"));
        jLabel3.setText(" Empleados");
        VerEmpelados.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 9, 130, 30));

        PanelIzquierdo.add(VerEmpelados, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 150, 50));

        ContrarEmpleados.setBackground(new java.awt.Color(58, 72, 77));
        ContrarEmpleados.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ContrarEmpleados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ContrarEmpleadosMouseClicked(evt);
            }
        });
        ContrarEmpleados.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon("src/main/java/images/general/add.png"));
        jLabel5.setText(" Contratar");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ContrarEmpleados.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 2, 130, 30));

        PanelIzquierdo.add(ContrarEmpleados, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 150, 40));

        jSeparator1.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));
        PanelIzquierdo.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 130, 10));

        jSeparator2.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));
        PanelIzquierdo.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 130, 10));

        getContentPane().add(PanelIzquierdo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 430));

        PanelSuperior.setBackground(new java.awt.Color(76, 94, 100));
        PanelSuperior.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(76, 94, 100));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LabelHoras.setFont(CargarArchivos.reloj_Font);
        LabelHoras.setForeground(new java.awt.Color(255, 255, 255));
        LabelHoras.setText("0");
        jPanel1.add(LabelHoras, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 70, 40));

        LabelMinutos.setFont(CargarArchivos.reloj_Font);
        LabelMinutos.setForeground(new java.awt.Color(255, 255, 255));
        LabelMinutos.setText("0");
        jPanel1.add(LabelMinutos, new org.netbeans.lib.awtextra.AbsoluteConstraints(93, 0, 50, 40));

        PanelSuperior.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 0, 140, 40));

        dineroEmpresaLabel.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        dineroEmpresaLabel.setText("500");
        PanelSuperior.add(dineroEmpresaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        LabelDias.setText("Dia 1");
        PanelSuperior.add(LabelDias, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 40, -1, -1));

        getContentPane().add(PanelSuperior, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 675, 120));

        MenuPanel1.setBackground(new java.awt.Color(60, 64, 65));
        MenuPanel1.setLayout(null);

        AnimationPanelEmpleados.setVisible(false);
        AnimationPanelEmpleados.setBackground(new java.awt.Color(192, 192, 192));
        AnimationPanelEmpleados.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 0, true));
        AnimationPanelEmpleados.setPreferredSize(new java.awt.Dimension(500, 300));
        AnimationPanelEmpleados.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        AnimationPanelEmpleados.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, -30, -1, -1));

        PanelDatosPersonales1.setBackground(new java.awt.Color(231, 231, 231));
        PanelDatosPersonales1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(146, 146, 146)));
        PanelDatosPersonales1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        unLabelName1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unLabelName1.setForeground(new java.awt.Color(121, 121, 121));
        unLabelName1.setText("Nombre:");
        PanelDatosPersonales1.add(unLabelName1, new org.netbeans.lib.awtextra.AbsoluteConstraints(148, 3, -1, 30));

        jSeparator8.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator8.setForeground(new java.awt.Color(135, 135, 135));
        PanelDatosPersonales1.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 65, 410, 10));

        NameLabel1.setBackground(new java.awt.Color(52, 55, 57));
        NameLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        NameLabel1.setForeground(new java.awt.Color(99, 99, 99));
        NameLabel1.setText("Javier Aragoneses");
        PanelDatosPersonales1.add(NameLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(218, 10, -1, -1));

        DniLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        DniLabel1.setForeground(new java.awt.Color(99, 99, 99));
        DniLabel1.setText("50633883G");
        PanelDatosPersonales1.add(DniLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 8, -1, 20));

        unLabelDni1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unLabelDni1.setForeground(new java.awt.Color(121, 121, 121));
        unLabelDni1.setText("NIF:");
        PanelDatosPersonales1.add(unLabelDni1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 3, 30, 30));

        jSeparator9.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator9.setForeground(new java.awt.Color(135, 135, 135));
        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);
        PanelDatosPersonales1.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 33, 9, 68));

        unFechaLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unFechaLabel2.setForeground(new java.awt.Color(121, 121, 121));
        unFechaLabel2.setText("Fecha de nacimiento:");
        PanelDatosPersonales1.add(unFechaLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 42, -1, -1));

        unNacionalidadLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unNacionalidadLabel1.setForeground(new java.awt.Color(121, 121, 121));
        unNacionalidadLabel1.setText("Nacionalidad:");
        PanelDatosPersonales1.add(unNacionalidadLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(176, 73, -1, 20));

        GeneroLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        GeneroLabel1.setForeground(new java.awt.Color(99, 99, 99));
        GeneroLabel1.setText("Sin genero");
        PanelDatosPersonales1.add(GeneroLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 73, -1, 20));

        FechaLabel1.setBackground(new java.awt.Color(52, 55, 57));
        FechaLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        FechaLabel1.setForeground(new java.awt.Color(99, 99, 99));
        FechaLabel1.setText("28 de septiembre de 2003");
        PanelDatosPersonales1.add(FechaLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 42, 230, -1));

        jSeparator10.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator10.setForeground(new java.awt.Color(135, 135, 135));
        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);
        PanelDatosPersonales1.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(158, 71, 10, 22));

        jSeparator12.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator12.setForeground(new java.awt.Color(135, 135, 135));
        PanelDatosPersonales1.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 32, 480, 10));

        jSeparator17.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator17.setForeground(new java.awt.Color(135, 135, 135));
        jSeparator17.setOrientation(javax.swing.SwingConstants.VERTICAL);
        PanelDatosPersonales1.add(jSeparator17, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 6, 8, 22));

        PanelFoto1.setBackground(new java.awt.Color(239, 239, 239));
        PanelFoto1.setForeground(new java.awt.Color(255, 255, 255));
        PanelFoto1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LabelFoto1.setIcon(new javax.swing.ImageIcon("src/main/java/images/nouser.png"));
        PanelFoto1.add(LabelFoto1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, -1, 70));

        jLabel9.setFont(new java.awt.Font("Arial", 0, 48)); // NOI18N
        jLabel9.setText("?");
        PanelFoto1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 8, -1, -1));

        PanelDatosPersonales1.add(PanelFoto1, new org.netbeans.lib.awtextra.AbsoluteConstraints(412, 33, 67, 66));

        unGeneroLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unGeneroLabel1.setForeground(new java.awt.Color(121, 121, 121));
        unGeneroLabel1.setText("Genero:");
        PanelDatosPersonales1.add(unGeneroLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 73, -1, 20));

        NacionalidadLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        NacionalidadLabel1.setForeground(new java.awt.Color(99, 99, 99));
        NacionalidadLabel1.setText("Española");
        PanelDatosPersonales1.add(NacionalidadLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 75, -1, -1));

        AnimationPanelEmpleados.add(PanelDatosPersonales1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 480, 100));

        PanelDatosConntrato1.setBackground(new java.awt.Color(231, 231, 231));
        PanelDatosConntrato1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(146, 146, 146)));
        PanelDatosConntrato1.setForeground(new java.awt.Color(215, 215, 215));
        PanelDatosConntrato1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        HorasLabel1.setBackground(new java.awt.Color(52, 55, 57));
        HorasLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        HorasLabel1.setForeground(new java.awt.Color(99, 99, 99));
        HorasLabel1.setText("10 horas");
        PanelDatosConntrato1.add(HorasLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, -1, -1));

        TrabajoLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        TrabajoLabel1.setForeground(new java.awt.Color(99, 99, 99));
        TrabajoLabel1.setText("Programador");
        PanelDatosConntrato1.add(TrabajoLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 8, -1, 20));

        unTrabajoLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unTrabajoLabel1.setForeground(new java.awt.Color(117, 117, 117));
        unTrabajoLabel1.setText("Trabajo:");
        PanelDatosConntrato1.add(unTrabajoLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 3, -1, 30));

        unFechaLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unFechaLabel3.setForeground(new java.awt.Color(117, 117, 117));
        unFechaLabel3.setText("Jornada:");
        PanelDatosConntrato1.add(unFechaLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(342, 10, -1, -1));

        jSeparator18.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator18.setForeground(new java.awt.Color(135, 135, 135));
        PanelDatosConntrato1.add(jSeparator18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 32, 480, 10));

        unLabelName3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unLabelName3.setForeground(new java.awt.Color(117, 117, 117));
        unLabelName3.setText("Duración:");
        PanelDatosConntrato1.add(unLabelName3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 3, -1, 30));

        DuracionLabel1.setBackground(new java.awt.Color(52, 55, 57));
        DuracionLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        DuracionLabel1.setForeground(new java.awt.Color(99, 99, 99));
        DuracionLabel1.setText("30 días");
        PanelDatosConntrato1.add(DuracionLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 10, -1, -1));

        unLabelSueldo1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unLabelSueldo1.setForeground(new java.awt.Color(117, 117, 117));
        unLabelSueldo1.setText("Salario diario:");
        PanelDatosConntrato1.add(unLabelSueldo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 37, -1, 30));

        SueldoLabel1.setBackground(new java.awt.Color(52, 55, 57));
        SueldoLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        SueldoLabel1.setForeground(new java.awt.Color(99, 99, 99));
        SueldoLabel1.setText("76€");
        PanelDatosConntrato1.add(SueldoLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(112, 44, -1, -1));

        jSeparator19.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator19.setForeground(new java.awt.Color(135, 135, 135));
        jSeparator19.setOrientation(javax.swing.SwingConstants.VERTICAL);
        PanelDatosConntrato1.add(jSeparator19, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 6, 8, 22));

        jSeparator20.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator20.setForeground(new java.awt.Color(135, 135, 135));
        jSeparator20.setOrientation(javax.swing.SwingConstants.VERTICAL);
        PanelDatosConntrato1.add(jSeparator20, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 6, 10, 22));

        unRendimientoLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unRendimientoLabel1.setForeground(new java.awt.Color(117, 117, 117));
        unRendimientoLabel1.setText("Rendimiento real:");
        PanelDatosConntrato1.add(unRendimientoLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(173, 37, -1, 30));

        RendimientoLabel1.setBackground(new java.awt.Color(52, 55, 57));
        RendimientoLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        RendimientoLabel1.setForeground(new java.awt.Color(99, 99, 99));
        RendimientoLabel1.setText("8.56€/h");
        PanelDatosConntrato1.add(RendimientoLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(305, 44, -1, -1));

        jSeparator21.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator21.setForeground(new java.awt.Color(135, 135, 135));
        jSeparator21.setOrientation(javax.swing.SwingConstants.VERTICAL);
        PanelDatosConntrato1.add(jSeparator21, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 40, 8, 22));

        AnimationPanelEmpleados.add(PanelDatosConntrato1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 135, 480, 70));

        DescartarButton.setFocusable(false);
        CerrarContrato.setBackground(new java.awt.Color(52, 68, 98));
        CerrarContrato.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        CerrarContrato.setText("Cerrar");
        CerrarContrato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CerrarContratoActionPerformed(evt);
            }
        });
        AnimationPanelEmpleados.add(CerrarContrato, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 150, 30));

        DatosPersonales1.setFont(CargarArchivos.contract_Font);
        DatosPersonales1.setForeground(new java.awt.Color(88, 88, 88));
        DatosPersonales1.setText("DATOS DE CONTRATO");
        AnimationPanelEmpleados.add(DatosPersonales1, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 7, -1, 20));

        SeguroCheckBox.setFocusable(false);
        SeguroCheckBox1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        SeguroCheckBox1.setForeground(new java.awt.Color(99, 99, 99));
        SeguroCheckBox1.setText("Pagar seguro de vida del empleado: 120€");
        SeguroCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SeguroCheckBox1ActionPerformed(evt);
            }
        });
        AnimationPanelEmpleados.add(SeguroCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, -1, -1));

        TrasladoCheckBox.setFocusable(false);
        TrasladoCheckBox1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        TrasladoCheckBox1.setForeground(new java.awt.Color(99, 99, 99));
        TrasladoCheckBox1.setText("Pagar traslado desde Castilla la Mancha: 40€");
        TrasladoCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TrasladoCheckBox1ActionPerformed(evt);
            }
        });
        AnimationPanelEmpleados.add(TrasladoCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, -1, 20));

        unFirmaRepresentanteLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        unFirmaRepresentanteLabel1.setForeground(new java.awt.Color(63, 63, 63));
        unFirmaRepresentanteLabel1.setText("Firma de la empresa:");
        AnimationPanelEmpleados.add(unFirmaRepresentanteLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 252, -1, 20));

        unFirmaTrabajadorLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        unFirmaTrabajadorLabel1.setForeground(new java.awt.Color(63, 63, 63));
        unFirmaTrabajadorLabel1.setText("Firma del trabajador:");
        AnimationPanelEmpleados.add(unFirmaTrabajadorLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 210, -1, -1));

        FirmaRepresentante1.setEditable(false);
        FirmaRepresentante1.setBackground(new java.awt.Color(192, 192, 192));
        FirmaRepresentante1.setHorizontalAlignment(JTextField.CENTER);
        FirmaRepresentante1.setFont(CargarArchivos.miFirma_Font);
        FirmaRepresentante1.setForeground(new java.awt.Color(22, 22, 22));
        FirmaRepresentante1.setBorder(null);
        FirmaRepresentante1.setCaretColor(new java.awt.Color(40, 40, 40));
        FirmaRepresentante1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FirmaRepresentante1ActionPerformed(evt);
            }
        });
        FirmaRepresentante1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                FirmaRepresentante1KeyTyped(evt);
            }
        });
        AnimationPanelEmpleados.add(FirmaRepresentante1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 270, 150, 25));

        jSeparator22.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator22.setForeground(new java.awt.Color(135, 135, 135));
        jSeparator22.setOrientation(javax.swing.SwingConstants.VERTICAL);
        AnimationPanelEmpleados.add(jSeparator22, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 210, 8, 80));

        FirmaTrabajadorLabel1.setFont(CargarArchivos.arrayFuentesRandom.get(0)
        );
        FirmaTrabajadorLabel1.setForeground(new java.awt.Color(58, 77, 127));
        FirmaTrabajadorLabel1.setText("Javier Aragoneses");
        AnimationPanelEmpleados.add(FirmaTrabajadorLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(348, 222, 170, 40));

        MenuPanel1.add(AnimationPanelEmpleados);
        AnimationPanelEmpleados.setBounds(80, 310, 500, 300);

        TransLayerEmpleados.setIcon(new javax.swing.ImageIcon("src/main/java/images/general/trans.png"));
        TransLayer.setFocusable(false);
        MenuPanel1.add(TransLayerEmpleados);
        TransLayerEmpleados.setBounds(0, 315, 680, 320);

        TablaEmpleados.setFocusable(false);
        TablaEmpleados.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        TablaEmpleados.setModel(abstractModelEmpleados);
        TablaEmpleados.getColumnModel().getColumn(1).setCellRenderer(new ColorRendimientoTable());
        TablaEmpleados.getColumnModel().getColumn(2).setCellRenderer(new ColorWorkingTable());
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        TablaEmpleados.setDefaultRenderer(Object.class, centerRenderer);
        TablaEmpleados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TablaEmpleadosMousePressed(evt);
            }
        });
        PaneEmpleados.setViewportView(TablaEmpleados);
        if (TablaEmpleados.getColumnModel().getColumnCount() > 0) {
            TablaEmpleados.getColumnModel().getColumn(0).setResizable(false);
            TablaEmpleados.getColumnModel().getColumn(1).setResizable(false);
            TablaEmpleados.getColumnModel().getColumn(1).setPreferredWidth(100);
            TablaEmpleados.getColumnModel().getColumn(2).setResizable(false);
        }

        PaneEmpleados.setFocusable(false);

        MenuPanel1.add(PaneEmpleados);
        PaneEmpleados.setBounds(10, 40, 390, 250);

        jLabel2.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel2.setText("Lista de empleados");
        MenuPanel1.add(jLabel2);
        jLabel2.setBounds(10, 10, 170, 19);

        PanelInfoEmpleado.setVisible(true);
        PanelInfoEmpleado.setBackground(new java.awt.Color(69, 73, 74));
        PanelInfoEmpleado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(107, 107, 107)));
        PanelInfoEmpleado.setFocusable(false);
        PanelInfoEmpleado.setLayout(null);

        jLabel1.setText("Nivel de felicidad");
        PanelInfoEmpleado.add(jLabel1);
        jLabel1.setBounds(10, 10, 150, 16);

        LabelFechas.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        LabelFechas.setText("Dias restantes de contrato: ");
        PanelInfoEmpleado.add(LabelFechas);
        LabelFechas.setBounds(10, 80, 210, 14);

        unHorarioLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        unHorarioLabel.setText("Horario laboral:");
        PanelInfoEmpleado.add(unHorarioLabel);
        unHorarioLabel.setBounds(10, 60, 100, 14);

        ProgressBarFelicidad.setForeground(new java.awt.Color(42, 127, 0));
        PanelInfoEmpleado.add(ProgressBarFelicidad);
        ProgressBarFelicidad.setBounds(10, 30, 240, 10);

        HorarioLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        HorarioLabel.setText("9:00  - 16:00 ");
        PanelInfoEmpleado.add(HorarioLabel);
        HorarioLabel.setBounds(100, 58, 100, 18);

        PanelRenovacion.setVisible(true);
        PanelRenovacion.setBackground(new java.awt.Color(69, 73, 74));
        PanelRenovacion.setLayout(null);

        RenovarBoton.setBackground(new java.awt.Color(0, 102, 6));
        RenovarBoton.setText("Renovar contrato");
        RenovarBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RenovarBotonActionPerformed(evt);
            }
        });
        PanelRenovacion.add(RenovarBoton);
        RenovarBoton.setBounds(6, 84, 202, 26);

        EliminarBoton.setBackground(new java.awt.Color(100, 0, 0));
        EliminarBoton.setIcon(new javax.swing.ImageIcon("src/main/java/images/general/trash.png"));
        EliminarBoton.setBorderPainted(false);
        EliminarBoton.setPreferredSize(new java.awt.Dimension(72, 22));
        EliminarBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarBotonActionPerformed(evt);
            }
        });
        PanelRenovacion.add(EliminarBoton);
        EliminarBoton.setBounds(216, 84, 31, 26);

        CheckBoxSeguroRenovacion.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        CheckBoxSeguroRenovacion.setText("Pagar seguro de vida: 20€");
        CheckBoxSeguroRenovacion.setFocusable(false);
        PanelRenovacion.add(CheckBoxSeguroRenovacion);
        CheckBoxSeguroRenovacion.setBounds(7, 21, 200, 19);

        PanelRenovarFields.setBackground(new java.awt.Color(69, 73, 74));
        PanelRenovarFields.setLayout(null);

        KeyAdapter field_listener = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        };
        FieldRenovarDuracion.addKeyListener(field_listener);
        int[]maxminD={2,10};
        FieldRenovarDuracion.addFocusListener(fieldTester(FieldRenovarDuracion, "días", maxminD));
        FieldRenovarDuracion.setText("5 días");
        FieldRenovarDuracion.setSelectionEnd(12);
        FieldRenovarDuracion.setSelectionStart(1);
        FieldRenovarDuracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldRenovarDuracionActionPerformed(evt);
            }
        });
        PanelRenovarFields.add(FieldRenovarDuracion);
        FieldRenovarDuracion.setBounds(6, 3, 76, 22);

        FieldRenovarJornada.addKeyListener(field_listener);
        FieldRenovarJornada.setText("8 horas");
        FieldRenovarJornada.setSelectionEnd(12);
        FieldRenovarJornada.setSelectionStart(1);
        int [] maxminJ = {4,12};
        FieldRenovarJornada.addFocusListener(fieldTester(FieldRenovarJornada,"horas",maxminJ));
        FieldRenovarJornada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldRenovarJornadaActionPerformed(evt);
            }
        });
        PanelRenovarFields.add(FieldRenovarJornada);
        FieldRenovarJornada.setBounds(81, 3, 80, 22);

        FieldRenovarSueldo.setText("100€");
        FieldRenovarSueldo.setSelectionEnd(12);
        FieldRenovarSueldo.setSelectionStart(1);
        FieldRenovarSueldo.addKeyListener(field_listener);
        int [] maxminS={5,1000};
        FieldRenovarSueldo.addFocusListener(fieldTester(FieldRenovarSueldo,"€",maxminS));
        FieldRenovarSueldo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldRenovarSueldoActionPerformed(evt);
            }
        });
        PanelRenovarFields.add(FieldRenovarSueldo);
        FieldRenovarSueldo.setBounds(160, 3, 85, 22);

        PanelRenovacion.add(PanelRenovarFields);
        PanelRenovarFields.setBounds(1, 49, 250, 30);

        jSeparator23.setForeground(new java.awt.Color(186, 186, 186));
        PanelRenovacion.add(jSeparator23);
        jSeparator23.setBounds(6, 10, 240, 10);

        PanelInfoEmpleado.add(PanelRenovacion);
        PanelRenovacion.setBounds(4, 160, 250, 117);

        PanelDatosDefecto.setBackground(new java.awt.Color(69, 73, 74));
        PanelDatosDefecto.setVisible(false);
        PanelDatosDefecto.setLayout(null);

        BotonSancionar.setText("Sancionar");
        BotonSancionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonSancionarActionPerformed(evt);
            }
        });
        PanelDatosDefecto.add(BotonSancionar);
        BotonSancionar.setBounds(0, 40, 100, 22);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sin especificar", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        PanelDatosDefecto.add(jComboBox1);
        jComboBox1.setBounds(110, 70, 130, 22);

        jComboBox2.setEditable(false);
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sin especificar", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        PanelDatosDefecto.add(jComboBox2);
        jComboBox2.setBounds(110, 10, 130, 22);

        BotonDespedir.setText("Despedir");
        BotonDespedir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonDespedirActionPerformed(evt);
            }
        });
        PanelDatosDefecto.add(BotonDespedir);
        BotonDespedir.setBounds(0, 70, 100, 22);

        jButton1.setText("Medicar");
        PanelDatosDefecto.add(jButton1);
        jButton1.setBounds(0, 10, 100, 22);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sin especificar", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });
        PanelDatosDefecto.add(jComboBox3);
        jComboBox3.setBounds(110, 40, 130, 22);

        PanelInfoEmpleado.add(PanelDatosDefecto);
        PanelDatosDefecto.setBounds(10, 177, 244, 100);

        jSeparator24.setForeground(new java.awt.Color(186, 186, 186));
        PanelInfoEmpleado.add(jSeparator24);
        jSeparator24.setBounds(10, 50, 240, 10);

        jSeparator25.setForeground(new java.awt.Color(186, 186, 186));
        PanelInfoEmpleado.add(jSeparator25);
        jSeparator25.setBounds(10, 170, 240, 10);

        jSeparator26.setForeground(new java.awt.Color(186, 186, 186));
        PanelInfoEmpleado.add(jSeparator26);
        jSeparator26.setBounds(10, 100, 240, 10);

        MenuPanel1.add(PanelInfoEmpleado);
        PanelInfoEmpleado.setBounds(408, 10, 271, 280);

        VerContratoEmpleados.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        VerContratoEmpleados.setForeground(new java.awt.Color(107, 107, 107));
        VerContratoEmpleados.setIcon(new javax.swing.ImageIcon("src/main/java/images/general/lupa.png"));
        VerContratoEmpleados.setEnabled(false);
        VerContratoEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VerContratoEmpleadosActionPerformed(evt);
            }
        });
        ListSelectionModel cellSelectionModel = TablaEmpleados.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cellSelectionModel.addListSelectionListener((ListSelectionEvent e) -> {
            if (TablaEmpleados.getSelectedRows().length==0){
                VerContratoEmpleados.setEnabled(false);
                ProgressBarFelicidad.setValue(0);
                PanelInfoEmpleado.setVisible(false);
            }
            else {
                PanelInfoEmpleado.setVisible(true);
                VerContratoEmpleados.setEnabled(true);

                ProgressBarFelicidad.setValue(GenerarEmpleados.empleados.get(TablaEmpleados.getSelectedRow()).getFelicidad());
                ProgressBarFelicidad.setForeground(Color.getHSBColor(ProgressBarFelicidad.getValue()/300f, 1f, 0.40f));
                //ProgressBar
                HorarioLabel.setText(GenerarEmpleados.empleados.get(TablaEmpleados.getSelectedRow()).getHorario());
                if (!GenerarEmpleados.empleados.get(TablaEmpleados.getSelectedRow()).isFinContrato()){
                    PanelRenovacion.setVisible(false);
                    PanelDatosDefecto.setVisible(true);
                }
                else {
                    PanelRenovacion.setVisible(true);
                    rellenarFieldsRenovacion();
                    PanelDatosDefecto.setVisible(false);
                }

            }
        });
        MenuPanel1.add(VerContratoEmpleados);
        VerContratoEmpleados.setBounds(370, 10, 30, 24);

        getContentPane().add(MenuPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 120, 675, 310));

        MenuPanel2.setBackground(new java.awt.Color(56, 60, 61));
        MenuPanel2.setLayout(null);

        AnimationPanel.setVisible(false);
        AnimationPanel.setBackground(new java.awt.Color(192, 192, 192));
        AnimationPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 0, true));
        AnimationPanel.setPreferredSize(new java.awt.Dimension(500, 300));
        AnimationPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelDatosPersonales.setBackground(new java.awt.Color(231, 231, 231));
        PanelDatosPersonales.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(146, 146, 146)));
        PanelDatosPersonales.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        unLabelName.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unLabelName.setForeground(new java.awt.Color(121, 121, 121));
        unLabelName.setText("Nombre:");
        PanelDatosPersonales.add(unLabelName, new org.netbeans.lib.awtextra.AbsoluteConstraints(148, 3, -1, 30));

        jSeparator3.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator3.setForeground(new java.awt.Color(135, 135, 135));
        PanelDatosPersonales.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 65, 410, 10));

        NameLabel.setBackground(new java.awt.Color(52, 55, 57));
        NameLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        NameLabel.setForeground(new java.awt.Color(99, 99, 99));
        NameLabel.setText("Javier Aragoneses");
        PanelDatosPersonales.add(NameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(218, 10, -1, -1));

        DniLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        DniLabel.setForeground(new java.awt.Color(99, 99, 99));
        DniLabel.setText("50633883G");
        PanelDatosPersonales.add(DniLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 8, -1, 20));

        unLabelDni.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unLabelDni.setForeground(new java.awt.Color(121, 121, 121));
        unLabelDni.setText("NIF:");
        PanelDatosPersonales.add(unLabelDni, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 3, 30, 30));

        jSeparator4.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator4.setForeground(new java.awt.Color(135, 135, 135));
        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        PanelDatosPersonales.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 33, 9, 68));

        unFechaLabel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unFechaLabel.setForeground(new java.awt.Color(121, 121, 121));
        unFechaLabel.setText("Fecha de nacimiento:");
        PanelDatosPersonales.add(unFechaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 42, -1, -1));

        unNacionalidadLabel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unNacionalidadLabel.setForeground(new java.awt.Color(121, 121, 121));
        unNacionalidadLabel.setText("Nacionalidad:");
        PanelDatosPersonales.add(unNacionalidadLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(176, 73, -1, 20));

        GeneroLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        GeneroLabel.setForeground(new java.awt.Color(99, 99, 99));
        GeneroLabel.setText("Sin genero");
        PanelDatosPersonales.add(GeneroLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 73, -1, 20));

        FechaLabel.setBackground(new java.awt.Color(52, 55, 57));
        FechaLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        FechaLabel.setForeground(new java.awt.Color(99, 99, 99));
        FechaLabel.setText("28 de septiembre de 2003");
        PanelDatosPersonales.add(FechaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 42, 230, -1));

        jSeparator6.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator6.setForeground(new java.awt.Color(135, 135, 135));
        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        PanelDatosPersonales.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(158, 71, 10, 22));

        jSeparator5.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator5.setForeground(new java.awt.Color(135, 135, 135));
        PanelDatosPersonales.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 32, 480, 10));

        jSeparator7.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator7.setForeground(new java.awt.Color(135, 135, 135));
        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);
        PanelDatosPersonales.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 6, 8, 22));

        PanelFoto.setBackground(new java.awt.Color(239, 239, 239));
        PanelFoto.setForeground(new java.awt.Color(255, 255, 255));
        PanelFoto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LabelFoto.setIcon(new javax.swing.ImageIcon("src/main/java/images/nouser.png"));
        PanelFoto.add(LabelFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, -1, 70));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 48)); // NOI18N
        jLabel8.setText("?");
        PanelFoto.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 8, -1, -1));

        PanelDatosPersonales.add(PanelFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(412, 33, 67, 66));

        unGeneroLabel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unGeneroLabel.setForeground(new java.awt.Color(121, 121, 121));
        unGeneroLabel.setText("Genero:");
        PanelDatosPersonales.add(unGeneroLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 73, -1, 20));

        NacionalidadLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        NacionalidadLabel.setForeground(new java.awt.Color(99, 99, 99));
        NacionalidadLabel.setText("Española");
        PanelDatosPersonales.add(NacionalidadLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 75, -1, -1));

        AnimationPanel.add(PanelDatosPersonales, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 480, 100));

        PanelDatosConntrato.setBackground(new java.awt.Color(231, 231, 231));
        PanelDatosConntrato.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(146, 146, 146)));
        PanelDatosConntrato.setForeground(new java.awt.Color(215, 215, 215));
        PanelDatosConntrato.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        HorasLabel.setBackground(new java.awt.Color(52, 55, 57));
        HorasLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        HorasLabel.setForeground(new java.awt.Color(99, 99, 99));
        HorasLabel.setText("10 horas");
        PanelDatosConntrato.add(HorasLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, -1, -1));

        TrabajoLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        TrabajoLabel.setForeground(new java.awt.Color(99, 99, 99));
        TrabajoLabel.setText("Programador");
        PanelDatosConntrato.add(TrabajoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 8, -1, 20));

        unTrabajoLabel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unTrabajoLabel.setForeground(new java.awt.Color(117, 117, 117));
        unTrabajoLabel.setText("Trabajo:");
        PanelDatosConntrato.add(unTrabajoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 3, -1, 30));

        unFechaLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unFechaLabel1.setForeground(new java.awt.Color(117, 117, 117));
        unFechaLabel1.setText("Jornada:");
        PanelDatosConntrato.add(unFechaLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(342, 10, -1, -1));

        jSeparator11.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator11.setForeground(new java.awt.Color(135, 135, 135));
        PanelDatosConntrato.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 32, 480, 10));

        unLabelName2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unLabelName2.setForeground(new java.awt.Color(117, 117, 117));
        unLabelName2.setText("Duración:");
        PanelDatosConntrato.add(unLabelName2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 3, -1, 30));

        DuracionLabel.setBackground(new java.awt.Color(52, 55, 57));
        DuracionLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        DuracionLabel.setForeground(new java.awt.Color(99, 99, 99));
        DuracionLabel.setText("30 días");
        PanelDatosConntrato.add(DuracionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 10, -1, -1));

        unLabelSueldo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unLabelSueldo.setForeground(new java.awt.Color(117, 117, 117));
        unLabelSueldo.setText("Salario diario:");
        PanelDatosConntrato.add(unLabelSueldo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 37, -1, 30));

        SueldoLabel.setBackground(new java.awt.Color(52, 55, 57));
        SueldoLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        SueldoLabel.setForeground(new java.awt.Color(99, 99, 99));
        SueldoLabel.setText("76€");
        PanelDatosConntrato.add(SueldoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(112, 44, -1, -1));

        jSeparator14.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator14.setForeground(new java.awt.Color(135, 135, 135));
        jSeparator14.setOrientation(javax.swing.SwingConstants.VERTICAL);
        PanelDatosConntrato.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 6, 8, 22));

        jSeparator15.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator15.setForeground(new java.awt.Color(135, 135, 135));
        jSeparator15.setOrientation(javax.swing.SwingConstants.VERTICAL);
        PanelDatosConntrato.add(jSeparator15, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 6, 10, 22));

        unRendimientoLabel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        unRendimientoLabel.setForeground(new java.awt.Color(117, 117, 117));
        unRendimientoLabel.setText("Rendimiento:");
        PanelDatosConntrato.add(unRendimientoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(173, 37, -1, 30));

        RendimientoLabel.setBackground(new java.awt.Color(52, 55, 57));
        RendimientoLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        RendimientoLabel.setForeground(new java.awt.Color(99, 99, 99));
        RendimientoLabel.setText("8.56€/h - 16.5€/h");
        PanelDatosConntrato.add(RendimientoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 44, -1, -1));

        jSeparator16.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator16.setForeground(new java.awt.Color(135, 135, 135));
        jSeparator16.setOrientation(javax.swing.SwingConstants.VERTICAL);
        PanelDatosConntrato.add(jSeparator16, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 40, 8, 22));

        AnimationPanel.add(PanelDatosConntrato, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 135, 480, 70));

        ContratarButton.setFocusable(false);
        ContratarButton.setBackground(new java.awt.Color(2, 116, 0));
        ContratarButton.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        ContratarButton.setText("Contratar");
        ContratarButton.setEnabled(false);
        ContratarButton.setFocusable(false);
        ContratarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ContratarButtonActionPerformed(evt);
            }
        });
        AnimationPanel.add(ContratarButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 260, 150, 30));

        DescartarButton.setFocusable(false);
        DescartarButton.setBackground(new java.awt.Color(93, 0, 0));
        DescartarButton.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        DescartarButton.setText("Descartar");
        DescartarButton.setEnabled(false);
        DescartarButton.setFocusable(false);
        DescartarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DescartarButtonActionPerformed(evt);
            }
        });
        AnimationPanel.add(DescartarButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 150, 30));

        DatosPersonales.setFont(CargarArchivos.contract_Font);
        DatosPersonales.setForeground(new java.awt.Color(88, 88, 88));
        DatosPersonales.setText("DATOS DE CONTRATO");
        AnimationPanel.add(DatosPersonales, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 7, -1, 20));

        SeguroCheckBox.setFocusable(false);
        SeguroCheckBox.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        SeguroCheckBox.setForeground(new java.awt.Color(99, 99, 99));
        SeguroCheckBox.setText("Pagar seguro de vida del empleado: 120€");
        AnimationPanel.add(SeguroCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, -1, -1));

        TrasladoCheckBox.setFocusable(false);
        TrasladoCheckBox.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        TrasladoCheckBox.setForeground(new java.awt.Color(99, 99, 99));
        TrasladoCheckBox.setText("Pagar traslado desde Castilla la Mancha: 40€");
        AnimationPanel.add(TrasladoCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, -1, 20));

        unFirmaRepresentanteLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        unFirmaRepresentanteLabel.setForeground(new java.awt.Color(63, 63, 63));
        unFirmaRepresentanteLabel.setText("Firma de la empresa:");
        AnimationPanel.add(unFirmaRepresentanteLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 252, -1, 20));

        unFirmaTrabajadorLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        unFirmaTrabajadorLabel.setForeground(new java.awt.Color(63, 63, 63));
        unFirmaTrabajadorLabel.setText("Firma del trabajador:");
        AnimationPanel.add(unFirmaTrabajadorLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 210, -1, -1));

        FirmaRepresentante.setBackground(new java.awt.Color(192, 192, 192));
        FirmaRepresentante.setFont(CargarArchivos.miFirma_Font);
        FirmaRepresentante.setForeground(new java.awt.Color(22, 22, 22));
        FirmaRepresentante.setHorizontalAlignment(JTextField.CENTER);
        FirmaRepresentante.setBorder(null);
        FirmaRepresentante.setCaretColor(new java.awt.Color(40, 40, 40));
        FirmaRepresentante.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                FirmaRepresentanteKeyTyped(evt);
            }
        });
        AnimationPanel.add(FirmaRepresentante, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 270, 150, 25));

        jSeparator13.setBackground(new java.awt.Color(135, 135, 135));
        jSeparator13.setForeground(new java.awt.Color(135, 135, 135));
        jSeparator13.setOrientation(javax.swing.SwingConstants.VERTICAL);
        AnimationPanel.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 210, 8, 80));

        FirmaTrabajadorLabel.setFont(CargarArchivos.arrayFuentesRandom.get(0)
        );
        FirmaTrabajadorLabel.setForeground(new java.awt.Color(58, 77, 127));
        FirmaTrabajadorLabel.setText("Javier Aragoneses");
        AnimationPanel.add(FirmaTrabajadorLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(348, 222, 170, 40));

        MenuPanel2.add(AnimationPanel);
        AnimationPanel.setBounds(80, 310, 500, 300);

        TransLayer.setIcon(new javax.swing.ImageIcon("src/main/java/images/general/trans.png"));
        TransLayer.setFocusable(false);
        MenuPanel2.add(TransLayer);
        TransLayer.setBounds(0, 315, 680, 320);

        TituloContratos.setBackground(new java.awt.Color(204, 204, 204));
        TituloContratos.setFont(CargarArchivos.bolsa_Font);
        TituloContratos.setForeground(new java.awt.Color(194, 194, 194));
        TituloContratos.setText("Bolsa de empleo");
        MenuPanel2.add(TituloContratos);
        TituloContratos.setBounds(12, 8, 230, 30);

        TablaContratos.setFocusable(false);
        TablaContratos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        TablaContratos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Actividad", "Trabajo", "Salario diario", "Procedencia", "Contrato"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaContratos.setToolTipText(null);
        TablaContratos.getColumnModel().getColumn(2).setCellRenderer(new com.mycompany.game.ColorSueldoTable());
        TablaContratos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        TablaContratos.setDefaultRenderer(Object.class, centerRenderer);
        TablaContratos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TablaContratosMousePressed(evt);
            }
        });
        PaneContratos.setViewportView(TablaContratos);
        if (TablaContratos.getColumnModel().getColumnCount() > 0) {
            TablaContratos.getColumnModel().getColumn(0).setResizable(false);
            TablaContratos.getColumnModel().getColumn(1).setResizable(false);
            TablaContratos.getColumnModel().getColumn(1).setPreferredWidth(125);
            TablaContratos.getColumnModel().getColumn(2).setResizable(false);
            TablaContratos.getColumnModel().getColumn(2).setPreferredWidth(100);
            TablaContratos.getColumnModel().getColumn(3).setResizable(false);
            TablaContratos.getColumnModel().getColumn(3).setPreferredWidth(130);
            TablaContratos.getColumnModel().getColumn(4).setResizable(false);
            TablaContratos.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        PaneContratos.setFocusable(false);

        MenuPanel2.add(PaneContratos);
        PaneContratos.setBounds(10, 45, 655, 236);

        ShowContractButton.setBackground(new java.awt.Color(1, 95, 0));
        ShowContractButton.setForeground(new java.awt.Color(210, 210, 210));
        ShowContractButton.setIcon(new javax.swing.ImageIcon("src/main/java/images/general/lupa.png"));
        ShowContractButton.setText("  Revisar contrato");
        cellSelectionModel = TablaContratos.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cellSelectionModel.addListSelectionListener((ListSelectionEvent e) -> {
            if (TablaContratos.getSelectedRows().length==0){
                ShowContractButton.setEnabled(false);
            }
            else {
                ShowContractButton.setEnabled(true);
            }
        });
        ShowContractButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ShowContractButton.setEnabled(false);
        ShowContractButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowContractButtonActionPerformed(evt);
            }
        });
        MenuPanel2.add(ShowContractButton);
        ShowContractButton.setBounds(495, 10, 170, 28);

        getContentPane().add(MenuPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 120, 675, 310));

        MenuPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setText("En construccion 3");
        MenuPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(263, 133, -1, -1));

        getContentPane().add(MenuPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 670, 300));

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void VerEmpeladosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_VerEmpeladosMouseClicked

        MenuPanel1.setVisible(true);
        MenuPanel2.setVisible(false);
        MenuPanel3.setVisible(false);
    }//GEN-LAST:event_VerEmpeladosMouseClicked

    private void ContrarEmpleadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ContrarEmpleadosMouseClicked
        MenuPanel1.setVisible(false);
        MenuPanel2.setVisible(true);
        MenuPanel3.setVisible(false);
    }//GEN-LAST:event_ContrarEmpleadosMouseClicked

    private void rellenarCamposContrato(int selectedRow, int tipo) {

        if (tipo == 0) {
            ArrayStrings.add(
                    GenerarEmpleados.contratos.get(selectedRow).getDNI());
            ArrayStrings.add(
                    GenerarEmpleados.contratos.get(selectedRow).getNombreCompleto());
            ArrayStrings.add(
                    GenerarEmpleados.contratos.get(selectedRow).getFechaNacimiento());
            ArrayStrings.add(
                    GenerarEmpleados.contratos.get(selectedRow).getGenero());

            //
            ArrayStrings.add(
                    GenerarEmpleados.contratos.get(selectedRow).getNivel());
            ArrayStrings.add(String.valueOf(GenerarEmpleados.contratos.get(
                    selectedRow).getSueldo()) + " €");
            ArrayStrings.add(String.valueOf(GenerarEmpleados.contratos.get(
                    selectedRow).getHoras()) + " horas");
            ArrayStrings.add(String.valueOf(GenerarEmpleados.contratos.get(
                    selectedRow).getDuracion()) + " días");
            ArrayStrings.add(
                    GenerarEmpleados.contratos.get(selectedRow).getAbstractRangoRendimiento());

            // Firma
            String nombre = GenerarEmpleados.contratos.get(selectedRow).getNombreCompleto();
            try {
                nombre = nombre.substring(0, nombre.indexOf(" ",
                        nombre.indexOf(" ") + 1));
            } catch (StringIndexOutOfBoundsException e) {
            }
            GenerarEmpleados.contratos.get(selectedRow).setFirmaEmpleado(nombre);
            ArrayStrings.add(" " + nombre);
            ArrayLabel.get(9).setFont(
                    GenerarEmpleados.contratos.get(selectedRow).getFuenteFirma());
            firmaListener();
            //

            contadorLabel = 0;

            // Detalles de contrato (Style) && checkbox EXTRAS
            LabelFoto.setIcon(
                    GenerarEmpleados.contratos.get(selectedRow).getFoto());
            SueldoLabel.setForeground(
                    GenerarEmpleados.contratos.get(selectedRow).getColorSueldo());
            NacionalidadLabel.setText(
                    GenerarEmpleados.contratos.get(selectedRow).getNacionalidadColor());
            TrasladoCheckBox.setText(
                    "Pagar traslado desde " + GenerarEmpleados.contratos.get(
                            selectedRow).getProcedencia());
            SeguroCheckBox.setText(
                    "Pagar seguro de vida del empleado: " + GenerarEmpleados.contratos.get(
                            selectedRow).getPrecioSeguro() + "€");

            // 
        } else if (tipo == 1) {
            selectedRow = TablaEmpleados.getSelectedRow();
            CrearEmpleados elemento = GenerarEmpleados.empleados.get(selectedRow);
            DniLabel1.setText(elemento.getDNI());
            NameLabel1.setText(elemento.getNombreCompleto());
            FechaLabel1.setText(elemento.getFechaNacimiento());
            GeneroLabel1.setText(elemento.getGenero());
            NacionalidadLabel1.setText(elemento.getNacionalidadColor());
            TrabajoLabel1.setText(elemento.getNivel());
            DuracionLabel1.setText(
                    String.valueOf(elemento.getDuracion()) + " días");
            HorasLabel1.setText(String.valueOf(elemento.getHoras()) + " horas");
            SueldoLabel1.setText(String.valueOf(elemento.getSueldo()) + " €");
            SueldoLabel1.setForeground(elemento.getColorSueldo());
            RendimientoLabel1.setText(
                    String.valueOf(elemento.getAbstractRendimiento()) + " €/h");
            FirmaTrabajadorLabel1.setText(" " + elemento.getFirmaEmpleado());
            FirmaTrabajadorLabel1.setFont(elemento.getFuenteFirma());
            FirmaRepresentante1.setText(elemento.getFirmaEmpresa());
            FirmaRepresentante1.setFont(CargarArchivos.miFirma_Font);
            LabelFoto1.setIcon(elemento.getFoto());
            TrasladoCheckBox1.setText(
                    "Pagar traslado desde " + elemento.getProcedencia());
            SeguroCheckBox1.setText(
                    "Pagar seguro de vida del empleado: " + elemento.getPrecioSeguro() + "€");
            checkboxState(elemento);

        }

    }

    private FocusListener fieldTester(JTextField textfield, String unidad,
            int[] maxmin) {

        FocusListener field_listener = new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                textfield.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                int fieldZ = 0;
                switch (unidad) {
                    case "días" ->
                        fieldZ = GenerarEmpleados.empleados.get(
                                TablaEmpleados.getSelectedRow()).getDuracion();
                    case "horas" ->
                        fieldZ = GenerarEmpleados.empleados.get(
                                TablaEmpleados.getSelectedRow()).getHoras();

                    case "€" ->
                        fieldZ = GenerarEmpleados.empleados.get(
                                TablaEmpleados.getSelectedRow()).getSueldo();
                }

                if (textfield.getText().isBlank()) {
                    textfield.setText(fieldZ + " " + unidad);
                } else {
                    if (Integer.valueOf(textfield.getText()) > maxmin[1]) {
                        textfield.setText(maxmin[1] + " " + unidad);
                    } else if (Integer.valueOf(textfield.getText()) < maxmin[0]) {
                        textfield.setText(maxmin[0] + " " + unidad);
                    } else {
                        textfield.setText(
                                Integer.valueOf(textfield.getText()) + " " + unidad);
                    }

                }
            }

        };
        return field_listener;

    }

    private void limpiarContrato() {
        ContratarButton.setText("Contratar");
        TrasladoCheckBox.setSelected(false);
        SeguroCheckBox.setSelected(false);
        DescartarButton.setEnabled(false);
        ArrayStrings.clear();
        for (JLabel e : ArrayLabel) {
            e.setText("");
        }
        FirmaRepresentante.setText("");
    }

    private void startTimers() {
        timer_añadir.start();
        timer_borrar.start();
    }

    private void stopTimers() {
        timer_añadir.stop();
        timer_borrar.stop();
    }

    private void helpDisable(JTable tabla) {
        tabla.getTableHeader().setEnabled(false);
        tabla.setEnabled(false);
        PaneEmpleados.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_NEVER);

    }

    private void helpAble(JTable tabla) {
        tabla.getTableHeader().setEnabled(true);
        tabla.setEnabled(true);
        PaneEmpleados.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    private void ShowContractButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowContractButtonActionPerformed
        ShowContractButton.setEnabled(false);
        helpDisable(TablaContratos);
        limpiarContrato();
        stopTimers();
        timerContrato();

        TransLayer.setLocation(new Point(0, -5));
        AnimationPanel.setVisible(true);
        comprobarPanel(AnimationPanel, true);
        AnimationPanel.setLocation(80, 10);

        selectedRow = TablaContratos.getSelectedRow();
        rellenarCamposContrato(selectedRow, 0);

        for (int j = 0; j < ArrayLabel.size(); j++) {
            animacionEscribir(ArrayStrings.get(j), ArrayLabel.get(j));
        }


    }//GEN-LAST:event_ShowContractButtonActionPerformed

    private void timerContrato() {
        contadorContrato = 30;
        timer_contrato = new Timer(1000, (ActionEvent e) -> {
            contadorContrato--;
            ContratarButton.setText("Contratar (" + String.valueOf(
                    contadorContrato) + ")");
            if (contadorContrato <= 0) {
                DescartarButton.doClick();
                ((Timer) e.getSource()).stop();
            }

        });
        timer_contrato.start();

    }

    private void animacionEscribir(String word, JLabel label) {
        Writer = new Timer(75, new ActionListener() {
            int posLetra = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (posLetra == word.length()) {
                    ((Timer) e.getSource()).stop();
                    contadorLabel++;
                    if (contadorLabel == ArrayLabel.size()) {
                        DescartarButton.setEnabled(true);
                    }

                } else {
                    label.setText(label.getText() + word.charAt(posLetra));
                    posLetra++;

                }
            }

        });

        Writer.start();

    }


    private void TablaContratosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaContratosMousePressed
        JTable table = (JTable) evt.getSource();
        if (evt.getClickCount() == 2 && table.getSelectedRow() != -1 && !AnimationPanel.isVisible()) {
            ShowContractButton.doClick();
        }
    }//GEN-LAST:event_TablaContratosMousePressed

    private void firmaListener() {

        FirmaRepresentante.getDocument().addDocumentListener(
                new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();

            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            public void changed() {

                if (FirmaRepresentante.getText().isBlank() || contadorLabel != ArrayStrings.size()) {
                    ContratarButton.setEnabled(false);
                } else {
                    ContratarButton.setEnabled(true);
                }

            }
        }
        );
    }

    private void comprobarCheckBox() {
        if (TrasladoCheckBox.isSelected()) {
            GenerarEmpleados.contratos.get(selectedRow).setTraslado(
                    true);
            dineroEmpresa -= GenerarEmpleados.contratos.get(selectedRow).getOriginPrice();
            GenerarEmpleados.contratos.get(selectedRow).setFechaIncorporacion(
                    dias_reloj);
            GenerarEmpleados.contratos.get(selectedRow).setFechaFinalizacion(
                    GenerarEmpleados.contratos.get(selectedRow).getFechaIncorporacion() + GenerarEmpleados.contratos.get(
                    selectedRow).getDuracion());

        } else {
            GenerarEmpleados.contratos.get(selectedRow).setTraslado(
                    false);
            GenerarEmpleados.contratos.get(selectedRow).setFechaIncorporacion(
                    dias_reloj + LuckyClass.probabilidadTiempoIncorporacion());
            GenerarEmpleados.contratos.get(selectedRow).setFechaFinalizacion(
                    GenerarEmpleados.contratos.get(selectedRow).getFechaIncorporacion() + GenerarEmpleados.contratos.get(
                    selectedRow).getDuracion());

        }
        if (SeguroCheckBox.isSelected()) {
            GenerarEmpleados.contratos.get(selectedRow).setSeguro(
                    true);
            dineroEmpresa -= GenerarEmpleados.contratos.get(selectedRow).getPrecioSeguro();
        } else {
            GenerarEmpleados.contratos.get(selectedRow).setSeguro(
                    false);
        }
    }


    private void ContratarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ContratarButtonActionPerformed
        AnimationPanel.setVisible(false);
        AnimationPanel.setLocation(80, 310);
        helpAble(TablaContratos);
        contratoIsVisible = false;
        comprobarCheckBox();
        TransLayer.setLocation(new Point(0, 315));
        GenerarEmpleados.contratos.get(selectedRow).setFirmaEmpresa(
                FirmaRepresentante.getText());
        GenerarEmpleados.empleados.add(GenerarEmpleados.contratos.get(
                selectedRow));
        if (GenerarEmpleados.empleados.get(GenerarEmpleados.empleados.size() - 1) instanceof CrearTecnico crearTecnico) {
            GenerarEmpleados.tecnicos.add(crearTecnico);
        }
        abstractModelEmpleados.fireTableRowsInserted(
                GenerarEmpleados.empleados.size() - 1,
                GenerarEmpleados.empleados.size() - 1);
        eliminarComun();

    }//GEN-LAST:event_ContratarButtonActionPerformed

    private void eliminarComun() {
        TransLayer.setLocation(new Point(0, 315));
        if (DniValidationFrame.PanelCristalRight != null) {
            DniValidationFrame.PanelCristalRight.setVisible(false);
            DniValidationFrame.PanelCristalNormal.setVisible(true);
        }
        DescartarButton.setEnabled(false);
        ContratarButton.setEnabled(false);
        timer_contrato.stop();
        TablaModelContratos.removeRow(selectedRow);
        GenerarEmpleados.contratos.remove(selectedRow);

        startTimers();

    }


    private void DescartarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DescartarButtonActionPerformed
        eliminarComun();
        AnimationPanel.setVisible(false);
        AnimationPanel.setLocation(80, 310);
        comprobarPanel(AnimationPanel, false);
        helpAble(TablaContratos);

    }//GEN-LAST:event_DescartarButtonActionPerformed

    private void comprobarPanel(JPanel panel, boolean abierto) {
        if (panel.equals(AnimationPanel)) {
            contratoIsVisible = abierto;
        }
    }

    private void FirmaRepresentanteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FirmaRepresentanteKeyTyped
        if (FirmaRepresentante.getText().length() >= 14) {
            evt.consume();

        }
    }//GEN-LAST:event_FirmaRepresentanteKeyTyped

    private void checkboxState(CrearEmpleados elemento) {
        if (elemento.getTraslado() == true) {
            TrasladoCheckBox1.setSelected(true);
        } else {
            TrasladoCheckBox1.setSelected(false);
        }
        if (elemento.getSeguro() == true) {
            SeguroCheckBox1.setSelected(true);
        } else {
            SeguroCheckBox1.setSelected(false);
        }

    }

    private void panelDisable(boolean estado) {
        CheckBoxSeguroRenovacion.setEnabled(estado);
        RenovarBoton.setEnabled(estado);
        EliminarBoton.setEnabled(estado);

    }

    private void visibleMet(boolean estado) {
        if (!PanelRenovacion.isVisible()) {
            PanelDatosDefecto.setVisible(estado);
        } else {
            PanelRenovarFields.setVisible(estado);
        }
    }


    private void VerContratoEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VerContratoEmpleadosActionPerformed
        helpDisable(TablaEmpleados);
        VerContratoEmpleados.setEnabled(false);
        visibleMet(false);
        panelDisable(false);
        rellenarCamposContrato(selectedRow, 1);
        TransLayerEmpleados.setLocation(new Point(0, -5));
        AnimationPanelEmpleados.setVisible(true);
        AnimationPanelEmpleados.setLocation(80, 10);
        comprobarPanel(AnimationPanelEmpleados, true);
    }//GEN-LAST:event_VerContratoEmpleadosActionPerformed

    private void CerrarContratoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CerrarContratoActionPerformed
        VerContratoEmpleados.setEnabled(true);
        AnimationPanelEmpleados.setVisible(false);
        comprobarPanel(AnimationPanelEmpleados, false);
        visibleMet(true);
        panelDisable(true);
        helpAble(TablaEmpleados);
        AnimationPanelEmpleados.setLocation(80, 310);
        TransLayerEmpleados.setLocation(new Point(0, 315));

    }//GEN-LAST:event_CerrarContratoActionPerformed

    private void SeguroCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SeguroCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SeguroCheckBox1ActionPerformed

    private void TrasladoCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TrasladoCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TrasladoCheckBox1ActionPerformed

    private void FirmaRepresentante1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FirmaRepresentante1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_FirmaRepresentante1KeyTyped

    private void FirmaRepresentante1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FirmaRepresentante1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FirmaRepresentante1ActionPerformed

    private void TablaEmpleadosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaEmpleadosMousePressed
        JTable table = (JTable) evt.getSource();
        if (evt.getClickCount() == 2 && table.getSelectedRow() != -1 && !AnimationPanelEmpleados.isVisible()) {
            VerContratoEmpleados.doClick();
        }
    }//GEN-LAST:event_TablaEmpleadosMousePressed

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
        locationMain = this.getLocation();
    }//GEN-LAST:event_formComponentMoved

    private void BotonSancionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonSancionarActionPerformed

    }//GEN-LAST:event_BotonSancionarActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void BotonDespedirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonDespedirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BotonDespedirActionPerformed

    private void FieldRenovarDuracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldRenovarDuracionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldRenovarDuracionActionPerformed

    private void EliminarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarBotonActionPerformed

    }//GEN-LAST:event_EliminarBotonActionPerformed

    private void FieldRenovarJornadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldRenovarJornadaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldRenovarJornadaActionPerformed

    private void FieldRenovarSueldoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldRenovarSueldoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldRenovarSueldoActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void RenovarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RenovarBotonActionPerformed
        LuckyClass.probabilidadRenovarContrato(FieldRenovarDuracion.getText(),
                FieldRenovarJornada.getText(), FieldRenovarSueldo.getText(),
                GenerarEmpleados.empleados.get(TablaEmpleados.getSelectedRow()));
    }//GEN-LAST:event_RenovarBotonActionPerformed

    public static void main(String args[]) {
        try {
            UIManager.put("Table.selectionBackground", new java.awt.Color(9, 48,
                    83));
            UIManager.put("TableHeader.height", 30);
            UIManager.put("Component.borderColor",
                    new java.awt.Color(95, 95, 95));
            UIManager.put("Component.focusedBorderColor",
                    new java.awt.Color(111, 111, 111));
            UIManager.put("Table.rowHeight", 25);

            UIManager.setLookAndFeel(new FlatDarkLaf());

        } catch (UnsupportedLookAndFeelException e) {
        }
        java.awt.EventQueue.invokeLater(() -> {
            new Game().setVisible(true);
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AnimationPanel;
    protected static javax.swing.JPanel AnimationPanelEmpleados;
    private javax.swing.JButton BotonDespedir;
    private javax.swing.JButton BotonSancionar;
    private javax.swing.JButton CerrarContrato;
    private javax.swing.JCheckBox CheckBoxSeguroRenovacion;
    private javax.swing.JPanel ContrarEmpleados;
    private javax.swing.JButton ContratarButton;
    private javax.swing.JLabel DatosPersonales;
    private javax.swing.JLabel DatosPersonales1;
    private javax.swing.JButton DescartarButton;
    private javax.swing.JLabel DniLabel;
    private javax.swing.JLabel DniLabel1;
    private javax.swing.JLabel DuracionLabel;
    private javax.swing.JLabel DuracionLabel1;
    private javax.swing.JButton EliminarBoton;
    private javax.swing.JLabel FechaLabel;
    private javax.swing.JLabel FechaLabel1;
    protected static javax.swing.JTextField FieldRenovarDuracion;
    private javax.swing.JTextField FieldRenovarJornada;
    private javax.swing.JTextField FieldRenovarSueldo;
    private javax.swing.JTextField FirmaRepresentante;
    private javax.swing.JTextField FirmaRepresentante1;
    private javax.swing.JLabel FirmaTrabajadorLabel;
    private javax.swing.JLabel FirmaTrabajadorLabel1;
    private javax.swing.JLabel GeneroLabel;
    private javax.swing.JLabel GeneroLabel1;
    private javax.swing.JLabel HorarioLabel;
    private javax.swing.JLabel HorasLabel;
    private javax.swing.JLabel HorasLabel1;
    private javax.swing.JLabel LabelDias;
    private javax.swing.JLabel LabelFechas;
    private javax.swing.JLabel LabelFoto;
    private javax.swing.JLabel LabelFoto1;
    protected static javax.swing.JLabel LabelHoras;
    protected static javax.swing.JLabel LabelMinutos;
    private javax.swing.JPanel MenuPanel1;
    private javax.swing.JPanel MenuPanel2;
    private javax.swing.JPanel MenuPanel3;
    private javax.swing.JLabel NacionalidadLabel;
    private javax.swing.JLabel NacionalidadLabel1;
    private javax.swing.JLabel NameLabel;
    private javax.swing.JLabel NameLabel1;
    private javax.swing.JScrollPane PaneContratos;
    private javax.swing.JScrollPane PaneEmpleados;
    private javax.swing.JPanel PanelDatosConntrato;
    private javax.swing.JPanel PanelDatosConntrato1;
    private javax.swing.JPanel PanelDatosDefecto;
    private javax.swing.JPanel PanelDatosPersonales;
    private javax.swing.JPanel PanelDatosPersonales1;
    private javax.swing.JPanel PanelFoto;
    private javax.swing.JPanel PanelFoto1;
    private javax.swing.JPanel PanelInfoEmpleado;
    private javax.swing.JPanel PanelIzquierdo;
    private javax.swing.JPanel PanelRenovacion;
    private javax.swing.JPanel PanelRenovarFields;
    private javax.swing.JPanel PanelSuperior;
    private javax.swing.JProgressBar ProgressBarFelicidad;
    private javax.swing.JLabel RendimientoLabel;
    private javax.swing.JLabel RendimientoLabel1;
    private javax.swing.JButton RenovarBoton;
    private javax.swing.JCheckBox SeguroCheckBox;
    private javax.swing.JCheckBox SeguroCheckBox1;
    private javax.swing.JButton ShowContractButton;
    private javax.swing.JLabel SueldoLabel;
    private javax.swing.JLabel SueldoLabel1;
    protected static javax.swing.JTable TablaContratos;
    private static javax.swing.JTable TablaEmpleados;
    private javax.swing.JLabel TituloContratos;
    private javax.swing.JLabel TrabajoLabel;
    private javax.swing.JLabel TrabajoLabel1;
    private javax.swing.JLabel TransLayer;
    private javax.swing.JLabel TransLayerEmpleados;
    private javax.swing.JCheckBox TrasladoCheckBox;
    private javax.swing.JCheckBox TrasladoCheckBox1;
    private javax.swing.JButton VerContratoEmpleados;
    private javax.swing.JPanel VerEmpelados;
    private javax.swing.JLabel dineroEmpresaLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator23;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator25;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel unFechaLabel;
    private javax.swing.JLabel unFechaLabel1;
    private javax.swing.JLabel unFechaLabel2;
    private javax.swing.JLabel unFechaLabel3;
    private javax.swing.JLabel unFirmaRepresentanteLabel;
    private javax.swing.JLabel unFirmaRepresentanteLabel1;
    private javax.swing.JLabel unFirmaTrabajadorLabel;
    private javax.swing.JLabel unFirmaTrabajadorLabel1;
    private javax.swing.JLabel unGeneroLabel;
    private javax.swing.JLabel unGeneroLabel1;
    private javax.swing.JLabel unHorarioLabel;
    private javax.swing.JLabel unLabelDni;
    private javax.swing.JLabel unLabelDni1;
    private javax.swing.JLabel unLabelName;
    private javax.swing.JLabel unLabelName1;
    private javax.swing.JLabel unLabelName2;
    private javax.swing.JLabel unLabelName3;
    private javax.swing.JLabel unLabelSueldo;
    private javax.swing.JLabel unLabelSueldo1;
    private javax.swing.JLabel unNacionalidadLabel;
    private javax.swing.JLabel unNacionalidadLabel1;
    private javax.swing.JLabel unRendimientoLabel;
    private javax.swing.JLabel unRendimientoLabel1;
    private javax.swing.JLabel unTrabajoLabel;
    private javax.swing.JLabel unTrabajoLabel1;
    // End of variables declaration//GEN-END:variables
    protected static DefaultTableModel TablaModelContratos;

}
