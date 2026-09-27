/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import com.mycompany.vetcontrol.modelo.Rol;
import com.mycompany.vetcontrol.modelo.Usuario;
import com.mycompany.vetcontrol.modelo.Veterinario;
import com.mycompany.vetcontrol.util.SeguridadContrasena;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.Arrays;
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
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author weprg
 */
/**
 * Formulario para crear y editar usuarios.
 */
public class DlgUsuario extends JDialog {

    private static final Color AZUL_OSCURO =
        new Color(23, 50, 77);

    private static final Color AZUL_PRINCIPAL =
        new Color(52, 120, 184);

    private static final Color FONDO =
        new Color(244, 247, 250);

    private static final Color BORDE =
        new Color(216, 225, 232);

    private JTextField txtNombreCompleto;
    private JTextField txtNombreUsuario;
    private JTextField txtCorreo;

    private JPasswordField txtContrasena;
    private JPasswordField txtConfirmarContrasena;

    private JComboBox<Rol> cmbRol;

    // Campos utilizados solamente por veterinarios.
    private JPanel panelDatosVeterinario;
    private JTextField txtEspecialidad;
    private JTextField txtTelefonoProfesional;

    private final Usuario usuarioOriginal;
    private final Veterinario veterinarioOriginal;

    private Usuario usuarioResultado;
    private Veterinario veterinarioResultado;

    private boolean guardado;

    public DlgUsuario(
            Window propietario,
            Usuario usuario,
            Veterinario veterinario,
            List<Rol> roles) {

        super(
            propietario,
            usuario == null
                ? "Nuevo usuario"
                : "Editar usuario",
            ModalityType.APPLICATION_MODAL
        );

        this.usuarioOriginal = usuario;
        this.veterinarioOriginal = veterinario;

        configurarVentana();
        crearInterfaz(roles);

        if (usuario != null) {
            cargarDatosUsuario(usuario, veterinario);
        }

        actualizarCamposVeterinario();
    }

    private void configurarVentana() {

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 730);
        setMinimumSize(new Dimension(540, 620));
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void crearInterfaz(List<Rol> roles) {

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(FONDO);
        contenedor.setBorder(
            new EmptyBorder(25, 30, 25, 30)
        );

        contenedor.add(crearEncabezado(), BorderLayout.NORTH);
        // El formulario puede crecer cuando se selecciona VETERINARIO.
        JScrollPane scrollFormulario = new JScrollPane(
            crearFormulario(roles)
        );

        scrollFormulario.setBorder(
            BorderFactory.createEmptyBorder()
        );

        scrollFormulario.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollFormulario.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollFormulario.getVerticalScrollBar()
            .setUnitIncrement(16);

        scrollFormulario.getViewport()
            .setBackground(FONDO);

        contenedor.add(
            scrollFormulario,
            BorderLayout.CENTER
        );
        contenedor.add(crearPanelBotones(), BorderLayout.SOUTH);

        setContentPane(contenedor);

        /*
         * El rol seleccionado determina si se muestran
         * los datos profesionales del veterinario.
         */
        cmbRol.addActionListener(
            evento -> actualizarCamposVeterinario()
        );

        getRootPane().setDefaultButton(
            obtenerBotonGuardar()
        );
    }

