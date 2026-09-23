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
public class PanelHistorialClinico extends JPanel {

    // Colores de la aplicación
    private static final Color FONDO= new Color(244, 247, 250);
    private static final Color AZUL_OSCURO= new Color(23, 50, 77);
    private static final Color AZUL_PRINCIPAL= new Color(52, 120, 184);
    private static final Color TURQUESA= new Color(67, 166, 160);
    private static final Color GRIS_BOTON= new Color(98, 114, 125);
    private static final Color TEXTO= new Color(36, 50, 61);
    private static final Color TEXTO_SECUNDARIO= new Color(98, 114, 125);
    private static final Color BORDE= new Color(216, 225, 232);
    private static final Color AZUL_SELECCION= new Color(232, 242, 247);

    private JTable tablaHistorial;
    private JTextField txtBuscar;

    public PanelHistorialClinico() {
        crearInterfaz();
    }

    private void crearInterfaz() {
        setLayout(new BorderLayout(0, 25));
        setBackground(FONDO);

        // Márgenes internos del panel
        setBorder(new EmptyBorder(40, 45, 40, 45));
        // Crear las secciones
        JPanel panelEncabezado= crearEncabezado();
        JPanel tarjetaHistorial= crearTarjetaHistorial();
        
        // Colocar las secciones
        add(panelEncabezado, BorderLayout.NORTH);
        add(tarjetaHistorial, BorderLayout.CENTER);
    }

    private JPanel crearEncabezado() {
        JPanel panelEncabezado = new JPanel();

        panelEncabezado.setLayout(new BoxLayout(panelEncabezado, BoxLayout.Y_AXIS));

        // Permite mostrar el fondo general
        panelEncabezado.setOpaque(false);

        // Título
        JLabel lblTitulo= new JLabel("Historial clínico");
        lblTitulo.setForeground(AZUL_OSCURO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Descripción
        JLabel lblDescripcion = new JLabel("Consultas, diagnósticos, tratamientos y vacunas");
        lblDescripcion.setForeground(TEXTO_SECUNDARIO);
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Agregar elementos al encabezado
        panelEncabezado.add(lblTitulo);
        panelEncabezado.add(Box.createVerticalStrut(8));
        panelEncabezado.add(lblDescripcion);

        return panelEncabezado;
    }

    private JPanel crearTarjetaHistorial() {
        JPanel tarjetaHistorial = new JPanel(new BorderLayout(0, 20));
        tarjetaHistorial.setBackground(Color.WHITE);

        // Borde exterior y márgenes interiores
        tarjetaHistorial.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1, true),
                new EmptyBorder(20, 32, 28, 32)
            )
        );

        // Buscador y botones en la parte superior
        tarjetaHistorial.add(crearSeccionBusqueda(), BorderLayout.NORTH);

        // Tabla en el centro
        tarjetaHistorial.add(crearTablaHistorial(), BorderLayout.CENTER);

        return tarjetaHistorial;
    }

    private JPanel crearSeccionBusqueda() {
        JPanel seccionBusqueda = new JPanel(new BorderLayout(18, 8));
        seccionBusqueda.setBackground(Color.WHITE);

        // Etiqueta del buscador
        JLabel lblBuscar= new JLabel("Buscar historial");
        lblBuscar.setForeground(AZUL_OSCURO);
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 15));

        // Campo de búsqueda
        txtBuscar = new JTextField();
        txtBuscar.putClientProperty("JTextField.placeholderText", "Expediente, mascota o propietario...");
        txtBuscar.setPreferredSize(new Dimension(300, 42));

        // Panel que contiene los botones
        JPanel panelBotones= crearPanelBotones();

        // Organizar la sección
        seccionBusqueda.add(lblBuscar, BorderLayout.NORTH);
        seccionBusqueda.add(txtBuscar, BorderLayout.CENTER);
        seccionBusqueda.add(panelBotones, BorderLayout.EAST);

        return seccionBusqueda;
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
        panelBotones.setBackground(Color.WHITE);

        // Crear botones
        JButton btnBuscar = crearBoton("Buscar", AZUL_PRINCIPAL, 105);
        JButton btnNuevo = crearBoton("+ Nueva", TURQUESA, 105);
        JButton btnVerDetalle = crearBoton("Ver detalle", GRIS_BOTON, 120);

        // Agregar los botones
        panelBotones.add(btnBuscar);
        panelBotones.add(Box.createHorizontalStrut(10));
        panelBotones.add(btnNuevo);
        panelBotones.add(Box.createHorizontalStrut(10));
        panelBotones.add(btnVerDetalle);

        return panelBotones;
    }

    private JButton crearBoton(String texto, Color color, int ancho) {
        JButton boton = new JButton(texto);
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);

        // Tamaño del botón
        Dimension tamanoBoton= new Dimension(ancho, 42);
        boton.setMinimumSize(tamanoBoton);
        boton.setPreferredSize(tamanoBoton);
        boton.setMaximumSize(tamanoBoton);

        // Configuración visual de FlatLaf
        boton.putClientProperty("JComponent.minimumHeight", 42);
        boton.putClientProperty("JButton.buttonType", "borderless");

        return boton;
    }

    private JScrollPane crearTablaHistorial() {
        // Columnas del historial clínico
        String[] columnas = {
            "Expediente",
            "Fecha",
            "Mascota",
            "Tipo",
            "Diagnóstico",
            "Veterinario"
        };

        // Datos temporales para comprobar el diseño
        Object[][] datosTemporales = {
            {"EXP-001", "15/09/2026", "Spike", "Consulta", "Dermatitis leve", "Dra. Morales"},
            {"EXP-002", "18/09/2026", "Garfield", "Vacuna", "Vacuna antirrábica", "Dr. López"},
            {"EXP-003", "20/09/2026", "Snoopy", "Consulta", "Infección auditiva", "Dra. Morales"},
            {"EXP-004", "22/09/2026", "Pelusa", "Tratamiento", "Control posoperatorio", "Dr. López"}
        };

        // Modelo no editable
        DefaultTableModel modelo= new DefaultTableModel(datosTemporales, columnas) {
            @Override
            public boolean isCellEditable(
                int fila,
                int columna) {
                    return false;
                }
            };

        // Crear la tabla
        tablaHistorial = new JTable(modelo);

        // Configuración visual
        tablaHistorial.setRowHeight(42);
        tablaHistorial.setShowVerticalLines(false);
        tablaHistorial.setShowHorizontalLines(true);
        tablaHistorial.setGridColor(BORDE);

        // Permitir seleccionar un registro
        tablaHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Colores al seleccionar una fila
        tablaHistorial.setSelectionBackground(AZUL_SELECCION);
        tablaHistorial.setSelectionForeground(TEXTO);
        tablaHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Encabezado de la tabla
        tablaHistorial.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaHistorial.getTableHeader().setBackground(AZUL_OSCURO);
        tablaHistorial.getTableHeader().setForeground(Color.WHITE);
        tablaHistorial.getTableHeader().setPreferredSize(new Dimension(0, 42));

        // Evitar que el usuario reordene las columnas
        tablaHistorial.getTableHeader().setReorderingAllowed(false);

        // Ocupar el área completa de la tarjeta
        tablaHistorial.setFillsViewportHeight(true);

        // Agregar barras de desplazamiento
        JScrollPane scroll = new JScrollPane(tablaHistorial);

        scroll.setBorder(BorderFactory.createEmptyBorder());

        return scroll;
    }
}