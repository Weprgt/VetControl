/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import com.mycompany.vetcontrol.modelo.Cliente;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author weprg
 */
public class DlgCliente extends JDialog {

    private static final Color FONDO =
        new Color(244, 247, 250);

    private static final Color AZUL_OSCURO =
        new Color(23, 50, 77);

    private static final Color AZUL_PRINCIPAL =
        new Color(52, 120, 184);

    private static final Color TEXTO_SECUNDARIO =
        new Color(98, 114, 125);

    private final JTextField txtNombres;
    private final JTextField txtApellidos;
    private final JTextField txtTelefono;
    private final JTextField txtCorreo;
    private final JTextField txtDireccion;

    /*
     * Cliente recibido al editar.
     * Será un objeto nuevo cuando se esté registrando.
     */
    private final Cliente cliente;

    /*
     * Indica si el usuario presionó Guardar.
     */
    private boolean confirmado;

    /**
     * @param propietario ventana que abre el diálogo
     * @param clienteEditar cliente seleccionado o null para uno nuevo
     */
    public DlgCliente(
            Window propietario,
            Cliente clienteEditar) {

        super(
            propietario,
            clienteEditar == null
                ? "Nuevo cliente"
                : "Editar cliente",
            ModalityType.APPLICATION_MODAL
        );

        this.cliente = clienteEditar == null
            ? new Cliente()
            : clienteEditar;

        this.confirmado = false;

        txtNombres = crearCampo();
        txtApellidos = crearCampo();
        txtTelefono = crearCampo();
        txtCorreo = crearCampo();
        txtDireccion = crearCampo();

        configurarVentana();
        crearInterfaz();

        if (clienteEditar != null) {
            cargarDatos();
        }
        /*
        * Calcula el tamaño necesario según los componentes.
        */
        pack();

        /*
        * Centra el diálogo después de calcular su tamaño.
        */
        setLocationRelativeTo(propietario);
    }

    private void configurarVentana() {

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(520, 620);
        setResizable(false);
    }

    private void crearInterfaz() {

        JPanel contenido = new JPanel(
            new BorderLayout(0, 25)
        );

        contenido.setBackground(FONDO);
        contenido.setBorder(
            new EmptyBorder(30, 35, 30, 35)
        );

        contenido.add(
            crearEncabezado(),
            BorderLayout.NORTH
        );

        contenido.add(
            crearFormulario(),
            BorderLayout.CENTER
        );

        contenido.add(
            crearBotones(),
            BorderLayout.SOUTH
        );

        setContentPane(contenido);
        getRootPane().setDefaultButton(
            obtenerBotonGuardar()
        );
    }

