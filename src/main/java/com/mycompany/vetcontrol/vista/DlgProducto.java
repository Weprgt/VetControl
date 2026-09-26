package com.mycompany.vetcontrol.vista;


import com.github.lgooddatepicker.components.DatePicker;
import com.mycompany.vetcontrol.modelo.Producto;
import com.mycompany.vetcontrol.modelo.Producto.Categoria;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.math.BigDecimal;
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author weprg
 */
public class DlgProducto extends JDialog {

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

    private final Producto productoOriginal;

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JComboBox<Categoria> cmbCategoria;
    private JTextArea txtDescripcion;
    private JTextField txtStockMinimo;
    private JTextField txtPrecioCompra;
    private JTextField txtPrecioVenta;
    private DatePicker dpVencimiento;

    private boolean guardado;

    public DlgProducto(
            Window propietario,
            Producto producto) {

        super(
            propietario,
            producto == null
                ? "Nuevo producto"
                : "Editar producto",
            ModalityType.APPLICATION_MODAL
        );

        this.productoOriginal = producto;

        configurarVentana();
        crearInterfaz();

        if (producto != null) {
            cargarProducto(producto);
        }
    }

    private void configurarVentana() {

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(680, 650);
        setMinimumSize(new Dimension(620, 580));
        setLocationRelativeTo(getOwner());
    }

    private void crearInterfaz() {

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

        JScrollPane scroll =
            new JScrollPane(crearFormulario());

        scroll.setBorder(
            BorderFactory.createEmptyBorder()
        );

        scroll.getVerticalScrollBar()
            .setUnitIncrement(16);

        panelPrincipal.add(
            scroll,
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
            productoOriginal == null
                ? "Nuevo producto"
                : "Editar producto"
        );

        lblTitulo.setForeground(AZUL_OSCURO);
        lblTitulo.setFont(
            new Font("Segoe UI", Font.BOLD, 26)
        );

        JLabel lblDescripcion = new JLabel(
            "Información general del producto"
        );

        lblDescripcion.setForeground(
            new Color(98, 114, 125)
        );

        encabezado.add(lblTitulo);
        encabezado.add(Box.createVerticalStrut(6));
        encabezado.add(lblDescripcion);

        return encabezado;
    }

    private JPanel crearFormulario() {

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

        txtCodigo = new JTextField();
        txtNombre = new JTextField();

        cmbCategoria =
            new JComboBox<>(Categoria.values());

        txtDescripcion = new JTextArea(3, 30);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        txtStockMinimo = new JTextField("0");
        txtPrecioCompra = new JTextField();
        txtPrecioVenta = new JTextField();

        dpVencimiento = new DatePicker();

        int fila = 0;

        agregarCampo(
            formulario,
            "Código *",
            txtCodigo,
            fila++
        );

        agregarCampo(
            formulario,
            "Nombre *",
            txtNombre,
            fila++
        );

        agregarCampo(
            formulario,
            "Categoría *",
            cmbCategoria,
            fila++
        );

        agregarCampo(
            formulario,
            "Descripción",
            new JScrollPane(txtDescripcion),
            fila++
        );

        agregarCampo(
            formulario,
            "Stock mínimo *",
            txtStockMinimo,
            fila++
        );

        agregarCampo(
            formulario,
            "Precio de compra",
            txtPrecioCompra,
            fila++
        );

        agregarCampo(
            formulario,
            "Precio de venta",
            txtPrecioVenta,
            fila++
        );

        agregarCampo(
            formulario,
            "Vencimiento",
            dpVencimiento,
            fila
        );

        return formulario;
    }

