/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import com.mycompany.vetcontrol.dao.CitaDAO;
import com.mycompany.vetcontrol.modelo.Cita;
import com.mycompany.vetcontrol.dao.MascotaDAO;
import com.mycompany.vetcontrol.dao.VeterinarioDAO;
import com.mycompany.vetcontrol.modelo.Mascota;
import com.mycompany.vetcontrol.modelo.Veterinario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

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
    private static final Color NARANJA= new Color(242, 161, 62);

    // Tabla que mostrará las citas registradas
    private JTable tablaCitas;
    
    // Acceso a las citas almacenadas en MySQL.
    private final CitaDAO citaDAO;

    // Modelo utilizado para modificar las filas de la tabla.
    private DefaultTableModel modeloTabla;

    // Campo de búsqueda utilizado por sus eventos.
    private JTextField txtBuscar;

    // Conserva el orden de las citas mostradas.
    private List<Cita> citasMostradas =
        new ArrayList<>();

    // Formatos utilizados en la tabla.
    private static final DateTimeFormatter FORMATO_FECHA =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final DateTimeFormatter FORMATO_HORA =
        DateTimeFormatter.ofPattern("HH:mm");
    
    // botones de busqueda y obtener información de la mascota y veterinario
    private final MascotaDAO mascotaDAO;
    private final VeterinarioDAO veterinarioDAO;

    private JButton btnBuscar;
    private JButton btnNueva;
    private JButton btnEditar;
    private JButton btnCancelar;

    public PanelCitas() {
        citaDAO = new CitaDAO();
        mascotaDAO = new MascotaDAO();
        veterinarioDAO = new VeterinarioDAO();

        crearInterfaz();
        cargarCitas();
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
        txtBuscar = new JTextField();

        txtBuscar.putClientProperty(
            "JTextField.placeholderText",
            "Mascota, propietario, veterinario, motivo o estado..."
        );

        txtBuscar.setPreferredSize(
            new Dimension(300, 42)
        );

        // Permite buscar presionando Enter.
        txtBuscar.addActionListener(
            evento -> buscarCitas()
        );
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

        JPanel panelBotones = new JPanel();

        panelBotones.setLayout(
            new BoxLayout(panelBotones, BoxLayout.X_AXIS)
        );

        panelBotones.setBackground(Color.WHITE);

        btnBuscar = crearBoton(
            "Buscar",
            AZUL_PRINCIPAL
        );

        btnNueva = crearBoton(
            "+ Nueva",
            TURQUESA
        );

        btnEditar = crearBoton(
            "Editar",
            NARANJA
        );

        btnCancelar = crearBoton(
            "Cancelar",
            ROJO
        );

        btnBuscar.addActionListener(
            evento -> buscarCitas()
        );

        btnNueva.addActionListener(
            evento -> registrarCita()
        );

        btnEditar.addActionListener(
            evento -> editarCita()
        );

        btnCancelar.addActionListener(
            evento -> cancelarCita()
        );

        panelBotones.add(btnBuscar);
        panelBotones.add(Box.createHorizontalStrut(10));

        panelBotones.add(btnNueva);
        panelBotones.add(Box.createHorizontalStrut(10));

        panelBotones.add(btnEditar);
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
    
    /**
 * Construye la tabla que mostrará las citas de MySQL.
 */
private JScrollPane crearTablaCitas() {

    String[] columnas = {
        "Código",
        "Fecha",
        "Hora",
        "Mascota",
        "Expediente",
        "Propietario",
        "Veterinario",
        "Motivo",
        "Estado"
    };

    /*
     * La tabla empieza vacía.
     * cargarCitas() agregará los registros de MySQL.
     */
    modeloTabla = new DefaultTableModel(columnas, 0) {

        @Override
        public boolean isCellEditable(
                int fila,
                int columna) {

            return false;
        }
    };

        // La tabla debe crearse antes de configurarla.
        tablaCitas = new JTable(modeloTabla);

        /*
         * Como existen varias columnas, se utiliza
         * desplazamiento horizontal.
         */
        tablaCitas.setAutoResizeMode(
            JTable.AUTO_RESIZE_OFF
        );

        tablaCitas.setRowHeight(42);
        tablaCitas.setShowVerticalLines(false);
        tablaCitas.setShowHorizontalLines(true);
        tablaCitas.setGridColor(BORDE);

        tablaCitas.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        tablaCitas.setSelectionBackground(
            new Color(232, 242, 247)
        );

        tablaCitas.setSelectionForeground(
            new Color(36, 50, 61)
        );

        tablaCitas.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        // Apariencia del encabezado.
        tablaCitas.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        tablaCitas.getTableHeader().setBackground(
            AZUL_OSCURO
        );

        tablaCitas.getTableHeader().setForeground(
            Color.WHITE
        );

        tablaCitas.getTableHeader().setPreferredSize(
            new Dimension(0, 42)
        );

        // Ancho de las columnas.
        tablaCitas.getColumnModel()
            .getColumn(0).setPreferredWidth(90);

        tablaCitas.getColumnModel()
            .getColumn(1).setPreferredWidth(100);

        tablaCitas.getColumnModel()
            .getColumn(2).setPreferredWidth(75);

        tablaCitas.getColumnModel()
            .getColumn(3).setPreferredWidth(120);

        tablaCitas.getColumnModel()
            .getColumn(4).setPreferredWidth(110);

        tablaCitas.getColumnModel()
            .getColumn(5).setPreferredWidth(180);

        tablaCitas.getColumnModel()
            .getColumn(6).setPreferredWidth(180);

        tablaCitas.getColumnModel()
            .getColumn(7).setPreferredWidth(220);

        tablaCitas.getColumnModel()
            .getColumn(8).setPreferredWidth(120);

        tablaCitas.setFillsViewportHeight(true);

        JScrollPane scroll =
            new JScrollPane(tablaCitas);

        scroll.setBorder(
            BorderFactory.createEmptyBorder()
        );

        return scroll;
    }
    /**
     * Carga todas las citas registradas.
     */
    private void cargarCitas() {

        List<Cita> citas =
            citaDAO.listar();

        mostrarCitas(citas);
    }

    /**
     * Busca citas utilizando el contenido del buscador.
     */
    private void buscarCitas() {

        String criterio =
            txtBuscar.getText().trim();

        if (criterio.isBlank()) {
            cargarCitas();
            return;
        }

        List<Cita> citas =
            citaDAO.buscar(criterio);

        mostrarCitas(citas);
    }

    /**
     * Coloca una lista de citas dentro de la tabla.
     */
    private void mostrarCitas(List<Cita> citas) {

        citasMostradas =
            new ArrayList<>(citas);

        modeloTabla.setRowCount(0);

        for (Cita cita : citasMostradas) {

            modeloTabla.addRow(new Object[] {
                cita.getCodigoVisible(),

                cita.getFechaHora().format(
                    FORMATO_FECHA
                ),

                cita.getFechaHora().format(
                    FORMATO_HORA
                ),

                cita.getNombreMascota(),
                cita.getNumeroExpediente(),
                cita.getNombrePropietario(),
                cita.getNombreVeterinario(),
                cita.getMotivo(),
                obtenerTextoEstado(cita.getEstado())
            });
        }
    }

    /**
     * Convierte el ENUM en un texto adecuado para la interfaz.
     */
    private String obtenerTextoEstado(
            Cita.Estado estado) {

        if (estado == null) {
            return "Sin estado";
        }

        return switch (estado) {
            case PROGRAMADA -> "Programada";
            case CONFIRMADA -> "Confirmada";
            case ATENDIDA -> "Atendida";
            case CANCELADA -> "Cancelada";
            case NO_ASISTIO -> "No asistió";
        };
    }
    
    /**
    * Abre el diálogo para programar una cita nueva.
    */
   private void registrarCita() {

       List<Mascota> mascotas =
           mascotaDAO.listarActivas();

       List<Veterinario> veterinarios =
           veterinarioDAO.listarActivos();

       if (mascotas.isEmpty()) {

           JOptionPane.showMessageDialog(
               this,
               "Primero debes registrar una mascota.",
               "No hay mascotas",
               JOptionPane.WARNING_MESSAGE
           );

           return;
       }

       if (veterinarios.isEmpty()) {

           JOptionPane.showMessageDialog(
               this,
               "No existen veterinarios activos.",
               "No hay veterinarios",
               JOptionPane.WARNING_MESSAGE
           );

           return;
       }

       DlgCita dialogo = new DlgCita(
           SwingUtilities.getWindowAncestor(this),
           null,
           mascotas,
           veterinarios
       );

       dialogo.setVisible(true);

       if (!dialogo.isGuardado()) {
           return;
       }

       Cita cita = dialogo.obtenerCita();

       if (citaDAO.insertar(cita)) {

           JOptionPane.showMessageDialog(
               this,
               "Cita programada correctamente.",
               "Registro completado",
               JOptionPane.INFORMATION_MESSAGE
           );

           cargarCitas();

       } else {

           mostrarErrorGuardado();
       }
   }

    /**
     * Modifica la cita seleccionada.
     */
    private void editarCita() {

        Cita cita = obtenerCitaSeleccionada();

        if (cita == null) {
            return;
        }

        List<Mascota> mascotas =
            mascotaDAO.listarActivas();

        List<Veterinario> veterinarios =
            veterinarioDAO.listarActivos();

        if (mascotas.isEmpty()
                || veterinarios.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Se necesitan mascotas y veterinarios activos.",
                "Datos insuficientes",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        DlgCita dialogo = new DlgCita(
            SwingUtilities.getWindowAncestor(this),
            cita,
            mascotas,
            veterinarios
        );

        dialogo.setVisible(true);

        if (!dialogo.isGuardado()) {
            return;
        }

        Cita citaEditada =
            dialogo.obtenerCita();

        if (citaDAO.actualizar(citaEditada)) {

            JOptionPane.showMessageDialog(
                this,
                "Cita actualizada correctamente.",
                "Actualización completada",
                JOptionPane.INFORMATION_MESSAGE
            );

            cargarCitas();

        } else {

            mostrarErrorGuardado();
        }
    }

    /**
     * Cambia el estado de la cita a CANCELADA.
     */
    private void cancelarCita() {

        Cita cita = obtenerCitaSeleccionada();

        if (cita == null) {
            return;
        }

        if (cita.getEstado()
                == Cita.Estado.CANCELADA) {

            JOptionPane.showMessageDialog(
                this,
                "La cita ya se encuentra cancelada.",
                "Cita cancelada",
                JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        if (cita.getEstado()
                == Cita.Estado.ATENDIDA) {

            JOptionPane.showMessageDialog(
                this,
                "Una cita atendida no puede cancelarse.",
                "Operación no permitida",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta =
            JOptionPane.showConfirmDialog(
                this,
                "¿Deseas cancelar la cita "
                + cita.getCodigoVisible()
                + "?\n\n"
                + cita.getNombreMascota()
                + " - "
                + cita.getNombreVeterinario(),
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        if (citaDAO.cambiarEstado(
                cita.getIdCita(),
                Cita.Estado.CANCELADA)) {

            JOptionPane.showMessageDialog(
                this,
                "Cita cancelada correctamente.",
                "Operación completada",
                JOptionPane.INFORMATION_MESSAGE
            );

            cargarCitas();

        } else {

            JOptionPane.showMessageDialog(
                this,
                "No fue posible cancelar la cita.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Obtiene el objeto asociado a la fila seleccionada.
     */
    private Cita obtenerCitaSeleccionada() {

        int filaSeleccionada =
            tablaCitas.getSelectedRow();

        if (filaSeleccionada == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Selecciona una cita en la tabla.",
                "Ninguna cita seleccionada",
                JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        int filaModelo =
            tablaCitas.convertRowIndexToModel(
                filaSeleccionada
            );

        if (filaModelo < 0
                || filaModelo >= citasMostradas.size()) {

            JOptionPane.showMessageDialog(
                this,
                "No fue posible identificar la cita.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );

            return null;
        }

        return citasMostradas.get(filaModelo);
    }

    /**
     * Informa que el horario puede encontrarse ocupado.
     */
    private void mostrarErrorGuardado() {

        JOptionPane.showMessageDialog(
            this,
            "No fue posible guardar la cita.\n\n"
            + "Comprueba que el veterinario no tenga otra "
            + "cita activa en la misma fecha y hora.",
            "Horario no disponible",
            JOptionPane.ERROR_MESSAGE
        );
    }

}
