/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.servicio;

import com.mycompany.vetcontrol.dao.UsuarioDAO;
import com.mycompany.vetcontrol.modelo.Usuario;
import com.mycompany.vetcontrol.util.SeguridadContrasena;

/**
 *
 * @author weprg
 */
public class ServicioAutenticacion {

    private final UsuarioDAO usuarioDAO;

    public ServicioAutenticacion() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Busca al usuario y verifica su contraseña.
     *
     * @return usuario autenticado o null si los datos son incorrectos
     */
    public Usuario autenticar(
            String nombreUsuario,
            char[] contrasena) {

        if (nombreUsuario == null
                || nombreUsuario.isBlank()
                || contrasena == null
                || contrasena.length == 0) {

            return null;
        }

        Usuario usuario =
            usuarioDAO.buscarPorNombreUsuario(
                nombreUsuario.trim()
            );

        if (usuario == null) {
            return null;
        }

        boolean contrasenaCorrecta =
            SeguridadContrasena.verificar(
                contrasena,
                usuario.getContrasenaHash()
            );

        return contrasenaCorrecta
            ? usuario
            : null;
    }
}