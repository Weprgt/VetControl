/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import com.mycompany.vetcontrol.modelo.HistorialClinico;
import com.mycompany.vetcontrol.modelo.HistorialClinico.TipoRegistro;
import com.mycompany.vetcontrol.modelo.Mascota;
import com.mycompany.vetcontrol.modelo.Veterinario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
public class DlgHistorialClinico extends JDialog {

    // Colores de VetControl.
    private static final Color AZUL_OSCURO =
        new Color(23, 50, 77);

    private static final Color AZUL_PRINCIPAL =
        new Color(52, 120, 184);

    private static final Color FONDO =
        new Color(244, 247, 250);

    private static final Color TEXTO =
        new Color(36, 50, 61);

    private static final Color BORDE =
        new Color(216, 225, 232);

    // Registro que se está editando. Será null al crear uno nuevo.
    private final HistorialClinico historialOriginal;

    private JComboBox<Mascota> cmbMascota;
    private JComboBox<Veterinario> cmbVeterinario;
    private JComboBox<TipoRegistro> cmbTipoRegistro;

    private DatePicker dpFechaAtencion;
    private TimePicker tpHoraAtencion;
    private DatePicker dpProximaDosis;

    private JTextField txtMotivo;
    private JTextField txtNombreVacuna;
    private JTextField txtLoteVacuna;

    private JTextArea txtDiagnostico;
    private JTextArea txtTratamiento;
    private JTextArea txtObservaciones;

    private JLabel lblNombreVacuna;
    private JLabel lblLoteVacuna;
    private JLabel lblProximaDosis;

    private boolean guardado;

    public DlgHistorialClinico(
            Window propietario,
            HistorialClinico historial,
            List<Mascota> mascotas,
            List<Veterinario> veterinarios) {

        super(
            propietario,
            historial == null
                ? "Nuevo registro clínico"
                : "Detalle del historial clínico",
            ModalityType.APPLICATION_MODAL
        );

        this.historialOriginal = historial;

        configurarVentana();
        crearInterfaz(mascotas, veterinarios);

        if (historial != null) {
            cargarDatos(historial);
        }

        actualizarCamposVacuna();
    }

    private void configurarVentana() {

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(760, 750);
        setMinimumSize(new Dimension(700, 650));
        setLocationRelativeTo(getOwner());
    }

    private void crearInterfaz(
            List<Mascota> mascotas,
            List<Veterinario> veterinarios) {

        JPanel panelPrincipal =
            new JPanel(new BorderLayout(0, 20));

        panelPrincipal.setBackground(FONDO);
        panelPrincipal.setBorder(
            new EmptyBorder(25, 30, 25, 30)
        );

        panelPrincipal.add(
            crearEncabezado(),
            BorderLayout.NORTH
        );

        JPanel formulario =
            crearFormulario(mascotas, veterinarios);

        JScrollPane desplazamiento =
            new JScrollPane(formulario);

        desplazamiento.setBorder(
            BorderFactory.createEmptyBorder()
        );

        desplazamiento.getVerticalScrollBar()
            .setUnitIncrement(16);

        panelPrincipal.add(
            desplazamiento,
            BorderLayout.CENTER
        );

        panelPrincipal.add(
            crearPanelBotones(),
            BorderLayout.SOUTH
        );

        setContentPane(panelPrincipal);
    }

    private JPanel crearEncabezado() {

        JPanel encabezado = new JPanel();
        encabezado.setLayout(
            new BoxLayout(encabezado, BoxLayout.Y_AXIS)
        );

        encabezado.setOpaque(false);

        JLabel lblTitulo = new JLabel(
            historialOriginal == null
                ? "Nuevo registro clínico"
                : "Detalle del historial clínico"
        );

        lblTitulo.setForeground(AZUL_OSCURO);
        lblTitulo.setFont(
            new Font("Segoe UI", Font.BOLD, 26)
        );

        JLabel lblDescripcion = new JLabel(
            "Información de la atención veterinaria"
        );

        lblDescripcion.setForeground(
            new Color(98, 114, 125)
        );

        lblDescripcion.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        encabezado.add(lblTitulo);
        encabezado.add(Box.createVerticalStrut(6));
        encabezado.add(lblDescripcion);

        return encabezado;
    }

