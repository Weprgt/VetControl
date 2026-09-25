/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import com.mycompany.vetcontrol.dao.ClienteDAO;
import com.mycompany.vetcontrol.modelo.Cliente;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import java.awt.Window;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author weprg
 */
public class PanelClientes extends JPanel{
    
    // Colores
    private static final Color FONDO = new Color(244, 247, 250);
    private static final Color AZUL_OSCURO= new Color(23, 50, 77);
    private static final Color AZUL_PRINCIPAL= new Color(52, 120, 184);
    private static final Color TURQUESA= new Color(67, 166, 160);
    private static final Color ROJO= new Color(217, 92, 89);
    private static final Color TEXTO_SECUNDARIO= new Color(98, 114, 125);
    private static final Color BORDE= new Color(216, 225, 232);
    
    // Tabla de datos
    private JTable tablaClientes;
    
    // Acceso a los clientes almacenados en MySQL.
    private final ClienteDAO clienteDAO;

    // Modelo utilizado para modificar las filas de la tabla.
    private DefaultTableModel modeloTabla;

    // Componentes que necesitan utilizar los eventos.
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnNuevo;
    private JButton btnEliminar;
    private JButton btnEditar;

    private List<Cliente> clientesMostrados =
        new ArrayList<>();

    public PanelClientes() {
        clienteDAO = new ClienteDAO();

        crearInterfaz();
        cargarClientes();
    }
    
    private void crearInterfaz() {
        setLayout(new BorderLayout(0, 25));
        setBackground(FONDO);
        setBorder(new EmptyBorder(40, 45, 40, 45));
        
        JPanel panelEncabezado= crearEncabezado();
        JPanel tarjetaClientes= crearTarjetaClientes();
        
        add(panelEncabezado, BorderLayout.NORTH);
        add(tarjetaClientes, BorderLayout.CENTER);
    }
    