    private void agregarCampo(
            JPanel formulario,
            String textoEtiqueta,
            java.awt.Component componente,
            int fila) {

        JLabel etiqueta =
            new JLabel(textoEtiqueta);

        etiqueta.setForeground(TEXTO);
        etiqueta.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

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
                ? new Dimension(380, 75)
                : new Dimension(380, 38)
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

        JButton btnGuardar =
            crearBoton(
                "Guardar",
                AZUL_PRINCIPAL
            );

        btnCancelar.addActionListener(
            evento -> dispose()
        );

        btnGuardar.addActionListener(
            evento -> confirmarFormulario()
        );

        panel.add(Box.createHorizontalGlue());
        panel.add(btnCancelar);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(btnGuardar);

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

    private void cargarProducto(
            Producto producto) {

        txtCodigo.setText(producto.getCodigo());
        txtNombre.setText(producto.getNombre());

        cmbCategoria.setSelectedItem(
            producto.getCategoria()
        );

        txtDescripcion.setText(
            textoSeguro(producto.getDescripcion())
        );

        txtStockMinimo.setText(
            String.valueOf(producto.getStockMinimo())
        );

        txtPrecioCompra.setText(
            producto.getPrecioCompra() == null
                ? ""
                : producto.getPrecioCompra().toPlainString()
        );

        txtPrecioVenta.setText(
            producto.getPrecioVenta() == null
                ? ""
                : producto.getPrecioVenta().toPlainString()
        );

        if (producto.getFechaVencimiento() != null) {
            dpVencimiento.setDate(
                producto.getFechaVencimiento()
            );
        }
    }

    private void confirmarFormulario() {

        if (txtCodigo.getText().isBlank()) {
            advertencia("Ingresa el código del producto.");
            txtCodigo.requestFocus();
            return;
        }

        if (txtNombre.getText().isBlank()) {
            advertencia("Ingresa el nombre del producto.");
            txtNombre.requestFocus();
            return;
        }

        int stockMinimo;

        try {
            stockMinimo = Integer.parseInt(
                txtStockMinimo.getText().trim()
            );
        } catch (NumberFormatException error) {
            advertencia(
                "El stock mínimo debe ser un número entero."
            );

            txtStockMinimo.requestFocus();
            return;
        }

        if (stockMinimo < 0) {
            advertencia(
                "El stock mínimo no puede ser negativo."
            );

            return;
        }

        BigDecimal precioCompra;

        try {
            precioCompra = obtenerDecimalOpcional(
                txtPrecioCompra.getText()
            );
        } catch (NumberFormatException error) {
            advertencia(
                "El precio de compra no es válido."
            );

            txtPrecioCompra.requestFocus();
            return;
        }

        BigDecimal precioVenta;

        try {
            precioVenta = obtenerDecimalOpcional(
                txtPrecioVenta.getText()
            );
        } catch (NumberFormatException error) {
            advertencia(
                "El precio de venta no es válido."
            );

            txtPrecioVenta.requestFocus();
            return;
        }

        if (precioCompra != null
                && precioCompra.signum() < 0) {

            advertencia(
                "El precio de compra no puede ser negativo."
            );

            return;
        }

        if (precioVenta != null
                && precioVenta.signum() < 0) {

            advertencia(
                "El precio de venta no puede ser negativo."
            );

            return;
        }

        guardado = true;
        dispose();
    }

    public Producto obtenerProducto() {

        Producto producto = new Producto(
            txtCodigo.getText().trim().toUpperCase(),
            txtNombre.getText().trim(),
            (Categoria) cmbCategoria.getSelectedItem(),
            textoOpcional(txtDescripcion.getText()),
            Integer.parseInt(
                txtStockMinimo.getText().trim()
            ),
            obtenerDecimalOpcional(
                txtPrecioCompra.getText()
            ),
            obtenerDecimalOpcional(
                txtPrecioVenta.getText()
            ),
            dpVencimiento.getDate()
        );

        if (productoOriginal != null) {

            producto.setIdProducto(
                productoOriginal.getIdProducto()
            );

            /*
             * El stock no se modifica desde este formulario.
             * Se conserva el valor actual.
             */
            producto.setStockActual(
                productoOriginal.getStockActual()
            );

            producto.setActivo(
                productoOriginal.isActivo()
            );
        }

        return producto;
    }

    public boolean isGuardado() {
        return guardado;
    }

    private BigDecimal obtenerDecimalOpcional(
            String texto) {

        if (texto == null || texto.isBlank()) {
            return null;
        }

        return new BigDecimal(
            texto.trim().replace(",", ".")
        );
    }

    private String textoOpcional(
            String texto) {

        if (texto == null || texto.isBlank()) {
            return null;
        }

        return texto.trim();
    }

    private String textoSeguro(
            String texto) {

        return texto == null
            ? ""
            : texto;
    }

    private void advertencia(
            String mensaje) {

        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Datos incompletos",
            JOptionPane.WARNING_MESSAGE
        );
    }
}
