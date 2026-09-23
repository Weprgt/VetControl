/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author weprg
 */
public final class ConexionBD {

    // Guarda las propiedades leídas desde config.properties
    private static final Properties PROPIEDADES =
        new Properties();

    /*
     * Este bloque se ejecuta una sola vez
     * cuando Java carga la clase ConexionBD.
     */
    static {
        try (InputStream archivo =
                ConexionBD.class
                    .getClassLoader()
                    .getResourceAsStream(
                        "config.properties"
                    )) {

            // Verificar que el archivo exista
            if (archivo == null) {
                throw new IllegalStateException(
                    "No se encontró config.properties"
                );
            }

            // Cargar URL, usuario y contraseña
            PROPIEDADES.load(archivo);

        } catch (IOException error) {

            throw new ExceptionInInitializerError(
                "No se pudo leer config.properties: "
                + error.getMessage()
            );
        }
    }

    /**
     * Constructor privado para impedir que se creen
     * objetos innecesarios de ConexionBD.
     */
    private ConexionBD() {
    }

    /**
     * Abre y devuelve una conexión con MySQL.
     *
     * @return conexión abierta
     * @throws SQLException si MySQL rechaza la conexión
     */
    public static Connection obtenerConexion()
            throws SQLException {

        String url =
            PROPIEDADES.getProperty("db.url");

        String usuario =
            PROPIEDADES.getProperty("db.usuario");

        String contrasena =
            PROPIEDADES.getProperty("db.contrasena");

        return DriverManager.getConnection(
            url,
            usuario,
            contrasena
        );
    }
}
