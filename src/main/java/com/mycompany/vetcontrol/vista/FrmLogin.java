/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import com.mycompany.vetcontrol.modelo.Usuario;
import com.mycompany.vetcontrol.servicio.ServicioAutenticacion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


/**
 *
 * @author weprg
 */
public class FrmLogin extends JFrame {
    // lista de colores
    private static final Color AZUL_OSCURO= new Color(23, 50, 77);
    private static final Color FONDO= new Color(244, 247, 250);
    private static final Color TEXTO= new Color(36, 50, 61);
    private static final Color TEXTO_SECUNDARIO= new Color(98, 114, 125);
    private static final Color BORDE= new Color(216, 225, 232);
    
    // formulario inicio de sesión
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIniciarSesion;
    
    // constructor
    public FrmLogin(){
        configurarVentana();
        crearInterfaz();
    }
    // ventana principal
    private void configurarVentana() {
        setTitle("VetControl - Inicio de sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
    }
    // Interfaz inicio de sesión
    private void crearInterfaz() {
        setLayout(new BorderLayout());
        add(crearPanelPresentacion(), BorderLayout.WEST);
        add(crearPanelFormulario(), BorderLayout.CENTER);
    }
    // Área de presentación
    private JPanel crearPanelPresentacion() {
        JPanel panelPresentacion= new JPanel();
        panelPresentacion.setLayout(new BoxLayout(panelPresentacion, BoxLayout.Y_AXIS));
        panelPresentacion.setBackground(AZUL_OSCURO);
        panelPresentacion.setPreferredSize(new Dimension(420, 0));
        panelPresentacion.setBorder(new EmptyBorder(70, 55, 60, 45));
        
        // Titulo
        JLabel lblNombre= new JLabel("VetControl");
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 40));
        
