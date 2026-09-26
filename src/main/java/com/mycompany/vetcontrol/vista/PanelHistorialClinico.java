/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import com.mycompany.vetcontrol.dao.HistorialClinicoDAO;
import com.mycompany.vetcontrol.dao.MascotaDAO;
import com.mycompany.vetcontrol.dao.VeterinarioDAO;
import com.mycompany.vetcontrol.modelo.HistorialClinico;
import com.mycompany.vetcontrol.modelo.Mascota;
import com.mycompany.vetcontrol.modelo.Veterinario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
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

    // Permite consultar los registros almacenados en MySQL.
    private final HistorialClinicoDAO historialDAO;
    
    // DAO utilizados para llenar los JComboBox del diálogo.
    private final MascotaDAO mascotaDAO;
    private final VeterinarioDAO veterinarioDAO;

    // Modelo utilizado para agregar y quitar filas de la tabla.
    private DefaultTableModel modeloTabla;

    // Botones que tendrán eventos.
    private JButton btnBuscar;
    private JButton btnNuevo;
    private JButton btnVerDetalle;

    // Conserva los objetos mostrados en el mismo orden de la tabla.
    private List<HistorialClinico> historialesMostrados =
        new ArrayList<>();

    // Formato utilizado para mostrar la fecha y hora.
    private static final DateTimeFormatter FORMATO_FECHA =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public PanelHistorialClinico() {

        historialDAO = new HistorialClinicoDAO();
        mascotaDAO = new MascotaDAO();
        veterinarioDAO = new VeterinarioDAO();

        crearInterfaz();
        cargarHistoriales();
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
        // También permite buscar presionando Enter.
        txtBuscar.addActionListener(
            evento -> buscarHistoriales()
        );

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

        panelBotones.setLayout(
            new BoxLayout(
                panelBotones,
                BoxLayout.X_AXIS
            )
        );

        panelBotones.setBackground(Color.WHITE);

        /*
         * No colocamos JButton antes del nombre porque estos
         * botones ya están declarados como atributos de la clase.
         */
        btnBuscar = crearBoton(
            "Buscar",
            AZUL_PRINCIPAL,
            105
        );

        btnNuevo = crearBoton(
            "+ Nueva",
            TURQUESA,
            105
        );

        btnVerDetalle = crearBoton(
            "Ver detalle",
            GRIS_BOTON,
            120
        );

        // Eventos.
        btnBuscar.addActionListener(
            evento -> buscarHistoriales()
        );

        btnNuevo.addActionListener(
            evento -> abrirNuevoHistorial()
        );

        btnVerDetalle.addActionListener(
            evento -> abrirDetalleHistorial()
        );

        // Agregar los botones al panel.
        panelBotones.add(btnBuscar);
        panelBotones.add(
            Box.createHorizontalStrut(10)
        );

        panelBotones.add(btnNuevo);
        panelBotones.add(
            Box.createHorizontalStrut(10)
        );

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
        String[] columnas = {
            "Código",
            "Expediente",
            "Fecha",
            "Mascota",
            "Tipo",
            "Diagnóstico",
            "Veterinario"
        };

        /*
         * La tabla comienza vacía.
         * cargarHistoriales() agregará los datos de MySQL.
         */
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna) {

                return false;
            }
        };

        tablaHistorial = new JTable(modeloTabla);
        
        tablaHistorial.addMouseListener(
            new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(
                        java.awt.event.MouseEvent evento) {

                    if (evento.getClickCount() == 2) {
                        abrirDetalleHistorial();
                    }
                }
            }
        );

        // Apariencia general.
        tablaHistorial.setRowHeight(42);
        tablaHistorial.setShowVerticalLines(false);
        tablaHistorial.setShowHorizontalLines(true);
        tablaHistorial.setGridColor(BORDE);
        tablaHistorial.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        // Colores de la fila seleccionada.
        tablaHistorial.setSelectionBackground(AZUL_SELECCION);
        tablaHistorial.setSelectionForeground(TEXTO);
        tablaHistorial.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        // Apariencia del encabezado.
        tablaHistorial.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        tablaHistorial.getTableHeader().setBackground(AZUL_OSCURO);
        tablaHistorial.getTableHeader().setForeground(Color.WHITE);
        tablaHistorial.getTableHeader().setPreferredSize(
            new Dimension(0, 42)
        );

        tablaHistorial.getTableHeader()
            .setReorderingAllowed(false);

        /*
         * Desactivamos el ajuste automático porque hay varias
         * columnas. Así aparecerá una barra horizontal cuando
         * sea necesario.
         */
        tablaHistorial.setAutoResizeMode(
            JTable.AUTO_RESIZE_OFF
        );

        tablaHistorial.getColumnModel()
            .getColumn(0).setPreferredWidth(90);  // Código

        tablaHistorial.getColumnModel()
            .getColumn(1).setPreferredWidth(110); // Expediente

        tablaHistorial.getColumnModel()
            .getColumn(2).setPreferredWidth(145); // Fecha

        tablaHistorial.getColumnModel()
            .getColumn(3).setPreferredWidth(130); // Mascota

        tablaHistorial.getColumnModel()
            .getColumn(4).setPreferredWidth(120); // Tipo

        tablaHistorial.getColumnModel()
            .getColumn(5).setPreferredWidth(260); // Diagnóstico

        tablaHistorial.getColumnModel()
            .getColumn(6).setPreferredWidth(180); // Veterinario

        tablaHistorial.setFillsViewportHeight(true);

        JScrollPane scroll =
            new JScrollPane(tablaHistorial);

        scroll.setBorder(
            BorderFactory.createEmptyBorder()
        );

        return scroll;
    }
    /**
    * Obtiene todos los registros clínicos desde MySQL.
    */
   private void cargarHistoriales() {

       List<HistorialClinico> historiales =
           historialDAO.listar();

       mostrarHistoriales(historiales);
   }

   /**
    * Busca utilizando el contenido del campo de texto.
    */
   private void buscarHistoriales() {

       String criterio =
           txtBuscar.getText().trim();

       /*
        * Si no se escribió ningún criterio,
        * vuelve a mostrar todos los registros.
        */
       if (criterio.isBlank()) {
           cargarHistoriales();
           return;
       }

       List<HistorialClinico> historiales =
           historialDAO.buscar(criterio);

       mostrarHistoriales(historiales);
   }

   /**
    * Coloca los registros clínicos dentro de la tabla.
    */
   private void mostrarHistoriales(
           List<HistorialClinico> historiales) {

       /*
        * Copiamos la lista para poder obtener posteriormente
        * el objeto relacionado con una fila seleccionada.
        */
       historialesMostrados =
           new ArrayList<>(historiales);

       modeloTabla.setRowCount(0);

       for (HistorialClinico historial : historialesMostrados) {

           String fecha = historial.getFechaAtencion() == null
               ? ""
               : historial.getFechaAtencion()
                   .format(FORMATO_FECHA);

           String tipo = historial.getTipoRegistro() == null
               ? ""
               : historial.getTipoRegistro()
                   .name()
                   .replace('_', ' ');

           modeloTabla.addRow(new Object[] {
               historial.getCodigoVisible(),
               historial.getNumeroExpediente(),
               fecha,
               historial.getNombreMascota(),
               tipo,
               textoSeguro(historial.getDiagnostico()),
               historial.getNombreVeterinario()
           });
       }
   }

   /**
    * Evita mostrar la palabra "null" en la tabla.
    */
   private String textoSeguro(String texto) {

       return texto == null
           ? ""
           : texto;
   }
   
   /**
    * Abre el formulario para registrar una atención clínica.
    */
   private void abrirNuevoHistorial() {

       List<Mascota> mascotas =
           mascotaDAO.listarActivas();

       List<Veterinario> veterinarios =
           veterinarioDAO.listarActivos();

       if (mascotas.isEmpty()) {

           JOptionPane.showMessageDialog(
               this,
               "No existen mascotas activas para registrar.",
               "Historial clínico",
               JOptionPane.WARNING_MESSAGE
           );

           return;
       }

       if (veterinarios.isEmpty()) {

           JOptionPane.showMessageDialog(
               this,
               "No existen veterinarios activos.",
               "Historial clínico",
               JOptionPane.WARNING_MESSAGE
           );

           return;
       }

       Window ventana =
           SwingUtilities.getWindowAncestor(this);

       DlgHistorialClinico dialogo =
           new DlgHistorialClinico(
               ventana,
               null,
               mascotas,
               veterinarios
           );

       dialogo.setVisible(true);

       if (!dialogo.isGuardado()) {
           return;
       }

       HistorialClinico historial =
           dialogo.obtenerHistorial();

       if (historialDAO.insertar(historial)) {

           JOptionPane.showMessageDialog(
               this,
               "El registro clínico fue guardado correctamente.",
               "Historial clínico",
               JOptionPane.INFORMATION_MESSAGE
           );

           cargarHistoriales();

       } else {

           JOptionPane.showMessageDialog(
               this,
               "No fue posible guardar el registro clínico.",
               "Error",
               JOptionPane.ERROR_MESSAGE
           );
       }
   }

    /**
     * Abre el registro correspondiente a la fila seleccionada.
     *
     * El mismo formulario permite consultar y actualizar
     * toda la información del registro.
     */
    private void abrirDetalleHistorial() {

        int filaSeleccionada =
            tablaHistorial.getSelectedRow();

        if (filaSeleccionada < 0) {

            JOptionPane.showMessageDialog(
                this,
                "Selecciona un registro clínico.",
                "Historial clínico",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        HistorialClinico historialSeleccionado =
            historialesMostrados.get(filaSeleccionada);

        /*
         * Volvemos a consultar el registro para garantizar
         * que el diálogo reciba la información más reciente.
         */
        HistorialClinico historial =
            historialDAO.buscarPorId(
                historialSeleccionado.getIdHistorial()
            );

        if (historial == null) {

            JOptionPane.showMessageDialog(
                this,
                "No fue posible encontrar el registro seleccionado.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        List<Mascota> mascotas =
            mascotaDAO.listarActivas();

        List<Veterinario> veterinarios =
            veterinarioDAO.listarActivos();

        Window ventana =
            SwingUtilities.getWindowAncestor(this);

        DlgHistorialClinico dialogo =
            new DlgHistorialClinico(
                ventana,
                historial,
                mascotas,
                veterinarios
            );

        dialogo.setVisible(true);

        if (!dialogo.isGuardado()) {
            return;
        }

        HistorialClinico historialActualizado =
            dialogo.obtenerHistorial();

        if (historialDAO.actualizar(historialActualizado)) {

            JOptionPane.showMessageDialog(
                this,
                "El registro clínico fue actualizado correctamente.",
                "Historial clínico",
                JOptionPane.INFORMATION_MESSAGE
            );

            cargarHistoriales();

        } else {

            JOptionPane.showMessageDialog(
                this,
                "No fue posible actualizar el registro clínico.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}