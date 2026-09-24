/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.dao;

import com.mycompany.vetcontrol.modelo.Cita;
import com.mycompany.vetcontrol.modelo.Cita.Estado;
import com.mycompany.vetcontrol.util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author weprg
 */
public class CitaDAO {

    /*
     * Parte común de las consultas.
     *
     * Une citas con mascotas, clientes, veterinarios y usuarios.
     */
    private static final String CONSULTA_BASE = """
        SELECT
            ci.id_cita,
            ci.id_mascota,
            ci.id_veterinario,
            ci.fecha_hora,
            ci.motivo,
            ci.estado,
            ci.observaciones,
            ci.fecha_registro,
            ci.horario_activo,
            m.nombre AS nombre_mascota,
            m.numero_expediente,
            u.nombre_completo AS nombre_veterinario,
            CONCAT(cl.nombres, ' ', cl.apellidos)
                AS nombre_propietario
        FROM citas ci
        INNER JOIN mascotas m
            ON m.id_mascota = ci.id_mascota
        INNER JOIN clientes cl
            ON cl.id_cliente = m.id_cliente
        INNER JOIN veterinarios v
            ON v.id_veterinario = ci.id_veterinario
        INNER JOIN usuarios u
            ON u.id_usuario = v.id_usuario
        """;

    /**
     * Registra una cita nueva.
     *
     * horario_activo no aparece porque MySQL lo genera.
     */
    public boolean insertar(Cita cita) {

        String sql = """
            INSERT INTO citas (
                id_mascota,
                id_veterinario,
                fecha_hora,
                motivo,
                estado,
                observaciones
            )
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia = conexion.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
            )
        ) {

            sentencia.setInt(1, cita.getIdMascota());
            sentencia.setInt(2, cita.getIdVeterinario());

            sentencia.setTimestamp(
                3,
                Timestamp.valueOf(cita.getFechaHora())
            );

            sentencia.setString(
                4,
                cita.getMotivo().trim()
            );

            Estado estado = cita.getEstado();

            sentencia.setString(
                5,
                estado == null
                    ? Estado.PROGRAMADA.name()
                    : estado.name()
            );

            asignarTextoOpcional(
                sentencia,
                6,
                cita.getObservaciones()
            );

            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet claves = sentencia.getGeneratedKeys()) {
                    if (claves.next()) {
                        cita.setIdCita(claves.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException error) {
            mostrarErrorCita(error);
        }

        return false;
    }

    /**
     * Obtiene todas las citas.
     */
    public List<Cita> listar() {

        List<Cita> citas = new ArrayList<>();

        String sql = CONSULTA_BASE
            + " ORDER BY ci.fecha_hora DESC";

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery()
        ) {

            while (resultado.next()) {
                citas.add(convertirCita(resultado));
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al listar las citas: "
                + error.getMessage()
            );
        }

        return citas;
    }

    /**
     * Obtiene solamente las citas que todavía requieren atención.
     */
    public List<Cita> listarPendientes() {

        List<Cita> citas = new ArrayList<>();

        String sql = CONSULTA_BASE + """
            WHERE ci.estado IN ('PROGRAMADA', 'CONFIRMADA')
            ORDER BY ci.fecha_hora
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery()
        ) {

            while (resultado.next()) {
                citas.add(convertirCita(resultado));
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al listar las citas pendientes: "
                + error.getMessage()
            );
        }

