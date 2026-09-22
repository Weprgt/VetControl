/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import java.awt.Color;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author weprg
 */
public class PanelInicio extends JPanel{
    
    // Lista de colores
    private static final Color FONDO= new Color(244, 247, 250);
    private static final Color AZUL_OSCURO= new Color(23, 50, 77);
    private static final Color TEXTO_SECUNDARIO= new Color(98, 114, 125);
    
    public PanelInicio() {
        crearInterfaz();
    }
    
    private void crearInterfaz() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(FONDO);
        setBorder(new EmptyBorder(40, 45, 40, 45));
        
        // Titulo
        JLabel lblTitulo= new JLabel("Inicio");
        lblTitulo.setForeground(AZUL_OSCURO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        
        // Descripcion
        JLabel lblDescripcion= new JLabel("Resumen general del sistema");
        lblDescripcion.setForeground(TEXTO_SECUNDARIO);
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        add(lblTitulo);
        add(Box.createVerticalStrut(8));
        add(lblDescripcion);
    }
    
    
}
