/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.dao;

import com.mycompany.vetcontrol.modelo.Mascota;
import com.mycompany.vetcontrol.modelo.Mascota.Sexo;
import com.mycompany.vetcontrol.util.ConexionBD;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author weprg
 */
public class MascotaDAO {

    /**
     * Registra una mascota nueva.
     *
     * @param mascota mascota que se desea registrar
     * @return true si se insertó correctamente
     */
    public boolean insertar(Mascota mascota) {

        String sql = """
            INSERT INTO mascotas (
                id_cliente,
                numero_expediente,
                nombre,
                especie,
                raza,
                sexo,
                fecha_nacimiento,
                color,
                observaciones
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia = conexion.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
            )
        ) {

            sentencia.setInt(1, mascota.getIdCliente());
            sentencia.setString(2, mascota.getNumeroExpediente());
            sentencia.setString(3, mascota.getNombre());
            sentencia.setString(4, mascota.getEspecie());

            asignarTextoOpcional(sentencia, 5, mascota.getRaza());

            Sexo sexo = mascota.getSexo();

            if (sexo == null) {
                sentencia.setString(6, Sexo.DESCONOCIDO.name());
            } else {
                sentencia.setString(6, sexo.name());
            }

            asignarFechaOpcional(
                sentencia,
                7,
                mascota.getFechaNacimiento()
            );

            asignarTextoOpcional(sentencia, 8, mascota.getColor());
            asignarTextoOpcional(
                sentencia,
                9,
                mascota.getObservaciones()
            );

            int filasAfectadas = sentencia.executeUpdate();

            /*
             * Recuperamos el id_mascota generado automáticamente
             * y lo colocamos dentro del objeto.
             */
            if (filasAfectadas > 0) {
                try (ResultSet claves = sentencia.getGeneratedKeys()) {
                    if (claves.next()) {
                        mascota.setIdMascota(claves.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al insertar la mascota: "
                + error.getMessage()
            );
        }

        return false;
    }

    /**
     * Devuelve todas las mascotas activas junto con su propietario.
     *
     * @return lista de mascotas activas
     */
    public List<Mascota> listarActivas() {

        List<Mascota> mascotas = new ArrayList<>();

        String sql = """
            SELECT
                m.id_mascota,
                m.id_cliente,
                m.numero_expediente,
                m.nombre,
                m.especie,
                m.raza,
                m.sexo,
                m.fecha_nacimiento,
                m.color,
                m.observaciones,
                m.activo,
                m.fecha_registro,
                CONCAT(c.nombres, ' ', c.apellidos)
                    AS nombre_propietario
            FROM mascotas m
            INNER JOIN clientes c
                ON c.id_cliente = m.id_cliente
            WHERE m.activo = 1
            ORDER BY m.nombre
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery()
        ) {

            while (resultado.next()) {
                mascotas.add(convertirMascota(resultado));
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al listar las mascotas: "
                + error.getMessage()
            );
        }

