/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.dao;

import com.mycompany.vetcontrol.modelo.Producto;
import com.mycompany.vetcontrol.modelo.Producto.Categoria;
import com.mycompany.vetcontrol.util.ConexionBD;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author weprg
 */
public class ProductoDAO {

    /**
     * Registra un producto nuevo con stock inicial cero.
     */
    public boolean insertar(Producto producto) {

        String sql = """
            INSERT INTO productos (
                codigo,
                nombre,
                categoria,
                descripcion,
                stock_actual,
                stock_minimo,
                precio_compra,
                precio_venta,
                fecha_vencimiento
            )
            VALUES (?, ?, ?, ?, 0, ?, ?, ?, ?)
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia = conexion.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
            )
        ) {

            sentencia.setString(
                1,
                producto.getCodigo().trim().toUpperCase()
            );

            sentencia.setString(
                2,
                producto.getNombre().trim()
            );

            sentencia.setString(
                3,
                producto.getCategoria().name()
            );

            asignarTextoOpcional(
                sentencia, 4, producto.getDescripcion()
            );

            sentencia.setInt(
                5,
                producto.getStockMinimo()
            );

            asignarDecimalOpcional(
                sentencia, 6, producto.getPrecioCompra()
            );

            asignarDecimalOpcional(
                sentencia, 7, producto.getPrecioVenta()
            );

            asignarFechaOpcional(
                sentencia, 8, producto.getFechaVencimiento()
            );

            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet claves = sentencia.getGeneratedKeys()) {
                    if (claves.next()) {
                        producto.setIdProducto(
                            claves.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException error) {
            mostrarErrorProducto(error);
        }

        return false;
    }

    /**
     * Obtiene todos los productos activos.
     */
    public List<Producto> listarActivos() {

        List<Producto> productos = new ArrayList<>();

        String sql = """
            SELECT
                id_producto,
                codigo,
                nombre,
                categoria,
                descripcion,
                stock_actual,
                stock_minimo,
                precio_compra,
                precio_venta,
                fecha_vencimiento,
                activo,
                fecha_registro
            FROM productos
            WHERE activo = 1
            ORDER BY nombre
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery()
        ) {

            while (resultado.next()) {
                productos.add(convertirProducto(resultado));
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al listar los productos: "
                + error.getMessage()
            );
        }

