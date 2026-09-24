/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.dao;

import com.mycompany.vetcontrol.modelo.Usuario;
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
public class UsuarioDAO {

    /**
     * Registra un usuario nuevo.
     *
     * La contraseña recibida ya debe estar convertida en un hash.
     */
    public boolean insertar(Usuario usuario) {

        String sql = """
            INSERT INTO usuarios (
                id_rol,
                nombre_completo,
                nombre_usuario,
                contrasena_hash,
                correo
            )
            VALUES (?, ?, ?, ?, ?)
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia = conexion.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
            )
        ) {

            sentencia.setInt(1, usuario.getIdRol());
            sentencia.setString(
                2,
                usuario.getNombreCompleto().trim()
            );
            sentencia.setString(
                3,
                usuario.getNombreUsuario().trim()
            );
            sentencia.setString(
                4,
                usuario.getContrasenaHash()
            );

            asignarCorreo(
                sentencia,
                5,
                usuario.getCorreo()
            );

            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet claves = sentencia.getGeneratedKeys()) {
                    if (claves.next()) {
                        usuario.setIdUsuario(claves.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al insertar el usuario: "
                + error.getMessage()
            );
        }

        return false;
    }

    /**
     * Obtiene todos los usuarios activos junto con su rol.
     */
    public List<Usuario> listarActivos() {

        List<Usuario> usuarios = new ArrayList<>();

        String sql = """
            SELECT
                u.id_usuario,
                u.id_rol,
                u.nombre_completo,
                u.nombre_usuario,
                u.contrasena_hash,
                u.correo,
                u.activo,
                u.fecha_creacion,
                r.nombre AS nombre_rol
            FROM usuarios u
            INNER JOIN roles r
                ON r.id_rol = u.id_rol
            WHERE u.activo = 1
            ORDER BY u.nombre_completo
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery()
        ) {

            while (resultado.next()) {
                usuarios.add(convertirUsuario(resultado));
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al listar los usuarios: "
                + error.getMessage()
            );
        }

        return usuarios;
    }

    /**
     * Busca usuarios por nombre, nombre de usuario,
     * correo o nombre del rol.
     */
    public List<Usuario> buscar(String texto) {

        List<Usuario> usuarios = new ArrayList<>();

        String sql = """
            SELECT
                u.id_usuario,
                u.id_rol,
                u.nombre_completo,
                u.nombre_usuario,
                u.contrasena_hash,
                u.correo,
                u.activo,
                u.fecha_creacion,
                r.nombre AS nombre_rol
            FROM usuarios u
            INNER JOIN roles r
                ON r.id_rol = u.id_rol
            WHERE u.activo = 1
              AND (
                    u.nombre_completo LIKE ?
                 OR u.nombre_usuario LIKE ?
                 OR u.correo LIKE ?
                 OR r.nombre LIKE ?
              )
            ORDER BY u.nombre_completo
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
                    usuarios.add(convertirUsuario(resultado));
                }
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al buscar usuarios: "
                + error.getMessage()
            );
        }

        return usuarios;
    }

    /**
     * Busca un usuario mediante su nombre de usuario.
     *
     * Este método se utilizará posteriormente durante
     * el inicio de sesión.
     */
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {

        String sql = """
            SELECT
                u.id_usuario,
                u.id_rol,
                u.nombre_completo,
                u.nombre_usuario,
                u.contrasena_hash,
                u.correo,
                u.activo,
                u.fecha_creacion,
                r.nombre AS nombre_rol
            FROM usuarios u
            INNER JOIN roles r
                ON r.id_rol = u.id_rol
            WHERE u.nombre_usuario = ?
              AND u.activo = 1
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setString(
                1,
                nombreUsuario.trim()
            );

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return convertirUsuario(resultado);
                }
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al buscar el usuario: "
                + error.getMessage()
            );
        }

        return null;
    }

    /**
     * Actualiza la información general de un usuario.
     *
     * La contraseña no se modifica aquí.
     */
    public boolean actualizar(Usuario usuario) {

        String sql = """
            UPDATE usuarios
            SET
                id_rol = ?,
                nombre_completo = ?,
                nombre_usuario = ?,
                correo = ?
            WHERE id_usuario = ?
              AND activo = 1
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, usuario.getIdRol());

            sentencia.setString(
                2,
                usuario.getNombreCompleto().trim()
            );

            sentencia.setString(
                3,
                usuario.getNombreUsuario().trim()
            );

            asignarCorreo(
                sentencia,
                4,
                usuario.getCorreo()
            );

            sentencia.setInt(
                5,
                usuario.getIdUsuario()
            );

            return sentencia.executeUpdate() > 0;

        } catch (SQLException error) {
            System.err.println(
                "Error al actualizar el usuario: "
                + error.getMessage()
            );
        }

        return false;
    }

    /**
     * Cambia únicamente el hash de la contraseña.
     */
    public boolean actualizarContrasena(
            int idUsuario,
            String nuevaContrasenaHash) {

        String sql = """
            UPDATE usuarios
            SET contrasena_hash = ?
            WHERE id_usuario = ?
              AND activo = 1
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setString(1, nuevaContrasenaHash);
            sentencia.setInt(2, idUsuario);

            return sentencia.executeUpdate() > 0;

        } catch (SQLException error) {
            System.err.println(
                "Error al actualizar la contraseña: "
                + error.getMessage()
            );
        }

        return false;
    }

    /**
     * Desactiva un usuario sin eliminarlo físicamente.
     */
    public boolean desactivar(int idUsuario) {

        String sql = """
            UPDATE usuarios
            SET activo = 0
            WHERE id_usuario = ?
              AND activo = 1
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idUsuario);

            return sentencia.executeUpdate() > 0;

        } catch (SQLException error) {
            System.err.println(
                "Error al desactivar el usuario: "
                + error.getMessage()
            );
        }

        return false;
    }

    /**
     * Convierte una fila de MySQL en un objeto Usuario.
     */
    private Usuario convertirUsuario(ResultSet resultado)
            throws SQLException {

        Usuario usuario = new Usuario();

        usuario.setIdUsuario(
            resultado.getInt("id_usuario")
        );

        usuario.setIdRol(
            resultado.getInt("id_rol")
        );

        usuario.setNombreCompleto(
            resultado.getString("nombre_completo")
        );

        usuario.setNombreUsuario(
            resultado.getString("nombre_usuario")
        );

        usuario.setContrasenaHash(
            resultado.getString("contrasena_hash")
        );

        usuario.setCorreo(
            resultado.getString("correo")
        );

        usuario.setActivo(
            resultado.getBoolean("activo")
        );

        if (resultado.getTimestamp("fecha_creacion") != null) {
            usuario.setFechaCreacion(
                resultado
                    .getTimestamp("fecha_creacion")
                    .toLocalDateTime()
            );
        }

        usuario.setNombreRol(
            resultado.getString("nombre_rol")
        );

        return usuario;
    }

    /**
     * Guarda NULL cuando el correo no fue proporcionado.
     */
    private void asignarCorreo(
            PreparedStatement sentencia,
            int posicion,
            String correo) throws SQLException {

        if (correo == null || correo.isBlank()) {
            sentencia.setNull(posicion, Types.VARCHAR);
        } else {
            sentencia.setString(
                posicion,
                correo.trim().toLowerCase()
            );
        }
    }
}
