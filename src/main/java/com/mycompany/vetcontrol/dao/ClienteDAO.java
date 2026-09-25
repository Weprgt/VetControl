/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.dao;

import com.mycompany.vetcontrol.modelo.Cliente;
import com.mycompany.vetcontrol.util.ConexionBD;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author weprg
 */
public class ClienteDAO {

    /**
     * Guarda un cliente nuevo.
     *
     * @return true si el cliente fue insertado
     */
    public boolean insertar(Cliente cliente) {

        String sql =
            "INSERT INTO clientes "
            + "(nombres, apellidos, telefono, "
            + "correo, direccion) "
            + "VALUES (?, ?, ?, ?, ?)";

        try (
            Connection conexion =
                ConexionBD.getConexion();

            PreparedStatement sentencia =
                conexion.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
                )
        ) {

            sentencia.setString(
                1,
                cliente.getNombres().trim()
            );

            sentencia.setString(
                2,
                cliente.getApellidos().trim()
            );

            sentencia.setString(
                3,
                cliente.getTelefono().trim()
            );

            establecerTextoNullable(
                sentencia,
                4,
                cliente.getCorreo()
            );

            establecerTextoNullable(
                sentencia,
                5,
                cliente.getDireccion()
            );

            int filasAfectadas =
                sentencia.executeUpdate();

            /*
             * Recupera el ID AUTO_INCREMENT
             * generado por MySQL.
             */
            if (filasAfectadas > 0) {

                try (ResultSet claves =
                        sentencia.getGeneratedKeys()) {

                    if (claves.next()) {
                        cliente.setIdCliente(
                            claves.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException error) {
            mostrarError(
                "insertar",
                error
            );
        }

        return false;
    }

    /**
     * Devuelve todos los clientes activos.
     */
    public List<Cliente> listarActivos() {

        List<Cliente> clientes =
            new ArrayList<>();

        String sql =
            "SELECT id_cliente, nombres, apellidos, "
            + "telefono, correo, direccion, activo, "
            + "fecha_registro "
            + "FROM clientes "
            + "WHERE activo = TRUE "
            + "ORDER BY nombres, apellidos";

        try (
            Connection conexion =
                ConexionBD.getConexion();

            PreparedStatement sentencia =
                conexion.prepareStatement(sql);

            ResultSet resultado =
                sentencia.executeQuery()
        ) {

            while (resultado.next()) {
                clientes.add(
                    convertirEnCliente(resultado)
                );
            }

        } catch (SQLException error) {
            mostrarError(
                "listar",
                error
            );
        }

        return clientes;
    }

    /**
     * Busca clientes activos por código visible,
     * nombre, teléfono o correo.
     */
    public List<Cliente> buscar(String criterio) {

        List<Cliente> clientes =
            new ArrayList<>();

        String texto =
            criterio == null
                ? ""
                : criterio.trim();

        /*
         * Convierte C-0003 en 3 para buscar
         * directamente por id_cliente.
         */
        String posibleId =
            extraerIdDesdeCodigo(texto);

        String sql =
            "SELECT id_cliente, nombres, apellidos, "
            + "telefono, correo, direccion, activo, "
            + "fecha_registro "
            + "FROM clientes "
            + "WHERE activo = TRUE "
            + "AND (CAST(id_cliente AS CHAR) = ? "
            + "OR CONCAT(nombres, ' ', apellidos) LIKE ? "
            + "OR telefono LIKE ? "
            + "OR correo LIKE ?) "
            + "ORDER BY nombres, apellidos";

        String patron =
            "%" + texto + "%";

        try (
            Connection conexion =
                ConexionBD.getConexion();

            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setString(1, posibleId);
            sentencia.setString(2, patron);
            sentencia.setString(3, patron);
            sentencia.setString(4, patron);

            try (
                ResultSet resultado =
                    sentencia.executeQuery()
            ) {

                while (resultado.next()) {
                    clientes.add(
                        convertirEnCliente(resultado)
                    );
                }
            }

        } catch (SQLException error) {
            mostrarError(
                "buscar",
                error
            );
        }

        return clientes;
    }

    /**
     * Actualiza los datos editables de un cliente.
     */
    public boolean actualizar(Cliente cliente) {

        String sql =
            "UPDATE clientes "
            + "SET nombres = ?, "
            + "apellidos = ?, "
            + "telefono = ?, "
            + "correo = ?, "
            + "direccion = ? "
            + "WHERE id_cliente = ? "
            + "AND activo = TRUE";

        try (
            Connection conexion =
                ConexionBD.getConexion();

            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setString(
                1,
                cliente.getNombres().trim()
            );

            sentencia.setString(
                2,
                cliente.getApellidos().trim()
            );

            sentencia.setString(
                3,
                cliente.getTelefono().trim()
            );

            establecerTextoNullable(
                sentencia,
                4,
                cliente.getCorreo()
            );

            establecerTextoNullable(
                sentencia,
                5,
                cliente.getDireccion()
            );

            sentencia.setInt(
                6,
                cliente.getIdCliente()
            );

            return sentencia.executeUpdate() > 0;

        } catch (SQLException error) {
            mostrarError(
                "actualizar",
                error
            );
        }

        return false;
    }

    /**
     * Desactiva un cliente sin eliminar su historial.
     */
    public boolean desactivar(int idCliente) {

        String sql =
            "UPDATE clientes "
            + "SET activo = FALSE "
            + "WHERE id_cliente = ? "
            + "AND activo = TRUE";

        try (
            Connection conexion =
                ConexionBD.getConexion();

            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(
                1,
                idCliente
            );

            return sentencia.executeUpdate() > 0;

        } catch (SQLException error) {
            mostrarError(
                "desactivar",
                error
            );
        }

        return false;
    }

    /**
     * Convierte una fila del ResultSet
     * en un objeto Cliente.
     *
     * Este método sí puede declarar SQLException porque
     * se ejecuta dentro de los try/catch del DAO.
     */
    private Cliente convertirEnCliente(
            ResultSet resultado)
            throws SQLException {

        Timestamp fecha =
            resultado.getTimestamp(
                "fecha_registro"
            );

        return new Cliente(
            resultado.getInt("id_cliente"),
            resultado.getString("nombres"),
            resultado.getString("apellidos"),
            resultado.getString("telefono"),
            resultado.getString("correo"),
            resultado.getString("direccion"),
            resultado.getBoolean("activo"),
            fecha == null
                ? null
                : fecha.toLocalDateTime()
        );
    }

    /**
     * Guarda NULL cuando el texto está vacío.
     */
    private void establecerTextoNullable(
            PreparedStatement sentencia,
            int posicion,
            String texto)
            throws SQLException {

        if (texto == null
                || texto.isBlank()) {

            sentencia.setNull(
                posicion,
                Types.VARCHAR
            );

        } else {
            sentencia.setString(
                posicion,
                texto.trim()
            );
        }
    }

    /**
     * Obtiene el número de un código como C-0003.
     */
    private String extraerIdDesdeCodigo(
            String codigo) {

        String texto =
            codigo.toUpperCase();

        if (texto.startsWith("C-")) {
            texto = texto.substring(2);
        }

        if (texto.matches("\\d+")) {
            return String.valueOf(
                Integer.parseInt(texto)
            );
        }

        return "-1";
    }

    /**
     * Centraliza los mensajes de error del DAO.
     */
    private void mostrarError(
            String operacion,
            SQLException error) {

        if (error.getErrorCode() == 1062) {
            System.err.println(
                "No se pudo " + operacion
                + " el cliente: el correo ya está registrado."
            );

        } else {
            System.err.println(
                "Error al " + operacion
                + " clientes: "
                + error.getMessage()
            );
        }
    }
}