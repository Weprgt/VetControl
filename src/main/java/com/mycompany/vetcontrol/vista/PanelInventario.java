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


/**
 * Pantalla para consultar y administrar
 * los productos del inventario.
 *
 * @author weprg
 */
public class PanelInventario extends JPanel {

    // Colores de la aplicación
    private static final Color FONDO= new Color(244, 247, 250);
    private static final Color AZUL_OSCURO= new Color(23, 50, 77);
    private static final Color AZUL_PRINCIPAL= new Color(52, 120, 184);
    private static final Color TURQUESA= new Color(67, 166, 160);
    private static final Color NARANJA= new Color(232, 151, 48);
    private static final Color TEXTO= new Color(36, 50, 61);
    private static final Color TEXTO_SECUNDARIO= new Color(98, 114, 125);
    private static final Color BORDE= new Color(216, 225, 232);
    private static final Color AZUL_SELECCION= new Color(232, 242, 247);
    private static final Color FONDO_ALERTA= new Color(255, 247, 230);

    private JTable tablaInventario;
    private JTextField txtBuscar;

    public PanelInventario() {
        crearInterfaz();
    }

    private void crearInterfaz() {
        setLayout(new BorderLayout(0, 25));
        setBackground(FONDO);
        setBorder(new EmptyBorder(40, 45, 40, 45));
        JPanel panelEncabezado= crearEncabezado();
        JPanel tarjetaInventario= crearTarjetaInventario();
        add(panelEncabezado, BorderLayout.NORTH);
        add(tarjetaInventario, BorderLayout.CENTER);
    }

    private JPanel crearEncabezado() {
        JPanel panelEncabezado = new JPanel();
        panelEncabezado.setLayout(new BoxLayout(panelEncabezado, BoxLayout.Y_AXIS));
        panelEncabezado.setOpaque(false);

        // Título
        JLabel lblTitulo= new JLabel("Inventario");
        lblTitulo.setForeground(AZUL_OSCURO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Descripción
        JLabel lblDescripcion = new JLabel("Control de productos, existencias y vencimientos");
        lblDescripcion.setForeground(TEXTO_SECUNDARIO);
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelEncabezado.add(lblTitulo);

        panelEncabezado.add(Box.createVerticalStrut(8));
        panelEncabezado.add(lblDescripcion);

        return panelEncabezado;
    }

    private JPanel crearTarjetaInventario() {
        JPanel tarjetaInventario = new JPanel(
            new BorderLayout(0, 20));

        tarjetaInventario.setBackground(Color.WHITE);

        tarjetaInventario.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1, true),
                new EmptyBorder(20, 32, 28, 32)));

        // Buscador y botones
        tarjetaInventario.add(crearSeccionBusqueda(), BorderLayout.NORTH);

        // Tabla de productos
        tarjetaInventario.add(crearTablaInventario(), BorderLayout.CENTER);

        // Alerta de stock y vencimientos
        tarjetaInventario.add(crearPanelAlertas(), BorderLayout.SOUTH);
        
        return tarjetaInventario;
    }

    private JPanel crearSeccionBusqueda() {
        JPanel seccionBusqueda = new JPanel(new BorderLayout(18, 8));

        seccionBusqueda.setBackground(Color.WHITE);

        // Etiqueta
        JLabel lblBuscar= new JLabel("Buscar producto");
        lblBuscar.setForeground(AZUL_OSCURO);
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 15));

        // Campo de búsqueda
        txtBuscar = new JTextField();
        txtBuscar.putClientProperty("JTextField.placeholderText", "Código, producto o categoría...");
        txtBuscar.setPreferredSize(new Dimension(300, 42));

        // Botones
        JPanel panelBotones= crearPanelBotones();
        seccionBusqueda.add(lblBuscar, BorderLayout.NORTH);
        seccionBusqueda.add(txtBuscar, BorderLayout.CENTER);
        seccionBusqueda.add(panelBotones, BorderLayout.EAST);

        return seccionBusqueda;
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
        panelBotones.setBackground(Color.WHITE);
        JButton btnBuscar = crearBoton("Buscar", AZUL_PRINCIPAL, 105);
        JButton btnNuevo = crearBoton("+ Producto", TURQUESA, 115);
        JButton btnMovimiento = crearBoton("Movimiento", NARANJA, 120);
        
        // Agregamos los botones
        panelBotones.add(btnBuscar);
        panelBotones.add(Box.createHorizontalStrut(10));
        panelBotones.add(btnNuevo);
        panelBotones.add(Box.createHorizontalStrut(10));
        panelBotones.add(btnMovimiento);
        
        return panelBotones;
    }

    private JButton crearBoton(String texto, Color color,int ancho) {
        JButton boton = new JButton(texto);
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        
        // Dimensiones de los botones
        Dimension tamanoBoton= new Dimension(ancho, 42);
        boton.setMinimumSize(tamanoBoton);
        boton.setPreferredSize(tamanoBoton);
        boton.setMaximumSize(tamanoBoton);
        boton.putClientProperty("JComponent.minimumHeight", 42);
        boton.putClientProperty("JButton.buttonType", "borderless");

        return boton;
    }

    private JScrollPane crearTablaInventario() {

        String[] columnas = {
            "Código",
            "Producto",
            "Categoría",
            "Stock",
            "Mínimo",
            "Vencimiento"
        };

        // Datos temporales para comprobar el diseño
        Object[][] datosTemporales = {
            {"M-001", "Amoxicilina 250 mg", "Medicamento", 42, 10, "03/2027"},
            {"V-014", "Vacuna antirrábica", "Vacuna", 6, 10, "11/2026"},
            {"A-203", "Alimento cachorro 2 kg", "Alimento", 18, 5, "—"},
            {"M-019", "Desparasitante", "Medicamento", 3, 8, "10/2026"}
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

        tablaInventario = new JTable(modelo);

        // Configuración visual
        tablaInventario.setRowHeight(42);
        tablaInventario.setShowVerticalLines(false);
        tablaInventario.setShowHorizontalLines(true);
        tablaInventario.setGridColor(BORDE);
        tablaInventario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaInventario.setSelectionBackground(AZUL_SELECCION);
        tablaInventario.setSelectionForeground(TEXTO);
        tablaInventario.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Encabezado de la tabla
        tablaInventario.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaInventario.getTableHeader().setBackground(AZUL_OSCURO);
        tablaInventario.getTableHeader().setForeground(Color.WHITE);
        tablaInventario.getTableHeader().setPreferredSize(new Dimension(0, 42));
        tablaInventario.getTableHeader().setReorderingAllowed(false);
        tablaInventario.setFillsViewportHeight(true);

        JScrollPane scroll= new JScrollPane(tablaInventario);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        return scroll;
    }

    private JPanel crearPanelAlertas() {
        JPanel panelAlertas = new JPanel(new BorderLayout());
        panelAlertas.setBackground(FONDO_ALERTA);
        panelAlertas.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(NARANJA, 1, true),
                new EmptyBorder(14, 18, 14, 18)));

        JLabel lblAlerta = new JLabel("⚠ 2 productos bajo el stock mínimo · "
            + "1 producto vence próximamente");

        lblAlerta.setForeground(new Color(145, 91, 22));
        lblAlerta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelAlertas.add(lblAlerta, BorderLayout.CENTER);

        return panelAlertas;
    }
}
