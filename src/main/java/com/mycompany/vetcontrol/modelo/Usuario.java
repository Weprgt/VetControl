/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.modelo;

import java.time.LocalDateTime;

/**
 *
 * @author weprg
 */
public class Usuario {

    private int idUsuario;
    private int idRol;
    private String nombreCompleto;
    private String nombreUsuario;
    private String contrasenaHash;
    private String correo;
    private boolean activo;
    private LocalDateTime fechaCreacion;

    /*
     * Se obtiene mediante un JOIN con la tabla roles.
     * No es una columna de la tabla usuarios.
     */
    private String nombreRol;

    /**
     * Constructor vacío.
     */
    public Usuario() {
        this.activo = true;
    }
    

    /**
     * Constructor para registrar un usuario nuevo.
     */
    public Usuario(
            int idRol,
            String nombreCompleto,
            String nombreUsuario,
            String contrasenaHash,
            String correo) {

        this.idRol = idRol;
        this.nombreCompleto = nombreCompleto;
        this.nombreUsuario = nombreUsuario;
        this.contrasenaHash = contrasenaHash;
        this.correo = correo;
        this.activo = true;
    }

    /**
     * Constructor completo para recuperar usuarios desde MySQL.
     */
    public Usuario(
            int idUsuario,
            int idRol,
            String nombreCompleto,
            String nombreUsuario,
            String contrasenaHash,
            String correo,
            boolean activo,
            LocalDateTime fechaCreacion) {

        this.idUsuario = idUsuario;
        this.idRol = idRol;
        this.nombreCompleto = nombreCompleto;
        this.nombreUsuario = nombreUsuario;
        this.contrasenaHash = contrasenaHash;
        this.correo = correo;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    /**
     * Permite mostrar el nombre del usuario en JComboBox y listas.
     */
    @Override
    public String toString() {
        return nombreCompleto;
    }
}
