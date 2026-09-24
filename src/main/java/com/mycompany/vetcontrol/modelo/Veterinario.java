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
public class Veterinario {

    private int idVeterinario;
    private int idUsuario;
    private String especialidad;
    private String telefonoProfesional;
    private boolean activo;
    private LocalDateTime fechaRegistro;

    /*
     * Datos obtenidos mediante JOIN con usuarios.
     * No son columnas de la tabla veterinarios.
     */
    private String nombreCompleto;
    private String nombreUsuario;
    private String correo;

    /**
     * Constructor vacío.
     */
    public Veterinario() {
        this.activo = true;
    }

    /**
     * Constructor para registrar un veterinario nuevo.
     */
    public Veterinario(
            int idUsuario,
            String especialidad,
            String telefonoProfesional) {

        this.idUsuario = idUsuario;
        this.especialidad = especialidad;
        this.telefonoProfesional = telefonoProfesional;
        this.activo = true;
    }

    /**
     * Constructor completo.
     */
    public Veterinario(
            int idVeterinario,
            int idUsuario,
            String especialidad,
            String telefonoProfesional,
            boolean activo,
            LocalDateTime fechaRegistro) {

        this.idVeterinario = idVeterinario;
        this.idUsuario = idUsuario;
        this.especialidad = especialidad;
        this.telefonoProfesional = telefonoProfesional;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdVeterinario() {
        return idVeterinario;
    }

    public void setIdVeterinario(int idVeterinario) {
        this.idVeterinario = idVeterinario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getTelefonoProfesional() {
        return telefonoProfesional;
    }

    public void setTelefonoProfesional(String telefonoProfesional) {
        this.telefonoProfesional = telefonoProfesional;
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

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Permite mostrar al veterinario dentro de un JComboBox.
     */
    @Override
    public String toString() {
        return nombreCompleto;
    }
}
