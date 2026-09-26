/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import com.mycompany.vetcontrol.modelo.MovimientoInventario;
import com.mycompany.vetcontrol.modelo.MovimientoInventario.TipoMovimiento;
import com.mycompany.vetcontrol.modelo.Producto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author weprg
 */
public class DlgMovimientoInventario extends JDialog {

    private static final Color FONDO =
        new Color(244, 247, 250);

    private static final Color AZUL_OSCURO =
        new Color(23, 50, 77);

    private static final Color AZUL_PRINCIPAL =
        new Color(52, 120, 184);

    private static final Color TEXTO =
        new Color(36, 50, 61);

    private static final Color BORDE =
        new Color(216, 225, 232);

    private final int idUsuario;

    private JComboBox<Producto> cmbProducto;
    private JComboBox<TipoMovimiento> cmbTipo;
    private JTextField txtCantidad;
    private JTextArea txtMotivo;
    private JLabel lblStockActual;

    private boolean guardado;

    public DlgMovimientoInventario(
            Window propietario,
            List<Producto> productos,
            Producto productoSeleccionado,
            int idUsuario) {

        super(
            propietario,
            "Movimiento de inventario",
            ModalityType.APPLICATION_MODAL
        );

        this.idUsuario = idUsuario;

        configurarVentana();
        crearInterfaz(productos);

        if (productoSeleccionado != null) {
            seleccionarProducto(
                productoSeleccionado.getIdProducto()
            );
        }

        actualizarStockMostrado();
    }

    private void configurarVentana() {

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(620, 500);
        setMinimumSize(new Dimension(580, 450));
        setLocationRelativeTo(getOwner());
    }

    private void crearInterfaz(
            List<Producto> productos) {

        JPanel principal =
            new JPanel(new BorderLayout(0, 20));

        principal.setBackground(FONDO);
        principal.setBorder(
            new EmptyBorder(25, 30, 25, 30)
        );

        principal.add(
            crearEncabezado(),
            BorderLayout.NORTH
        );

        principal.add(
            crearFormulario(productos),
            BorderLayout.CENTER
        );

        principal.add(
            crearPanelBotones(),
            BorderLayout.SOUTH
        );

        setContentPane(principal);
    }

    private JPanel crearEncabezado() {

        JPanel encabezado = new JPanel();

        encabezado.setLayout(
            new BoxLayout(encabezado, BoxLayout.Y_AXIS)
        );

        encabezado.setOpaque(false);

        JLabel titulo =
            new JLabel("Movimiento de inventario");

        titulo.setForeground(AZUL_OSCURO);
        titulo.setFont(
            new Font("Segoe UI", Font.BOLD, 26)
        );

        JLabel descripcion =
            new JLabel(
                "Registra entradas, salidas y ajustes de existencias"
            );

        descripcion.setForeground(
            new Color(98, 114, 125)
        );

        encabezado.add(titulo);
        encabezado.add(Box.createVerticalStrut(6));
        encabezado.add(descripcion);

        return encabezado;
    }

    private JPanel crearFormulario(
            List<Producto> productos) {

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

        cmbProducto = new JComboBox<>();

        for (Producto producto : productos) {
            cmbProducto.addItem(producto);
        }

        cmbTipo =
            new JComboBox<>(TipoMovimiento.values());

        txtCantidad = new JTextField();

        txtMotivo = new JTextArea(4, 30);
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);

        lblStockActual =
            new JLabel("Stock actual: 0");

        lblStockActual.setForeground(AZUL_OSCURO);
        lblStockActual.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        cmbProducto.addActionListener(
            evento -> actualizarStockMostrado()
        );

        int fila = 0;

        agregarCampo(
            formulario,
            "Producto *",
            cmbProducto,
            fila++
        );

        agregarCampo(
            formulario,
            "Existencias",
            lblStockActual,
            fila++
        );

        agregarCampo(
            formulario,
            "Tipo de movimiento *",
            cmbTipo,
            fila++
        );

        agregarCampo(
            formulario,
            "Cantidad *",
            txtCantidad,
            fila++
        );

        agregarCampo(
            formulario,
            "Motivo *",
            new javax.swing.JScrollPane(txtMotivo),
            fila
        );

