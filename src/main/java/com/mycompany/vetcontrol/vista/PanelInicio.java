/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import com.mycompany.vetcontrol.dao.InicioDAO;
import com.mycompany.vetcontrol.modelo.Cita;
import com.mycompany.vetcontrol.modelo.ResumenInicio;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author weprg
 */

public class PanelInicio extends JPanel {

    // Paleta de colores de VetControl.
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

    private static final Color TEXTO_SECUNDARIO =
        new Color(98, 114, 125);

    private static final Color BORDE =
        new Color(216, 225, 232);

    // Acceso a los datos del resumen.
    private final InicioDAO inicioDAO;

    /*
     * Estas etiquetas se declaran como atributos
     * porque su texto cambiará al cargar los datos.
     */
    private JLabel lblCantidadClientes;
    private JLabel lblCantidadMascotas;
    private JLabel lblCantidadCitas;
    private JLabel lblCantidadStock;
    
    // Tabla que mostrará las próximas citas.
    private JTable tablaProximasCitas;
    private DefaultTableModel modeloTabla;
    

    public PanelInicio() {

        inicioDAO = new InicioDAO();

        crearInterfaz();
        cargarResumen();
        cargarProximasCitas();
    }

    /**
     * Construye los componentes del panel.
     */
    private void crearInterfaz() {

        setLayout(new BorderLayout(0, 25));
        setBackground(FONDO);
        setBorder(
            new EmptyBorder(40, 45, 40, 45)
        );

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearPanelResumen(), BorderLayout.CENTER);
    }

    /**
     * Crea el título y la descripción.
     */
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
            new JLabel("Inicio");

        lblTitulo.setForeground(AZUL_OSCURO);
        lblTitulo.setFont(
            new Font("Segoe UI", Font.BOLD, 30)
        );

        lblTitulo.setAlignmentX(
            Component.LEFT_ALIGNMENT
        );

        JLabel lblDescripcion = new JLabel(
            "Resumen general del sistema"
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

    /**
    * Crea las tarjetas y la tabla de próximas citas.
    */
   private JPanel crearPanelResumen() {

       /*
        * Este panel contiene toda la información
        * que aparece debajo del encabezado.
        */
       JPanel contenedor =
           new JPanel(new BorderLayout(0, 25));

       contenedor.setOpaque(false);

       // Panel horizontal con las cuatro tarjetas.
       JPanel panelTarjetas =
           new JPanel(new GridLayout(1, 4, 18, 0));

       panelTarjetas.setOpaque(false);

       lblCantidadClientes = crearEtiquetaCantidad();
       lblCantidadMascotas = crearEtiquetaCantidad();
       lblCantidadCitas = crearEtiquetaCantidad();
       lblCantidadStock = crearEtiquetaCantidad();

       panelTarjetas.add(
           crearTarjeta(
               "Clientes activos",
               "Propietarios registrados",
               lblCantidadClientes,
               AZUL_PRINCIPAL
           )
       );

       panelTarjetas.add(
           crearTarjeta(
               "Mascotas activas",
               "Pacientes registrados",
               lblCantidadMascotas,
               TURQUESA
           )
       );

       panelTarjetas.add(
           crearTarjeta(
               "Citas para hoy",
               "Programadas y confirmadas",
               lblCantidadCitas,
               NARANJA
           )
       );

       panelTarjetas.add(
           crearTarjeta(
               "Stock bajo",
               "Productos que requieren atención",
               lblCantidadStock,
               ROJO
           )
       );

       contenedor.add(
           panelTarjetas,
           BorderLayout.NORTH
       );

       contenedor.add(
           crearTarjetaProximasCitas(),
           BorderLayout.CENTER
       );

       return contenedor;
   }

    /**
     * Crea una tarjeta reutilizable.
     */
    private JPanel crearTarjeta(
            String titulo,
            String descripcion,
            JLabel lblCantidad,
            Color color) {

        JPanel tarjeta = new JPanel();

        tarjeta.setLayout(
            new BoxLayout(
                tarjeta,
                BoxLayout.Y_AXIS
            )
        );

        tarjeta.setBackground(Color.WHITE);

        tarjeta.setBorder(
            BorderFactory.createCompoundBorder(
                new MatteBorder(
                    0,
                    5,
                    0,
                    0,
                    color
                ),
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(
                        BORDE,
                        1
                    ),
                    new EmptyBorder(
                        22,
                        20,
                        22,
                        16
                    )
                )
            )
        );

        JLabel lblTitulo =
            new JLabel(titulo);

        lblTitulo.setForeground(AZUL_OSCURO);
        lblTitulo.setFont(
            new Font("Segoe UI", Font.BOLD, 16)
        );

        lblTitulo.setAlignmentX(
            Component.LEFT_ALIGNMENT
        );

        lblCantidad.setForeground(color);
        lblCantidad.setAlignmentX(
            Component.LEFT_ALIGNMENT
        );

        JLabel lblDescripcion =
            new JLabel(descripcion);

        lblDescripcion.setForeground(
            TEXTO_SECUNDARIO
        );

        lblDescripcion.setFont(
            new Font("Segoe UI", Font.PLAIN, 12)
        );

        lblDescripcion.setAlignmentX(
            Component.LEFT_ALIGNMENT
        );

        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(15));
        tarjeta.add(lblCantidad);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblDescripcion);

        return tarjeta;
    }

    /**
     * Crea una etiqueta preparada para mostrar
     * una cantidad dentro de una tarjeta.
     */
    private JLabel crearEtiquetaCantidad() {

        JLabel etiqueta = new JLabel("0");

        etiqueta.setFont(
            new Font("Segoe UI", Font.BOLD, 34)
        );

        return etiqueta;
    }
    
    /**
    * Crea la tarjeta blanca que contiene
    * la tabla de próximas citas.
    */
   private JPanel crearTarjetaProximasCitas() {

       JPanel tarjeta =
           new JPanel(new BorderLayout(0, 15));

       tarjeta.setBackground(Color.WHITE);

       tarjeta.setBorder(
           BorderFactory.createCompoundBorder(
               BorderFactory.createLineBorder(
                   BORDE,
                   1,
                   true
               ),
               new EmptyBorder(20, 24, 24, 24)
           )
       );

       JLabel lblTitulo =
           new JLabel("Próximas citas");

       lblTitulo.setForeground(AZUL_OSCURO);
       lblTitulo.setFont(
           new Font("Segoe UI", Font.BOLD, 18)
       );

       tarjeta.add(
           lblTitulo,
           BorderLayout.NORTH
       );

       tarjeta.add(
           crearTablaProximasCitas(),
           BorderLayout.CENTER
       );

       return tarjeta;
   }

    /**
     * Construye la tabla del panel de inicio.
     */
    private JScrollPane crearTablaProximasCitas() {

        String[] columnas = {
            "Código",
            "Fecha",
            "Hora",
            "Mascota",
            "Propietario",
            "Veterinario",
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

        tablaProximasCitas =
            new JTable(modeloTabla);

        tablaProximasCitas.setRowHeight(38);
        tablaProximasCitas.setShowVerticalLines(false);
        tablaProximasCitas.setShowHorizontalLines(true);
        tablaProximasCitas.setGridColor(BORDE);

        tablaProximasCitas.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        tablaProximasCitas.setSelectionBackground(
            new Color(232, 242, 247)
        );

        tablaProximasCitas.setSelectionForeground(
            new Color(36, 50, 61)
        );

        tablaProximasCitas.setFont(
            new Font("Segoe UI", Font.PLAIN, 13)
        );

        tablaProximasCitas.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 13)
        );

        tablaProximasCitas.getTableHeader().setBackground(
            AZUL_OSCURO
        );

        tablaProximasCitas.getTableHeader().setForeground(
            Color.WHITE
        );

        tablaProximasCitas.getTableHeader().setPreferredSize(
            new Dimension(0, 38)
        );

        tablaProximasCitas.setFillsViewportHeight(true);

        JScrollPane scroll =
            new JScrollPane(tablaProximasCitas);

        scroll.setBorder(
            BorderFactory.createEmptyBorder()
        );

        return scroll;
    }
    
    /**
    * Consulta y muestra las próximas citas.
    */
   private void cargarProximasCitas() {

       try {
           List<Cita> citas =
               inicioDAO.listarProximasCitas();

           modeloTabla.setRowCount(0);

           DateTimeFormatter formatoFecha =
               DateTimeFormatter.ofPattern("dd/MM/yyyy");

           DateTimeFormatter formatoHora =
               DateTimeFormatter.ofPattern("HH:mm");

           for (Cita cita : citas) {

               String fecha = "";
               String hora = "";

               if (cita.getFechaHora() != null) {

                   fecha = cita.getFechaHora()
                       .format(formatoFecha);

                   hora = cita.getFechaHora()
                       .format(formatoHora);
               }

               modeloTabla.addRow(
                   new Object[] {
                       cita.getCodigoVisible(),
                       fecha,
                       hora,
                       cita.getNombreMascota(),
                       cita.getNombrePropietario(),
                       cita.getNombreVeterinario(),
                       formatearEstado(cita.getEstado())
                   }
               );
           }

       } catch (SQLException error) {

           JOptionPane.showMessageDialog(
               this,
               "No fue posible cargar las próximas citas.\n"
                   + error.getMessage(),
               "Error de base de datos",
               JOptionPane.ERROR_MESSAGE
           );
       }
   }

   /**
    * Convierte PROGRAMADA en Programada
    * y NO_ASISTIO en No asistió.
    */
   private String formatearEstado(Cita.Estado estado) {

       if (estado == null) {
           return "";
       }

       return switch (estado) {
           case PROGRAMADA -> "Programada";
           case CONFIRMADA -> "Confirmada";
           case ATENDIDA -> "Atendida";
           case CANCELADA -> "Cancelada";
           case NO_ASISTIO -> "No asistió";
       };
   }

    /**
     * Consulta MySQL y actualiza las cantidades.
     */
    private void cargarResumen() {

        try {
            ResumenInicio resumen =
                inicioDAO.obtenerResumen();

            lblCantidadClientes.setText(
                String.valueOf(
                    resumen.getClientesActivos()
                )
            );

            lblCantidadMascotas.setText(
                String.valueOf(
                    resumen.getMascotasActivas()
                )
            );

            lblCantidadCitas.setText(
                String.valueOf(
                    resumen.getCitasHoy()
                )
            );

            lblCantidadStock.setText(
                String.valueOf(
                    resumen.getProductosStockBajo()
                )
            );

        } catch (SQLException error) {

            JOptionPane.showMessageDialog(
                this,
                "No fue posible cargar el resumen.\n"
                    + error.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