    private JPanel crearFormulario(
            List<Mascota> mascotas,
            List<Veterinario> veterinarios) {

        JPanel formulario =
            new JPanel(new GridBagLayout());

        formulario.setBackground(Color.WHITE);
        formulario.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    BORDE,
                    1,
                    true
                ),
                new EmptyBorder(25, 25, 25, 25)
            )
        );

        cmbMascota = new JComboBox<>();
        for (Mascota mascota : mascotas) {
            cmbMascota.addItem(mascota);
        }

        cmbVeterinario = new JComboBox<>();
        for (Veterinario veterinario : veterinarios) {
            cmbVeterinario.addItem(veterinario);
        }

        cmbTipoRegistro =
            new JComboBox<>(TipoRegistro.values());

        cmbTipoRegistro.addActionListener(
            evento -> actualizarCamposVacuna()
        );

        dpFechaAtencion = new DatePicker();
        tpHoraAtencion = new TimePicker();
        dpProximaDosis = new DatePicker();

        // Fecha y hora iniciales para un registro nuevo.
        dpFechaAtencion.setDate(LocalDate.now());
        tpHoraAtencion.setTime(
            LocalTime.now().withSecond(0).withNano(0)
        );

        txtMotivo = new JTextField();
        txtNombreVacuna = new JTextField();
        txtLoteVacuna = new JTextField();

        txtDiagnostico = crearAreaTexto(3);
        txtTratamiento = crearAreaTexto(3);
        txtObservaciones = crearAreaTexto(3);

        lblNombreVacuna = crearEtiqueta(
            "Nombre de la vacuna"
        );

        lblLoteVacuna = crearEtiqueta(
            "Lote de la vacuna"
        );

        lblProximaDosis = crearEtiqueta(
            "Próxima dosis"
        );

        int fila = 0;

        agregarCampo(
            formulario,
            crearEtiqueta("Mascota *"),
            cmbMascota,
            fila++
        );

        agregarCampo(
            formulario,
            crearEtiqueta("Veterinario *"),
            cmbVeterinario,
            fila++
        );

        agregarCampo(
            formulario,
            crearEtiqueta("Tipo de registro *"),
            cmbTipoRegistro,
            fila++
        );

        agregarCampo(
            formulario,
            crearEtiqueta("Fecha de atención *"),
            dpFechaAtencion,
            fila++
        );

        agregarCampo(
            formulario,
            crearEtiqueta("Hora de atención *"),
            tpHoraAtencion,
            fila++
        );

        agregarCampo(
            formulario,
            crearEtiqueta("Motivo de consulta"),
            txtMotivo,
            fila++
        );

        agregarCampo(
            formulario,
            crearEtiqueta("Diagnóstico"),
            new JScrollPane(txtDiagnostico),
            fila++
        );

        agregarCampo(
            formulario,
            crearEtiqueta("Tratamiento"),
            new JScrollPane(txtTratamiento),
            fila++
        );

        agregarCampo(
            formulario,
            crearEtiqueta("Observaciones"),
            new JScrollPane(txtObservaciones),
            fila++
        );

        agregarCampo(
            formulario,
            lblNombreVacuna,
            txtNombreVacuna,
            fila++
        );

        agregarCampo(
            formulario,
            lblLoteVacuna,
            txtLoteVacuna,
            fila++
        );

        agregarCampo(
            formulario,
            lblProximaDosis,
            dpProximaDosis,
            fila
        );

        return formulario;
    }

    private JLabel crearEtiqueta(String texto) {

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(TEXTO);
        etiqueta.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        return etiqueta;
    }

    private JTextArea crearAreaTexto(int filas) {

        JTextArea area = new JTextArea(filas, 30);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        return area;
    }

    private void agregarCampo(
            JPanel formulario,
            JLabel etiqueta,
            java.awt.Component componente,
            int fila) {

        GridBagConstraints restricciones =
            new GridBagConstraints();

        restricciones.gridx = 0;
        restricciones.gridy = fila;
        restricciones.weightx = 0;
        restricciones.anchor =
            GridBagConstraints.NORTHWEST;

        restricciones.insets =
            new Insets(6, 5, 12, 18);

        formulario.add(etiqueta, restricciones);

        restricciones.gridx = 1;
        restricciones.weightx = 1;
        restricciones.fill =
            GridBagConstraints.HORIZONTAL;

        restricciones.insets =
            new Insets(4, 5, 12, 5);

        componente.setPreferredSize(
            componente instanceof JScrollPane
                ? new Dimension(400, 75)
                : new Dimension(400, 38)
        );

        formulario.add(componente, restricciones);
    }

    private JPanel crearPanelBotones() {

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(
            new BoxLayout(panelBotones, BoxLayout.X_AXIS)
        );

        panelBotones.setOpaque(false);

        JButton btnCancelar =
            crearBoton("Cancelar", new Color(98, 114, 125));

        JButton btnGuardar =
            crearBoton("Guardar", AZUL_PRINCIPAL);

        btnCancelar.addActionListener(
            evento -> dispose()
        );

        btnGuardar.addActionListener(
            evento -> confirmarFormulario()
        );

        panelBotones.add(Box.createHorizontalGlue());
        panelBotones.add(btnCancelar);
        panelBotones.add(Box.createHorizontalStrut(10));
        panelBotones.add(btnGuardar);

        return panelBotones;
    }

    private JButton crearBoton(
            String texto,
            Color color) {

        JButton boton = new JButton(texto);

        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        boton.setFocusPainted(false);

        Dimension tamano =
            new Dimension(120, 42);

        boton.setMinimumSize(tamano);
        boton.setPreferredSize(tamano);
        boton.setMaximumSize(tamano);

        boton.putClientProperty(
            "JButton.buttonType",
            "borderless"
        );

        return boton;
    }

    /**
     * Los campos de vacuna solamente se activan cuando
     * el tipo de registro seleccionado es VACUNA.
     */
    private void actualizarCamposVacuna() {

        boolean esVacuna =
            cmbTipoRegistro != null
            && cmbTipoRegistro.getSelectedItem()
                == TipoRegistro.VACUNA;

        txtNombreVacuna.setEnabled(esVacuna);
        txtLoteVacuna.setEnabled(esVacuna);
        dpProximaDosis.setEnabled(esVacuna);

        lblNombreVacuna.setEnabled(esVacuna);
        lblLoteVacuna.setEnabled(esVacuna);
        lblProximaDosis.setEnabled(esVacuna);

        /*
         * Si se cambia de VACUNA a otro tipo,
         * eliminamos los datos que ya no corresponden.
         */
        if (!esVacuna) {
            txtNombreVacuna.setText("");
            txtLoteVacuna.setText("");
            dpProximaDosis.clear();
        }
    }

    private void cargarDatos(
            HistorialClinico historial) {

        seleccionarMascota(
            historial.getIdMascota()
        );

        seleccionarVeterinario(
            historial.getIdVeterinario()
        );

        cmbTipoRegistro.setSelectedItem(
            historial.getTipoRegistro()
        );

        if (historial.getFechaAtencion() != null) {

            dpFechaAtencion.setDate(
                historial.getFechaAtencion().toLocalDate()
            );

            tpHoraAtencion.setTime(
                historial.getFechaAtencion().toLocalTime()
                    .withSecond(0)
                    .withNano(0)
            );
        }

        txtMotivo.setText(
            textoSeguro(historial.getMotivoConsulta())
        );

        txtDiagnostico.setText(
            textoSeguro(historial.getDiagnostico())
        );

        txtTratamiento.setText(
            textoSeguro(historial.getTratamiento())
        );

        txtObservaciones.setText(
            textoSeguro(historial.getObservaciones())
        );

        txtNombreVacuna.setText(
            textoSeguro(historial.getNombreVacuna())
        );

        txtLoteVacuna.setText(
            textoSeguro(historial.getLoteVacuna())
        );

        if (historial.getProximaDosis() != null) {
            dpProximaDosis.setDate(
                historial.getProximaDosis()
            );
        }
    }

    private void seleccionarMascota(int idMascota) {

        for (int posicion = 0;
                posicion < cmbMascota.getItemCount();
                posicion++) {

            Mascota mascota =
                cmbMascota.getItemAt(posicion);

            if (mascota.getIdMascota() == idMascota) {
                cmbMascota.setSelectedIndex(posicion);
                return;
            }
        }
    }

    private void seleccionarVeterinario(
            int idVeterinario) {

        for (int posicion = 0;
                posicion < cmbVeterinario.getItemCount();
                posicion++) {

            Veterinario veterinario =
                cmbVeterinario.getItemAt(posicion);

            if (veterinario.getIdVeterinario()
                    == idVeterinario) {

                cmbVeterinario.setSelectedIndex(posicion);
                return;
            }
        }
    }

    private void confirmarFormulario() {

        if (cmbMascota.getSelectedItem() == null) {
            advertencia("Selecciona una mascota.");
            return;
        }

        if (cmbVeterinario.getSelectedItem() == null) {
            advertencia("Selecciona un veterinario.");
            return;
        }

        if (dpFechaAtencion.getDate() == null) {
            advertencia("Selecciona la fecha de atención.");
            return;
        }

        if (tpHoraAtencion.getTime() == null) {
            advertencia("Selecciona la hora de atención.");
            return;
        }

        TipoRegistro tipo =
            (TipoRegistro) cmbTipoRegistro.getSelectedItem();

        if (tipo == TipoRegistro.VACUNA
                && txtNombreVacuna.getText().isBlank()) {

            advertencia(
                "Ingresa el nombre de la vacuna."
            );

            txtNombreVacuna.requestFocus();
            return;
        }

        guardado = true;
        dispose();
    }

    /**
     * Construye el objeto con los datos del formulario.
     */
    public HistorialClinico obtenerHistorial() {

        Mascota mascota =
            (Mascota) cmbMascota.getSelectedItem();

        Veterinario veterinario =
            (Veterinario) cmbVeterinario.getSelectedItem();

        LocalDateTime fechaAtencion =
            LocalDateTime.of(
                dpFechaAtencion.getDate(),
                tpHoraAtencion.getTime()
            );

        Integer idCita = historialOriginal == null
            ? null
            : historialOriginal.getIdCita();

        HistorialClinico historial =
            new HistorialClinico(
                mascota.getIdMascota(),
                veterinario.getIdVeterinario(),
                idCita,
                fechaAtencion,
                (TipoRegistro)
                    cmbTipoRegistro.getSelectedItem(),
                textoOpcional(txtMotivo.getText()),
                textoOpcional(txtDiagnostico.getText()),
                textoOpcional(txtTratamiento.getText()),
                textoOpcional(txtObservaciones.getText()),
                textoOpcional(txtNombreVacuna.getText()),
                textoOpcional(txtLoteVacuna.getText()),
                dpProximaDosis.getDate()
            );

        if (historialOriginal != null) {
            historial.setIdHistorial(
                historialOriginal.getIdHistorial()
            );
        }

        return historial;
    }

    public boolean isGuardado() {
        return guardado;
    }

    private String textoOpcional(String texto) {

        if (texto == null || texto.isBlank()) {
            return null;
        }

        return texto.trim();
    }

    private String textoSeguro(String texto) {

        return texto == null
            ? ""
            : texto;
    }

    private void advertencia(String mensaje) {

        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Datos incompletos",
            JOptionPane.WARNING_MESSAGE
        );
    }
}