/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.dao;

import com.mycompany.vetcontrol.modelo.HistorialClinico;
import com.mycompany.vetcontrol.modelo.HistorialClinico.TipoRegistro;
import com.mycompany.vetcontrol.util.ConexionBD;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author weprg
 */
public class HistorialClinicoDAO {

    /**
     * Consulta base con los datos de mascota, propietario
     * y veterinario.
     */
    private static final String CONSULTA_BASE = """
        SELECT
            h.id_historial,
            h.id_mascota,
            h.id_veterinario,
            h.id_cita,
            h.fecha_atencion,
            h.tipo_registro,
            h.motivo_consulta,
            h.diagnostico,
            h.tratamiento,
            h.observaciones,
            h.nombre_vacuna,
            h.lote_vacuna,
            h.proxima_dosis,
            h.fecha_registro,
            m.nombre AS nombre_mascota,
            m.numero_expediente,
            u.nombre_completo AS nombre_veterinario,
            CONCAT(cl.nombres, ' ', cl.apellidos)
                AS nombre_propietario
        FROM historial_clinico h
        INNER JOIN mascotas m
            ON m.id_mascota = h.id_mascota
        INNER JOIN clientes cl
            ON cl.id_cliente = m.id_cliente
        INNER JOIN veterinarios v
            ON v.id_veterinario = h.id_veterinario
        INNER JOIN usuarios u
            ON u.id_usuario = v.id_usuario
        """;

