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
            Connection conexion =
                ConexionBD.getConexion();

            PreparedStatement sentencia =
                conexion.prepareStatement(sql);

            ResultSet resultado =
                sentencia.executeQuery()
        ) {

            while (resultado.next()) {

                Rol rol = new Rol(
                    resultado.getInt("id_rol"),
                    resultado.getString("nombre"),
                    resultado.getString("descripcion")
                );

                roles.add(rol);
            }

        } catch (SQLException error) {

            System.err.println(
                "Error al listar los roles: "
                + error.getMessage()
            );
        }

        return roles;
    }
}