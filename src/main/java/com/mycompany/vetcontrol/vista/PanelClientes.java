/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author weprg
 */
public class PanelClientes extends JPanel{
    
    // Colores
    private static final Color FONDO = new Color(244, 247, 250);
    private static final Color AZUL_OSCURO= new Color(23, 50, 77);
    private static final Color AZUL_PRINCIPAL= new Color(52, 120, 184);
    private static final Color TURQUESA= new Color(67, 166, 160);
    private static final Color ROJO= new Color(217, 92, 89);
    private static final Color TEXTO_SECUNDARIO= new Color(98, 114, 125);
    private static final Color BORDE= new Color(216, 225, 232);
    
    public PanelClientes() {
        crearInterfaz();
    }
    
    private void crearInterfaz() {
        setLayout(new BorderLayout(0, 25));
        setBackground(FONDO);
        setBorder(new EmptyBorder(40, 45, 40, 45));
        
        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearTarjetaClientes(), BorderLayout.CENTER);
    }
    
    private JPanel crearEncabezado(){   
        // Panel encabezado
        JPanel encabezado= new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.setBackground(FONDO);
        
        // Título del panel
        JLabel lblTitulo= new JLabel("Clientes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        
        // Descripción del panel
        JLabel lblDescripcion= new JLabel("Registro y consulta de propietarios");
        lblDescripcion.setForeground(TEXTO_SECUNDARIO);
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        // agreamos los componentes
        add(lblTitulo);
        add(Box.createVerticalStrut(8));
        add(lblDescripcion);
        
        return encabezado;
    }
    
    private JPanel crearTarjetaClientes() {
        JPanel tarjeta= new JPanel(new BorderLayout(0, 20));
        
        tarjeta.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    BORDE, 1, true),
                    new EmptyBorder(20, 32, 28, 32)));
        tarjeta.add(crearSeccionBusqueda(), BorderLayout.NORTH);
        
        return tarjeta;
    }
    
    // Barra de busqueda
    private JPanel crearSeccionBusqueda(){
        JPanel seccionBusqueda= new JPanel(new BorderLayout(18, 8));
        seccionBusqueda.setBackground(Color.WHITE);
        
        // Etiqueta buscar cliente
        JLabel lblBuscar= new JLabel("Buscar Cliente");
        lblBuscar.setForeground(AZUL_OSCURO);
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        
        // Cuadro de busqueda
        JTextField txtBuscar= new JTextField();
        txtBuscar.putClientProperty("JTextField.placeholderText", "Nombre, teléfono o correo...");
        txtBuscar.setPreferredSize(new Dimension(300, 42));
        
        // Panel de botones
        JPanel panelBotones= crearPanelBotones();
        
        seccionBusqueda.add(lblBuscar, BorderLayout.NORTH);
        seccionBusqueda.add(txtBuscar, BorderLayout.CENTER);
        seccionBusqueda.add(panelBotones, BorderLayout.EAST);
        
        return seccionBusqueda;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panelBotones= new JPanel();
        
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
        panelBotones.setBackground(Color.WHITE);
        
        // Lista de botones
        JButton btnBuscar= crearBoton("Buscar", AZUL_PRINCIPAL);
        JButton btnNuevo= crearBoton("+ Nuevo", TURQUESA);
        JButton btnEliminar= crearBoton("Eliminar", ROJO);
        
        // Agrega los botones
        panelBotones.add(btnBuscar);
        panelBotones.add(Box.createHorizontalStrut(10));
        
        panelBotones.add(btnNuevo);
        panelBotones.add(Box.createHorizontalStrut(10));
        
        panelBotones.add(btnEliminar);
        
        return panelBotones;
    }
    
    private JButton crearBoton(String texto, Color color){
        JButton boton= new JButton(texto);
        
        // apariencia de los botones
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        // dimensiones de los botones
        Dimension tamanoBoton= new Dimension(115,42);
        boton.setMinimumSize(tamanoBoton);
        boton.setPreferredSize(tamanoBoton);
        boton.setMaximumSize(tamanoBoton);
        // forma de los botones
        boton.putClientProperty("JComponent.minimumHeight", 42);
        boton.putClientProperty("JButton.buttonType", "borderless");
        
        return boton;
    }
    
}
