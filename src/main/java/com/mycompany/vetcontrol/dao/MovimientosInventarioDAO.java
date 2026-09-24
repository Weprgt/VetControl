/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.dao;

import com.mycompany.vetcontrol.modelo.MovimientoInventario;
import com.mycompany.vetcontrol.modelo.MovimientoInventario.TipoMovimiento;
import com.mycompany.vetcontrol.util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author weprg
 */
public class MovimientosInventarioDAO {

    /**
     * Consulta compartida para mostrar los datos del producto
     * y del usuario responsable.
     */
    private static final String CONSULTA_BASE = """
        SELECT
            mi.id_movimiento,
            mi.id_producto,
            mi.id_usuario,
            mi.tipo_movimiento,
            mi.cantidad,
            mi.motivo,
            mi.fecha_movimiento,
            p.codigo AS codigo_producto,
            p.nombre AS nombre_producto,
            u.nombre_completo AS nombre_usuario
        FROM movimientos_inventario mi
        INNER JOIN productos p
            ON p.id_producto = mi.id_producto
        INNER JOIN usuarios u
            ON u.id_usuario = mi.id_usuario
        """;

    /**
     * Registra el movimiento y actualiza el stock como una sola
     * operación mediante una transacción.
     *
     * @param movimiento movimiento que se desea registrar
     * @return true si ambas operaciones fueron exitosas
     */
    public boolean registrar(MovimientoInventario movimiento) {

        if (movimiento == null) {
            System.err.println(
                "El movimiento no puede ser nulo."
            );
            return false;
        }

        if (movimiento.getTipoMovimiento() == null) {
            System.err.println(
                "Debe seleccionar un tipo de movimiento."
            );
            return false;
        }

        if (movimiento.getCantidad() <= 0) {
            System.err.println(
                "La cantidad debe ser mayor que cero."
            );
            return false;
        }

        try (Connection conexion = ConexionBD.getConexion()) {

            /*
             * Guardamos el estado original para restaurarlo
             * antes de cerrar la conexión.
             */
            boolean autoCommitOriginal =
                conexion.getAutoCommit();

            conexion.setAutoCommit(false);

            try {
                int stockActual = obtenerStockBloqueado(
                    conexion,
                    movimiento.getIdProducto()
                );

                int nuevoStock = calcularNuevoStock(
                    stockActual,
                    movimiento.getCantidad(),
                    movimiento.getTipoMovimiento()
                );

                insertarMovimiento(
                    conexion,
                    movimiento
                );

                actualizarStock(
                    conexion,
                    movimiento.getIdProducto(),
                    nuevoStock
                );

                /*
                 * Confirma el INSERT y el UPDATE.
                 */
                conexion.commit();
                conexion.setAutoCommit(autoCommitOriginal);

                return true;

            } catch (SQLException | IllegalArgumentException error) {

                /*
                 * Si alguna operación falla, deshace todo.
                 */
                try {
                    conexion.rollback();
                } catch (SQLException errorRollback) {
                    System.err.println(
                        "No se pudo revertir la transacción: "
                        + errorRollback.getMessage()
                    );
                }

                try {
                    conexion.setAutoCommit(autoCommitOriginal);
                } catch (SQLException errorAutoCommit) {
                    System.err.println(
                        "No se pudo restaurar autoCommit: "
                        + errorAutoCommit.getMessage()
                    );
                }

                System.err.println(
                    "Error al registrar el movimiento: "
                    + error.getMessage()
                );

                return false;
            }

        } catch (SQLException error) {
            System.err.println(
                "Error de conexión al registrar el movimiento: "
                + error.getMessage()
            );
        }

        return false;
    }

    /**
     * Consulta y bloquea la fila del producto hasta terminar
     * la transacción.
     */
    private int obtenerStockBloqueado(
            Connection conexion,
            int idProducto) throws SQLException {

        String sql = """
            SELECT stock_actual
            FROM productos
            WHERE id_producto = ?
              AND activo = 1
            FOR UPDATE
            """;

        try (PreparedStatement sentencia =
                conexion.prepareStatement(sql)) {

            sentencia.setInt(1, idProducto);

            try (ResultSet resultado = sentencia.executeQuery()) {

                if (!resultado.next()) {
                    throw new SQLException(
                        "El producto no existe o está inactivo."
                    );
                }

                return resultado.getInt("stock_actual");
            }
        }
    }

    /**
     * Calcula el stock después del movimiento.
     */
    private int calcularNuevoStock(
            int stockActual,
            int cantidad,
            TipoMovimiento tipo) {

        int nuevoStock;

        switch (tipo) {
            case ENTRADA, AJUSTE_ENTRADA ->
                nuevoStock = stockActual + cantidad;

            case SALIDA, AJUSTE_SALIDA ->
                nuevoStock = stockActual - cantidad;

            default ->
                throw new IllegalArgumentException(
                    "Tipo de movimiento no válido."
                );
        }

        if (nuevoStock < 0) {
            throw new IllegalArgumentException(
                "Stock insuficiente. Existencias actuales: "
                + stockActual
            );
        }

        return nuevoStock;
    }

