/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import com.mycompany.vetcontrol.dao.RolDAO;
import com.mycompany.vetcontrol.dao.UsuarioDAO;
import com.mycompany.vetcontrol.dao.VeterinarioDAO;
import com.mycompany.vetcontrol.modelo.Rol;
import com.mycompany.vetcontrol.modelo.Usuario;
import com.mycompany.vetcontrol.modelo.Veterinario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

public class PanelUsuarios extends JPanel {

    private static final Color FONDO =
        new Color(244, 247, 250);

    private static final Color AZUL_OSCURO =
        new Color(23, 50, 77);

    private static final Color AZUL_PRINCIPAL =
        new Color(52, 120, 184);

    private static final Color TURQUESA =
        new Color(67, 166, 160);

    private static final Color NARANJA =
        new Color(242, 161, 62);

    private static final Color ROJO =
        new Color(217, 92, 89);

    private static final Color TEXTO =
        new Color(36, 50, 61);

    private static final Color TEXTO_SECUNDARIO =
        new Color(98, 114, 125);

    private static final Color BORDE =
        new Color(216, 225, 232);

    private final Usuario usuarioActual;

    private final UsuarioDAO usuarioDAO;
    private final VeterinarioDAO veterinarioDAO;
    private final RolDAO rolDAO;

    private JTextField txtBuscar;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    /*
    * Se declara como atributo porque debe cambiar
    * entre Activar y Desactivar al seleccionar una fila.
    */
   private JButton btnEstado;

    private List<Usuario> usuariosMostrados =
        new ArrayList<>();

    private List<Rol> roles =
        new ArrayList<>();

    public PanelUsuarios(Usuario usuarioActual) {

        this.usuarioActual = usuarioActual;

        usuarioDAO = new UsuarioDAO();
        veterinarioDAO = new VeterinarioDAO();
        rolDAO = new RolDAO();

        crearInterfaz();
        cargarRoles();
        cargarUsuarios();
    }

