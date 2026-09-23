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
    public boolean insertar(Cliente cliente)
            throws SQLException {

        String sql =
            "INSERT INTO clientes "
            + "(nombres, apellidos, telefono, "
            + "correo, direccion) "
            + "VALUES (?, ?, ?, ?, ?)";

        try (
            Connection conexion =
                ConexionBD.obtenerConexion();

            PreparedStatement sentencia =
                conexion.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
                )
        ) {

            sentencia.setString(
                1,
                cliente.getNombres()
            );

            sentencia.setString(
                2,
                cliente.getApellidos()
            );

            sentencia.setString(
                3,
                cliente.getTelefono()
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
             * Recuperar el ID AUTO_INCREMENT
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

            return false;
        }
    }

    /**
     * Devuelve todos los clientes activos.
     */
    public List<Cliente> listarActivos()
            throws SQLException {

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
                ConexionBD.obtenerConexion();

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
        }

        return clientes;
    }

    /**
     * Busca clientes activos por código visible,
     * nombre, teléfono o correo.
     */
    public List<Cliente> buscar(String criterio)
            throws SQLException {

        List<Cliente> clientes =
            new ArrayList<>();

        String texto =
            criterio == null
                ? ""
                : criterio.trim();

        /*
         * Convierte C-0003 en 3 para poder buscar
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

        String patron = "%" + texto + "%";

        try (
            Connection conexion =
                ConexionBD.obtenerConexion();

            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setString(1, posibleId);
            sentencia.setString(2, patron);
            sentencia.setString(3, patron);
            sentencia.setString(4, patron);

            try (ResultSet resultado =
                    sentencia.executeQuery()) {

                while (resultado.next()) {
                    clientes.add(
                        convertirEnCliente(resultado)
                    );
                }
            }
        }

        return clientes;
    }

    /**
     * Actualiza los datos editables de un cliente.
     */
    public boolean actualizar(Cliente cliente)
            throws SQLException {

        String sql =
            "UPDATE clientes "
            + "SET nombres = ?, "
            + "apellidos = ?, "
            + "telefono = ?, "
            + "correo = ?, "
            + "direccion = ? "
            + "WHERE id_cliente = ?";

        try (
            Connection conexion =
                ConexionBD.obtenerConexion();

            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setString(
                1,
                cliente.getNombres()
            );

            sentencia.setString(
                2,
                cliente.getApellidos()
            );

            sentencia.setString(
                3,
                cliente.getTelefono()
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
        }
    }

    /**
     * Desactiva un cliente sin eliminar su historial.
     */
    public boolean desactivar(int idCliente)
            throws SQLException {

        String sql =
            "UPDATE clientes "
            + "SET activo = FALSE "
            + "WHERE id_cliente = ? "
            + "AND activo = TRUE";

        try (
            Connection conexion =
                ConexionBD.obtenerConexion();

            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idCliente);

            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Convierte una fila del ResultSet
     * en un objeto Cliente.
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
     *
     * Esto evita almacenar cadenas vacías en
     * columnas opcionales como correo y dirección.
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

        /*
         * Solo devuelve el contenido cuando
         * está formado completamente por números.
         */
        if (texto.matches("\\d+")) {
            return String.valueOf(
                Integer.parseInt(texto)
            );
        }

        return "-1";
    }
}
