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
public class PanelClientes extends JPanel{
    
    public PanelClientes() {
        crearInterfaz();
    }
    
    private void crearInterfaz() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(244, 247, 250));
        setBorder(new EmptyBorder(40, 45, 40, 45));
        
        // Título
        JLabel lblTitulo= new JLabel("Clientes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        
        // Descripción
        JLabel lblDescripcion= new JLabel("Registro y consulta de propietarios");
        lblDescripcion.setForeground(new Color(98, 114, 125));
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        add(lblTitulo);
        add(Box.createVerticalStrut(8));
        add(lblDescripcion);
    }
    
}