    /**
     * Registra una entrada nueva en el historial clínico.
     */
    public boolean insertar(HistorialClinico historial) {

        String sql = """
            INSERT INTO historial_clinico (
                id_mascota,
                id_veterinario,
                id_cita,
                fecha_atencion,
                tipo_registro,
                motivo_consulta,
                diagnostico,
                tratamiento,
                observaciones,
                nombre_vacuna,
                lote_vacuna,
                proxima_dosis
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia = conexion.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
            )
        ) {

            sentencia.setInt(1, historial.getIdMascota());
            sentencia.setInt(2, historial.getIdVeterinario());

            asignarIdCita(
                sentencia,
                3,
                historial.getIdCita()
            );

            sentencia.setTimestamp(
                4,
                Timestamp.valueOf(historial.getFechaAtencion())
            );

            TipoRegistro tipo = historial.getTipoRegistro();

            sentencia.setString(
                5,
                tipo == null
                    ? TipoRegistro.CONSULTA.name()
                    : tipo.name()
            );

            asignarTextoOpcional(
                sentencia, 6, historial.getMotivoConsulta()
            );

            asignarTextoOpcional(
                sentencia, 7, historial.getDiagnostico()
            );

            asignarTextoOpcional(
                sentencia, 8, historial.getTratamiento()
            );

            asignarTextoOpcional(
                sentencia, 9, historial.getObservaciones()
            );

            asignarTextoOpcional(
                sentencia, 10, historial.getNombreVacuna()
            );

            asignarTextoOpcional(
                sentencia, 11, historial.getLoteVacuna()
            );

            asignarFechaOpcional(
                sentencia, 12, historial.getProximaDosis()
            );

            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet claves = sentencia.getGeneratedKeys()) {
                    if (claves.next()) {
                        historial.setIdHistorial(
                            claves.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al insertar el historial clínico: "
                + error.getMessage()
            );
        }

        return false;
    }

    /**
     * Obtiene todas las entradas, empezando por la más reciente.
     */
    public List<HistorialClinico> listar() {

        List<HistorialClinico> historiales =
            new ArrayList<>();

        String sql = CONSULTA_BASE
            + " ORDER BY h.fecha_atencion DESC";

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery()
        ) {

            while (resultado.next()) {
                historiales.add(
                    convertirHistorial(resultado)
                );
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al listar el historial clínico: "
                + error.getMessage()
            );
        }

        return historiales;
    }

    /**
     * Obtiene el historial completo de una mascota.
     */
    public List<HistorialClinico> listarPorMascota(
            int idMascota) {

        List<HistorialClinico> historiales =
            new ArrayList<>();

        String sql = CONSULTA_BASE + """
            WHERE h.id_mascota = ?
            ORDER BY h.fecha_atencion DESC
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idMascota);

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    historiales.add(
                        convertirHistorial(resultado)
                    );
                }
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al consultar el historial de la mascota: "
                + error.getMessage()
            );
        }

        return historiales;
    }

    /**
     * Busca por mascota, expediente, propietario, veterinario,
     * diagnóstico, tratamiento, vacuna o tipo de registro.
     */
    public List<HistorialClinico> buscar(String texto) {

        List<HistorialClinico> historiales =
            new ArrayList<>();

        String sql = CONSULTA_BASE + """
            WHERE (
                   m.nombre LIKE ?
                OR m.numero_expediente LIKE ?
                OR cl.nombres LIKE ?
                OR cl.apellidos LIKE ?
                OR CONCAT(cl.nombres, ' ', cl.apellidos) LIKE ?
                OR u.nombre_completo LIKE ?
                OR h.tipo_registro LIKE ?
                OR h.motivo_consulta LIKE ?
                OR h.diagnostico LIKE ?
                OR h.tratamiento LIKE ?
                OR h.nombre_vacuna LIKE ?
            )
            ORDER BY h.fecha_atencion DESC
            """;

        String criterio = "%" + texto.trim() + "%";

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            for (int posicion = 1; posicion <= 11; posicion++) {
                sentencia.setString(posicion, criterio);
            }

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    historiales.add(
                        convertirHistorial(resultado)
                    );
                }
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al buscar en el historial clínico: "
                + error.getMessage()
            );
        }

        return historiales;
    }

    /**
     * Busca una entrada del historial mediante su ID.
     */
    public HistorialClinico buscarPorId(int idHistorial) {

        String sql = CONSULTA_BASE
            + " WHERE h.id_historial = ?";

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idHistorial);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return convertirHistorial(resultado);
                }
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al buscar el historial clínico: "
                + error.getMessage()
            );
        }

        return null;
    }

    /**
     * Actualiza una entrada clínica existente.
     */
    public boolean actualizar(HistorialClinico historial) {

        String sql = """
            UPDATE historial_clinico
            SET
                id_mascota = ?,
                id_veterinario = ?,
                id_cita = ?,
                fecha_atencion = ?,
                tipo_registro = ?,
                motivo_consulta = ?,
                diagnostico = ?,
                tratamiento = ?,
                observaciones = ?,
                nombre_vacuna = ?,
                lote_vacuna = ?,
                proxima_dosis = ?
            WHERE id_historial = ?
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, historial.getIdMascota());
            sentencia.setInt(2, historial.getIdVeterinario());

            asignarIdCita(
                sentencia, 3, historial.getIdCita()
            );

            sentencia.setTimestamp(
                4,
                Timestamp.valueOf(historial.getFechaAtencion())
            );

            sentencia.setString(
                5,
                historial.getTipoRegistro().name()
            );

            asignarTextoOpcional(
                sentencia, 6, historial.getMotivoConsulta()
            );

            asignarTextoOpcional(
                sentencia, 7, historial.getDiagnostico()
            );

            asignarTextoOpcional(
                sentencia, 8, historial.getTratamiento()
            );

            asignarTextoOpcional(
                sentencia, 9, historial.getObservaciones()
            );

            asignarTextoOpcional(
                sentencia, 10, historial.getNombreVacuna()
            );

            asignarTextoOpcional(
                sentencia, 11, historial.getLoteVacuna()
            );

            asignarFechaOpcional(
                sentencia, 12, historial.getProximaDosis()
            );

            sentencia.setInt(
                13,
                historial.getIdHistorial()
            );

            return sentencia.executeUpdate() > 0;

        } catch (SQLException error) {
            System.err.println(
                "Error al actualizar el historial clínico: "
                + error.getMessage()
            );
        }

        return false;
    }

    /**
     * Convierte una fila de MySQL en un objeto HistorialClinico.
     */
    private HistorialClinico convertirHistorial(
            ResultSet resultado) throws SQLException {

        HistorialClinico historial =
            new HistorialClinico();

        historial.setIdHistorial(
            resultado.getInt("id_historial")
        );

        historial.setIdMascota(
            resultado.getInt("id_mascota")
        );

        historial.setIdVeterinario(
            resultado.getInt("id_veterinario")
        );

        int idCita = resultado.getInt("id_cita");

        if (resultado.wasNull()) {
            historial.setIdCita(null);
        } else {
            historial.setIdCita(idCita);
        }

        historial.setFechaAtencion(
            resultado
                .getTimestamp("fecha_atencion")
                .toLocalDateTime()
        );

        historial.setTipoRegistro(
            TipoRegistro.valueOf(
                resultado.getString("tipo_registro")
            )
        );

        historial.setMotivoConsulta(
            resultado.getString("motivo_consulta")
        );

        historial.setDiagnostico(
            resultado.getString("diagnostico")
        );

        historial.setTratamiento(
            resultado.getString("tratamiento")
        );

        historial.setObservaciones(
            resultado.getString("observaciones")
        );

        historial.setNombreVacuna(
            resultado.getString("nombre_vacuna")
        );

        historial.setLoteVacuna(
            resultado.getString("lote_vacuna")
        );

        Date proximaDosis =
            resultado.getDate("proxima_dosis");

        if (proximaDosis != null) {
            historial.setProximaDosis(
                proximaDosis.toLocalDate()
            );
        }

        if (resultado.getTimestamp("fecha_registro") != null) {
            historial.setFechaRegistro(
                resultado
                    .getTimestamp("fecha_registro")
                    .toLocalDateTime()
            );
        }

        historial.setNombreMascota(
            resultado.getString("nombre_mascota")
        );

        historial.setNumeroExpediente(
            resultado.getString("numero_expediente")
        );

        historial.setNombreVeterinario(
            resultado.getString("nombre_veterinario")
        );

        historial.setNombrePropietario(
            resultado.getString("nombre_propietario")
        );

        return historial;
    }

    /**
     * Asigna el ID de cita o NULL.
     */
    private void asignarIdCita(
            PreparedStatement sentencia,
            int posicion,
            Integer idCita) throws SQLException {

        if (idCita == null) {
            sentencia.setNull(posicion, Types.INTEGER);
        } else {
            sentencia.setInt(posicion, idCita);
        }
    }

    /**
     * Asigna un texto o NULL cuando está vacío.
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
     * Asigna una fecha o NULL.
     */
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
}
