/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.dao;

import com.mycompany.vetcontrol.modelo.Cita;
import com.mycompany.vetcontrol.modelo.Cita.Estado;
import com.mycompany.vetcontrol.modelo.ResumenInicio;
import com.mycompany.vetcontrol.util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author weprg
 */
/**
 * Obtiene de MySQL los datos necesarios
 * para mostrar el resumen del sistema.
 */
public class InicioDAO {

    /**
     * Consulta las cantidades que aparecen
     * en las tarjetas del panel de inicio.
     *
     * @return resumen con las cantidades actuales
     * @throws SQLException si ocurre un problema con MySQL
     */
    
  
    public ResumenInicio obtenerResumen()
            throws SQLException {

        /*
         * Contamos únicamente los clientes
         * que continúan activos.
         */
        String sqlClientes =
            "SELECT COUNT(*) "
            + "FROM clientes "
            + "WHERE activo = TRUE";

        /*
         * Contamos únicamente las mascotas
         * que continúan activas.
         */
        String sqlMascotas =
            "SELECT COUNT(*) "
            + "FROM mascotas "
            + "WHERE activo = TRUE";

        /*
         * Cuenta las citas de hoy que todavía
         * están pendientes de ser atendidas.
         */
        String sqlCitas =
            "SELECT COUNT(*) "
            + "FROM citas "
            + "WHERE DATE(fecha_hora) = CURRENT_DATE "
            + "AND estado IN ('PROGRAMADA', 'CONFIRMADA')";

        /*
         * Un producto tiene existencias bajas cuando
         * su cantidad actual alcanza o baja del mínimo.
         */
        String sqlStockBajo =
            "SELECT COUNT(*) "
            + "FROM productos "
            + "WHERE activo = TRUE "
            + "AND stock_actual <= stock_minimo";

        try (Connection conexion =
                ConexionBD.getConexion()) {

            int clientesActivos =
                contarRegistros(conexion, sqlClientes);

            int mascotasActivas =
                contarRegistros(conexion, sqlMascotas);

            int citasHoy =
                contarRegistros(conexion, sqlCitas);

            int productosStockBajo =
                contarRegistros(conexion, sqlStockBajo);

            return new ResumenInicio(
                clientesActivos,
                mascotasActivas,
                citasHoy,
                productosStockBajo
            );
        }
    }

    /**
     * Ejecuta una consulta SELECT COUNT(*)
     * y devuelve la cantidad obtenida.
     */
    private int contarRegistros(
            Connection conexion,
            String sql)
            throws SQLException {

        try (
            PreparedStatement sentencia =
                conexion.prepareStatement(sql);

            ResultSet resultado =
                sentencia.executeQuery()
        ) {

            if (resultado.next()) {
                return resultado.getInt(1);
            }

            return 0;
        }
    }
    /**
    * Devuelve las próximas citas que todavía
    * están programadas o confirmadas.
    *
    * Se muestran como máximo seis registros
    * para no sobrecargar el panel de inicio.
    */
   public List<Cita> listarProximasCitas()
           throws SQLException {

       List<Cita> citas = new ArrayList<>();

       String sql =
           "SELECT "
           + "c.id_cita, "
           + "c.fecha_hora, "
           + "c.estado, "
           + "m.nombre AS nombre_mascota, "
           + "CONCAT(cl.nombres, ' ', cl.apellidos) "
           + "AS nombre_propietario, "
           + "u.nombre_completo AS nombre_veterinario "
           + "FROM citas c "
           + "INNER JOIN mascotas m "
           + "ON m.id_mascota = c.id_mascota "
           + "INNER JOIN clientes cl "
           + "ON cl.id_cliente = m.id_cliente "
           + "INNER JOIN veterinarios v "
           + "ON v.id_veterinario = c.id_veterinario "
           + "INNER JOIN usuarios u "
           + "ON u.id_usuario = v.id_usuario "
           + "WHERE c.fecha_hora >= NOW() "
           + "AND c.estado IN "
           + "('PROGRAMADA', 'CONFIRMADA') "
           + "ORDER BY c.fecha_hora ASC "
           + "LIMIT 6";

       try (
           Connection conexion =
               ConexionBD.getConexion();

           PreparedStatement sentencia =
               conexion.prepareStatement(sql);

           ResultSet resultado =
               sentencia.executeQuery()
       ) {

           while (resultado.next()) {

               Cita cita = new Cita();

               cita.setIdCita(
                   resultado.getInt("id_cita")
               );

               Timestamp fechaHora =
                   resultado.getTimestamp("fecha_hora");

               if (fechaHora != null) {
                   cita.setFechaHora(
                       fechaHora.toLocalDateTime()
                   );
               }

               cita.setEstado(
                   Estado.valueOf(
                       resultado.getString("estado")
                   )
               );

               cita.setNombreMascota(
                   resultado.getString(
                       "nombre_mascota"
                   )
               );

               cita.setNombrePropietario(
                   resultado.getString(
                       "nombre_propietario"
                   )
               );

               cita.setNombreVeterinario(
                   resultado.getString(
                       "nombre_veterinario"
                   )
               );

               citas.add(cita);
           }
       }

       return citas;
   }
}
