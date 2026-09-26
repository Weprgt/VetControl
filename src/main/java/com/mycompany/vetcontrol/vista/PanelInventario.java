/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import com.mycompany.vetcontrol.dao.ProductoDAO;
import com.mycompany.vetcontrol.modelo.Producto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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


/**
 * Pantalla para consultar y administrar
 * los productos del inventario.
 *
 * @author weprg
 */
public class PanelInventario extends JPanel {

    // Colores de la aplicación
    private static final Color FONDO= new Color(244, 247, 250);
    private static final Color AZUL_OSCURO= new Color(23, 50, 77);
    private static final Color AZUL_PRINCIPAL= new Color(52, 120, 184);
    private static final Color TURQUESA= new Color(67, 166, 160);
    private static final Color NARANJA= new Color(232, 151, 48);
    private static final Color TEXTO= new Color(36, 50, 61);
    private static final Color TEXTO_SECUNDARIO= new Color(98, 114, 125);
    private static final Color BORDE= new Color(216, 225, 232);
    private static final Color AZUL_SELECCION= new Color(232, 242, 247);
    private static final Color FONDO_ALERTA= new Color(255, 247, 230);

    private JTable tablaInventario;
    private JTextField txtBuscar;
    
    // DAO para consultar los productos de MySQL.
    private final ProductoDAO productoDAO;

    // Modelo dinámico de la tabla.
    private DefaultTableModel modeloTabla;

    // Productos mostrados actualmente.
    private List<Producto> productosMostrados =
        new ArrayList<>();

    // Componentes utilizados por los eventos.
    private JButton btnBuscar;
    private JButton btnNuevo;
    private JButton btnMovimiento;
    private JLabel lblAlerta;

    // Formato de vencimiento.
    private static final DateTimeFormatter FORMATO_FECHA =
    DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PanelInventario() {

        productoDAO = new ProductoDAO();

        crearInterfaz();
        cargarProductos();
    }

    private void crearInterfaz() {
        setLayout(new BorderLayout(0, 25));
        setBackground(FONDO);
        setBorder(new EmptyBorder(40, 45, 40, 45));
        JPanel panelEncabezado= crearEncabezado();
        JPanel tarjetaInventario= crearTarjetaInventario();
        add(panelEncabezado, BorderLayout.NORTH);
        add(tarjetaInventario, BorderLayout.CENTER);
    }

