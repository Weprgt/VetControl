/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import com.mycompany.vetcontrol.modelo.Cita;
import com.mycompany.vetcontrol.modelo.Cita.Estado;
import com.mycompany.vetcontrol.modelo.Mascota;
import com.mycompany.vetcontrol.modelo.Veterinario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.BorderFactory;
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
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;

/**
 *
 * @author weprg
 */
public class DlgCita extends JDialog {

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

    // Componentes del formulario.
    private JComboBox<Mascota> cmbMascota;
    private JComboBox<Veterinario> cmbVeterinario;
    private DatePicker selectorFecha;
    private TimePicker selectorHora;
    private JTextField txtMotivo;
    private JComboBox<Estado> cmbEstado;
    private JTextArea txtObservaciones;

    // Será null cuando se registre una cita nueva.
    private final Cita citaOriginal;

    // Indica si el usuario confirmó el formulario.
    private boolean guardado;

    public DlgCita(
            Window propietario,
            Cita cita,
            List<Mascota> mascotas,
            List<Veterinario> veterinarios) {

        super(
            propietario,
            cita == null
                ? "Nueva cita"
                : "Editar cita",
            ModalityType.APPLICATION_MODAL
        );

        this.citaOriginal = cita;

        configurarVentana();
        crearInterfaz(mascotas, veterinarios);

        if (cita != null) {
            cargarDatosCita(cita);
        }
    }

