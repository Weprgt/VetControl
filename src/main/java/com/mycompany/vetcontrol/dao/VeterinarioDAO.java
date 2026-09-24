/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.dao;

import com.mycompany.vetcontrol.modelo.Veterinario;
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
public class VeterinarioDAO {

    /**
     * Registra los datos profesionales de un veterinario.
     *
     * El usuario asociado debe existir previamente.
     */
    public boolean insertar(Veterinario veterinario) {

        String sql = """
            INSERT INTO veterinarios (
                id_usuario,
                especialidad,
                telefono_profesional
            )
            VALUES (?, ?, ?)
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia = conexion.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
            )
        ) {

            sentencia.setInt(
                1,
                veterinario.getIdUsuario()
            );

            asignarTextoOpcional(
                sentencia,
                2,
                veterinario.getEspecialidad()
            );

            asignarTextoOpcional(
                sentencia,
                3,
                veterinario.getTelefonoProfesional()
            );

            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet claves = sentencia.getGeneratedKeys()) {
                    if (claves.next()) {
                        veterinario.setIdVeterinario(
                            claves.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al insertar el veterinario: "
                + error.getMessage()
            );
        }

        return false;
    }

    /**
     * Obtiene los veterinarios activos junto con sus datos de usuario.
     */
    public List<Veterinario> listarActivos() {

        List<Veterinario> veterinarios = new ArrayList<>();

        String sql = """
            SELECT
                v.id_veterinario,
                v.id_usuario,
                v.especialidad,
                v.telefono_profesional,
                v.activo,
                v.fecha_registro,
                u.nombre_completo,
                u.nombre_usuario,
                u.correo
            FROM veterinarios v
            INNER JOIN usuarios u
                ON u.id_usuario = v.id_usuario
            WHERE v.activo = 1
              AND u.activo = 1
            ORDER BY u.nombre_completo
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery()
        ) {

            while (resultado.next()) {
                veterinarios.add(
                    convertirVeterinario(resultado)
                );
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al listar los veterinarios: "
                + error.getMessage()
            );
        }

        return veterinarios;
    }

    /**
     * Busca veterinarios por nombre, usuario, especialidad,
     * teléfono o correo.
     */
    public List<Veterinario> buscar(String texto) {

        List<Veterinario> veterinarios = new ArrayList<>();

        String sql = """
            SELECT
                v.id_veterinario,
                v.id_usuario,
                v.especialidad,
                v.telefono_profesional,
                v.activo,
                v.fecha_registro,
                u.nombre_completo,
                u.nombre_usuario,
                u.correo
            FROM veterinarios v
            INNER JOIN usuarios u
                ON u.id_usuario = v.id_usuario
            WHERE v.activo = 1
              AND u.activo = 1
              AND (
                    u.nombre_completo LIKE ?
                 OR u.nombre_usuario LIKE ?
                 OR u.correo LIKE ?
                 OR v.especialidad LIKE ?
                 OR v.telefono_profesional LIKE ?
              )
            ORDER BY u.nombre_completo
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
                    veterinarios.add(
                        convertirVeterinario(resultado)
                    );
                }
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al buscar veterinarios: "
                + error.getMessage()
            );
        }

        return veterinarios;
    }

    /**
     * Busca un veterinario mediante su identificador.
     */
    public Veterinario buscarPorId(int idVeterinario) {

        String sql = """
            SELECT
                v.id_veterinario,
                v.id_usuario,
                v.especialidad,
                v.telefono_profesional,
                v.activo,
                v.fecha_registro,
                u.nombre_completo,
                u.nombre_usuario,
                u.correo
            FROM veterinarios v
            INNER JOIN usuarios u
                ON u.id_usuario = v.id_usuario
            WHERE v.id_veterinario = ?
              AND v.activo = 1
              AND u.activo = 1
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idVeterinario);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return convertirVeterinario(resultado);
                }
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al buscar el veterinario: "
                + error.getMessage()
            );
        }

        return null;
    }

    /**
     * Busca los datos veterinarios asociados a un usuario.
     */
    public Veterinario buscarPorIdUsuario(int idUsuario) {

        String sql = """
            SELECT
                v.id_veterinario,
                v.id_usuario,
                v.especialidad,
                v.telefono_profesional,
                v.activo,
                v.fecha_registro,
                u.nombre_completo,
                u.nombre_usuario,
                u.correo
            FROM veterinarios v
            INNER JOIN usuarios u
                ON u.id_usuario = v.id_usuario
            WHERE v.id_usuario = ?
              AND v.activo = 1
              AND u.activo = 1
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idUsuario);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return convertirVeterinario(resultado);
                }
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al buscar el veterinario por usuario: "
                + error.getMessage()
            );
        }

        return null;
    }

    /**
     * Actualiza la información profesional del veterinario.
     *
     * Los datos personales se actualizan desde UsuarioDAO.
     */
    public boolean actualizar(Veterinario veterinario) {

        String sql = """
            UPDATE veterinarios
            SET
                especialidad = ?,
                telefono_profesional = ?
            WHERE id_veterinario = ?
              AND activo = 1
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            asignarTextoOpcional(
                sentencia,
                1,
                veterinario.getEspecialidad()
            );

            asignarTextoOpcional(
                sentencia,
                2,
                veterinario.getTelefonoProfesional()
            );

            sentencia.setInt(
                3,
                veterinario.getIdVeterinario()
            );

            return sentencia.executeUpdate() > 0;

        } catch (SQLException error) {
            System.err.println(
                "Error al actualizar el veterinario: "
                + error.getMessage()
            );
        }

        return false;
    }

    /**
     * Desactiva únicamente el registro profesional.
     *
     * La desactivación del usuario se realizará por separado
     * utilizando UsuarioDAO.
     */
    public boolean desactivar(int idVeterinario) {

        String sql = """
            UPDATE veterinarios
            SET activo = 0
            WHERE id_veterinario = ?
              AND activo = 1
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idVeterinario);

            return sentencia.executeUpdate() > 0;

        } catch (SQLException error) {
            System.err.println(
                "Error al desactivar el veterinario: "
                + error.getMessage()
            );
        }

        return false;
    }

    /**
     * Convierte una fila de MySQL en un objeto Veterinario.
     */
    private Veterinario convertirVeterinario(
            ResultSet resultado) throws SQLException {

        Veterinario veterinario = new Veterinario();

        veterinario.setIdVeterinario(
            resultado.getInt("id_veterinario")
        );

        veterinario.setIdUsuario(
            resultado.getInt("id_usuario")
        );

        veterinario.setEspecialidad(
            resultado.getString("especialidad")
        );

        veterinario.setTelefonoProfesional(
            resultado.getString("telefono_profesional")
        );

        veterinario.setActivo(
            resultado.getBoolean("activo")
        );

        if (resultado.getTimestamp("fecha_registro") != null) {
            veterinario.setFechaRegistro(
                resultado
                    .getTimestamp("fecha_registro")
                    .toLocalDateTime()
            );
        }

        veterinario.setNombreCompleto(
            resultado.getString("nombre_completo")
        );

        veterinario.setNombreUsuario(
            resultado.getString("nombre_usuario")
        );

        veterinario.setCorreo(
            resultado.getString("correo")
        );

        return veterinario;
    }

    /**
     * Envía NULL cuando un campo opcional está vacío.
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