        return productos;
    }

    /**
     * Busca por código, nombre, categoría o descripción.
     */
    public List<Producto> buscar(String texto) {

        List<Producto> productos = new ArrayList<>();

        String sql = """
            SELECT
                id_producto,
                codigo,
                nombre,
                categoria,
                descripcion,
                stock_actual,
                stock_minimo,
                precio_compra,
                precio_venta,
                fecha_vencimiento,
                activo,
                fecha_registro
            FROM productos
            WHERE activo = 1
              AND (
                    codigo LIKE ?
                 OR nombre LIKE ?
                 OR categoria LIKE ?
                 OR descripcion LIKE ?
              )
            ORDER BY nombre
            """;

        String criterio = "%" + texto.trim() + "%";

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            for (int posicion = 1; posicion <= 4; posicion++) {
                sentencia.setString(posicion, criterio);
            }

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    productos.add(convertirProducto(resultado));
                }
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al buscar productos: "
                + error.getMessage()
            );
        }

        return productos;
    }

    /**
     * Busca un producto mediante su ID.
     */
    public Producto buscarPorId(int idProducto) {

        String sql = """
            SELECT
                id_producto,
                codigo,
                nombre,
                categoria,
                descripcion,
                stock_actual,
                stock_minimo,
                precio_compra,
                precio_venta,
                fecha_vencimiento,
                activo,
                fecha_registro
            FROM productos
            WHERE id_producto = ?
              AND activo = 1
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idProducto);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return convertirProducto(resultado);
                }
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al buscar el producto: "
                + error.getMessage()
            );
        }

        return null;
    }

    /**
     * Obtiene los productos que alcanzaron el stock mínimo.
     */
    public List<Producto> listarConStockBajo() {

        List<Producto> productos = new ArrayList<>();

        String sql = """
            SELECT
                id_producto,
                codigo,
                nombre,
                categoria,
                descripcion,
                stock_actual,
                stock_minimo,
                precio_compra,
                precio_venta,
                fecha_vencimiento,
                activo,
                fecha_registro
            FROM productos
            WHERE activo = 1
              AND stock_actual <= stock_minimo
            ORDER BY stock_actual, nombre
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery()
        ) {

            while (resultado.next()) {
                productos.add(convertirProducto(resultado));
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al consultar productos con stock bajo: "
                + error.getMessage()
            );
        }

        return productos;
    }

    /**
     * Obtiene productos vencidos o próximos a vencer.
     *
     * @param dias cantidad de días que se desean anticipar
     */
    public List<Producto> listarProximosAVencer(int dias) {

        List<Producto> productos = new ArrayList<>();

        String sql = """
            SELECT
                id_producto,
                codigo,
                nombre,
                categoria,
                descripcion,
                stock_actual,
                stock_minimo,
                precio_compra,
                precio_venta,
                fecha_vencimiento,
                activo,
                fecha_registro
            FROM productos
            WHERE activo = 1
              AND fecha_vencimiento IS NOT NULL
              AND fecha_vencimiento
                    <= DATE_ADD(CURDATE(), INTERVAL ? DAY)
            ORDER BY fecha_vencimiento
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, dias);

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    productos.add(convertirProducto(resultado));
                }
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al consultar productos por vencer: "
                + error.getMessage()
            );
        }

        return productos;
    }

    /**
     * Actualiza los datos generales del producto.
     *
     * No modifica stock_actual.
     */
    public boolean actualizar(Producto producto) {

        String sql = """
            UPDATE productos
            SET
                codigo = ?,
                nombre = ?,
                categoria = ?,
                descripcion = ?,
                stock_minimo = ?,
                precio_compra = ?,
                precio_venta = ?,
                fecha_vencimiento = ?
            WHERE id_producto = ?
              AND activo = 1
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setString(
                1,
                producto.getCodigo().trim().toUpperCase()
            );

            sentencia.setString(
                2,
                producto.getNombre().trim()
            );

            sentencia.setString(
                3,
                producto.getCategoria().name()
            );

            asignarTextoOpcional(
                sentencia, 4, producto.getDescripcion()
            );

            sentencia.setInt(
                5,
                producto.getStockMinimo()
            );

            asignarDecimalOpcional(
                sentencia, 6, producto.getPrecioCompra()
            );

            asignarDecimalOpcional(
                sentencia, 7, producto.getPrecioVenta()
            );

            asignarFechaOpcional(
                sentencia, 8, producto.getFechaVencimiento()
            );

            sentencia.setInt(
                9,
                producto.getIdProducto()
            );

            return sentencia.executeUpdate() > 0;

        } catch (SQLException error) {
            mostrarErrorProducto(error);
        }

        return false;
    }

    /**
     * Desactiva el producto sin eliminar sus movimientos.
     */
    public boolean desactivar(int idProducto) {

        String sql = """
            UPDATE productos
            SET activo = 0
            WHERE id_producto = ?
              AND activo = 1
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idProducto);

            return sentencia.executeUpdate() > 0;

        } catch (SQLException error) {
            System.err.println(
                "Error al desactivar el producto: "
                + error.getMessage()
            );
        }

        return false;
    }

    /**
     * Convierte una fila de MySQL en un Producto.
     */
    private Producto convertirProducto(ResultSet resultado)
            throws SQLException {

        Producto producto = new Producto();

        producto.setIdProducto(
            resultado.getInt("id_producto")
        );

        producto.setCodigo(
            resultado.getString("codigo")
        );

        producto.setNombre(
            resultado.getString("nombre")
        );

        producto.setCategoria(
            Categoria.valueOf(
                resultado.getString("categoria")
            )
        );

        producto.setDescripcion(
            resultado.getString("descripcion")
        );

        producto.setStockActual(
            resultado.getInt("stock_actual")
        );

        producto.setStockMinimo(
            resultado.getInt("stock_minimo")
        );

        producto.setPrecioCompra(
            resultado.getBigDecimal("precio_compra")
        );

        producto.setPrecioVenta(
            resultado.getBigDecimal("precio_venta")
        );

        Date fechaVencimiento =
            resultado.getDate("fecha_vencimiento");

        if (fechaVencimiento != null) {
            producto.setFechaVencimiento(
                fechaVencimiento.toLocalDate()
            );
        }

        producto.setActivo(
            resultado.getBoolean("activo")
        );

        if (resultado.getTimestamp("fecha_registro") != null) {
            producto.setFechaRegistro(
                resultado
                    .getTimestamp("fecha_registro")
                    .toLocalDateTime()
            );
        }

        return producto;
    }

    private void asignarTextoOpcional(
            PreparedStatement sentencia,
            int posicion,
            String texto) throws SQLException {

        if (texto == null || texto.isBlank()) {
            sentencia.setNull(posicion, Types.VARCHAR);
        } else {
            sentencia.setString(posicion, texto.trim());
        }
    }

    private void asignarDecimalOpcional(
            PreparedStatement sentencia,
            int posicion,
            BigDecimal valor) throws SQLException {

        if (valor == null) {
            sentencia.setNull(posicion, Types.DECIMAL);
        } else {
            sentencia.setBigDecimal(posicion, valor);
        }
    }

    private void asignarFechaOpcional(
            PreparedStatement sentencia,
            int posicion,
            LocalDate fecha) throws SQLException {

        if (fecha == null) {
            sentencia.setNull(posicion, Types.DATE);
        } else {
            sentencia.setDate(
                posicion,
                Date.valueOf(fecha)
            );
        }
    }

    private void mostrarErrorProducto(SQLException error) {

        if (error.getErrorCode() == 1062) {
            System.err.println(
                "Ya existe un producto con ese código."
            );
        } else {
            System.err.println(
                "Error al guardar el producto: "
                + error.getMessage()
            );
        }
    }
}
