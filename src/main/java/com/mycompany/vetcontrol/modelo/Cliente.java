/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.Cliente to edit this template
 */
package com.mycompany.vetcontrol.modelo;

import java.time.LocalDateTime;

/**
 *
 * @author weprg
 */

public class Cliente {
    private int idCliente;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String correo;
    private String direccion;
    private boolean activo;
    private LocalDateTime fechaRegistro;

    /**
     * Constructor vacío.
     */
    public Cliente() {
        activo = true;
    }

    /**
     * Constructor utilizado para registrar un cliente nuevo.
     * El ID y la fecha serán generados por MySQL.
     */
    public Cliente(
            String nombres,
            String apellidos,
            String telefono,
            String correo,
            String direccion) {

        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.activo = true;
    }

    /**
     * Constructor utilizado al recuperar un cliente
     * completo desde la base de datos.
     */
    public Cliente(
            int idCliente,
            String nombres,
            String apellidos,
            String telefono,
            String correo,
            String direccion,
            boolean activo,
            LocalDateTime fechaRegistro) {

        this.idCliente = idCliente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
    }

    /**
     * Código que se mostrará en la interfaz.
     * No necesita almacenarse en MySQL.
     */
    public String getCodigoVisible() {
        return String.format(
            "C-%04d",
            idCliente
        );
    }

    /**
     * Devuelve nombres y apellidos juntos.
     */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(
            LocalDateTime fechaRegistro) {

        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return getNombreCompleto();
    }
}