        return formulario;
    }

    private void agregarCampo(
            JPanel formulario,
            String texto,
            java.awt.Component componente,
            int fila) {

        JLabel etiqueta = new JLabel(texto);

        etiqueta.setForeground(TEXTO);
        etiqueta.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        GridBagConstraints restricciones =
            new GridBagConstraints();

        restricciones.gridx = 0;
        restricciones.gridy = fila;
        restricciones.anchor =
            GridBagConstraints.NORTHWEST;

        restricciones.insets =
            new Insets(6, 5, 14, 18);

        formulario.add(etiqueta, restricciones);

        restricciones.gridx = 1;
        restricciones.weightx = 1;
        restricciones.fill =
            GridBagConstraints.HORIZONTAL;

        restricciones.insets =
            new Insets(4, 5, 14, 5);

        componente.setPreferredSize(
            componente instanceof javax.swing.JScrollPane
                ? new Dimension(350, 85)
                : new Dimension(350, 38)
        );

        formulario.add(componente, restricciones);
    }

    private JPanel crearPanelBotones() {

        JPanel panel = new JPanel();

        panel.setLayout(
            new BoxLayout(panel, BoxLayout.X_AXIS)
        );

        panel.setOpaque(false);

        JButton btnCancelar =
            crearBoton(
                "Cancelar",
                new Color(98, 114, 125)
            );

        JButton btnRegistrar =
            crearBoton(
                "Registrar",
                AZUL_PRINCIPAL
            );

        btnCancelar.addActionListener(
            evento -> dispose()
        );

        btnRegistrar.addActionListener(
            evento -> confirmarFormulario()
        );

        panel.add(Box.createHorizontalGlue());
        panel.add(btnCancelar);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(btnRegistrar);

        return panel;
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

    private void actualizarStockMostrado() {

        if (cmbProducto == null
                || lblStockActual == null) {
            return;
        }

        Producto producto =
            (Producto) cmbProducto.getSelectedItem();

        int stock = producto == null
            ? 0
            : producto.getStockActual();

        lblStockActual.setText(
            "Stock actual: " + stock
        );
    }

    private void seleccionarProducto(
            int idProducto) {

        for (int posicion = 0;
                posicion < cmbProducto.getItemCount();
                posicion++) {

            Producto producto =
                cmbProducto.getItemAt(posicion);

            if (producto.getIdProducto() == idProducto) {
                cmbProducto.setSelectedIndex(posicion);
                return;
            }
        }
    }

    private void confirmarFormulario() {

        Producto producto =
            (Producto) cmbProducto.getSelectedItem();

        if (producto == null) {
            advertencia("Selecciona un producto.");
            return;
        }

        int cantidad;

        try {
            cantidad = Integer.parseInt(
                txtCantidad.getText().trim()
            );
        } catch (NumberFormatException error) {

            advertencia(
                "La cantidad debe ser un número entero."
            );

            txtCantidad.requestFocus();
            return;
        }

        if (cantidad <= 0) {
            advertencia(
                "La cantidad debe ser mayor que cero."
            );

            return;
        }

        TipoMovimiento tipo =
            (TipoMovimiento) cmbTipo.getSelectedItem();

        /*
         * Esta comprobación mejora la experiencia.
         * El DAO también evita el stock negativo.
         */
        boolean esSalida =
            tipo == TipoMovimiento.SALIDA
            || tipo == TipoMovimiento.AJUSTE_SALIDA;

        if (esSalida
                && cantidad > producto.getStockActual()) {

            advertencia(
                "La cantidad supera el stock disponible.\n"
                + "Stock actual: "
                + producto.getStockActual()
            );

            return;
        }

        if (txtMotivo.getText().isBlank()) {
            advertencia(
                "Ingresa el motivo del movimiento."
            );

            txtMotivo.requestFocus();
            return;
        }

        guardado = true;
        dispose();
    }

    public MovimientoInventario obtenerMovimiento() {

        Producto producto =
            (Producto) cmbProducto.getSelectedItem();

        return new MovimientoInventario(
            producto.getIdProducto(),
            idUsuario,
            (TipoMovimiento) cmbTipo.getSelectedItem(),
            Integer.parseInt(
                txtCantidad.getText().trim()
            ),
            txtMotivo.getText().trim()
        );
    }

    public boolean isGuardado() {
        return guardado;
    }

    private void advertencia(
            String mensaje) {

        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Movimiento de inventario",
            JOptionPane.WARNING_MESSAGE
        );
    }
}