        return citas;
    }

    /**
     * Busca citas por mascota, expediente, propietario,
     * veterinario, motivo o estado.
     */
    public List<Cita> buscar(String texto) {

        List<Cita> citas = new ArrayList<>();

        String sql = CONSULTA_BASE + """
            WHERE (
                   m.nombre LIKE ?
                OR m.numero_expediente LIKE ?
                OR cl.nombres LIKE ?
                OR cl.apellidos LIKE ?
                OR CONCAT(cl.nombres, ' ', cl.apellidos) LIKE ?
                OR u.nombre_completo LIKE ?
                OR ci.motivo LIKE ?
                OR ci.estado LIKE ?
            )
            ORDER BY ci.fecha_hora DESC
            """;

        String criterio = "%" + texto.trim() + "%";

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            for (int posicion = 1; posicion <= 8; posicion++) {
                sentencia.setString(posicion, criterio);
            }

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    citas.add(convertirCita(resultado));
                }
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al buscar citas: "
                + error.getMessage()
            );
        }

        return citas;
    }

    /**
     * Busca una cita por su ID.
     */
    public Cita buscarPorId(int idCita) {

        String sql = CONSULTA_BASE
            + " WHERE ci.id_cita = ?";

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idCita);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return convertirCita(resultado);
                }
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al buscar la cita: "
                + error.getMessage()
            );
        }

        return null;
    }

    /**
     * Actualiza los datos generales de una cita.
     *
     * horario_activo se actualiza automáticamente en MySQL.
     */
    public boolean actualizar(Cita cita) {

        String sql = """
            UPDATE citas
            SET
                id_mascota = ?,
                id_veterinario = ?,
                fecha_hora = ?,
                motivo = ?,
                estado = ?,
                observaciones = ?
            WHERE id_cita = ?
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, cita.getIdMascota());
            sentencia.setInt(2, cita.getIdVeterinario());

            sentencia.setTimestamp(
                3,
                Timestamp.valueOf(cita.getFechaHora())
            );

            sentencia.setString(
                4,
                cita.getMotivo().trim()
            );

            sentencia.setString(
                5,
                cita.getEstado().name()
            );

            asignarTextoOpcional(
                sentencia,
                6,
                cita.getObservaciones()
            );

            sentencia.setInt(7, cita.getIdCita());

            return sentencia.executeUpdate() > 0;

        } catch (SQLException error) {
            mostrarErrorCita(error);
        }

        return false;
    }

    /**
     * Modifica únicamente el estado de una cita.
     *
     * Servirá para confirmar, atender, cancelar o marcar
     * que el paciente no asistió.
     */
    public boolean cambiarEstado(
            int idCita,
            Estado nuevoEstado) {

        String sql = """
            UPDATE citas
            SET estado = ?
            WHERE id_cita = ?
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setString(1, nuevoEstado.name());
            sentencia.setInt(2, idCita);

            return sentencia.executeUpdate() > 0;

        } catch (SQLException error) {
            mostrarErrorCita(error);
        }

        return false;
    }

    /**
     * Convierte una fila de MySQL en un objeto Cita.
     */
    private Cita convertirCita(ResultSet resultado)
            throws SQLException {

        Cita cita = new Cita();

        cita.setIdCita(
            resultado.getInt("id_cita")
        );

        cita.setIdMascota(
            resultado.getInt("id_mascota")
        );

        cita.setIdVeterinario(
            resultado.getInt("id_veterinario")
        );

        cita.setFechaHora(
            resultado
                .getTimestamp("fecha_hora")
                .toLocalDateTime()
        );

        cita.setMotivo(
            resultado.getString("motivo")
        );

        cita.setEstado(
            Estado.valueOf(resultado.getString("estado"))
        );

        cita.setObservaciones(
            resultado.getString("observaciones")
        );

        if (resultado.getTimestamp("fecha_registro") != null) {
            cita.setFechaRegistro(
                resultado
                    .getTimestamp("fecha_registro")
                    .toLocalDateTime()
            );
        }

        if (resultado.getTimestamp("horario_activo") != null) {
            cita.setHorarioActivo(
                resultado
                    .getTimestamp("horario_activo")
                    .toLocalDateTime()
            );
        }

        cita.setNombreMascota(
            resultado.getString("nombre_mascota")
        );

        cita.setNumeroExpediente(
            resultado.getString("numero_expediente")
        );

        cita.setNombreVeterinario(
            resultado.getString("nombre_veterinario")
        );

        cita.setNombrePropietario(
            resultado.getString("nombre_propietario")
        );

        return cita;
    }

    /**
     * Guarda NULL cuando un campo opcional está vacío.
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

    /**
     * Muestra un mensaje especial cuando se intenta asignar al
     * veterinario dos citas activas en el mismo horario.
     */
    private void mostrarErrorCita(SQLException error) {

        if (error.getErrorCode() == 1062) {
            System.err.println(
                "El veterinario ya tiene una cita activa "
                + "programada en ese horario."
            );
        } else {
            System.err.println(
                "Error al guardar la cita: "
                + error.getMessage()
            );
        }
    }
}