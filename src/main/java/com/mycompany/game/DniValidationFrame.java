package com.mycompany.game;

import com.sun.source.doctree.SeeTree;
import java.awt.Color;
import java.awt.event.ActionEvent;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

public class DniValidationFrame extends javax.swing.JFrame {

    private int xArrastrado, yArrastrado, yPresionado, xPresionado, dM, yM, arM, abM, x, y, xMain, yMain, progressNum;

    private void listener() {
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                xArrastrado = e.getX();
                yArrastrado = e.getY();

                JFrame Frame = (JFrame) e.getSource();
                setLocation(Frame.getLocation().x + xArrastrado - xPresionado,
                        Frame.getLocation().y + yArrastrado - yPresionado);
                x = getLocation().x;
                y = getLocation().y;
                xMain = Game.locationMain.x;
                yMain = Game.locationMain.y;
                dM = 247 + xMain;
                yM = 238 + xMain;
                arM = 165 + yMain;
                abM = 170 + yMain;

                if (x <= dM && x >= yM && y >= arM && y <= abM && Game.contratoIsVisible) {
                    PanelCristalRight.setVisible(true);
                    PanelCristalNormal.setVisible(false);

                } else {
                    PanelCristalRight.setVisible(false);
                    PanelCristalNormal.setVisible(true);

                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                xPresionado = e.getX();
                yPresionado = e.getY();
            }

        });
    }

    public DniValidationFrame() {

        initComponents();
        listener();

        Timer newTimer = new Timer(1000, (ActionEvent e) -> {
            // System.out.println("Location validador: "+this.getLocation());
        });
        newTimer.start();

        setBackground(new Color(0, 0, 0, 0));
        this.setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel7 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        PanelBar = new javax.swing.JPanel();
        ExitLabel = new javax.swing.JLabel();
        LabelTitle = new javax.swing.JLabel();
        LabelIcon = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        DniBoton = new javax.swing.JButton();
        ProgressBarDni = new javax.swing.JProgressBar();
        LabelMensaje = new javax.swing.JLabel();
        PanelCristalRight = new javax.swing.JPanel();
        PanelCristalNormal = new javax.swing.JPanel();

        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(88, 88, 88));
        setMinimumSize(new java.awt.Dimension(138, 122));
        setUndecorated(true);
        getContentPane().setLayout(null);

        jPanel7.setBackground(new java.awt.Color(67, 69, 70));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel7);
        jPanel7.setBounds(-2, 19, 140, 11);

        jPanel4.setBackground(new java.awt.Color(67, 69, 70));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel4);
        jPanel4.setBounds(0, 20, 8, 40);

        PanelBar.setBackground(new java.awt.Color(189, 189, 189));
        PanelBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 134, 134)));
        PanelBar.setLayout(null);

        ExitLabel.setForeground(new java.awt.Color(107, 107, 107));
        ExitLabel.setIcon(new javax.swing.ImageIcon("src/main/java/images/general/close.png"));
        ExitLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ExitLabelMouseClicked(evt);
            }
        });
        PanelBar.add(ExitLabel);
        ExitLabel.setBounds(119, 1, 18, 18);

        LabelTitle.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        LabelTitle.setForeground(new java.awt.Color(63, 63, 63));
        LabelTitle.setText("Validador");
        PanelBar.add(LabelTitle);
        LabelTitle.setBounds(25, -1, 80, 20);

        LabelIcon.setIcon(new javax.swing.ImageIcon("src/main/java/images/general/carne.png"));
        PanelBar.add(LabelIcon);
        LabelIcon.setBounds(5, 1, 18, 18);

        getContentPane().add(PanelBar);
        PanelBar.setBounds(0, 0, 138, 20);

        jPanel5.setBackground(new java.awt.Color(67, 69, 70));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel5);
        jPanel5.setBounds(130, 20, 8, 40);

        jPanel6.setBackground(new java.awt.Color(67, 69, 70));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        DniBoton.setBackground(new java.awt.Color(9, 71, 98));
        DniBoton.setText("Comprobar DNI");
        DniBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DniBotonActionPerformed(evt);
            }
        });
        jPanel6.add(DniBoton, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 38, 122, -1));
        jPanel6.add(ProgressBarDni, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 22, 122, 10));

        LabelMensaje.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        LabelMensaje.setForeground(new java.awt.Color(92, 0, 0));
        LabelMensaje.setText("DNI INVÁLIDO");
        jPanel6.add(LabelMensaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, -1));

        getContentPane().add(jPanel6);
        jPanel6.setBounds(0, 54, 140, 70);

        PanelCristalRight.setBackground(new java.awt.Color(66, 52, 0,160));
        PanelCristalRight.setVisible(false);
        PanelCristalRight.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(126, 126, 126)));

        javax.swing.GroupLayout PanelCristalRightLayout = new javax.swing.GroupLayout(PanelCristalRight);
        PanelCristalRight.setLayout(PanelCristalRightLayout);
        PanelCristalRightLayout.setHorizontalGroup(
            PanelCristalRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        PanelCristalRightLayout.setVerticalGroup(
            PanelCristalRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        getContentPane().add(PanelCristalRight);
        PanelCristalRight.setBounds(8, 30, 122, 24);

        PanelCristalNormal.setBackground(new java.awt.Color(96, 96, 96,150));
        PanelCristalNormal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(126, 126, 126)));

        javax.swing.GroupLayout PanelCristalNormalLayout = new javax.swing.GroupLayout(PanelCristalNormal);
        PanelCristalNormal.setLayout(PanelCristalNormalLayout);
        PanelCristalNormalLayout.setHorizontalGroup(
            PanelCristalNormalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        PanelCristalNormalLayout.setVerticalGroup(
            PanelCristalNormalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        getContentPane().add(PanelCristalNormal);
        PanelCristalNormal.setBounds(8, 30, 122, 24);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void DniBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DniBotonActionPerformed
        DniBoton.setEnabled(false);
        Timer progressbar_timer = new Timer(10, (ActionEvent e) -> {
            progressNum++;
            ProgressBarDni.setValue(progressNum);

            if (progressNum == 100) {
                if (CargarArchivos.dniValidos.contains(
                        GenerarEmpleados.contratos.get(Game.selectedRow).getDNI())) {
                    LabelMensaje.setText("DNI VÁLIDO");
                    LabelMensaje.setForeground(Color.GREEN);
                } else {
                    LabelMensaje.setText("DNI NO VÁLIDO");
                    LabelMensaje.setForeground(Color.RED);
                }
            }

        });
        progressbar_timer.start();

    }//GEN-LAST:event_DniBotonActionPerformed

    private void ExitLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ExitLabelMouseClicked
        this.setVisible(false);
    }//GEN-LAST:event_ExitLabelMouseClicked

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(
                    DniValidationFrame.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(
                    DniValidationFrame.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(
                    DniValidationFrame.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(
                    DniValidationFrame.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DniValidationFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DniBoton;
    private javax.swing.JLabel ExitLabel;
    private javax.swing.JLabel LabelIcon;
    private javax.swing.JLabel LabelMensaje;
    private javax.swing.JLabel LabelTitle;
    private javax.swing.JPanel PanelBar;
    protected static javax.swing.JPanel PanelCristalNormal;
    protected static javax.swing.JPanel PanelCristalRight;
    private javax.swing.JProgressBar ProgressBarDni;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    // End of variables declaration//GEN-END:variables
}