    private JPanel crearEncabezado() {
        JPanel panelEncabezado = new JPanel();
        panelEncabezado.setLayout(new BoxLayout(panelEncabezado, BoxLayout.Y_AXIS));
        panelEncabezado.setOpaque(false);

        // Título
        JLabel lblTitulo= new JLabel("Inventario");
        lblTitulo.setForeground(AZUL_OSCURO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Descripción
        JLabel lblDescripcion = new JLabel("Control de productos, existencias y vencimientos");
        lblDescripcion.setForeground(TEXTO_SECUNDARIO);
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelEncabezado.add(lblTitulo);

        panelEncabezado.add(Box.createVerticalStrut(8));
        panelEncabezado.add(lblDescripcion);

        return panelEncabezado;
    }

    private JPanel crearTarjetaInventario() {
        JPanel tarjetaInventario = new JPanel(
            new BorderLayout(0, 20));

        tarjetaInventario.setBackground(Color.WHITE);

        tarjetaInventario.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1, true),
                new EmptyBorder(20, 32, 28, 32)));

        // Buscador y botones
        tarjetaInventario.add(crearSeccionBusqueda(), BorderLayout.NORTH);

        // Tabla de productos
        tarjetaInventario.add(crearTablaInventario(), BorderLayout.CENTER);

        // Alerta de stock y vencimientos
        tarjetaInventario.add(crearPanelAlertas(), BorderLayout.SOUTH);
        
        return tarjetaInventario;
    }

    private JPanel crearSeccionBusqueda() {
        JPanel seccionBusqueda = new JPanel(new BorderLayout(18, 8));

        seccionBusqueda.setBackground(Color.WHITE);

        // Etiqueta
        JLabel lblBuscar= new JLabel("Buscar producto");
        lblBuscar.setForeground(AZUL_OSCURO);
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 15));

        // Campo de búsqueda
        txtBuscar = new JTextField();
        txtBuscar.putClientProperty("JTextField.placeholderText", "Código, producto o categoría...");
        txtBuscar.setPreferredSize(new Dimension(300, 42));
        
        // buscar por medio de Enter
        txtBuscar.addActionListener(
            evento -> buscarProductos()
        );

        // Botones
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

        /*
         * No se escribe JButton porque los botones
         * ya son atributos de la clase.
         */
        btnBuscar = crearBoton(
            "Buscar",
            AZUL_PRINCIPAL,
            105
        );

        btnNuevo = crearBoton(
            "+ Producto",
            TURQUESA,
            115
        );

        btnMovimiento = crearBoton(
            "Movimiento",
            NARANJA,
            120
        );

        // Eventos.
        btnBuscar.addActionListener(
            evento -> buscarProductos()
        );

        btnNuevo.addActionListener(
            evento -> abrirNuevoProducto()
        );

        // Agregar botones.
        panelBotones.add(btnBuscar);
        panelBotones.add(
            Box.createHorizontalStrut(10)
        );

        panelBotones.add(btnNuevo);
        panelBotones.add(
            Box.createHorizontalStrut(10)
        );

        panelBotones.add(btnMovimiento);

        return panelBotones;
    }

    private JButton crearBoton(String texto, Color color,int ancho) {
        JButton boton = new JButton(texto);
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        
        // Dimensiones de los botones
        Dimension tamanoBoton= new Dimension(ancho, 42);
        boton.setMinimumSize(tamanoBoton);
        boton.setPreferredSize(tamanoBoton);
        boton.setMaximumSize(tamanoBoton);
        boton.putClientProperty("JComponent.minimumHeight", 42);
        boton.putClientProperty("JButton.buttonType", "borderless");

        return boton;
    }

    private JScrollPane crearTablaInventario() {

        String[] columnas = {
            "Código",
            "Producto",
            "Categoría",
            "Stock",
            "Mínimo",
            "Compra",
            "Venta",
            "Vencimiento"
        };

        /*
         * La tabla comienza vacía.
         * cargarProductos() colocará los datos de MySQL.
         */
        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna) {

                return false;
            }
        };

        tablaInventario = new JTable(modeloTabla);
        
        /*
        * El doble clic abre el producto seleccionado
        * para consultar o editar su información.
        */
        tablaInventario.addMouseListener(
            new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(
                        java.awt.event.MouseEvent evento) {

                    if (evento.getClickCount() == 2) {
                       abrirEditarProducto();
                    }
                }
            }
        );

        tablaInventario.setRowHeight(42);
        tablaInventario.setShowVerticalLines(false);
        tablaInventario.setShowHorizontalLines(true);
        tablaInventario.setGridColor(BORDE);

        tablaInventario.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        tablaInventario.setSelectionBackground(
            AZUL_SELECCION
        );

        tablaInventario.setSelectionForeground(TEXTO);

        tablaInventario.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        tablaInventario.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        tablaInventario.getTableHeader()
            .setBackground(AZUL_OSCURO);

        tablaInventario.getTableHeader()
            .setForeground(Color.WHITE);

        tablaInventario.getTableHeader()
            .setPreferredSize(new Dimension(0, 42));

        tablaInventario.getTableHeader()
            .setReorderingAllowed(false);

        /*
         * La tabla tiene varias columnas, por lo que permitimos
         * desplazamiento horizontal.
         */
        tablaInventario.setAutoResizeMode(
            JTable.AUTO_RESIZE_OFF
        );

        tablaInventario.getColumnModel()
            .getColumn(0).setPreferredWidth(100);

        tablaInventario.getColumnModel()
            .getColumn(1).setPreferredWidth(220);

        tablaInventario.getColumnModel()
            .getColumn(2).setPreferredWidth(130);

        tablaInventario.getColumnModel()
            .getColumn(3).setPreferredWidth(80);

        tablaInventario.getColumnModel()
            .getColumn(4).setPreferredWidth(80);

        tablaInventario.getColumnModel()
            .getColumn(5).setPreferredWidth(100);

        tablaInventario.getColumnModel()
            .getColumn(6).setPreferredWidth(100);

        tablaInventario.getColumnModel()
            .getColumn(7).setPreferredWidth(120);

        tablaInventario.setFillsViewportHeight(true);

        JScrollPane scroll =
            new JScrollPane(tablaInventario);

        scroll.setBorder(
            BorderFactory.createEmptyBorder()
        );

        return scroll;
    }

    private JPanel crearPanelAlertas() {
        JPanel panelAlertas = new JPanel(new BorderLayout());
        panelAlertas.setBackground(FONDO_ALERTA);
        panelAlertas.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(NARANJA, 1, true),
                new EmptyBorder(14, 18, 14, 18)));

        lblAlerta = new JLabel(
            "No existen alertas de inventario"
        );

        lblAlerta.setForeground(new Color(145, 91, 22));
        lblAlerta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelAlertas.add(lblAlerta, BorderLayout.CENTER);

        return panelAlertas;
    }
    
    /**
    * Obtiene los productos activos desde MySQL.
    */
   private void cargarProductos() {

       List<Producto> productos =
           productoDAO.listarActivos();

       mostrarProductos(productos);
       actualizarAlertas();
   }

   /**
    * Busca por código, nombre, categoría o descripción.
    */
   private void buscarProductos() {

       String criterio =
           txtBuscar.getText().trim();

       if (criterio.isBlank()) {
           cargarProductos();
           return;
       }

       List<Producto> productos =
           productoDAO.buscar(criterio);

       mostrarProductos(productos);
   }

    /**
    * Coloca los productos dentro de la tabla.
    */
    private void mostrarProductos(
           List<Producto> productos) {

       productosMostrados =
           new ArrayList<>(productos);

       modeloTabla.setRowCount(0);

       for (Producto producto : productosMostrados) {

           String vencimiento =
               producto.getFechaVencimiento() == null
                   ? "—"
                   : producto.getFechaVencimiento()
                       .format(FORMATO_FECHA);

           modeloTabla.addRow(new Object[] {
               producto.getCodigo(),
               producto.getNombre(),
               producto.getCategoria(),
               producto.getStockActual(),
               producto.getStockMinimo(),
               formatoPrecio(producto.getPrecioCompra()),
               formatoPrecio(producto.getPrecioVenta()),
               vencimiento
           });
       }
   }

    /**
    * Actualiza la alerta inferior con datos reales.
    */
    private void actualizarAlertas() {

       int cantidadStockBajo =
           productoDAO.listarConStockBajo().size();

       /*
        * Mostramos productos vencidos o que vencerán
        * durante los próximos 30 días.
        */
       int cantidadPorVencer =
           productoDAO.listarProximosAVencer(30).size();

       if (cantidadStockBajo == 0
               && cantidadPorVencer == 0) {

           lblAlerta.setText(
               "Inventario sin alertas"
           );

           return;
       }

       lblAlerta.setText(
           "⚠ "
           + cantidadStockBajo
           + " producto(s) con stock mínimo"
           + " · "
           + cantidadPorVencer
           + " producto(s) vencidos o próximos a vencer"
       );
   }

    /**
    * Muestra un precio vacío cuando su valor es null.
    */
    private String formatoPrecio(
           java.math.BigDecimal precio) {

        return precio == null
           ? "—"
           : "Q " + precio.toPlainString();
    }
    /**
    * Abre el formulario para registrar un producto.
    */
   private void abrirNuevoProducto() {

       Window ventana =
           SwingUtilities.getWindowAncestor(this);

       DlgProducto dialogo =
           new DlgProducto(
               ventana,
               null
           );

       dialogo.setVisible(true);

       if (!dialogo.isGuardado()) {
           return;
       }

       Producto producto =
           dialogo.obtenerProducto();

       if (productoDAO.insertar(producto)) {

           JOptionPane.showMessageDialog(
               this,
               "El producto fue registrado correctamente.",
               "Inventario",
               JOptionPane.INFORMATION_MESSAGE
           );

           cargarProductos();

       } else {

           JOptionPane.showMessageDialog(
               this,
               "No fue posible registrar el producto.\n"
               + "Comprueba que el código no esté repetido.",
               "Error",
               JOptionPane.ERROR_MESSAGE
           );
       }
   }

    /**
     * Abre el producto seleccionado para editarlo.
     */
    private void abrirEditarProducto() {

        int filaSeleccionada =
            tablaInventario.getSelectedRow();

        if (filaSeleccionada < 0) {

            JOptionPane.showMessageDialog(
                this,
                "Selecciona un producto.",
                "Inventario",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Producto productoSeleccionado =
            productosMostrados.get(filaSeleccionada);

        /*
         * Consulta nuevamente el producto para obtener
         * la información más reciente de MySQL.
         */
        Producto producto =
            productoDAO.buscarPorId(
                productoSeleccionado.getIdProducto()
            );

        if (producto == null) {

            JOptionPane.showMessageDialog(
                this,
                "No fue posible encontrar el producto.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        Window ventana =
            SwingUtilities.getWindowAncestor(this);

        DlgProducto dialogo =
            new DlgProducto(
                ventana,
                producto
            );

        dialogo.setVisible(true);

        if (!dialogo.isGuardado()) {
            return;
        }

        Producto productoActualizado =
            dialogo.obtenerProducto();

        if (productoDAO.actualizar(productoActualizado)) {

            JOptionPane.showMessageDialog(
                this,
                "El producto fue actualizado correctamente.",
                "Inventario",
                JOptionPane.INFORMATION_MESSAGE
            );

            cargarProductos();

        } else {

            JOptionPane.showMessageDialog(
                this,
                "No fue posible actualizar el producto.\n"
                + "Comprueba que el código no esté repetido.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
