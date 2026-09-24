/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author weprg
 */
public final class SeguridadContrasena {

    private static final int ITERACIONES = 210_000;
    private static final int LONGITUD_SAL = 16;
    private static final int LONGITUD_HASH = 256;

    private static final String ALGORITMO =
        "PBKDF2WithHmacSHA256";

    /**
     * Evita crear objetos de esta clase de utilidades.
     */
    private SeguridadContrasena() {
    }

    /**
     * Convierte una contraseña en un valor seguro para almacenarlo.
     *
     * Formato:
     * pbkdf2$iteraciones$sal$hash
     */
    public static String generarHash(char[] contrasena) {

        byte[] sal = new byte[LONGITUD_SAL];
        new SecureRandom().nextBytes(sal);

        byte[] hash = aplicarPBKDF2(
            contrasena,
            sal,
            ITERACIONES
        );

        return "pbkdf2"
            + "$" + ITERACIONES
            + "$" + Base64.getEncoder().encodeToString(sal)
            + "$" + Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Compara una contraseña escrita con un hash almacenado.
     */
    public static boolean verificar(
            char[] contrasena,
            String valorAlmacenado) {

        if (valorAlmacenado == null
                || valorAlmacenado.isBlank()) {
            return false;
        }

        String[] partes = valorAlmacenado.split("\\$");

        if (partes.length != 4
                || !partes[0].equals("pbkdf2")) {
            return false;
        }

        try {
            int iteraciones =
                Integer.parseInt(partes[1]);

            byte[] sal =
                Base64.getDecoder().decode(partes[2]);

            byte[] hashGuardado =
                Base64.getDecoder().decode(partes[3]);

            byte[] hashCalculado = aplicarPBKDF2(
                contrasena,
                sal,
                iteraciones
            );

            return comparacionSegura(
                hashGuardado,
                hashCalculado
            );

        } catch (IllegalArgumentException error) {
            return false;
        }
    }

    /**
     * Ejecuta el algoritmo PBKDF2.
     */
    private static byte[] aplicarPBKDF2(
            char[] contrasena,
            byte[] sal,
            int iteraciones) {

        PBEKeySpec especificacion = new PBEKeySpec(
            contrasena,
            sal,
            iteraciones,
            LONGITUD_HASH
        );

        try {
            SecretKeyFactory fabrica =
                SecretKeyFactory.getInstance(ALGORITMO);

            return fabrica
                .generateSecret(especificacion)
                .getEncoded();

        } catch (NoSuchAlgorithmException
                | InvalidKeySpecException error) {

            throw new IllegalStateException(
                "No fue posible proteger la contraseña.",
                error
            );

        } finally {
            /*
             * Borra la contraseña almacenada internamente
             * por PBEKeySpec.
             */
            especificacion.clearPassword();
        }
    }

    /**
     * Compara todos los bytes para reducir ataques por tiempo.
     */
    private static boolean comparacionSegura(
            byte[] primero,
            byte[] segundo) {

        if (primero.length != segundo.length) {
            return false;
        }

        int diferencia = 0;

        for (int posicion = 0;
                posicion < primero.length;
                posicion++) {

            diferencia |=
                primero[posicion] ^ segundo[posicion];
        }

        return diferencia == 0;
    }
}