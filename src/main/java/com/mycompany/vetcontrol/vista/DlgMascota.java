/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import com.mycompany.vetcontrol.modelo.Cliente;
import com.mycompany.vetcontrol.modelo.Mascota;
import com.mycompany.vetcontrol.modelo.Mascota.Sexo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author weprg
 */
public class DlgMascota extends JDialog {

    // Colores de VetControl.
    private static final Color AZUL_OSCURO =
        new Color(23, 50, 77);

    private static final Color AZUL_PRINCIPAL =
        new Color(52, 120, 184);

    private static final Color TEXTO =
        new Color(36, 50, 61);

    private static final Color TEXTO_SECUNDARIO =
        new Color(98, 114, 125);

    private static final Color BORDE =
        new Color(216, 225, 232);

    private static final Color FONDO =
        new Color(244, 247, 250);

    // Componentes del formulario.
    private JComboBox<Cliente> cmbPropietario;
    private JTextField txtExpediente;
    private JTextField txtNombre;
    private JTextField txtEspecie;
    private JTextField txtRaza;
    private JComboBox<Sexo> cmbSexo;
    private JTextField txtFechaNacimiento;
    private JTextField txtColor;
    private JTextArea txtObservaciones;

    /*
     * Guarda la mascota recibida cuando se está editando.
     * Será null cuando se registre una mascota nueva.
     */
    private final Mascota mascotaOriginal;

    // Indica si el usuario confirmó el formulario.
    private boolean guardado;

    /**
     * @param propietario ventana desde la cual se abre el diálogo
     * @param mascota mascota que se editará; null si es un registro nuevo
     * @param clientes clientes activos que aparecerán en el selector
     */
    public DlgMascota(
            Window propietario,
            Mascota mascota,
            List<Cliente> clientes) {

        super(
            propietario,
            mascota == null
                ? "Nueva mascota"
                : "Editar mascota",
            ModalityType.APPLICATION_MODAL
        );

        this.mascotaOriginal = mascota;

        configurarVentana();
        crearInterfaz(clientes);

        if (mascota != null) {
            cargarDatosMascota(mascota);
        }
    }

    /**
     * Configura las propiedades generales del diálogo.
     */
    private void configurarVentana() {

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(FONDO);
    }

    /**
     * Construye el formulario completo.
     */
    private void crearInterfaz(List<Cliente> clientes) {

        setLayout(new BorderLayout());

        JPanel contenido = new JPanel(new BorderLayout(0, 25));
        contenido.setBackground(Color.WHITE);
        contenido.setBorder(new EmptyBorder(30, 40, 25, 40));

        contenido.add(crearEncabezado(), BorderLayout.NORTH);
        contenido.add(crearFormularioDesplazable(clientes), BorderLayout.CENTER);

        contenido.add(
            crearPanelBotones(),
            BorderLayout.SOUTH
        );

        add(contenido);

        pack();
        setMinimumSize(new Dimension(570, 720));
        setSize(new Dimension(570, 720));
        setLocationRelativeTo(getOwner());
    }

    /**
     * Crea el título y la descripción.
     */
    private JPanel crearEncabezado() {

        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(Color.WHITE);

        String titulo = mascotaOriginal == null
            ? "Registrar mascota"
            : "Editar mascota";

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(AZUL_OSCURO);
        lblTitulo.setFont(
            new Font("Segoe UI", Font.BOLD, 26)
        );

        JLabel lblDescripcion = new JLabel(
            "Completa la información del paciente"
        );

        lblDescripcion.setForeground(TEXTO_SECUNDARIO);
        lblDescripcion.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblDescripcion, BorderLayout.SOUTH);