        // Descripcion
        JLabel lblDescripcion= new JLabel(
            "<html>"
          + "Gestión clínica simple, segura" + "<br>"
          + "y siempre organizada."
          + "</html>"
        );
        lblDescripcion.setForeground(new Color(216, 233, 230));
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 21));
        JLabel lblClinica= new JLabel("Clínica Veterinaria Huellitas");
        lblClinica.setForeground(new Color(187, 212, 208));
        
        // Agregar los componentes
        
        // Título
        panelPresentacion.add(lblNombre);
        panelPresentacion.add(Box.createVerticalStrut(25));
        // Descripción
        panelPresentacion.add(lblDescripcion);
        panelPresentacion.add(Box.createVerticalGlue());
        // Lema
        panelPresentacion.add(lblClinica);
        
        return panelPresentacion;
    }
    
    private JPanel crearPanelFormulario() {
        JPanel panelFormulario= new JPanel(new GridBagLayout());
        panelFormulario.setBackground(FONDO);
        
        // Dimensiones del formulario de inicio de sesión
        JPanel tarjetaLogin= new JPanel();
        tarjetaLogin.setLayout(new BoxLayout(tarjetaLogin, BoxLayout.Y_AXIS));
        tarjetaLogin.setBackground(Color.WHITE);
        tarjetaLogin.setPreferredSize(new Dimension(430, 480));
        tarjetaLogin.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1, true),
            new EmptyBorder(45, 45, 45, 45))
        );
        
        // Etiqueta inicio de sesion
        JLabel lblTitulo= new JLabel("Bienvenido");
        lblTitulo.setForeground(TEXTO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setMaximumSize(new Dimension(340, 42));
        
        // Instrucciones
        JLabel lblInstruccion= new JLabel("Ingresa tus datos para continuar");
        lblInstruccion.setForeground(TEXTO_SECUNDARIO);
        lblInstruccion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInstruccion.setAlignmentX(LEFT_ALIGNMENT);
        lblInstruccion.setMaximumSize(new Dimension(340, 25));
        lblInstruccion.setHorizontalAlignment(SwingConstants.CENTER);
        
        // agrega los componentes
        tarjetaLogin.add(lblTitulo);
        tarjetaLogin.add(Box.createVerticalStrut(12));
        tarjetaLogin.add(lblInstruccion);
        tarjetaLogin.add(Box.createVerticalStrut(35));
        
        // Formulario datos de usuario
        
        // Etiqueta usuario
        JLabel lblUsuario= new JLabel("Usuario");
        lblUsuario.setForeground(TEXTO);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsuario.setAlignmentX(LEFT_ALIGNMENT);        
        tarjetaLogin.add(lblUsuario);
        tarjetaLogin.add(Box.createVerticalStrut(8));
        
        // Campo de texto usuario
        txtUsuario = new JTextField();
        txtUsuario.putClientProperty("JTextField.placeholderText", "Ingresa tu usuario");
        Dimension tamanoCampo = new Dimension(340, 42);
        txtUsuario.setMinimumSize(tamanoCampo);
        txtUsuario.setPreferredSize(tamanoCampo);
        txtUsuario.setMaximumSize(tamanoCampo);
        txtUsuario.setAlignmentX(LEFT_ALIGNMENT);
        tarjetaLogin.add(txtUsuario);
        tarjetaLogin.add(Box.createVerticalStrut(22));
        
        // Etiqueta de contraseña
        JLabel lblContrasena= new JLabel("Contraseña");
        lblContrasena.setForeground(TEXTO);
        lblContrasena.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblContrasena.setAlignmentX(LEFT_ALIGNMENT);
        tarjetaLogin.add(lblContrasena);
        tarjetaLogin.add(Box.createVerticalStrut(8));
        
        // Campo de contraseña
        txtContrasena = new JPasswordField();
        txtContrasena.putClientProperty("JTextField.placeholderText", "Ingresa tu contraseña");
        txtContrasena.setMinimumSize(tamanoCampo);
        txtContrasena.setPreferredSize(tamanoCampo);
        txtContrasena.setMaximumSize(tamanoCampo);
        txtContrasena.setAlignmentX(LEFT_ALIGNMENT);
        tarjetaLogin.add(txtContrasena);
        tarjetaLogin.add(Box.createVerticalStrut(30));
        
        // Botón inicio de sesión
        btnIniciarSesion= new JButton("INICIAR SESIÓN");
        btnIniciarSesion.setForeground(Color.WHITE);
        btnIniciarSesion.setBackground(new Color(52, 120, 184));
        btnIniciarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIniciarSesion.setFocusPainted(false);
        
        // Dimensiones y colocación del botón
        Dimension tamanoBoton= new Dimension(340, 44);
        btnIniciarSesion.setMinimumSize(tamanoBoton);
        btnIniciarSesion.setPreferredSize(tamanoBoton);
        btnIniciarSesion.setMaximumSize(tamanoBoton);
        btnIniciarSesion.setAlignmentX(LEFT_ALIGNMENT);
        btnIniciarSesion.putClientProperty("JButton.buttonType", "borderless");
        btnIniciarSesion.addActionListener(evento -> {
            try {
                iniciarSesion();
            } catch (SQLException ex) {
                System.getLogger(FrmLogin.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
        getRootPane().setDefaultButton(btnIniciarSesion);
        tarjetaLogin.add(btnIniciarSesion);
        tarjetaLogin.add(Box.createVerticalStrut(30));
 
        panelFormulario.add(tarjetaLogin);
        
        return panelFormulario;       
    }
    private void iniciarSesion() throws SQLException {

    String nombreUsuario =
        txtUsuario.getText().trim();

    char[] contrasena =
        txtContrasena.getPassword();

    if (nombreUsuario.isBlank()
            || contrasena.length == 0) {

        JOptionPane.showMessageDialog(
            this,
            "Ingresa tu usuario y contraseña.",
            "Datos incompletos",
            JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    /*
     * Evita varios clics mientras se realiza la consulta.
     */
    btnIniciarSesion.setEnabled(false);

    try {
        ServicioAutenticacion servicio =
            new ServicioAutenticacion();

        Usuario usuario =
            servicio.autenticar(
                nombreUsuario,
                contrasena
            );

        if (usuario == null) {
            JOptionPane.showMessageDialog(
                this,
                "Usuario o contraseña incorrectos.",
                "Acceso denegado",
                JOptionPane.ERROR_MESSAGE
            );

            txtContrasena.setText("");
            txtContrasena.requestFocus();

            return;
        }

        /*
         * Cierra el inicio de sesión y abre la aplicación.
         */
        dispose();

        FrmPrincipal ventanaPrincipal =
            new FrmPrincipal(usuario);

        ventanaPrincipal.setVisible(true);

        } finally {
            /*
             * Borra la contraseña del arreglo en memoria.
             */
            Arrays.fill(contrasena, '\0');

            btnIniciarSesion.setEnabled(true);
        }
    }
}