        return mascotas;
    }

    /**
     * Busca mascotas por nombre, especie, raza, expediente
     * o nombre del propietario.
     *
     * @param texto contenido escrito en el buscador
     * @return mascotas que coincidan con la búsqueda
     */
    public List<Mascota> buscar(String texto) {

        List<Mascota> mascotas = new ArrayList<>();

        String sql = """
            SELECT
                m.id_mascota,
                m.id_cliente,
                m.numero_expediente,
                m.nombre,
                m.especie,
                m.raza,
                m.sexo,
                m.fecha_nacimiento,
                m.color,
                m.observaciones,
                m.activo,
                m.fecha_registro,
                CONCAT(c.nombres, ' ', c.apellidos)
                    AS nombre_propietario
            FROM mascotas m
            INNER JOIN clientes c
                ON c.id_cliente = m.id_cliente
            WHERE m.activo = 1
              AND (
                    m.numero_expediente LIKE ?
                 OR m.nombre LIKE ?
                 OR m.especie LIKE ?
                 OR m.raza LIKE ?
                 OR c.nombres LIKE ?
                 OR c.apellidos LIKE ?
                 OR CONCAT(c.nombres, ' ', c.apellidos) LIKE ?
              )
            ORDER BY m.nombre
            """;

        String criterio = "%" + texto.trim() + "%";

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            // Los siete signos ? reciben el mismo criterio.
            for (int posicion = 1; posicion <= 7; posicion++) {
                sentencia.setString(posicion, criterio);
            }

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    mascotas.add(convertirMascota(resultado));
                }
            }

        } catch (SQLException error) {
            System.err.println(
                "Error al buscar mascotas: "
                + error.getMessage()
            );
        }

        return mascotas;
    }

    /**
     * Actualiza la información de una mascota existente.
     *
     * @param mascota objeto con los datos modificados
     * @return true si la mascota fue actualizada
     */
    public boolean actualizar(Mascota mascota) {

        String sql = """
            UPDATE mascotas
            SET
                id_cliente = ?,
                numero_expediente = ?,
                nombre = ?,
                especie = ?,
                raza = ?,
                sexo = ?,
                fecha_nacimiento = ?,
                color = ?,
                observaciones = ?
            WHERE id_mascota = ?
              AND activo = 1
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, mascota.getIdCliente());
            sentencia.setString(2, mascota.getNumeroExpediente());
            sentencia.setString(3, mascota.getNombre());
            sentencia.setString(4, mascota.getEspecie());

            asignarTextoOpcional(sentencia, 5, mascota.getRaza());

            Sexo sexo = mascota.getSexo();

            if (sexo == null) {
                sentencia.setString(6, Sexo.DESCONOCIDO.name());
            } else {
                sentencia.setString(6, sexo.name());
            }

            asignarFechaOpcional(
                sentencia,
                7,
                mascota.getFechaNacimiento()
            );

            asignarTextoOpcional(sentencia, 8, mascota.getColor());
            asignarTextoOpcional(
                sentencia,
                9,
                mascota.getObservaciones()
            );

            sentencia.setInt(10, mascota.getIdMascota());

            return sentencia.executeUpdate() > 0;

        } catch (SQLException error) {
            System.err.println(
                "Error al actualizar la mascota: "
                + error.getMessage()
            );
        }

        return false;
    }

    /**
     * Realiza una eliminación lógica.
     *
     * La mascota continúa en la base de datos, pero deja de aparecer
     * en las consultas de registros activos.
     *
     * @param idMascota identificador de la mascota
     * @return true si fue desactivada
     */
    public boolean desactivar(int idMascota) {

        String sql = """
            UPDATE mascotas
            SET activo = 0
            WHERE id_mascota = ?
              AND activo = 1
            """;

        try (
            Connection conexion = ConexionBD.getConexion();
            PreparedStatement sentencia =
                conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idMascota);

            return sentencia.executeUpdate() > 0;

        } catch (SQLException error) {
            System.err.println(
                "Error al desactivar la mascota: "
                + error.getMessage()
            );
        }

        return false;
    }

    /**
     * Convierte una fila del ResultSet en un objeto Mascota.
     */
    private Mascota convertirMascota(ResultSet resultado)
            throws SQLException {

        Mascota mascota = new Mascota();

        mascota.setIdMascota(
            resultado.getInt("id_mascota")
        );

        mascota.setIdCliente(
            resultado.getInt("id_cliente")
        );

        mascota.setNumeroExpediente(
            resultado.getString("numero_expediente")
        );

        mascota.setNombre(
            resultado.getString("nombre")
        );

        mascota.setEspecie(
            resultado.getString("especie")
        );

        mascota.setRaza(
            resultado.getString("raza")
        );

        mascota.setSexo(
            Sexo.valueOf(resultado.getString("sexo"))
        );

        Date fechaNacimiento =
            resultado.getDate("fecha_nacimiento");

        if (fechaNacimiento != null) {
            mascota.setFechaNacimiento(
                fechaNacimiento.toLocalDate()
            );
        }

        mascota.setColor(
            resultado.getString("color")
        );

        mascota.setObservaciones(
            resultado.getString("observaciones")
        );

        mascota.setActivo(
            resultado.getBoolean("activo")
        );

        if (resultado.getTimestamp("fecha_registro") != null) {
            mascota.setFechaRegistro(
                resultado
                    .getTimestamp("fecha_registro")
                    .toLocalDateTime()
            );
        }

        mascota.setNombrePropietario(
            resultado.getString("nombre_propietario")
        );

        return mascota;
    }

    /**
     * Envía NULL a MySQL cuando un texto opcional está vacío.
     */
    private void asignarTextoOpcional(
            PreparedStatement sentencia,
            int posicion,
            String texto) throws SQLException {

        if (texto == null || texto.isBlank()) {
            sentencia.setNull(
                posicion,
                java.sql.Types.VARCHAR
            );
        } else {
            sentencia.setString(
                posicion,
                texto.trim()
            );
        }
    }

    /**
     * Envía NULL cuando no se conoce la fecha de nacimiento.
     */
    private void asignarFechaOpcional(
            PreparedStatement sentencia,
            int posicion,
            java.time.LocalDate fecha) throws SQLException {

        if (fecha == null) {
            sentencia.setNull(
                posicion,
                java.sql.Types.DATE
            );
        } else {
            sentencia.setDate(
                posicion,
                Date.valueOf(fecha)
            );
        }
    }
}
