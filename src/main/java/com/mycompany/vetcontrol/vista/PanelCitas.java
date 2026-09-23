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
public class PanelCitas extends JPanel {

    // Colores 
    private static final Color FONDO= new Color(244, 247, 250);
    private static final Color AZUL_OSCURO= new Color(23, 50, 77);
    private static final Color TEXTO_SECUNDARIO= new Color(98, 114, 125);
    private static final Color AZUL_PRINCIPAL= new Color(52, 120, 184);
    private static final Color TURQUESA= new Color(67, 166, 160);
    private static final Color ROJO= new Color(217, 92, 89);
    private static final Color BORDE= new Color(216, 225, 232);

    // Tabla que mostrará las citas registradas
    private JTable tablaCitas;

    public PanelCitas() {
        crearInterfaz();
    }

    private void crearInterfaz() {
        setLayout(new BorderLayout(0, 25));
        setBackground(FONDO);
        setBorder(new EmptyBorder(40, 45, 40, 45)); // Márgenes internos del panel

        JPanel panelEncabezado = crearEncabezado(); // Encabezado
        JPanel tarjetaCitas = crearTarjetaCitas(); // Área de información de citas

        add(panelEncabezado, BorderLayout.NORTH);
        add(tarjetaCitas, BorderLayout.CENTER);
    }

    private JPanel crearEncabezado() {
        // Panel vertical para título y descripción
        JPanel panelEncabezado = new JPanel();
        panelEncabezado.setLayout(new BoxLayout(panelEncabezado, BoxLayout.Y_AXIS));
        panelEncabezado.setOpaque(false);
        
        // Título de la pantalla
        JLabel lblTitulo = new JLabel("Citas");
        lblTitulo.setForeground(AZUL_OSCURO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Descripción de la pantalla
        JLabel lblDescripcion = new JLabel("Programación y control de consultas");
        lblDescripcion.setForeground(TEXTO_SECUNDARIO);
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Agregar los componentes al encabezado
        panelEncabezado.add(lblTitulo);
        panelEncabezado.add(Box.createVerticalStrut(8));
        panelEncabezado.add(lblDescripcion);

        return panelEncabezado;
    }

    // Tarjeta de información de las citas
    private JPanel crearTarjetaCitas() {
        JPanel tarjetaCitas= new JPanel(new BorderLayout(0, 20));
        tarjetaCitas.setBackground(Color.WHITE);
        tarjetaCitas.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1, true),
                new EmptyBorder(20, 32, 28, 32)));
        
        // Barra de búsqueda y botones
        tarjetaCitas.add(crearSeccionBusqueda(), BorderLayout.NORTH);
        tarjetaCitas.add(crearTablaCitas(), BorderLayout.CENTER);
        
        return tarjetaCitas;
    }
    private JPanel crearSeccionBusqueda(){
        JPanel seccionBusqueda= new JPanel(new BorderLayout(18, 8));
        seccionBusqueda.setBackground(Color.WHITE);
        
        // Etiqueta del buscador
        JLabel lblBuscar= new JLabel("Buscar cita");
        lblBuscar.setForeground(AZUL_OSCURO);
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        
        // Campo de búsqueda
        JTextField txtBuscar= new JTextField();
        txtBuscar.putClientProperty("JTextField.placeholderText", "Mascota, propietario o veterinario...");
        txtBuscar.setPreferredSize(new Dimension(300, 42));
        
        // Crear el panel que contiene los botones
        JPanel panelBotones= crearPanelBotones();
        
        // Organizar los componentes
        seccionBusqueda.add(lblBuscar, BorderLayout.NORTH);
        seccionBusqueda.add(txtBuscar, BorderLayout.CENTER);
        seccionBusqueda.add(panelBotones, BorderLayout.EAST);
        
        return seccionBusqueda;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panelBotones= new JPanel();
        
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
        panelBotones.setBackground(Color.WHITE);
        
        // Crear botones
        JButton btnBuscar= crearBoton("Buscar", AZUL_PRINCIPAL);
        JButton btnNueva= crearBoton("+ Nueva", TURQUESA);
        JButton btnCancelar= crearBoton("Cancelar", ROJO);
        
        // Agreagar botones
        panelBotones.add(btnBuscar);
        panelBotones.add(Box.createHorizontalStrut(10));

        panelBotones.add(btnNueva);
        panelBotones.add(Box.createHorizontalStrut(10));

        panelBotones.add(btnCancelar);

        return panelBotones;
    }
    
    private JButton crearBoton(
        String texto,
        Color color) {

        JButton boton = new JButton(texto);
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);

        // Todos los botones tendrán el mismo tamaño
        Dimension tamanoBoton= new Dimension(115, 42);
        boton.setMinimumSize(tamanoBoton);
        boton.setPreferredSize(tamanoBoton);
        boton.setMaximumSize(tamanoBoton);

        // Estilo compatible con FlatLaf
        boton.putClientProperty("JComponent.minimumHeight", 42);
        boton.putClientProperty("JButton.buttonType", "borderless");

        return boton;
    }
    
    private JScrollPane crearTablaCitas() {
    // Columnas necesarias para identificar una cita
    String[] columnas = {
        "Código",
        "Fecha",
        "Hora",
        "Mascota",
        "Veterinario",
        "Estado"
    };

    // Datos temporales para comprobar el diseño
    Object[][] datosTemporales = {
        {"CT-001", "22/09/2026", "09:00", "Spike", "Dra. Morales", "Programada"},
        {"CT-002", "22/09/2026", "10:30", "Garfield", "Dr. López", "Confirmada"},
        {"CT-003", "22/09/2026", "14:00", "Snoopy", "Dra. Morales", "Programada"},
        {"CT-004", "23/09/2026", "08:30", "Pelusa", "Dr. López", "Cancelada"}
    };

    // Evita que el usuario edite directamente las celdas
    DefaultTableModel modelo= new DefaultTableModel(datosTemporales, columnas) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
            }
        };

        // Crear la tabla
        tablaCitas = new JTable(modelo);

        // Apariencia general
        tablaCitas.setRowHeight(42);
        tablaCitas.setShowVerticalLines(false);
        tablaCitas.setShowHorizontalLines(true);
        tablaCitas.setGridColor(BORDE);

        // Solo se podrá seleccionar una cita
        tablaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Colores de selección
        tablaCitas.setSelectionBackground(new Color(232, 242, 247));
        tablaCitas.setSelectionForeground(new Color(36, 50, 61));
        tablaCitas.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Encabezado de la tabla
        tablaCitas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaCitas.getTableHeader().setBackground(AZUL_OSCURO);
        tablaCitas.getTableHeader().setForeground(Color.WHITE);
        tablaCitas.getTableHeader().setPreferredSize(new Dimension(0, 42));

        // La tabla ocupará todo el espacio disponible
        tablaCitas.setFillsViewportHeight(true);

        // Agregar desplazamiento a la tabla
        JScrollPane scroll= new JScrollPane(tablaCitas);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        return scroll;
    }
}
