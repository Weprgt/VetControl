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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

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
    
    // Tabla de datos
    private JTable tablaClientes;
    
    
    public PanelClientes() {
        crearInterfaz();
    }
    
    private void crearInterfaz() {
        setLayout(new BorderLayout(0, 25));
        setBackground(FONDO);
        setBorder(new EmptyBorder(40, 45, 40, 45));
        
        JPanel panelEncabezado= crearEncabezado();
        JPanel tarjetaClientes= crearTarjetaClientes();
        
        add(panelEncabezado, BorderLayout.NORTH);
        add(crearTarjetaClientes(), BorderLayout.CENTER);
    }
    
    private JPanel crearEncabezado(){   
        // Panel encabezado
        JPanel panelEncabezado= new JPanel();
        panelEncabezado.setLayout(new BoxLayout(panelEncabezado, BoxLayout.Y_AXIS));
        panelEncabezado.setOpaque(false);
        
        // Título del panel
        JLabel lblTitulo= new JLabel("Clientes");
        lblTitulo.setForeground(AZUL_OSCURO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Descripción del panel
        JLabel lblDescripcion= new JLabel("Registro y consulta de propietarios");
        lblDescripcion.setForeground(TEXTO_SECUNDARIO);
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // agreamos los componentes
        panelEncabezado.add(lblTitulo);
        panelEncabezado.add(Box.createVerticalStrut(8));
        panelEncabezado.add(lblDescripcion);
        
        return panelEncabezado;
    }
    
    private JPanel crearTarjetaClientes() {
        JPanel tarjeta= new JPanel(new BorderLayout(0, 20));
        
        // apariencia y posición de la tarjeta
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1, true),
                    new EmptyBorder(20, 32, 28, 32)));

        // Agrega la tabla de datos
        tarjeta.add(crearSeccionBusqueda(), BorderLayout.NORTH);
        tarjeta.add(crearTablaClientes(), BorderLayout.CENTER);
        
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
    
    // Panel tabla de datos
    private JScrollPane crearTablaClientes(){
        String[] columnas= {
            "Código", 
            "Nombre completo", 
            "Teléfono", 
            "Correo"};
        
        // datos de prueba
        Object[][] datosTemporales= {
            {"C-001", "Radiohead", "1010-1010", "creep@mail.com"},
            {"C-002", "Nirvana", "2020-2020", "litium@mail.com"},
            {"C-003", "Travis", "3030-3030", "sing@mail.com"},
            {"C-004", "Oasis", "4040-4040", "wonderwall@mail.com"},
            };
        
        // tabla por defecto
        DefaultTableModel modelo= new DefaultTableModel(datosTemporales, columnas){
            @Override
            public boolean isCellEditable(int fila, int columna){
                return false;
            }
        };
        
        // creación de la tabla clientes
        tablaClientes= new JTable(modelo);
        
        // apariencia de la tabla clientes
        tablaClientes.setRowHeight(42);
        tablaClientes.setShowVerticalLines(false);
        tablaClientes.setShowHorizontalLines(true);
        tablaClientes.setGridColor(BORDE);
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // definición de colores y fuente
        tablaClientes.setSelectionBackground(new Color(232, 242, 247));
        tablaClientes.setSelectionForeground(new Color(36, 50, 61));
        tablaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // Encabezados de las columnas
        tablaClientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaClientes.getTableHeader().setBackground(AZUL_OSCURO);
        tablaClientes.getTableHeader().setForeground(Color.WHITE);
        tablaClientes.getTableHeader().setPreferredSize(new Dimension(0, 42));
        tablaClientes.setFillsViewportHeight(true);
        // barras de desplazamiento
        JScrollPane scroll= new JScrollPane(tablaClientes);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        
        return scroll;
        
    }
    
}