        return panel;
    }
    /**
    * Coloca el formulario dentro de un área desplazable.
    *
    * De esta manera todos los campos permanecen disponibles,
    * incluso cuando la ventana no tiene suficiente altura.
    */
   private JScrollPane crearFormularioDesplazable(
           List<Cliente> clientes) {

       JPanel formulario =
           crearFormulario(clientes);

       JScrollPane scrollFormulario =
           new JScrollPane(
               formulario,
               JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
               JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
           );

       // Elimina el borde predeterminado del JScrollPane.
       scrollFormulario.setBorder(
           BorderFactory.createEmptyBorder()
       );

       scrollFormulario.getViewport().setBackground(
           Color.WHITE
       );

       /*
        * Determina cuántos píxeles avanza la rueda
        * del ratón en cada movimiento.
        */
       scrollFormulario
           .getVerticalScrollBar()
           .setUnitIncrement(16);

       return scrollFormulario;
   }

    /**
     * Crea y organiza los campos del formulario.
     */
    private JPanel crearFormulario(List<Cliente> clientes) {

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);

        GridBagConstraints restricciones =
            new GridBagConstraints();

        restricciones.gridx = 0;
        restricciones.gridy = 0;
        restricciones.weightx = 1;
        restricciones.fill = GridBagConstraints.HORIZONTAL;
        restricciones.anchor = GridBagConstraints.WEST;
        restricciones.insets = new Insets(0, 0, 6, 0);

        // Selector de propietario.
        agregarEtiqueta(
            formulario,
            restricciones,
            "Propietario *"
        );

        cmbPropietario = new JComboBox<>();

        for (Cliente cliente : clientes) {
            cmbPropietario.addItem(cliente);
        }

        /*
         * Muestra el nombre completo en lugar de la representación
         * predeterminada del objeto Cliente.
         */
        cmbPropietario.setRenderer(
            new DefaultListCellRenderer() {

                @Override
                public java.awt.Component
                        getListCellRendererComponent(
                            javax.swing.JList<?> lista,
                            Object valor,
                            int indice,
                            boolean seleccionado,
                            boolean tieneFoco) {

                    super.getListCellRendererComponent(
                        lista,
                        valor,
                        indice,
                        seleccionado,
                        tieneFoco
                    );

                    if (valor instanceof Cliente cliente) {
                        setText(cliente.getNombreCompleto());
                    }

                    return this;
                }
            }
        );

        configurarCampo(cmbPropietario);
        agregarCampo(
            formulario,
            restricciones,
            cmbPropietario
        );

        // Número de expediente.
        agregarEtiqueta(
            formulario,
            restricciones,
            "Número de expediente *"
        );

        txtExpediente = new JTextField();
        txtExpediente.setEditable(false);
        txtExpediente.setFocusable(false);
        txtExpediente.setBackground(
            new Color(244, 247, 250)
        );

        /*
         * Cuando es una mascota nueva todavía no existe un ID.
         * El expediente será generado después de insertarla.
         */
        if (mascotaOriginal == null) {
            txtExpediente.setText(
                "Se generará automáticamente"
            );
        }

        configurarCampo(txtExpediente);
        agregarCampo(
            formulario,
            restricciones,
            txtExpediente
        );

        // Nombre de la mascota.
        agregarEtiqueta(
            formulario,
            restricciones,
            "Nombre *"
        );

        txtNombre = new JTextField();
        configurarCampo(txtNombre);
        agregarCampo(formulario, restricciones, txtNombre);

        // Especie.
        agregarEtiqueta(
            formulario,
            restricciones,
            "Especie *"
        );

        txtEspecie = new JTextField();
        txtEspecie.putClientProperty(
            "JTextField.placeholderText",
            "Ejemplo: Perro, gato o ave"
        );

        configurarCampo(txtEspecie);
        agregarCampo(formulario, restricciones, txtEspecie);

        // Raza.
        agregarEtiqueta(
            formulario,
            restricciones,
            "Raza"
        );

        txtRaza = new JTextField();
        configurarCampo(txtRaza);
        agregarCampo(formulario, restricciones, txtRaza);

        // Sexo.
        agregarEtiqueta(
            formulario,
            restricciones,
            "Sexo"
        );

        cmbSexo = new JComboBox<>(Sexo.values());
        configurarCampo(cmbSexo);
        agregarCampo(formulario, restricciones, cmbSexo);

        // Fecha de nacimiento.
        agregarEtiqueta(
            formulario,
            restricciones,
            "Fecha de nacimiento"
        );

        txtFechaNacimiento = new JTextField();
        txtFechaNacimiento.putClientProperty(
            "JTextField.placeholderText",
            "AAAA-MM-DD"
        );

        configurarCampo(txtFechaNacimiento);
        agregarCampo(
            formulario,
            restricciones,
            txtFechaNacimiento
        );

        // Color.
        agregarEtiqueta(
            formulario,
            restricciones,
            "Color"
        );

        txtColor = new JTextField();
        configurarCampo(txtColor);
        agregarCampo(formulario, restricciones, txtColor);

        // Observaciones.
        agregarEtiqueta(
            formulario,
            restricciones,
            "Obervaciones"
        );

        txtObservaciones = new JTextArea(3, 20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        txtObservaciones.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        JScrollPane scrollObservaciones =
            new JScrollPane(txtObservaciones);

        scrollObservaciones.setBorder(
            BorderFactory.createLineBorder(BORDE)
        );

        restricciones.gridy++;
        restricciones.weighty = 0;
        restricciones.fill = GridBagConstraints.BOTH;
        restricciones.insets = new Insets(0, 0, 10, 0);

        formulario.add(
            scrollObservaciones,
            restricciones
        );

        return formulario;
    }

    /**
     * Agrega una etiqueta al formulario.
     */
    private void agregarEtiqueta(
            JPanel formulario,
            GridBagConstraints restricciones,
            String texto) {

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(TEXTO);
        etiqueta.setFont(
            new Font("Segoe UI", Font.BOLD, 13)
        );

        restricciones.weighty = 0;
        restricciones.fill = GridBagConstraints.HORIZONTAL;
        restricciones.insets = new Insets(0, 0, 6, 0);

        formulario.add(etiqueta, restricciones);
        restricciones.gridy++;
    }

    /**
     * Agrega un campo y deja separación con el siguiente.
     */
    private void agregarCampo(
            JPanel formulario,
            GridBagConstraints restricciones,
            java.awt.Component campo) {

        restricciones.insets = new Insets(0, 0, 12, 0);
        formulario.add(campo, restricciones);
        restricciones.gridy++;
    }

    /**
     * Asigna una altura uniforme a los campos.
     */
    private void configurarCampo(
            javax.swing.JComponent componente) {

        componente.setPreferredSize(
            new Dimension(400, 38)
        );

        componente.setMinimumSize(
            new Dimension(400, 38)
        );
    }

    /**
     * Crea los botones Guardar y Cancelar.
     */
    private JPanel crearPanelBotones() {

        JPanel panel = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );

        panel.setBackground(Color.WHITE);

        JButton btnCancelar =
            new JButton("Cancelar");

        btnCancelar.setPreferredSize(
            new Dimension(110, 42)
        );

        btnCancelar.setForeground(TEXTO);
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        btnCancelar.setFocusPainted(false);
        btnCancelar.putClientProperty(
            "JButton.buttonType",
            "borderless"
        );

        JButton btnGuardar =
            new JButton("Guardar");

        btnGuardar.setPreferredSize(
            new Dimension(110, 42)
        );

        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBackground(AZUL_PRINCIPAL);
        btnGuardar.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        btnGuardar.setFocusPainted(false);
        btnGuardar.putClientProperty(
            "JButton.buttonType",
            "borderless"
        );

        btnCancelar.addActionListener(
            evento -> dispose()
        );

        btnGuardar.addActionListener(
            evento -> confirmarFormulario()
        );

        panel.add(btnCancelar);
        panel.add(btnGuardar);

        return panel;
    }

    /**
     * Coloca los datos existentes cuando se edita una mascota.
     */
    private void cargarDatosMascota(Mascota mascota) {

        txtExpediente.setText(
            mascota.getNumeroExpediente()
        );

        txtNombre.setText(mascota.getNombre());
        txtEspecie.setText(mascota.getEspecie());
        txtRaza.setText(mascota.getRaza());
        cmbSexo.setSelectedItem(mascota.getSexo());

        if (mascota.getFechaNacimiento() != null) {
            txtFechaNacimiento.setText(
                mascota.getFechaNacimiento().toString()
            );
        }

        txtColor.setText(mascota.getColor());
        txtObservaciones.setText(
            mascota.getObservaciones()
        );

        /*
         * Busca en el JComboBox al cliente que sea propietario
         * de la mascota que se está editando.
         */
        for (int indice = 0;
                indice < cmbPropietario.getItemCount();
                indice++) {

            Cliente cliente =
                cmbPropietario.getItemAt(indice);

            if (cliente.getIdCliente()
                    == mascota.getIdCliente()) {

                cmbPropietario.setSelectedIndex(indice);
                break;
            }
        }
    }

    /**
     * Valida el formulario y confirma el guardado.
     */
    private void confirmarFormulario() {

        if (cmbPropietario.getSelectedItem() == null) {
            mostrarAdvertencia(
                "Debes seleccionar un propietario."
            );
            return;
        }

        if (txtNombre.getText().isBlank()) {
            mostrarAdvertencia(
                "Ingresa el nombre de la mascota."
            );
            txtNombre.requestFocus();
            return;
        }

        if (txtEspecie.getText().isBlank()) {
            mostrarAdvertencia(
                "Ingresa la especie de la mascota."
            );
            txtEspecie.requestFocus();
            return;
        }

        // Comprueba que la fecha tenga el formato correcto.
        try {
            obtenerFechaNacimiento();
        } catch (DateTimeParseException error) {
            mostrarAdvertencia(
                "La fecha debe utilizar el formato AAAA-MM-DD."
            );

            txtFechaNacimiento.requestFocus();
            return;
        }

        LocalDate fechaNacimiento =
            obtenerFechaNacimiento();

        if (fechaNacimiento != null
                && fechaNacimiento.isAfter(LocalDate.now())) {

            mostrarAdvertencia(
                "La fecha de nacimiento no puede ser futura."
            );

            txtFechaNacimiento.requestFocus();
            return;
        }

        guardado = true;
        dispose();
    }

    /**
     * Convierte el contenido del campo de fecha a LocalDate.
     */
    private LocalDate obtenerFechaNacimiento() {

        String texto =
            txtFechaNacimiento.getText().trim();

        if (texto.isBlank()) {
            return null;
        }

        return LocalDate.parse(texto);
    }

    /**
     * Muestra un mensaje de validación.
     */
    private void mostrarAdvertencia(String mensaje) {

        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Datos incompletos",
            JOptionPane.WARNING_MESSAGE
        );
    }

    /**
     * Indica si el usuario presionó Guardar.
     */
    public boolean isGuardado() {
        return guardado;
    }

    /**
     * Construye el objeto Mascota con los datos del formulario.
     */
    public Mascota obtenerMascota() {

        Cliente propietario =
            (Cliente) cmbPropietario.getSelectedItem();

        Mascota mascota = mascotaOriginal == null
            ? new Mascota()
            : mascotaOriginal;

        mascota.setIdCliente(
            propietario.getIdCliente()
        );

        mascota.setNombre(
            txtNombre.getText().trim()
        );

        mascota.setEspecie(
            txtEspecie.getText().trim()
        );

        mascota.setRaza(
            textoOpcional(txtRaza.getText())
        );

        mascota.setSexo(
            (Sexo) cmbSexo.getSelectedItem()
        );

        mascota.setFechaNacimiento(
            obtenerFechaNacimiento()
        );

        mascota.setColor(
            textoOpcional(txtColor.getText())
        );

        mascota.setObservaciones(
            textoOpcional(
                txtObservaciones.getText()
            )
        );

        mascota.setNombrePropietario(
            propietario.getNombreCompleto()
        );

        return mascota;
    }

    /**
     * Convierte campos opcionales vacíos en null.
     */
    private String textoOpcional(String texto) {

        return texto == null || texto.isBlank()
            ? null
            : texto.trim();
    }
}