    private JPanel crearEncabezado() {

        JPanel panel = new JPanel(
            new BorderLayout(0, 7)
        );

        panel.setOpaque(false);

        JLabel lblTitulo = new JLabel(
            cliente.getIdCliente() == 0
                ? "Registrar cliente"
                : "Editar cliente"
        );

        lblTitulo.setForeground(AZUL_OSCURO);
        lblTitulo.setFont(
            new Font("Segoe UI", Font.BOLD, 25)
        );

        JLabel lblDescripcion = new JLabel(
            "Ingresa los datos del propietario"
        );

        lblDescripcion.setForeground(TEXTO_SECUNDARIO);
        lblDescripcion.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblDescripcion, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearFormulario() {

        JPanel formulario =
            new JPanel(new GridBagLayout());

        formulario.setBackground(Color.WHITE);

        formulario.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(216, 225, 232)
                ),
                new EmptyBorder(22, 25, 22, 25)
            )
        );

        GridBagConstraints restricciones =
            new GridBagConstraints();

        restricciones.gridx = 0;
        restricciones.gridy = 0;
        restricciones.weightx = 1;
        restricciones.fill =
            GridBagConstraints.HORIZONTAL;
        restricciones.anchor =
            GridBagConstraints.WEST;
        restricciones.insets =
            new Insets(0, 0, 7, 0);

        agregarCampo(
            formulario,
            restricciones,
            "Nombres *",
            txtNombres
        );

        agregarCampo(
            formulario,
            restricciones,
            "Apellidos *",
            txtApellidos
        );

        agregarCampo(
            formulario,
            restricciones,
            "Teléfono *",
            txtTelefono
        );

        agregarCampo(
            formulario,
            restricciones,
            "Correo",
            txtCorreo
        );

        agregarCampo(
            formulario,
            restricciones,
            "Dirección",
            txtDireccion
        );

        return formulario;
    }

    /**
     * Agrega una etiqueta y su campo al formulario.
     */
    private void agregarCampo(
            JPanel formulario,
            GridBagConstraints restricciones,
            String texto,
            JTextField campo) {

        JLabel etiqueta = new JLabel(texto);

        etiqueta.setForeground(AZUL_OSCURO);
        etiqueta.setFont(
            new Font("Segoe UI", Font.BOLD, 13)
        );

        restricciones.insets =
            new Insets(0, 0, 6, 0);

        formulario.add(
            etiqueta,
            restricciones
        );

        restricciones.gridy++;
        restricciones.insets =
            new Insets(0, 0, 15, 0);

        formulario.add(
            campo,
            restricciones
        );

        restricciones.gridy++;
    }

    private JPanel crearBotones() {
        /*
         * Coloca ambos botones juntos a la derecha.
         */
        JPanel panel = new JPanel(
            new FlowLayout(
                FlowLayout.RIGHT,
                12,
                0
            )
        );

        panel.setOpaque(false);

        // Botón secundario.
        JButton btnCancelar =
            new JButton("Cancelar");

        btnCancelar.setPreferredSize(
            new Dimension(120, 42)
        );

        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setForeground(AZUL_OSCURO);
        btnCancelar.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        btnCancelar.setFocusPainted(false);

        btnCancelar.setBorder(
            BorderFactory.createLineBorder(
                new Color(190, 202, 212),
                1,
                true
            )
        );

        btnCancelar.addActionListener(
            evento -> dispose()
        );

        // Botón principal.
        JButton btnGuardar =
            new JButton("Guardar");

        btnGuardar.setPreferredSize(
            new Dimension(130, 42)
        );

        btnGuardar.setBackground(AZUL_PRINCIPAL);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        btnGuardar.setFocusPainted(false);

        btnGuardar.putClientProperty(
            "JButton.buttonType",
            "borderless"
        );

        btnGuardar.addActionListener(
            evento -> guardar()
        );

        panel.add(btnCancelar);
        panel.add(btnGuardar);

        /*
         * Se conserva para utilizar Guardar
         * como botón predeterminado con Enter.
         */
        panel.putClientProperty(
            "botonGuardar",
            btnGuardar
        );

        return panel;
    }

    /**
     * Busca el botón Guardar dentro del panel inferior.
     *
     * Este método no es necesario para la lógica del formulario,
     * únicamente permite utilizar la tecla Enter.
     */
    private JButton obtenerBotonGuardar() {

        JPanel panelBotones =
            (JPanel) ((BorderLayout)
                ((JPanel) getContentPane())
                    .getLayout())
                .getLayoutComponent(BorderLayout.SOUTH);

        return (JButton) panelBotones.getClientProperty(
            "botonGuardar"
        );
    }

    private JTextField crearCampo() {

        JTextField campo = new JTextField();

        campo.setPreferredSize(
            new Dimension(360, 38)
        );

        return campo;
    }

    /**
     * Muestra los datos cuando se edita un cliente.
     */
    private void cargarDatos() {

        txtNombres.setText(cliente.getNombres());
        txtApellidos.setText(cliente.getApellidos());
        txtTelefono.setText(cliente.getTelefono());
        txtCorreo.setText(cliente.getCorreo());
        txtDireccion.setText(cliente.getDireccion());
    }

    /**
     * Valida y copia los datos al objeto Cliente.
     */
    private void guardar() {

        String nombres =
            txtNombres.getText().trim();

        String apellidos =
            txtApellidos.getText().trim();

        String telefono =
            txtTelefono.getText().trim();

        String correo =
            txtCorreo.getText().trim();

        String direccion =
            txtDireccion.getText().trim();

        if (nombres.isBlank()
                || apellidos.isBlank()
                || telefono.isBlank()) {

            JOptionPane.showMessageDialog(
                this,
                "Completa los campos obligatorios.",
                "Datos incompletos",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!correo.isBlank()
                && !correo.matches(
                    "^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$"
                )) {

            JOptionPane.showMessageDialog(
                this,
                "Ingresa un correo electrónico válido.",
                "Correo inválido",
                JOptionPane.WARNING_MESSAGE
            );

            txtCorreo.requestFocus();
            return;
        }

        cliente.setNombres(nombres);
        cliente.setApellidos(apellidos);
        cliente.setTelefono(telefono);

        cliente.setCorreo(
            correo.isBlank() ? null : correo
        );

        cliente.setDireccion(
            direccion.isBlank() ? null : direccion
        );

        confirmado = true;
        dispose();
    }

    /**
     * Devuelve el cliente solamente si se presionó Guardar.
     */
    public Cliente getClienteConfirmado() {

        return confirmado
            ? cliente
            : null;
    }
}