    private void crearInterfaz() {

        setLayout(new BorderLayout(0, 25));
        setBackground(FONDO);
        setBorder(
            new EmptyBorder(40, 45, 40, 45)
        );

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearTarjetaUsuarios(), BorderLayout.CENTER);
    }

    private JPanel crearEncabezado() {

        JPanel encabezado = new JPanel();

        encabezado.setLayout(
            new BoxLayout(
                encabezado,
                BoxLayout.Y_AXIS
            )
        );

        encabezado.setOpaque(false);

        JLabel lblTitulo =
            new JLabel("Usuarios");

        lblTitulo.setForeground(AZUL_OSCURO);
        lblTitulo.setFont(
            new Font("Segoe UI", Font.BOLD, 30)
        );

        lblTitulo.setAlignmentX(
            Component.LEFT_ALIGNMENT
        );

        JLabel lblDescripcion = new JLabel(
            "Administración de cuentas y permisos"
        );

        lblDescripcion.setForeground(
            TEXTO_SECUNDARIO
        );

        lblDescripcion.setFont(
            new Font("Segoe UI", Font.PLAIN, 16)
        );

        lblDescripcion.setAlignmentX(
            Component.LEFT_ALIGNMENT
        );

        encabezado.add(lblTitulo);
        encabezado.add(Box.createVerticalStrut(8));
        encabezado.add(lblDescripcion);

        return encabezado;
    }

    private JPanel crearTarjetaUsuarios() {

        JPanel tarjeta =
            new JPanel(new BorderLayout(0, 20));

        tarjeta.setBackground(Color.WHITE);

        tarjeta.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    BORDE,
                    1,
                    true
                ),
                new EmptyBorder(20, 32, 28, 32)
            )
        );

        tarjeta.add(
            crearSeccionBusqueda(),
            BorderLayout.NORTH
        );

        tarjeta.add(
            crearTablaUsuarios(),
            BorderLayout.CENTER
        );

        return tarjeta;
    }

    private JPanel crearSeccionBusqueda() {

        JPanel seccion =
            new JPanel(new BorderLayout(18, 8));

        seccion.setBackground(Color.WHITE);

        JLabel lblBuscar =
            new JLabel("Buscar usuario");

        lblBuscar.setForeground(AZUL_OSCURO);
        lblBuscar.setFont(
            new Font("Segoe UI", Font.BOLD, 15)
        );

        txtBuscar = new JTextField();

        txtBuscar.putClientProperty(
            "JTextField.placeholderText",
            "Nombre, usuario, correo o rol..."
        );

        txtBuscar.setPreferredSize(
            new Dimension(300, 42)
        );

        txtBuscar.addActionListener(
            evento -> buscarUsuarios()
        );

        seccion.add(
            lblBuscar,
            BorderLayout.NORTH
        );

        seccion.add(
            txtBuscar,
            BorderLayout.CENTER
        );

        seccion.add(
            crearPanelBotones(),
            BorderLayout.EAST
        );

        return seccion;
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

        JButton btnBuscar =
            crearBoton("Buscar", AZUL_PRINCIPAL);

        JButton btnNuevo =
            crearBoton("+ Nuevo", TURQUESA);

        JButton btnEditar =
            crearBoton("Editar", NARANJA);

        btnEstado =
            crearBoton("Desactivar", ROJO);

        btnBuscar.addActionListener(
            evento -> buscarUsuarios()
        );

        btnNuevo.addActionListener(
            evento -> nuevoUsuario()
        );

        btnEditar.addActionListener(
            evento -> editarUsuario()
        );

        btnEstado.addActionListener(
            evento -> cambiarEstadoUsuario()
        );

        panelBotones.add(btnBuscar);
        panelBotones.add(
            Box.createHorizontalStrut(8)
        );

        panelBotones.add(btnNuevo);
        panelBotones.add(
            Box.createHorizontalStrut(8)
        );

        panelBotones.add(btnEditar);
        panelBotones.add(
            Box.createHorizontalStrut(8)
        );

        panelBotones.add(btnEstado);

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
            new Dimension(110, 42);

        boton.setMinimumSize(tamano);
        boton.setPreferredSize(tamano);
        boton.setMaximumSize(tamano);

        boton.putClientProperty(
            "JButton.buttonType",
            "borderless"
        );

        return boton;
    }

    private JScrollPane crearTablaUsuarios() {

        String[] columnas = {
            "ID",
            "Nombre completo",
            "Usuario",
            "Correo",
            "Rol",
            "Estado"
        };

        modeloTabla =
            new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna) {

                return false;
            }
        };
        
        

        tablaUsuarios =
            new JTable(modeloTabla);

        tablaUsuarios.setRowHeight(42);
        tablaUsuarios.setShowVerticalLines(false);
        tablaUsuarios.setShowHorizontalLines(true);
        tablaUsuarios.setGridColor(BORDE);

        tablaUsuarios.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        
        tablaUsuarios.getSelectionModel()
            .addListSelectionListener(evento -> {

                if (!evento.getValueIsAdjusting()) {
                    actualizarBotonEstado();
                }
            });

        tablaUsuarios.setSelectionBackground(
            new Color(232, 242, 247)
        );

        tablaUsuarios.setSelectionForeground(
            TEXTO
        );

        tablaUsuarios.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        tablaUsuarios.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        tablaUsuarios.getTableHeader().setBackground(
            AZUL_OSCURO
        );

        tablaUsuarios.getTableHeader().setForeground(
            Color.WHITE
        );

        tablaUsuarios.getTableHeader().setPreferredSize(
            new Dimension(0, 42)
        );

        tablaUsuarios.setFillsViewportHeight(true);

        /*
         * Ajustamos los anchos de las columnas.
         */
        tablaUsuarios.getColumnModel()
            .getColumn(0)
            .setPreferredWidth(50);

        tablaUsuarios.getColumnModel()
            .getColumn(1)
            .setPreferredWidth(230);

        tablaUsuarios.getColumnModel()
            .getColumn(2)
            .setPreferredWidth(150);

        tablaUsuarios.getColumnModel()
            .getColumn(3)
            .setPreferredWidth(220);

        tablaUsuarios.getColumnModel()
            .getColumn(4)
            .setPreferredWidth(130);
        
        tablaUsuarios.getColumnModel()
            .getColumn(5)
            .setPreferredWidth(90);

        JScrollPane scroll =
            new JScrollPane(tablaUsuarios);

        scroll.setBorder(
            BorderFactory.createEmptyBorder()
        );

        return scroll;
    }

    private void cargarRoles() {

        roles = rolDAO.listar();

        if (roles.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "No fue posible cargar los roles.",
                "Roles no disponibles",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void cargarUsuarios() {

        List<Usuario> usuarios =
            usuarioDAO.listarTodos();

        mostrarUsuarios(usuarios);
    }

    private void buscarUsuarios() {

        String criterio =
            txtBuscar.getText().trim();

        if (criterio.isBlank()) {
            cargarUsuarios();
            return;
        }

        mostrarUsuarios(
            usuarioDAO.buscar(criterio)
        );
    }

    private void mostrarUsuarios(
            List<Usuario> usuarios) {

        usuariosMostrados =
            new ArrayList<>(usuarios);

        modeloTabla.setRowCount(0);

        for (Usuario usuario : usuarios) {

           modeloTabla.addRow(new Object[] {
            usuario.getIdUsuario(),
            usuario.getNombreCompleto(),
            usuario.getNombreUsuario(),
            usuario.getCorreo() == null
                ? ""
                : usuario.getCorreo(),
            usuario.getNombreRol(),
            usuario.isActivo()
                ? "ACTIVO"
                : "INACTIVO"
        });
        }
    }

    private void nuevoUsuario() {

        if (roles.isEmpty()) {

            mensajeAdvertencia(
                "No hay roles disponibles."
            );

            return;
        }

        Window ventana =
            SwingUtilities.getWindowAncestor(this);

        DlgUsuario dialogo = new DlgUsuario(
            ventana,
            null,
            null,
            roles
        );

        dialogo.setVisible(true);

        if (!dialogo.isGuardado()) {
            return;
        }

        Usuario usuario =
            dialogo.getUsuarioResultado();

        Veterinario veterinario =
            dialogo.getVeterinarioResultado();

        if (!usuarioDAO.insertar(usuario)) {

            mensajeError(
                "No fue posible registrar el usuario.\n"
                + "Comprueba que el usuario y el correo "
                + "no estén repetidos."
            );

            return;
        }

        /*
         * Si el rol seleccionado es VETERINARIO,
         * guarda también sus datos profesionales.
         */
        if (veterinario != null) {

            veterinario.setIdUsuario(
                usuario.getIdUsuario()
            );

            if (!veterinarioDAO.guardarPorUsuario(
                    veterinario)) {

                /*
                 * El usuario se desactiva para evitar
                 * dejar una cuenta incompleta.
                 */
                usuarioDAO.desactivar(
                    usuario.getIdUsuario()
                );

                mensajeError(
                    "El usuario fue creado, pero no fue "
                    + "posible registrar sus datos "
                    + "profesionales."
                );

                cargarUsuarios();
                return;
            }
        }

        JOptionPane.showMessageDialog(
            this,
            "Usuario registrado correctamente.",
            "Registro completado",
            JOptionPane.INFORMATION_MESSAGE
        );

        cargarUsuarios();
    }

    private void editarUsuario() {

        Usuario usuarioSeleccionado =
            obtenerUsuarioSeleccionado();

        if (usuarioSeleccionado == null) {
            return;
        }

        Veterinario veterinarioAnterior =
            veterinarioDAO.buscarPorIdUsuario(
                usuarioSeleccionado.getIdUsuario()
            );

        Window ventana =
            SwingUtilities.getWindowAncestor(this);

        DlgUsuario dialogo = new DlgUsuario(
            ventana,
            usuarioSeleccionado,
            veterinarioAnterior,
            roles
        );

        dialogo.setVisible(true);

        if (!dialogo.isGuardado()) {
            return;
        }

        Usuario usuarioEditado =
            dialogo.getUsuarioResultado();

        Veterinario veterinarioEditado =
            dialogo.getVeterinarioResultado();

        boolean cambioContrasena =
            !Objects.equals(
                usuarioSeleccionado.getContrasenaHash(),
                usuarioEditado.getContrasenaHash()
            );

        if (!usuarioDAO.actualizar(usuarioEditado)) {

            mensajeError(
                "No fue posible actualizar el usuario.\n"
                + "Comprueba que el usuario y el correo "
                + "no estén repetidos."
            );

            return;
        }

        /*
         * UsuarioDAO.actualizar() no modifica la
         * contraseña. Se cambia por separado únicamente
         * si el administrador escribió una nueva.
         */
        if (cambioContrasena) {

            boolean contrasenaActualizada =
                usuarioDAO.actualizarContrasena(
                    usuarioEditado.getIdUsuario(),
                    usuarioEditado.getContrasenaHash()
                );

            if (!contrasenaActualizada) {

                mensajeError(
                    "Los datos fueron actualizados, "
                    + "pero no la contraseña."
                );

                cargarUsuarios();
                return;
            }
        }

        if (veterinarioEditado != null) {

            veterinarioEditado.setIdUsuario(
                usuarioEditado.getIdUsuario()
            );

            if (!veterinarioDAO.guardarPorUsuario(
                    veterinarioEditado)) {

                mensajeError(
                    "El usuario fue actualizado, pero "
                    + "no sus datos profesionales."
                );

                cargarUsuarios();
                return;
            }

        } else if (veterinarioAnterior != null) {

            /*
             * Si antes era veterinario y ahora posee otro
             * rol, se desactiva su registro profesional.
             */
            veterinarioDAO.desactivar(
                veterinarioAnterior
                    .getIdVeterinario()
            );
        }

        JOptionPane.showMessageDialog(
            this,
            "Usuario actualizado correctamente.",
            "Actualización completada",
            JOptionPane.INFORMATION_MESSAGE
        );

        cargarUsuarios();
    }

    /**
    * Activa o desactiva la cuenta seleccionada.
    */
   private void cambiarEstadoUsuario() {

       Usuario usuarioSeleccionado =
           obtenerUsuarioSeleccionado();

       if (usuarioSeleccionado == null) {
           return;
       }

       /*
        * No se permite que el administrador desactive
        * la cuenta con la que inició sesión.
        */
       if (usuarioSeleccionado.isActivo()
               && usuarioSeleccionado.getIdUsuario()
               == usuarioActual.getIdUsuario()) {

           mensajeAdvertencia(
               "No puedes desactivar tu propia cuenta "
               + "mientras tienes la sesión iniciada."
           );

           return;
       }

       if (usuarioSeleccionado.isActivo()) {

           desactivarCuenta(usuarioSeleccionado);

       } else {

           reactivarCuenta(usuarioSeleccionado);
       }
   }

    private Usuario obtenerUsuarioSeleccionado() {

        int fila =
            tablaUsuarios.getSelectedRow();

        if (fila < 0) {

            mensajeAdvertencia(
                "Selecciona un usuario de la tabla."
            );

            return null;
        }

        return usuariosMostrados.get(fila);
    }

    private void mensajeAdvertencia(
            String mensaje) {

        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Atención",
            JOptionPane.WARNING_MESSAGE
        );
    }

    private void mensajeError(
            String mensaje) {

        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
    * Cambia la apariencia del botón según el estado
    * de la cuenta seleccionada.
    */
   private void actualizarBotonEstado() {

       int fila =
           tablaUsuarios.getSelectedRow();

       if (fila < 0
               || fila >= usuariosMostrados.size()) {

           btnEstado.setText("Desactivar");
           btnEstado.setBackground(ROJO);
           return;
       }

       Usuario usuario =
           usuariosMostrados.get(fila);

       if (usuario.isActivo()) {

           btnEstado.setText("Desactivar");
           btnEstado.setBackground(ROJO);

       } else {

           btnEstado.setText("Activar");
           btnEstado.setBackground(TURQUESA);
       }
   }
   /**
    * Desactiva la cuenta seleccionada.
    */
   private void desactivarCuenta(Usuario usuario) {

       int respuesta =
           JOptionPane.showConfirmDialog(
               this,
               "¿Deseas desactivar al usuario "
               + usuario.getNombreCompleto()
               + "?\n\n"
               + "Ya no podrá iniciar sesión.",
               "Confirmar desactivación",
               JOptionPane.YES_NO_OPTION,
               JOptionPane.WARNING_MESSAGE
           );

       if (respuesta != JOptionPane.YES_OPTION) {
           return;
       }

       if (!usuarioDAO.desactivar(
               usuario.getIdUsuario())) {

           mensajeError(
               "No fue posible desactivar el usuario."
           );

           return;
       }

       /*
        * Si es veterinario, también desactiva
        * su registro profesional.
        */
       if ("VETERINARIO".equalsIgnoreCase(
               usuario.getNombreRol())) {

           veterinarioDAO.cambiarEstadoPorUsuario(
               usuario.getIdUsuario(),
               false
           );
       }

       JOptionPane.showMessageDialog(
           this,
           "Usuario desactivado correctamente.",
           "Cuenta desactivada",
           JOptionPane.INFORMATION_MESSAGE
       );

       cargarUsuarios();
   }


   /**
    * Reactiva una cuenta desactivada.
    */
   private void reactivarCuenta(Usuario usuario) {

       int respuesta =
           JOptionPane.showConfirmDialog(
               this,
               "¿Deseas reactivar al usuario "
               + usuario.getNombreCompleto()
               + "?\n\n"
               + "Podrá volver a iniciar sesión.",
               "Confirmar reactivación",
               JOptionPane.YES_NO_OPTION,
               JOptionPane.QUESTION_MESSAGE
           );

       if (respuesta != JOptionPane.YES_OPTION) {
           return;
       }

       if (!usuarioDAO.reactivar(
               usuario.getIdUsuario())) {

           mensajeError(
               "No fue posible reactivar el usuario."
           );

           return;
       }

       /*
        * Reactiva también la información profesional
        * cuando la cuenta pertenece a un veterinario.
        */
       if ("VETERINARIO".equalsIgnoreCase(
               usuario.getNombreRol())) {

           veterinarioDAO.cambiarEstadoPorUsuario(
               usuario.getIdUsuario(),
               true
           );
       }

       JOptionPane.showMessageDialog(
           this,
           "Usuario reactivado correctamente.",
           "Cuenta activa",
           JOptionPane.INFORMATION_MESSAGE
       );

       cargarUsuarios();
   }
}
