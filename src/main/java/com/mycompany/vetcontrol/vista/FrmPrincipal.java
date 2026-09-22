/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author weprg
 */
public class FrmPrincipal extends JFrame{
    
    // Colores de la aplicación
    private static final Color AZUL_OSCURO= new Color(23, 50, 77);
    private static final Color FONDO= new Color(244, 247, 250);
    
    // Constructor
    public FrmPrincipal() {
        configurarVentana();
        crearInterfaz();
    }
    
    private void configurarVentana() {
        setTitle("VetControl - Clínica Veterinaria Huellitas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000,650));
        getContentPane().setBackground(new Color(244, 247, 250));
        setLocationRelativeTo(null);
    }
    
    private void crearInterfaz() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(FONDO);
        
        // Panel superior, contendra la barra superior
        JPanel barraSuperior= crearBarraSuperior();
        getContentPane().add(
            barraSuperior,
            BorderLayout.NORTH);
    }
    
    private JPanel crearBarraSuperior() {
        // Contiene Título de la pantalla y usuario
        JPanel barraSuperior= new JPanel(
            new BorderLayout()
        );
    
        barraSuperior.setBackground(AZUL_OSCURO);
        
        barraSuperior.setBorder(new EmptyBorder(18, 28, 18, 28));
        
        // Titulo de la pantalla
        JLabel lblNombreAplicacion= new JLabel("VetControl");
        lblNombreAplicacion.setForeground(Color.WHITE);
        lblNombreAplicacion.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        // Usuario/Cliente/Administrador
        JLabel lblUsuario= new JLabel("Administrador");
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        
        barraSuperior.add(lblNombreAplicacion, BorderLayout.WEST);
        barraSuperior.add(lblUsuario, BorderLayout.EAST);
        
        return barraSuperior;
    }   
}