    private JPanel crearEncabezado(){   
        // Panel encabezado
        JPanel panelEncabezado= new JPanel();
        panelEncabezado.setLayout(new BoxLayout(panelEncabezado, BoxLayout.Y_AXIS));
        panelEncabezado.setOpaque(false);
        
        // Título del panel
        JLabel lblTitulo= new JLabel("Clientes");
        lblTitulo.setForeground(AZUL_OSCURO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Descripción del panel
        JLabel lblDescripcion= new JLabel("Registro y consulta de propietarios");
        lblDescripcion.setForeground(TEXTO_SECUNDARIO);
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // agreamos los componentes
        panelEncabezado.add(lblTitulo);
        panelEncabezado.add(Box.createVerticalStrut(8));
        panelEncabezado.add(lblDescripcion);
        
        return panelEncabezado;
    }
    
    private JPanel crearTarjetaClientes() {
        JPanel tarjeta= new JPanel(new BorderLayout(0, 20));
        
        // apariencia y posición de la tarjeta
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1, true),
                    new EmptyBorder(20, 32, 28, 32)));

        // Agrega la tabla de datos
        tarjeta.add(crearSeccionBusqueda(), BorderLayout.NORTH);
        tarjeta.add(crearTablaClientes(), BorderLayout.CENTER);
        
        return tarjeta;
    }
    
    // Barra de busqueda
    private JPanel crearSeccionBusqueda(){
        JPanel seccionBusqueda= new JPanel(new BorderLayout(18, 8));
        seccionBusqueda.setBackground(Color.WHITE);
        
        // Etiqueta buscar cliente
        JLabel lblBuscar= new JLabel("Buscar Cliente");
        lblBuscar.setForeground(AZUL_OSCURO);
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        
        // Cuadro de busqueda
        txtBuscar = new JTextField();
        txtBuscar.putClientProperty("JTextField.placeholderText", "Nombre, teléfono o correo...");
        txtBuscar.setPreferredSize(new Dimension(300, 42));
        txtBuscar.addActionListener(evento -> buscarClientes());
        
        // Panel de botones
        JPanel panelBotones= crearPanelBotones();
        
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

        btnBuscar = crearBoton(
            "Buscar",
            AZUL_PRINCIPAL
        );

        btnNuevo = crearBoton(
            "+ Nuevo",
            TURQUESA
        );

        btnEditar = crearBoton(
            "Editar",
            new Color(242, 161, 62)
        );

        btnEliminar = crearBoton(
            "Desactivar",
            ROJO
        );

        btnBuscar.addActionListener(
            evento -> buscarClientes()
        );

        btnNuevo.addActionListener(
            evento -> nuevoCliente()
        );

        btnEditar.addActionListener(
            evento -> editarCliente()
        );

        btnEliminar.addActionListener(
            evento -> desactivarCliente()
        );

        panelBotones.add(btnBuscar);
        panelBotones.add(
            Box.createHorizontalStrut(10)
        );

        panelBotones.add(btnNuevo);
        panelBotones.add(
            Box.createHorizontalStrut(10)
        );

        panelBotones.add(btnEditar);
        panelBotones.add(
            Box.createHorizontalStrut(10)
        );

        panelBotones.add(btnEliminar);

        return panelBotones;
    }
    
    private JButton crearBoton(String texto, Color color){
        JButton boton= new JButton(texto);
        
        // apariencia de los botones
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        // dimensiones de los botones
        Dimension tamanoBoton= new Dimension(115,42);
        boton.setMinimumSize(tamanoBoton);
        boton.setPreferredSize(tamanoBoton);
        boton.setMaximumSize(tamanoBoton);
        // forma de los botones
        boton.putClientProperty("JComponent.minimumHeight", 42);
        boton.putClientProperty("JButton.buttonType", "borderless");
        
        return boton;
    }
    
    // Panel tabla de datos
    private JScrollPane crearTablaClientes(){
        String[] columnas= {
            "Código", 
            "Nombre completo", 
            "Teléfono", 
            "Correo",
            "direccion"};
        
        modeloTabla = new DefaultTableModel(columnas, 0) {

    @Override
    public boolean isCellEditable(
            int fila,
            int columna) {

        return false;
    }
};
        
        // creación de la tabla clientes
        tablaClientes = new JTable(modeloTabla);
        
        // apariencia de la tabla clientes
        tablaClientes.setRowHeight(42);
        tablaClientes.setShowVerticalLines(false);
        tablaClientes.setShowHorizontalLines(true);
        tablaClientes.setGridColor(BORDE);
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // definición de colores y fuente
        tablaClientes.setSelectionBackground(new Color(232, 242, 247));
        tablaClientes.setSelectionForeground(new Color(36, 50, 61));
        tablaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // Encabezados de las columnas
        tablaClientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaClientes.getTableHeader().setBackground(AZUL_OSCURO);
        tablaClientes.getTableHeader().setForeground(Color.WHITE);
        tablaClientes.getTableHeader().setPreferredSize(new Dimension(0, 42));
        tablaClientes.setFillsViewportHeight(true);
        // barras de desplazamiento
        JScrollPane scroll= new JScrollPane(tablaClientes);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        
        return scroll;
        
    }
    /**
    * Obtiene todos los clientes activos desde MySQL.
    */
    private void cargarClientes() {

        List<Cliente> clientes =
            clienteDAO.listarActivos();

        mostrarClientes(clientes);
}

    /**
     * Busca utilizando el contenido del campo de texto.
     */
    private void buscarClientes() {

        String criterio =
            txtBuscar.getText().trim();

        /*
         * Si el campo está vacío, vuelve a mostrar
         * todos los clientes activos.
         */
        if (criterio.isBlank()) {
            cargarClientes();
            return;
        }

        List<Cliente> clientes =
            clienteDAO.buscar(criterio);

        mostrarClientes(clientes);
    }

    /**
     * Coloca una lista de clientes dentro de la tabla.
     */
    private void mostrarClientes(
        List<Cliente> clientes) {

        clientesMostrados =
            new ArrayList<>(clientes);

        modeloTabla.setRowCount(0);

        for (Cliente cliente : clientesMostrados) {

            modeloTabla.addRow(new Object[] {
                cliente.getCodigoVisible(),
                cliente.getNombreCompleto(),
                cliente.getTelefono(),
                cliente.getCorreo(),
                cliente.getDireccion()
            });
        }
    }
    /**
 * Abre el formulario para registrar un cliente.
 */
