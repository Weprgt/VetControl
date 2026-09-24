/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.dao;

import com.mycompany.vetcontrol.modelo.Rol;
import com.mycompany.vetcontrol.util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author weprg
 */
public class RolDAO {

    /**
     * Obtiene todos los roles registrados.
     *
     * Este método servirá para llenar un JComboBox cuando
     * construyamos la pantalla de administración de usuarios.
     *
     * @return lista de roles
     */
    public List<Rol> listar() {

        List<Rol> roles = new ArrayList<>();

        String sql = """
            SELECT
                id_rol,
                nombre,
                descripcion
            FROM roles
            ORDER BY nombre
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery()
        ) {

            while (resultado.next()) {
                roles.add(convertirRol(resultado));
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al listar los roles: "
                + error.getMessage()
            );
        }

        return roles;
    }

    /**
     * Busca un rol utilizando su identificador.
     *
     * @param idRol identificador del rol
     * @return rol encontrado o null si no existe
     */
    public Rol buscarPorId(int idRol) {

        String sql = """
            SELECT
                id_rol,
                nombre,
                descripcion
            FROM roles
            WHERE id_rol = ?
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idRol);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return convertirRol(resultado);
                }
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al buscar el rol por ID: "
                + error.getMessage()
            );
        }

        return null;
    }

    /**
     * Busca un rol mediante su nombre.
     *
     * Ejemplo:
     * buscarPorNombre("VETERINARIO")
     *
     * @param nombre nombre del rol
     * @return rol encontrado o null si no existe
     */
    public Rol buscarPorNombre(String nombre) {

        String sql = """
            SELECT
                id_rol,
                nombre,
                descripcion
            FROM roles
            WHERE nombre = ?
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setString(
                1,
                nombre.trim().toUpperCase()
            );

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return convertirRol(resultado);
                }
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al buscar el rol por nombre: "
                + error.getMessage()
            );
        }

        return null;
    }

    /**
     * Convierte una fila obtenida de MySQL en un objeto Rol.
     */
    private Rol convertirRol(ResultSet resultado)
            throws SQLException {

        return new Rol(
            resultado.getInt("id_rol"),
            resultado.getString("nombre"),
            resultado.getString("descripcion")
        );
    }
}
