/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 *
 * @author weprg
 */
public class FrmPrincipal extends JFrame{
    
    // Colores de la aplicación
    private static final Color AZUL_OSCURO= new Color(23, 50, 77);
    private static final Color AZUL_PRINCIPAL= new Color(52, 120, 184);
    private static final Color AZUL_CLARO= new Color(232, 242, 247);
    private static final Color TEXTO= new Color(36, 50, 61);
    private static final Color FONDO= new Color(244, 247, 250);
    
    // Botones
    private JButton botonSeleccionado; // Mostrar botón seleccionado
    
    
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
        
        // Menú lateral
        JPanel menuLateral= crearMenuLateral();
        
        // Panel de inicio
        PanelInicio panelInicio= new PanelInicio();
        
        // Añadiendo los componentes a FrmPrincipal
        getContentPane().add( // Barra superior
            barraSuperior,
            BorderLayout.NORTH);
        
        getContentPane().add( // Menú Lateral
            menuLateral,
            BorderLayout.WEST);
        
        getContentPane().add( // Panel de inicio
            panelInicio,
            BorderLayout.CENTER);
 
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
    
    private JPanel crearMenuLateral() {
        JPanel menuLateral= new JPanel();
        
        menuLateral.setLayout(
            new BoxLayout(
                menuLateral,
                BoxLayout.Y_AXIS));
        
        menuLateral.setBackground(Color.WHITE);
        menuLateral.setPreferredSize(new Dimension(230, 0));
        menuLateral.setBorder(new EmptyBorder(25, 18, 20, 18));
        
        JLabel lblNavegacion= new JLabel("NAVEGACIÓN");
        lblNavegacion.setForeground(new Color(98, 114, 125));
        lblNavegacion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNavegacion.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        menuLateral.add(lblNavegacion);
        menuLateral.add(Box.createVerticalStrut(18));
        
        JButton btnInicio= crearBotonMenu("Inicio");
        menuLateral.add(btnInicio);
        menuLateral.add(Box.createVerticalStrut(5));
        
        menuLateral.add(crearBotonMenu("Clientes"));
        menuLateral.add(Box.createVerticalStrut(5));
        
        menuLateral.add(crearBotonMenu("Mascotas"));
        menuLateral.add(Box.createVerticalStrut(5));
        
        menuLateral.add(crearBotonMenu("Citas"));
        menuLateral.add(Box.createVerticalStrut(5));
        
        menuLateral.add(crearBotonMenu("Historial Clínico"));
        menuLateral.add(Box.createVerticalStrut(5));
       
        menuLateral.add(crearBotonMenu("Inventario"));
        
        seleccionarBoton(btnInicio);
        
        return menuLateral;
        
    }
    
    private JButton crearBotonMenu(String texto) {
        JButton boton= new JButton(texto);
        
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        boton.setForeground(new Color(36, 50, 61));
        boton.setBackground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(new EmptyBorder(13, 18, 13, 12));
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        boton.putClientProperty("JButton.buttonType", "borderless");
        
        boton.addActionListener(evento->{
           seleccionarBoton(boton); 
        });
        
        return boton;
    }
    
    private void seleccionarBoton(JButton boton) {
        // Restaura el botón anteriormente seleccionado
        if(botonSeleccionado != null){
            botonSeleccionado.setBackground(Color.WHITE);
            botonSeleccionado.setForeground(TEXTO);
            botonSeleccionado.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            botonSeleccionado.setBorder(new EmptyBorder(13, 18, 13, 12));
        }
        
        // Guardar el nuevo botón seleccionado
        botonSeleccionado= boton;
        botonSeleccionado.setBackground(AZUL_CLARO);
        botonSeleccionado.setForeground(AZUL_PRINCIPAL);
        botonSeleccionado.setFont(new Font("Segoe UI", Font.BOLD, 15));
        botonSeleccionado.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 4, 0, 0, AZUL_PRINCIPAL), // crea una franja azul
            new EmptyBorder(13, 14, 13, 12)));
        
    }
}
