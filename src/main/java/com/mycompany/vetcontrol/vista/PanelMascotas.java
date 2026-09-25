/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import com.mycompany.vetcontrol.dao.ClienteDAO;
import com.mycompany.vetcontrol.dao.MascotaDAO;
import com.mycompany.vetcontrol.modelo.Cliente;
import com.mycompany.vetcontrol.modelo.Mascota;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
import java.time.Period;
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
    private static final Color NARANJA=  new Color(242, 161, 62);
    
    // Permite obtener los propietarios disponibles.
    private final ClienteDAO clienteDAO;

    // Botones utilizados por los eventos del panel.
    private JButton btnBuscar;
    private JButton btnNueva;
    private JButton btnEditar;
    private JButton btnEliminar;
    
    private JTextField txtBuscar;
    private JTable tablaMascotas;
    
    // Permite consultar las mascotas almacenadas en MySQL.
    private final MascotaDAO mascotaDAO;

    // Modelo que administra las filas de la tabla.
    private DefaultTableModel modeloTabla;

    // Conserva el mismo orden de las mascotas mostradas en la tabla.
    // Será necesario posteriormente para editar y eliminar.
    private List<Mascota> mascotasMostradas = new ArrayList<>();

    // Constructor del panel
    public PanelMascotas() {
        mascotaDAO = new MascotaDAO();
        clienteDAO = new ClienteDAO();
        crearInterfaz();
        cargarMascotas();
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
        txtBuscar.addActionListener(evento -> buscarMascotas()); // También permite buscar presionando Enter.

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
            new BoxLayout(panelBotones, BoxLayout.X_AXIS)
        );

        panelBotones.setBackground(Color.WHITE);

        // Crear los botones.
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

        btnEliminar = crearBoton(
            "Eliminar",
            ROJO
        );

        // Asignar las acciones.
        btnBuscar.addActionListener(
            evento -> buscarMascotas()
        );

        btnNueva.addActionListener(
            evento -> registrarMascota()
        );

        btnEditar.addActionListener(
            evento -> editarMascota()
        );

        btnEliminar.addActionListener(
            evento -> eliminarMascota()
        );

        // Agregar los botones al panel.
        panelBotones.add(btnBuscar);
        panelBotones.add(Box.createHorizontalStrut(10));

        panelBotones.add(btnNueva);
        panelBotones.add(Box.createHorizontalStrut(10));

        panelBotones.add(btnEditar);
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

    // Construye la tabla que mostrará las mascotas de MySQL.
    private JScrollPane crearTablaMascotas() {

        String[] columnas = {
            "Expediente",
            "Mascota",
            "Especie",
            "Raza",
            "Sexo",
            "Nacimiento",
            "Edad",
            "Color",
            "Propietario"
        };

        /*
         * La tabla comienza vacía.
         * cargarMascotas() añadirá los registros obtenidos de MySQL.
         */
        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna) {

                return false;
            }
        };

        /*
         * Primero se crea la tabla.
         * Antes de esta línea tablaMascotas es null.
         */
        tablaMascotas = new JTable(modeloTabla);

        /*
         * Impide que Swing comprima automáticamente
         * todas las columnas dentro del espacio disponible.
         */
        tablaMascotas.setAutoResizeMode(
            JTable.AUTO_RESIZE_OFF
        );

        // Apariencia general.
        tablaMascotas.setRowHeight(42);
        tablaMascotas.setShowVerticalLines(false);
        tablaMascotas.setShowHorizontalLines(true);
        tablaMascotas.setGridColor(BORDE);

        tablaMascotas.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        tablaMascotas.setSelectionBackground(SELECCION);
        tablaMascotas.setSelectionForeground(TEXTO);

        tablaMascotas.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        // Apariencia del encabezado.
        tablaMascotas.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        tablaMascotas.getTableHeader().setBackground(
            AZUL_OSCURO
        );

        tablaMascotas.getTableHeader().setForeground(
            Color.WHITE
        );

        tablaMascotas.getTableHeader().setPreferredSize(
            new Dimension(0, 42)
        );

        /*
         * Los anchos se configuran después de crear la tabla,
         * porque hasta ese momento existen sus columnas.
         */
        tablaMascotas.getColumnModel()
            .getColumn(0).setPreferredWidth(100);

        tablaMascotas.getColumnModel()
            .getColumn(1).setPreferredWidth(120);

        tablaMascotas.getColumnModel()
            .getColumn(2).setPreferredWidth(100);

        tablaMascotas.getColumnModel()
            .getColumn(3).setPreferredWidth(130);

        tablaMascotas.getColumnModel()
            .getColumn(4).setPreferredWidth(100);

        tablaMascotas.getColumnModel()
            .getColumn(5).setPreferredWidth(120);

        tablaMascotas.getColumnModel()
            .getColumn(6).setPreferredWidth(90);

        tablaMascotas.getColumnModel()
            .getColumn(7).setPreferredWidth(120);

        tablaMascotas.getColumnModel()
            .getColumn(8).setPreferredWidth(180);

        tablaMascotas.setFillsViewportHeight(true);

        /*
         * El JScrollPane mostrará una barra horizontal
         * cuando las columnas superen el ancho disponible.
         */
        JScrollPane scroll =
            new JScrollPane(tablaMascotas);

        scroll.setBorder(
            BorderFactory.createEmptyBorder()
        );

        return scroll;
    }
    /**
    * Obtiene todas las mascotas activas desde MySQL.
    */
    private void cargarMascotas() {

       List<Mascota> mascotas =
           mascotaDAO.listarActivas();

       mostrarMascotas(mascotas);
   }

   /**
    * Busca mascotas utilizando el contenido del campo de texto.
    */
   private void buscarMascotas() {

       String criterio = txtBuscar.getText().trim();

       /*
        * Si no se escribió ningún criterio,
        * vuelve a cargar todas las mascotas.
        */
       if (criterio.isBlank()) {
           cargarMascotas();
           return;
       }

       List<Mascota> mascotas =
           mascotaDAO.buscar(criterio);

       mostrarMascotas(mascotas);
   }

   /**
    * Coloca una lista de mascotas dentro de la tabla.
    */
   private void mostrarMascotas(List<Mascota> mascotas) {

    /*
     * Guardamos la lista porque posteriormente necesitaremos
     * identificar la mascota seleccionada para editarla
     * o desactivarla.
     */
    mascotasMostradas = new ArrayList<>(mascotas);

    // Elimina las filas mostradas anteriormente.
    modeloTabla.setRowCount(0);

    for (Mascota mascota : mascotasMostradas) {

        modeloTabla.addRow(new Object[] {
            mascota.getNumeroExpediente(),
            mascota.getNombre(),
            mascota.getEspecie(),
            textoOpcional(mascota.getRaza()),
            obtenerTextoSexo(mascota.getSexo()),
            mascota.getFechaNacimiento() == null
                ? "—"
                : mascota.getFechaNacimiento(),
            calcularEdad(mascota.getFechaNacimiento()),
            textoOpcional(mascota.getColor()),
            mascota.getNombrePropietario()
        });
        }
    }

    /**
     * Calcula la edad actual a partir de la fecha de nacimiento.
     */
    private String calcularEdad(LocalDate fechaNacimiento) {

        if (fechaNacimiento == null) {
            return "Sin registrar";
        }

        /*
         * Evita calcular una edad incorrecta si accidentalmente
         * se guardó una fecha futura.
         */
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            return "Fecha inválida";
        }

        Period periodo =
            Period.between(fechaNacimiento, LocalDate.now());

        int anios = periodo.getYears();
        int meses = periodo.getMonths();

        if (anios == 0) {
            return meses == 1
                ? "1 mes"
                : meses + " meses";
        }

        return anios == 1
            ? "1 año"
            : anios + " años";
    }

    /**
     * Evita mostrar la palabra null en las celdas.
     */
    private String textoOpcional(String texto) {

        return texto == null || texto.isBlank()
            ? "—"
            : texto;
    }
    /**
    * Abre el diálogo para registrar una mascota.
    */
    private void registrarMascota() {

    List<Cliente> clientes =
        clienteDAO.listarActivos();

    /*
     * Una mascota no puede registrarse sin propietario.
     */
    if (clientes.isEmpty()) {

        JOptionPane.showMessageDialog(
            this,
            "Primero debes registrar un cliente.",
            "No hay propietarios",
            JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    DlgMascota dialogo = new DlgMascota(
        SwingUtilities.getWindowAncestor(this),
        null,
        clientes
    );

    dialogo.setVisible(true);

    /*
     * Si el usuario cerró el diálogo o presionó Cancelar,
     * no se realiza ninguna operación.
     */
    if (!dialogo.isGuardado()) {
        return;
    }

    Mascota mascota =
        dialogo.obtenerMascota();

    if (mascotaDAO.insertar(mascota)) {

        JOptionPane.showMessageDialog(
            this,
            "Mascota registrada correctamente.",
            "Registro completado",
            JOptionPane.INFORMATION_MESSAGE
        );

        cargarMascotas();

    } else {

        JOptionPane.showMessageDialog(
            this,
            "No fue posible registrar la mascota.\n"
            + "Comprueba que el expediente no esté repetido.",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}

    /**
     * Abre el diálogo con la información de la mascota seleccionada.
     */
    private void editarMascota() {

        Mascota mascota =
            obtenerMascotaSeleccionada();

        if (mascota == null) {
            return;
        }

        List<Cliente> clientes =
            clienteDAO.listarActivos();

        if (clientes.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "No existen propietarios activos.",
                "No hay propietarios",
                JOptionPane.WARNING_MESSAGE
            );

        return;
    }

    DlgMascota dialogo = new DlgMascota(
        SwingUtilities.getWindowAncestor(this),
        mascota,
        clientes
    );

    dialogo.setVisible(true);

    if (!dialogo.isGuardado()) {
        return;
    }

    Mascota mascotaEditada =
        dialogo.obtenerMascota();

    if (mascotaDAO.actualizar(mascotaEditada)) {

        JOptionPane.showMessageDialog(
            this,
            "Mascota actualizada correctamente.",
            "Actualización completada",
            JOptionPane.INFORMATION_MESSAGE
        );

        cargarMascotas();

    } else {

        JOptionPane.showMessageDialog(
            this,
            "No fue posible actualizar la mascota.\n"
            + "Comprueba que el expediente no esté repetido.",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        }
    }

    /**
     * Solicita confirmación y desactiva la mascota seleccionada.
     */
    private void eliminarMascota() {

        Mascota mascota =
            obtenerMascotaSeleccionada();

        if (mascota == null) {
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Deseas eliminar a "
            + mascota.getNombre()
            + "?\n\n"
            + "El registro será desactivado, no eliminado "
            + "permanentemente.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        if (mascotaDAO.desactivar(
                mascota.getIdMascota())) {

            JOptionPane.showMessageDialog(
                this,
                "Mascota eliminada correctamente.",
                "Operación completada",
                JOptionPane.INFORMATION_MESSAGE
            );

            cargarMascotas();

        } else {

            JOptionPane.showMessageDialog(
                this,
                "No fue posible eliminar la mascota.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Obtiene el objeto correspondiente a la fila seleccionada.
     */
    private Mascota obtenerMascotaSeleccionada() {

        int filaSeleccionada =
            tablaMascotas.getSelectedRow();

        if (filaSeleccionada == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Selecciona una mascota en la tabla.",
                "Ninguna mascota seleccionada",
                JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        /*
         * Convierte el índice visual al índice del modelo.
         * Esto será útil si después permitimos ordenar la tabla.
         */
        int filaModelo =
            tablaMascotas.convertRowIndexToModel(
                filaSeleccionada
            );

        if (filaModelo < 0
                || filaModelo >= mascotasMostradas.size()) {

            JOptionPane.showMessageDialog(
                this,
                "No fue posible identificar la mascota seleccionada.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );

            return null;
        }

        return mascotasMostradas.get(filaModelo);
    }
    /**
    * Convierte el ENUM de sexo en un texto más agradable.
    */
    private String obtenerTextoSexo(
           Mascota.Sexo sexo) {

       if (sexo == null) {
           return "Desconocido";
       }

       return switch (sexo) {
           case MACHO -> "Macho";
           case HEMBRA -> "Hembra";
           case DESCONOCIDO -> "Desconocido";
       };
    }
}