    private JPanel crearEncabezado() {

        JPanel encabezado = new JPanel();
        encabezado.setLayout(
            new BoxLayout(encabezado, BoxLayout.Y_AXIS)
        );

        encabezado.setOpaque(false);
        encabezado.setBorder(
            new EmptyBorder(0, 0, 20, 0)
        );

        JLabel lblTitulo = new JLabel(
            usuarioOriginal == null
                ? "Registrar usuario"
                : "Editar usuario"
        );

        lblTitulo.setForeground(AZUL_OSCURO);
        lblTitulo.setFont(
            new Font("Segoe UI", Font.BOLD, 26)
        );

        JLabel lblDescripcion = new JLabel(
            "Ingresa los datos de acceso y el rol del usuario"
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

    private JPanel crearFormulario(List<Rol> roles) {

        JPanel formulario = new JPanel();
        formulario.setLayout(
            new BoxLayout(formulario, BoxLayout.Y_AXIS)
        );

        formulario.setBackground(Color.WHITE);
        formulario.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                new EmptyBorder(22, 25, 22, 25)
            )
        );

        txtNombreCompleto = crearCampoTexto(
            "Nombre completo"
        );

        txtNombreUsuario = crearCampoTexto(
            "Nombre de usuario"
        );

        txtCorreo = crearCampoTexto(
            "Correo electrónico"
        );

        cmbRol = new JComboBox<>();

        for (Rol rol : roles) {
            cmbRol.addItem(rol);
        }

        cmbRol.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 40)
        );

        txtContrasena = new JPasswordField();
        configurarCampo(txtContrasena);

        txtConfirmarContrasena = new JPasswordField();
        configurarCampo(txtConfirmarContrasena);

        formulario.add(crearEtiqueta("Nombre completo"));
        formulario.add(Box.createVerticalStrut(6));
        formulario.add(txtNombreCompleto);
        formulario.add(Box.createVerticalStrut(14));

        formulario.add(crearEtiqueta("Nombre de usuario"));
        formulario.add(Box.createVerticalStrut(6));
        formulario.add(txtNombreUsuario);
        formulario.add(Box.createVerticalStrut(14));

        formulario.add(crearEtiqueta("Correo"));
        formulario.add(Box.createVerticalStrut(6));
        formulario.add(txtCorreo);
        formulario.add(Box.createVerticalStrut(14));

        formulario.add(crearEtiqueta("Rol"));
        formulario.add(Box.createVerticalStrut(6));
        formulario.add(cmbRol);
        formulario.add(Box.createVerticalStrut(14));

        String textoContrasena =
            usuarioOriginal == null
                ? "Contraseña"
                : "Nueva contraseña (opcional)";

        formulario.add(crearEtiqueta(textoContrasena));
        formulario.add(Box.createVerticalStrut(6));
        formulario.add(txtContrasena);
        formulario.add(Box.createVerticalStrut(14));

        formulario.add(crearEtiqueta("Confirmar contraseña"));
        formulario.add(Box.createVerticalStrut(6));
        formulario.add(txtConfirmarContrasena);
        formulario.add(Box.createVerticalStrut(16));

        panelDatosVeterinario =
            crearPanelDatosVeterinario();

        formulario.add(panelDatosVeterinario);

        return formulario;
    }

    private JPanel crearPanelDatosVeterinario() {

        JPanel panel = new JPanel(
            new GridLayout(2, 2, 12, 8)
        );

        panel.setBackground(Color.WHITE);
        panel.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDE),
                "Datos profesionales"
            )
        );

        txtEspecialidad = new JTextField();
        txtTelefonoProfesional = new JTextField();

        txtEspecialidad.putClientProperty(
            "JTextField.placeholderText",
            "Ej. Medicina general"
        );

        txtTelefonoProfesional.putClientProperty(
            "JTextField.placeholderText",
            "Ej. 5555-1234"
        );

        panel.add(crearEtiqueta("Especialidad"));
        panel.add(crearEtiqueta("Teléfono profesional"));
        panel.add(txtEspecialidad);
        panel.add(txtTelefonoProfesional);

        panel.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 95)
        );

        return panel;
    }

    private JPanel crearPanelBotones() {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(
            new EmptyBorder(20, 0, 0, 0)
        );

        JButton btnCancelar =
            crearBoton("Cancelar", new Color(120, 130, 140));

        JButton btnGuardar =
            crearBoton("Guardar", AZUL_PRINCIPAL);

        /*
         * Guardamos una referencia para convertirlo
         * en el botón predeterminado del diálogo.
         */
        btnGuardar.putClientProperty(
            "botonGuardar",
            Boolean.TRUE
        );

        btnCancelar.addActionListener(
            evento -> dispose()
        );

        btnGuardar.addActionListener(
            evento -> confirmarFormulario()
        );

        panel.add(btnCancelar);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(btnGuardar);

        return panel;
    }

    private JTextField crearCampoTexto(
            String textoPlaceholder) {

        JTextField campo = new JTextField();

        campo.putClientProperty(
            "JTextField.placeholderText",
            textoPlaceholder
        );

        configurarCampo(campo);

        return campo;
    }

    private void configurarCampo(JTextField campo) {

        Dimension tamano =
            new Dimension(Integer.MAX_VALUE, 40);
        
        campo.setMinimumSize(new Dimension(200, 40));
        campo.setPreferredSize(new Dimension(400, 40));
        campo.setMaximumSize(tamano);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JLabel crearEtiqueta(String texto) {

        JLabel etiqueta = new JLabel(texto);

        etiqueta.setForeground(AZUL_OSCURO);
        etiqueta.setFont(
            new Font("Segoe UI", Font.BOLD, 13)
        );

        etiqueta.setAlignmentX(LEFT_ALIGNMENT);

        return etiqueta;
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
        boton.setPreferredSize(
            new Dimension(125, 42)
        );

        boton.putClientProperty(
            "JButton.buttonType",
            "borderless"
        );

        return boton;
    }

    /**
     * Busca dentro del panel inferior el botón Guardar.
     */
    private JButton obtenerBotonGuardar() {

        JPanel panel =
            (JPanel) getContentPane()
                .getComponent(2);

        for (java.awt.Component componente
                : panel.getComponents()) {

            if (componente instanceof JButton boton
                    && Boolean.TRUE.equals(
                        boton.getClientProperty(
                            "botonGuardar"
                        )
                    )) {

                return boton;
            }
        }

        return null;
    }

    private void actualizarCamposVeterinario() {

        Rol rol = (Rol) cmbRol.getSelectedItem();
        cmbRol.setAlignmentX(Component.LEFT_ALIGNMENT);

        boolean esVeterinario =
            rol != null
            && "VETERINARIO".equalsIgnoreCase(
                rol.getNombre()
            );

        panelDatosVeterinario.setVisible(esVeterinario);
        panelDatosVeterinario.setAlignmentX(Component.LEFT_ALIGNMENT);

        /*
         * Recalcula el diseño después de mostrar
         * u ocultar los campos profesionales.
         */
        panelDatosVeterinario.revalidate();
        panelDatosVeterinario.repaint();
    }

    private void cargarDatosUsuario(
            Usuario usuario,
            Veterinario veterinario) {

        txtNombreCompleto.setText(
            usuario.getNombreCompleto()
        );

        txtNombreUsuario.setText(
            usuario.getNombreUsuario()
        );

        txtCorreo.setText(
            usuario.getCorreo() == null
                ? ""
                : usuario.getCorreo()
        );

        /*
         * Selecciona el rol que corresponde al usuario.
         */
        for (int indice = 0;
                indice < cmbRol.getItemCount();
                indice++) {

            Rol rol = cmbRol.getItemAt(indice);

            if (rol.getIdRol() == usuario.getIdRol()) {
                cmbRol.setSelectedIndex(indice);
                break;
            }
        }

        if (veterinario != null) {

            txtEspecialidad.setText(
                veterinario.getEspecialidad() == null
                    ? ""
                    : veterinario.getEspecialidad()
            );

            txtTelefonoProfesional.setText(
                veterinario.getTelefonoProfesional() == null
                    ? ""
                    : veterinario.getTelefonoProfesional()
            );
        }
    }

    private void confirmarFormulario() {

        String nombreCompleto =
            txtNombreCompleto.getText().trim();

        String nombreUsuario =
            txtNombreUsuario.getText().trim();

        String correo =
            txtCorreo.getText().trim();

        Rol rol =
            (Rol) cmbRol.getSelectedItem();

        char[] contrasena =
            txtContrasena.getPassword();

        char[] confirmacion =
            txtConfirmarContrasena.getPassword();

        try {

            if (nombreCompleto.isBlank()) {
                advertencia("Ingresa el nombre completo.");
                txtNombreCompleto.requestFocus();
                return;
            }

            if (nombreUsuario.isBlank()) {
                advertencia("Ingresa el nombre de usuario.");
                txtNombreUsuario.requestFocus();
                return;
            }

            if (nombreUsuario.contains(" ")) {
                advertencia(
                    "El nombre de usuario no puede contener espacios."
                );

                txtNombreUsuario.requestFocus();
                return;
            }

            if (rol == null) {
                advertencia("Selecciona un rol.");
                return;
            }

            /*
             * Una contraseña es obligatoria al crear.
             * Al editar puede dejarse vacía para conservarla.
             */
            if (usuarioOriginal == null
                    && contrasena.length == 0) {

                advertencia("Ingresa una contraseña.");
                txtContrasena.requestFocus();
                return;
            }

            if (contrasena.length > 0
                    && contrasena.length < 8) {

                advertencia(
                    "La contraseña debe tener al menos 8 caracteres."
                );

                txtContrasena.requestFocus();
                return;
            }

            if (!Arrays.equals(
                    contrasena,
                    confirmacion)) {

                advertencia(
                    "Las contraseñas no coinciden."
                );

                txtConfirmarContrasena.requestFocus();
                return;
            }

            String hash;

            if (contrasena.length > 0) {
                hash =
                    SeguridadContrasena.generarHash(
                        contrasena
                    );
            } else {
                hash =
                    usuarioOriginal.getContrasenaHash();
            }

            construirResultadoUsuario(
                rol,
                nombreCompleto,
                nombreUsuario,
                correo,
                hash
            );

            construirResultadoVeterinario(rol);

            guardado = true;
            dispose();

        } finally {

            /*
             * Elimina las contraseñas de los arreglos
             * una vez terminada la validación.
             */
            Arrays.fill(contrasena, '\0');
            Arrays.fill(confirmacion, '\0');
        }
    }

    private void construirResultadoUsuario(
            Rol rol,
            String nombreCompleto,
            String nombreUsuario,
            String correo,
            String hash) {

        usuarioResultado = new Usuario();

        if (usuarioOriginal != null) {

            usuarioResultado.setIdUsuario(
                usuarioOriginal.getIdUsuario()
            );

            usuarioResultado.setActivo(
                usuarioOriginal.isActivo()
            );

            usuarioResultado.setFechaCreacion(
                usuarioOriginal.getFechaCreacion()
            );
        }

        usuarioResultado.setIdRol(
            rol.getIdRol()
        );

        usuarioResultado.setNombreRol(
            rol.getNombre()
        );

        usuarioResultado.setNombreCompleto(
            nombreCompleto
        );

        usuarioResultado.setNombreUsuario(
            nombreUsuario
        );

        usuarioResultado.setCorreo(
            correo.isBlank() ? null : correo
        );

        usuarioResultado.setContrasenaHash(hash);
    }

    private void construirResultadoVeterinario(Rol rol) {

        if (!"VETERINARIO".equalsIgnoreCase(
                rol.getNombre())) {

            veterinarioResultado = null;
            return;
        }

        veterinarioResultado = new Veterinario();

        if (veterinarioOriginal != null) {

            veterinarioResultado.setIdVeterinario(
                veterinarioOriginal.getIdVeterinario()
            );

            veterinarioResultado.setActivo(
                veterinarioOriginal.isActivo()
            );

            veterinarioResultado.setFechaRegistro(
                veterinarioOriginal.getFechaRegistro()
            );
        }

        veterinarioResultado.setEspecialidad(
            txtEspecialidad.getText().trim()
        );

        veterinarioResultado.setTelefonoProfesional(
            txtTelefonoProfesional.getText().trim()
        );
    }

    private void advertencia(String mensaje) {

        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Datos incompletos",
            JOptionPane.WARNING_MESSAGE
        );
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Usuario getUsuarioResultado() {
        return usuarioResultado;
    }

    public Veterinario getVeterinarioResultado() {
        return veterinarioResultado;
    }
}
