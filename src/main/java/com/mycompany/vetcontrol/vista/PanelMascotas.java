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
public class PanelMascotas extends JPanel{
     // Paleta de colores de VetControl
    private static final Color FONDO= new Color(244, 247, 250);
    private static final Color AZUL_OSCURO= new Color(23, 50, 77);
    private static final Color AZUL_PRINCIPAL= new Color(52, 120, 184);
    private static final Color TURQUESA= new Color(67, 166, 160);
    private static final Color ROJO= new Color(217, 92, 89);
    private static final Color TEXTO= new Color(36, 50, 61);
    private static final Color TEXTO_SECUNDARIO= new Color(98, 114, 125);
    private static final Color BORDE= new Color(216, 225, 232);
    private static final Color SELECCION= new Color(232, 242, 247);
    
    private JTextField txtBuscar;
    private JTable tablaMascotas;

    // Constructor del panel
    public PanelMascotas() {
        crearInterfaz();
    }

    private void crearInterfaz() {
        setLayout(new BorderLayout(0, 25));
        setBackground(FONDO);
        setBorder(new EmptyBorder(40, 45, 40, 45)); // Espacio alrededor de toda la pantalla
        
        // Agregar los componentes
        add(crearEncabezado(), BorderLayout.NORTH); // Encabezado en la parte superior
        add(crearTarjetaMascotas(), BorderLayout.CENTER); // Tarjeta blanca en el centro
    }

    // Título y la descripción de la pantalla
    private JPanel crearEncabezado() {
        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.setBackground(FONDO);
        
        // Etiqueta título principal.
        JLabel lblTitulo= new JLabel("Mascotas");
        lblTitulo.setForeground(AZUL_OSCURO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);

        // Descripción debajo del título.
        JLabel lblDescripcion = new JLabel("Pacientes asociados a sus propietarios");
        lblDescripcion.setForeground(TEXTO_SECUNDARIO);
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDescripcion.setAlignmentX(LEFT_ALIGNMENT);

        // Agregar los componentes
        encabezado.add(lblTitulo);
        encabezado.add(Box.createVerticalStrut(8));
        encabezado.add(lblDescripcion);

        return encabezado;
    }

    // Crea la tarjeta que contiene la búsqueda y la tabla.
    private JPanel crearTarjetaMascotas() {
        JPanel tarjeta = new JPanel(new BorderLayout(0, 20));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder( // Borde exterior y espacio interior.
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDE, 1, true),
                        new EmptyBorder(28, 32, 28, 32)));

        // Agrega botones en la parte superior.
        tarjeta.add(crearSeccionBusqueda(), BorderLayout.NORTH);
        tarjeta.add(crearTablaMascotas(), BorderLayout.CENTER);

        return tarjeta;
    }

    // Crea el buscador y los botones principales.
    private JPanel crearSeccionBusqueda() {
        JPanel seccionBusqueda = new JPanel(new BorderLayout(18, 8));
        seccionBusqueda.setBackground(Color.WHITE);

        // Etiqueta del buscador.
        JLabel lblBuscar= new JLabel("Buscar mascota");
        lblBuscar.setForeground(AZUL_OSCURO);
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 15));

        // Campo de búsqueda.
        txtBuscar = new JTextField();
        txtBuscar.putClientProperty("JTextField.placeholderText", "Nombre, especie, raza o propietario...");
        txtBuscar.setPreferredSize(new Dimension(300, 42));

        // Botones alineados horizontalmente.
        JPanel panelBotones = crearPanelBotones();
        seccionBusqueda.add(lblBuscar, BorderLayout.NORTH);
        seccionBusqueda.add(txtBuscar, BorderLayout.CENTER);
        seccionBusqueda.add(panelBotones, BorderLayout.EAST);

        return seccionBusqueda;
    }

    // Agrupa los botones Buscar, Nueva y Eliminar.
    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(
                new BoxLayout(panelBotones, BoxLayout.X_AXIS));
        panelBotones.setBackground(Color.WHITE);
        
        // botones del panel mascotas
        JButton btnBuscar = crearBoton("Buscar", AZUL_PRINCIPAL);
        JButton btnNueva = crearBoton("+  Nueva", TURQUESA);
        JButton btnEliminar = crearBoton("Eliminar", ROJO);

        // Agregar los botones
        panelBotones.add(btnBuscar);
        panelBotones.add(Box.createHorizontalStrut(10));
        panelBotones.add(btnNueva);
        panelBotones.add(Box.createHorizontalStrut(10));
        panelBotones.add(btnEliminar);

        return panelBotones;
    }

    // Método para crear botones uniformes.
    private JButton crearBoton(String texto,  Color color) {
        JButton boton = new JButton(texto);
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);

        // Todos los botones tendrán el mismo tamaño.
        Dimension tamanoBoton= new Dimension(115, 42);
        boton.setMinimumSize(tamanoBoton);
        boton.setPreferredSize(tamanoBoton);
        boton.setMaximumSize(tamanoBoton);
        boton.putClientProperty("JComponent.minimumHeight", 42);

        // Elegimos el estilo rectangular sin borde visible.
        boton.putClientProperty("JButton.buttonType", "borderless");

        return boton;
    }

    // Construye la tabla de mascotas con datos de prueba.
    private JScrollPane crearTablaMascotas() {
        // Nombres de las columnas.
        String[] columnas = {
            "Expediente",
            "Mascota",
            "Especie",
            "Raza",
            "Edad",
            "Propietario"
        };

        Object[][] datosTemporales = {
            {"EXP-001", "Spike", "Perro", "Bulldog", "4 años", "Tom"},
            {"EXP-002", "Garfield", "Gato", "Naranja rayado", "6 años", "John"},
            {"EXP-003", "Snoopy", "Perro", "Beagle", "5 años", "Charlie Brown"},
            {"EXP-004", "Pelusa", "Gato", "Común", "3 año", "Angelica"}
        };

        // Modelo que controla los datos de la tabla.
        DefaultTableModel modelo= new DefaultTableModel(datosTemporales, columnas) {
            // Impide editar directamente las celdas.
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        // Diseño de la tabla Mascotas
        tablaMascotas = new JTable(modelo);
        tablaMascotas.setRowHeight(42);
        tablaMascotas.setShowVerticalLines(false);
        tablaMascotas.setShowHorizontalLines(true);
        tablaMascotas.setGridColor(BORDE);
        tablaMascotas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Solo se puede seleccionar una mascota.
        tablaMascotas.setSelectionBackground(SELECCION); // colorea la mascota seleccionada
        tablaMascotas.setSelectionForeground(TEXTO);
        tablaMascotas.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Diseño del encabezado de la tabla.
        tablaMascotas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaMascotas.getTableHeader().setBackground(AZUL_OSCURO);
        tablaMascotas.getTableHeader().setForeground(Color.WHITE);
        tablaMascotas.getTableHeader().setPreferredSize(new Dimension(0, 42));
        tablaMascotas.setFillsViewportHeight(true); // La tabla ocupa toda el área disponible.

        JScrollPane scroll= new JScrollPane(tablaMascotas);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        return scroll;
    }
}
