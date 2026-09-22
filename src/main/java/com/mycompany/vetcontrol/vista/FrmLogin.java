/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author weprg
 */
public class FrmLogin extends JFrame {
    // lista de colores
    private static final Color AZUL_OSCURO= new Color(23, 50, 77);
    private static final Color FONDO= new Color(244, 247, 250);
    private static final Color TEXTO= new Color(36, 50, 61);
    private static final Color TEXTO_SECUNDARIO= new Color(98, 114, 125);
    private static final Color BORDE= new Color(216, 225, 232);
    
    // constructor
    public FrmLogin(){
        configurarVentana();
        crearInterfaz();
    }
    // ventana principal
    private void configurarVentana() {
        setTitle("VetControl - Inicio de sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
    }
    // Interfaz inicio de sesión
    private void crearInterfaz() {
        setLayout(new BorderLayout());
        add(crearPanelPresentacion(), BorderLayout.WEST);
        add(crearPanelFormulario(), BorderLayout.CENTER);
    }
    // Área de presentación
    private JPanel crearPanelPresentacion() {
        JPanel panelPresentacion= new JPanel();
        panelPresentacion.setLayout(new BoxLayout(panelPresentacion, BoxLayout.Y_AXIS));
        panelPresentacion.setBackground(AZUL_OSCURO);
        panelPresentacion.setPreferredSize(new Dimension(420, 0));
        panelPresentacion.setBorder(new EmptyBorder(70, 55, 60, 45));
        
        // Titulo
        JLabel lblNombre= new JLabel("VetControl");
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 40));
        
        // Descripcion
        JLabel lblDescripcion= new JLabel(
            "<html>"
          + "Gestión Clínica simple, segura" + "<br>"
          + "y siempre organizada."
          + "</html>"
        );
        lblDescripcion.setForeground(new Color(216, 233, 230));
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 21));
        JLabel lblClinica= new JLabel("Clínica Veterinaria Huellitas");
        lblClinica.setForeground(new Color(187, 212, 208));
        
        // Agregar los componentes
        // Título
        panelPresentacion.add(lblNombre);
        panelPresentacion.add(Box.createVerticalStrut(25));
        // Descripción
        panelPresentacion.add(lblDescripcion);
        panelPresentacion.add(Box.createVerticalGlue());
        // Lema
        panelPresentacion.add(lblClinica);
        
        return panelPresentacion;
    }
    
    private JPanel crearPanelFormulario() {
        JPanel panelFormulario= new JPanel(new GridBagLayout());
        panelFormulario.setBackground(FONDO);
        
        // Dimensiones del formulario de inicio de sesión
        JPanel tarjetaLogin= new JPanel();
        tarjetaLogin.setLayout(new BoxLayout(tarjetaLogin, BoxLayout.Y_AXIS));
        tarjetaLogin.setBackground(Color.WHITE);
        tarjetaLogin.setPreferredSize(new Dimension(430, 360));
        tarjetaLogin.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1, true),
            new EmptyBorder(45, 45, 45, 45))
        );
        
        // Etiqueta inicio de sesion
        JLabel lblTitulo= new JLabel("Bienvenido");
        lblTitulo.setForeground(TEXTO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        // Instrucciones
        JLabel lblInstruccion= new JLabel("Ingresa tus datos para continuar");
        lblInstruccion.setForeground(TEXTO_SECUNDARIO);
        lblInstruccion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInstruccion.setAlignmentX(CENTER_ALIGNMENT);
        lblInstruccion.setHorizontalAlignment(SwingConstants.CENTER);
        // agrega los componentes
        tarjetaLogin.add(lblTitulo);
        tarjetaLogin.add(Box.createVerticalStrut(12));
        tarjetaLogin.add(lblInstruccion);
        panelFormulario.add(tarjetaLogin);
        
        return panelFormulario;       
    }
}