    private void configurarVentana() {

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    /**
     * Construye el contenido del diálogo.
     */
    private void crearInterfaz(
            List<Mascota> mascotas,
            List<Veterinario> veterinarios) {

        JPanel contenido =
            new JPanel(new BorderLayout(0, 22));

        contenido.setBackground(Color.WHITE);
        contenido.setBorder(
            new EmptyBorder(30, 40, 25, 40)
        );

        contenido.add(
            crearEncabezado(),
            BorderLayout.NORTH
        );

        contenido.add(
            crearFormularioDesplazable(
                mascotas,
                veterinarios
            ),
            BorderLayout.CENTER
        );

        contenido.add(
            crearPanelBotones(),
            BorderLayout.SOUTH
        );

        add(contenido);

        setSize(new Dimension(580, 690));
        setMinimumSize(new Dimension(580, 650));
        setLocationRelativeTo(getOwner());
    }

    private JPanel crearEncabezado() {

        JPanel encabezado =
            new JPanel(new BorderLayout(0, 6));

        encabezado.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel(
            citaOriginal == null
                ? "Programar cita"
                : "Editar cita"
        );

        lblTitulo.setForeground(AZUL_OSCURO);
        lblTitulo.setFont(
            new Font("Segoe UI", Font.BOLD, 26)
        );

        JLabel lblDescripcion = new JLabel(
            "Completa los datos de la consulta"
        );

        lblDescripcion.setForeground(TEXTO_SECUNDARIO);
        lblDescripcion.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        encabezado.add(lblTitulo, BorderLayout.NORTH);
        encabezado.add(
            lblDescripcion,
            BorderLayout.SOUTH
        );

        return encabezado;
    }

    /**
     * Coloca el formulario dentro de un JScrollPane para
     * evitar que desaparezcan campos en pantallas pequeñas.
     */
    private JScrollPane crearFormularioDesplazable(
            List<Mascota> mascotas,
            List<Veterinario> veterinarios) {

        JScrollPane scroll = new JScrollPane(
            crearFormulario(mascotas, veterinarios),
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setBorder(
            BorderFactory.createEmptyBorder()
        );

        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        return scroll;
    }

    private JPanel crearFormulario(
            List<Mascota> mascotas,
            List<Veterinario> veterinarios) {

        JPanel formulario =
            new JPanel(new GridBagLayout());

        formulario.setBackground(Color.WHITE);

        GridBagConstraints gbc =
            new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Mascota.
        agregarEtiqueta(formulario, gbc, "Mascota *");

        cmbMascota = new JComboBox<>();

        for (Mascota mascota : mascotas) {
            cmbMascota.addItem(mascota);
        }

        configurarCampo(cmbMascota);
        agregarCampo(formulario, gbc, cmbMascota);

        // Veterinario.
        agregarEtiqueta(
            formulario,
            gbc,
            "Veterinario *"
        );

        cmbVeterinario = new JComboBox<>();

        for (Veterinario veterinario : veterinarios) {
            cmbVeterinario.addItem(veterinario);
        }

        configurarCampo(cmbVeterinario);
        agregarCampo(
            formulario,
            gbc,
            cmbVeterinario
        );

        // Fecha.
        agregarEtiqueta(
            formulario,
            gbc,
            "Fecha *"
        );

        selectorFecha = new DatePicker();

        configurarCampo(selectorFecha);
        agregarCampo(
            formulario,
            gbc,
            selectorFecha
        );

        // Hora.
        agregarEtiqueta(
            formulario,
            gbc,
            "Hora *"
        );

        selectorHora = new TimePicker();

        configurarCampo(selectorHora);
        agregarCampo(
            formulario,
            gbc,
            selectorHora
        );

        // Motivo.
        agregarEtiqueta(
            formulario,
            gbc,
            "Motivo de la consulta *"
        );

        txtMotivo = new JTextField();
        configurarCampo(txtMotivo);
        agregarCampo(formulario, gbc, txtMotivo);

        // Estado.
        agregarEtiqueta(
            formulario,
            gbc,
            "Estado"
        );

        cmbEstado = new JComboBox<>(Estado.values());

        /*
         * Una cita nueva siempre comienza como PROGRAMADA.
         * El estado se podrá modificar al editarla.
         */
        if (citaOriginal == null) {
            cmbEstado.setSelectedItem(Estado.PROGRAMADA);
            cmbEstado.setEnabled(false);
        }

        configurarCampo(cmbEstado);
        agregarCampo(formulario, gbc, cmbEstado);

        // Observaciones.
        agregarEtiqueta(
            formulario,
            gbc,
            "Observaciones"
        );

        txtObservaciones = new JTextArea(4, 20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        txtObservaciones.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        JScrollPane scrollObservaciones =
            new JScrollPane(txtObservaciones);

        scrollObservaciones.setPreferredSize(
            new Dimension(420, 100)
        );

        scrollObservaciones.setBorder(
            BorderFactory.createLineBorder(BORDE)
        );

        agregarCampo(
            formulario,
            gbc,
            scrollObservaciones
        );

        return formulario;
    }

    private void agregarEtiqueta(
            JPanel formulario,
            GridBagConstraints gbc,
            String texto) {

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(TEXTO);
        etiqueta.setFont(
            new Font("Segoe UI", Font.BOLD, 13)
        );

        gbc.insets = new Insets(0, 0, 6, 0);
        formulario.add(etiqueta, gbc);
        gbc.gridy++;
    }

    private void agregarCampo(
            JPanel formulario,
            GridBagConstraints gbc,
            java.awt.Component componente) {

        gbc.insets = new Insets(0, 0, 14, 0);
        formulario.add(componente, gbc);
        gbc.gridy++;
    }

    private void configurarCampo(
            javax.swing.JComponent componente) {

        componente.setPreferredSize(
            new Dimension(420, 40)
        );

        componente.setMinimumSize(
            new Dimension(420, 40)
        );
    }

    private JPanel crearPanelBotones() {

        JPanel panel = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );

        panel.setBackground(Color.WHITE);

        JButton btnCerrar =
            new JButton("Cancelar");

        btnCerrar.setPreferredSize(
            new Dimension(110, 42)
        );

        btnCerrar.setForeground(TEXTO);
        btnCerrar.setBackground(Color.WHITE);
        btnCerrar.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        btnCerrar.setFocusPainted(false);
        btnCerrar.putClientProperty(
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

        btnCerrar.addActionListener(
            evento -> dispose()
        );

        btnGuardar.addActionListener(
            evento -> confirmarFormulario()
        );

        panel.add(btnCerrar);
        panel.add(btnGuardar);

        return panel;
    }

    /**
     * Carga los datos cuando se edita una cita.
     */
    private void cargarDatosCita(Cita cita) {

        seleccionarMascota(cita.getIdMascota());

        seleccionarVeterinario(
            cita.getIdVeterinario()
        );

        selectorFecha.setDate(
            cita.getFechaHora().toLocalDate()
        );

        selectorHora.setTime(
            cita.getFechaHora()
                .toLocalTime()
                .withSecond(0)
                .withNano(0)
        );
        txtMotivo.setText(cita.getMotivo());
        cmbEstado.setSelectedItem(cita.getEstado());
        txtObservaciones.setText(
            cita.getObservaciones()
        );
    }

    private void seleccionarMascota(int idMascota) {

        for (int indice = 0;
                indice < cmbMascota.getItemCount();
                indice++) {

            Mascota mascota =
                cmbMascota.getItemAt(indice);

            if (mascota.getIdMascota() == idMascota) {
                cmbMascota.setSelectedIndex(indice);
                return;
            }
        }
    }

    private void seleccionarVeterinario(
            int idVeterinario) {

        for (int indice = 0;
                indice < cmbVeterinario.getItemCount();
                indice++) {

            Veterinario veterinario =
                cmbVeterinario.getItemAt(indice);

            if (veterinario.getIdVeterinario()
                    == idVeterinario) {

                cmbVeterinario.setSelectedIndex(indice);
                return;
            }
        }
    }

    /*
    * Valida la información antes de guardar la cita.
    */
   private void confirmarFormulario() {

       // La cita debe estar asociada a una mascota.
       if (cmbMascota.getSelectedItem() == null) {

           advertencia(
               "Selecciona una mascota."
           );

           return;
       }

       // La cita debe tener un veterinario asignado.
       if (cmbVeterinario.getSelectedItem() == null) {

           advertencia(
               "Selecciona un veterinario."
           );

           return;
       }

       // Verifica que el usuario haya elegido una fecha.
       if (selectorFecha.getDate() == null) {

           advertencia(
               "Selecciona la fecha de la cita."
           );

           return;
       }

       // Verifica que el usuario haya elegido una hora.
       if (selectorHora.getTime() == null) {

           advertencia(
               "Selecciona la hora de la cita."
           );

           return;
       }

       // El motivo es obligatorio.
       if (txtMotivo.getText().isBlank()) {

           advertencia(
               "Ingresa el motivo de la consulta."
           );

           txtMotivo.requestFocus();
           return;
       }

       /*
        * Combina la fecha y la hora seleccionadas
        * dentro de un único objeto LocalDateTime.
        */
       LocalDateTime fechaHora =
           obtenerFechaHora();

       /*
        * Las citas nuevas no pueden programarse
        * en una fecha y hora anteriores al momento actual.
        *
        * Esta comprobación no se aplica durante la edición
        * para permitir consultar o corregir citas antiguas.
        */
       if (citaOriginal == null
               && fechaHora.isBefore(LocalDateTime.now())) {

           advertencia(
               "La fecha y hora no pueden estar en el pasado."
           );

           return;
       }

       /*
        * Si todas las validaciones fueron superadas,
        * indicamos que el usuario confirmó el formulario.
        */
       guardado = true;
       dispose();
   }
    private LocalDateTime obtenerFechaHora() {

        return LocalDateTime.of(
            selectorFecha.getDate(),
            selectorHora.getTime()
        );
    }

    private void advertencia(String mensaje) {

        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Datos de la cita",
            JOptionPane.WARNING_MESSAGE
        );
    }

    public boolean isGuardado() {
        return guardado;
    }

    /**
     * Construye la cita utilizando el formulario.
     */
    public Cita obtenerCita() {

        Mascota mascota =
            (Mascota) cmbMascota.getSelectedItem();

        Veterinario veterinario =
            (Veterinario) cmbVeterinario.getSelectedItem();

        Cita cita = citaOriginal == null
            ? new Cita()
            : citaOriginal;

        cita.setIdMascota(
            mascota.getIdMascota()
        );

        cita.setIdVeterinario(
            veterinario.getIdVeterinario()
        );

        cita.setFechaHora(obtenerFechaHora());

        cita.setMotivo(
            txtMotivo.getText().trim()
        );

        cita.setEstado(
            (Estado) cmbEstado.getSelectedItem()
        );

        cita.setObservaciones(
            textoOpcional(txtObservaciones.getText())
        );

        return cita;
    }

    private String textoOpcional(String texto) {

        return texto == null || texto.isBlank()
            ? null
            : texto.trim();
    }
}