    /**
     * Inserta el movimiento utilizando la misma conexión
     * de la transacción.
     */
    private void insertarMovimiento(
            Connection conexion,
            MovimientoInventario movimiento)
            throws SQLException {

        String sql = """
            INSERT INTO movimientos_inventario (
                id_producto,
                id_usuario,
                tipo_movimiento,
                cantidad,
                motivo
            )
            VALUES (?, ?, ?, ?, ?)
            """;

        try (
            PreparedStatement sentencia = conexion.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
            )
        ) {

            sentencia.setInt(
                1,
                movimiento.getIdProducto()
            );

            sentencia.setInt(
                2,
                movimiento.getIdUsuario()
            );

            sentencia.setString(
                3,
                movimiento.getTipoMovimiento().name()
            );

            sentencia.setInt(
                4,
                movimiento.getCantidad()
            );

            asignarTextoOpcional(
                sentencia,
                5,
                movimiento.getMotivo()
            );

            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas == 0) {
                throw new SQLException(
                    "No se pudo insertar el movimiento."
                );
            }

            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (claves.next()) {
                    movimiento.setIdMovimiento(
                        claves.getInt(1)
                    );
                }
            }
        }
    }

    /**
     * Actualiza el stock utilizando la misma conexión.
     */
    private void actualizarStock(
            Connection conexion,
            int idProducto,
            int nuevoStock) throws SQLException {

        String sql = """
            UPDATE productos
            SET stock_actual = ?
            WHERE id_producto = ?
              AND activo = 1
            """;

        try (PreparedStatement sentencia =
                conexion.prepareStatement(sql)) {

            sentencia.setInt(1, nuevoStock);
            sentencia.setInt(2, idProducto);

            if (sentencia.executeUpdate() == 0) {
                throw new SQLException(
                    "No se pudo actualizar el stock."
                );
            }
        }
    }

    /**
     * Obtiene todos los movimientos, empezando por el más reciente.
     */
    public List<MovimientoInventario> listar() {

        List<MovimientoInventario> movimientos =
            new ArrayList<>();

        String sql = CONSULTA_BASE
            + " ORDER BY mi.fecha_movimiento DESC";

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery()
        ) {

            while (resultado.next()) {
                movimientos.add(
                    convertirMovimiento(resultado)
                );
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al listar movimientos: "
                + error.getMessage()
            );
        }

        return movimientos;
    }

    /**
     * Obtiene el historial de movimientos de un producto.
     */
    public List<MovimientoInventario> listarPorProducto(
            int idProducto) {

        List<MovimientoInventario> movimientos =
            new ArrayList<>();

        String sql = CONSULTA_BASE + """
            WHERE mi.id_producto = ?
            ORDER BY mi.fecha_movimiento DESC
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idProducto);

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    movimientos.add(
                        convertirMovimiento(resultado)
                    );
                }
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al consultar movimientos del producto: "
                + error.getMessage()
            );
        }

        return movimientos;
    }

    /**
     * Busca por código, producto, tipo, motivo o usuario.
     */
    public List<MovimientoInventario> buscar(String texto) {

        List<MovimientoInventario> movimientos =
            new ArrayList<>();

        String sql = CONSULTA_BASE + """
            WHERE (
                   p.codigo LIKE ?
                OR p.nombre LIKE ?
                OR mi.tipo_movimiento LIKE ?
                OR mi.motivo LIKE ?
                OR u.nombre_completo LIKE ?
            )
            ORDER BY mi.fecha_movimiento DESC
            """;

        String criterio = "%" + texto.trim() + "%";

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            for (int posicion = 1; posicion <= 5; posicion++) {
                sentencia.setString(posicion, criterio);
            }

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    movimientos.add(
                        convertirMovimiento(resultado)
                    );
                }
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al buscar movimientos: "
                + error.getMessage()
            );
        }

        return movimientos;
    }

    /**
     * Convierte una fila de MySQL en un movimiento.
     */
    private MovimientoInventario convertirMovimiento(
            ResultSet resultado) throws SQLException {

        MovimientoInventario movimiento =
            new MovimientoInventario();

        movimiento.setIdMovimiento(
            resultado.getInt("id_movimiento")
        );

        movimiento.setIdProducto(
            resultado.getInt("id_producto")
        );

        movimiento.setIdUsuario(
            resultado.getInt("id_usuario")
        );

        movimiento.setTipoMovimiento(
            TipoMovimiento.valueOf(
                resultado.getString("tipo_movimiento")
            )
        );

        movimiento.setCantidad(
            resultado.getInt("cantidad")
        );

        movimiento.setMotivo(
            resultado.getString("motivo")
        );

        movimiento.setFechaMovimiento(
            resultado
                .getTimestamp("fecha_movimiento")
                .toLocalDateTime()
        );

        movimiento.setCodigoProducto(
            resultado.getString("codigo_producto")
        );

        movimiento.setNombreProducto(
            resultado.getString("nombre_producto")
        );

        movimiento.setNombreUsuario(
            resultado.getString("nombre_usuario")
        );

        return movimiento;
    }

    /**
     * Guarda NULL cuando el motivo está vacío.
     */
    private void asignarTextoOpcional(
            PreparedStatement sentencia,
            int posicion,
            String texto) throws SQLException {

        if (texto == null || texto.isBlank()) {
            sentencia.setNull(posicion, Types.VARCHAR);
        } else {
            sentencia.setString(
                posicion,
                texto.trim()
            );
        }
    }
}
