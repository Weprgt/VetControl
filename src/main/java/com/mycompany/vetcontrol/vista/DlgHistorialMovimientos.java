/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.vista;

import com.mycompany.vetcontrol.modelo.MovimientoInventario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author weprg
 */

public class DlgHistorialMovimientos extends JDialog {

    private static final Color FONDO =
        new Color(244, 247, 250);

    private static final Color AZUL_OSCURO =
        new Color(23, 50, 77);

    private static final Color AZUL_PRINCIPAL =
        new Color(52, 120, 184);

    private static final Color BORDE =
        new Color(216, 225, 232);

    private static final DateTimeFormatter FORMATO_FECHA =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public DlgHistorialMovimientos(
            Window propietario,
            List<MovimientoInventario> movimientos) {

        super(
            propietario,
            "Historial de movimientos",
            ModalityType.APPLICATION_MODAL
        );

        configurarVentana();
        crearInterfaz(movimientos);
    }

    private void configurarVentana() {

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(950, 580);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(getOwner());
    }

    private void crearInterfaz(
            List<MovimientoInventario> movimientos) {

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
            crearTabla(movimientos),
            BorderLayout.CENTER
        );

        principal.add(
            crearPanelInferior(),
            BorderLayout.SOUTH
        );

        setContentPane(principal);
    }

    private JPanel crearEncabezado() {

        JPanel encabezado =
            new JPanel(new BorderLayout());

        encabezado.setOpaque(false);

        JLabel titulo =
            new JLabel("Historial de movimientos");

        titulo.setForeground(AZUL_OSCURO);
        titulo.setFont(
            new Font("Segoe UI", Font.BOLD, 26)
        );

        JLabel descripcion =
            new JLabel(
                "Entradas, salidas y ajustes registrados"
            );

        descripcion.setForeground(
            new Color(98, 114, 125)
        );

        encabezado.add(
            titulo,
            BorderLayout.NORTH
        );

        encabezado.add(
            descripcion,
            BorderLayout.SOUTH
        );

        return encabezado;
    }

    private JScrollPane crearTabla(
            List<MovimientoInventario> movimientos) {

        String[] columnas = {
            "Fecha",
            "Código",
            "Producto",
            "Tipo",
            "Cantidad",
            "Motivo",
            "Usuario"
        };

        DefaultTableModel modelo =
            new DefaultTableModel(columnas, 0) {

                @Override
                public boolean isCellEditable(
                        int fila,
                        int columna) {

                    return false;
                }
            };

        for (MovimientoInventario movimiento
                : movimientos) {

            String fecha =
                movimiento.getFechaMovimiento() == null
                    ? ""
                    : movimiento.getFechaMovimiento()
                        .format(FORMATO_FECHA);

            modelo.addRow(new Object[] {
                fecha,
                movimiento.getCodigoProducto(),
                movimiento.getNombreProducto(),
                movimiento.getTipoMovimiento(),
                movimiento.getCantidad(),
                movimiento.getMotivo(),
                movimiento.getNombreUsuario()
            });
        }

        JTable tabla =
            new JTable(modelo);

        tabla.setRowHeight(40);
        tabla.setShowVerticalLines(false);
        tabla.setShowHorizontalLines(true);
        tabla.setGridColor(BORDE);

        tabla.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        tabla.setSelectionBackground(
            new Color(232, 242, 247)
        );

        tabla.setSelectionForeground(
            new Color(36, 50, 61)
        );

        tabla.setFont(
            new Font("Segoe UI", Font.PLAIN, 13)
        );

        tabla.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 13)
        );

        tabla.getTableHeader()
            .setBackground(AZUL_OSCURO);

        tabla.getTableHeader()
            .setForeground(Color.WHITE);

        tabla.getTableHeader()
            .setPreferredSize(new Dimension(0, 42));

        tabla.getTableHeader()
            .setReorderingAllowed(false);

        tabla.setAutoResizeMode(
            JTable.AUTO_RESIZE_OFF
        );

        tabla.getColumnModel()
            .getColumn(0).setPreferredWidth(140);

        tabla.getColumnModel()
            .getColumn(1).setPreferredWidth(100);

        tabla.getColumnModel()
            .getColumn(2).setPreferredWidth(200);

        tabla.getColumnModel()
            .getColumn(3).setPreferredWidth(140);

        tabla.getColumnModel()
            .getColumn(4).setPreferredWidth(90);

        tabla.getColumnModel()
            .getColumn(5).setPreferredWidth(260);

        tabla.getColumnModel()
            .getColumn(6).setPreferredWidth(180);

        tabla.setFillsViewportHeight(true);

        JScrollPane scroll =
            new JScrollPane(tabla);

        scroll.setBorder(
            BorderFactory.createLineBorder(
                BORDE,
                1,
                true
            )
        );

        return scroll;
    }

    private JPanel crearPanelInferior() {

        JPanel panel =
            new JPanel(new BorderLayout());

        panel.setOpaque(false);

        JButton btnCerrar =
            new JButton("Cerrar");

        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBackground(AZUL_PRINCIPAL);
        btnCerrar.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        btnCerrar.setPreferredSize(
            new Dimension(120, 42)
        );

        btnCerrar.putClientProperty(
            "JButton.buttonType",
            "borderless"
        );

        btnCerrar.addActionListener(
            evento -> dispose()
        );

        panel.add(
            btnCerrar,
            BorderLayout.EAST
        );

        return panel;
    }
}