private void nuevoCliente() {

    Window ventana =
        SwingUtilities.getWindowAncestor(this);

    DlgCliente dialogo =
        new DlgCliente(ventana, null);

    dialogo.setVisible(true);

    Cliente cliente =
        dialogo.getClienteConfirmado();

    if (cliente == null) {
        return;
    }

    if (clienteDAO.insertar(cliente)) {

        JOptionPane.showMessageDialog(
            this,
            "Cliente registrado correctamente.",
            "Registro exitoso",
            JOptionPane.INFORMATION_MESSAGE
        );

        cargarClientes();

    } else {
        JOptionPane.showMessageDialog(
            this,
            "No se pudo registrar el cliente.\n"
            + "Verifica que el correo no esté repetido.",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}

    /**
     * Edita el cliente seleccionado.
     */
    private void editarCliente() {

        Cliente cliente =
            obtenerClienteSeleccionado();

        if (cliente == null) {

            JOptionPane.showMessageDialog(
                this,
                "Selecciona un cliente para editar.",
                "Cliente no seleccionado",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Window ventana =
            SwingUtilities.getWindowAncestor(this);

        DlgCliente dialogo =
            new DlgCliente(ventana, cliente);

        dialogo.setVisible(true);

        Cliente clienteEditado =
            dialogo.getClienteConfirmado();

        if (clienteEditado == null) {
            return;
        }

        if (clienteDAO.actualizar(clienteEditado)) {

            JOptionPane.showMessageDialog(
                this,
                "Cliente actualizado correctamente.",
                "Actualización exitosa",
                JOptionPane.INFORMATION_MESSAGE
            );

        } else {
            JOptionPane.showMessageDialog(
                this,
                "No se pudo actualizar el cliente.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }

        /*
         * Recarga incluso si hubo error para restaurar
         * los valores verdaderos de MySQL.
         */
        cargarClientes();
    }

    /**
     * Desactiva al cliente seleccionado.
     */
    private void desactivarCliente() {

        Cliente cliente =
            obtenerClienteSeleccionado();

        if (cliente == null) {

            JOptionPane.showMessageDialog(
                this,
                "Selecciona un cliente para desactivar.",
                "Cliente no seleccionado",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta =
            JOptionPane.showConfirmDialog(
                this,
                "¿Deseas desactivar a "
                + cliente.getNombreCompleto()
                + "?",
                "Confirmar desactivación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        if (clienteDAO.desactivar(
                cliente.getIdCliente())) {

            JOptionPane.showMessageDialog(
                this,
                "Cliente desactivado correctamente.",
                "Operación exitosa",
                JOptionPane.INFORMATION_MESSAGE
            );

            cargarClientes();

        } else {
            JOptionPane.showMessageDialog(
                this,
                "No se pudo desactivar el cliente.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Obtiene el objeto correspondiente a la fila seleccionada.
     */
    private Cliente obtenerClienteSeleccionado() {

        int filaVista =
            tablaClientes.getSelectedRow();

        if (filaVista < 0) {
            return null;
        }

        int filaModelo =
            tablaClientes.convertRowIndexToModel(
                filaVista
            );

        if (filaModelo < 0
                || filaModelo >= clientesMostrados.size()) {

            return null;
        }

        return clientesMostrados.get(filaModelo);
    }
    
}
