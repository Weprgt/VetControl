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
public class Cita {

    /**
     * Estados permitidos por el ENUM de MySQL.
     */
    public enum Estado {
        PROGRAMADA,
        CONFIRMADA,
        ATENDIDA,
        CANCELADA,
        NO_ASISTIO
    }

    private int idCita;
    private int idMascota;
    private int idVeterinario;
    private LocalDateTime fechaHora;
    private String motivo;
    private Estado estado;
    private String observaciones;
    private LocalDateTime fechaRegistro;

    /*
     * Campo generado automáticamente por MySQL.
     * No se envía en INSERT ni UPDATE.
     */
    private LocalDateTime horarioActivo;

    /*
     * Datos obtenidos mediante consultas JOIN.
     * No son columnas adicionales de citas.
     */
    private String nombreMascota;
    private String numeroExpediente;
    private String nombreVeterinario;
    private String nombrePropietario;

    /**
     * Constructor vacío.
     */
    public Cita() {
        this.estado = Estado.PROGRAMADA;
    }

    /**
     * Constructor para registrar una cita nueva.
     */
    public Cita(
            int idMascota,
            int idVeterinario,
            LocalDateTime fechaHora,
            String motivo,
            String observaciones) {

        this.idMascota = idMascota;
        this.idVeterinario = idVeterinario;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.observaciones = observaciones;
        this.estado = Estado.PROGRAMADA;
    }

    /**
     * Constructor completo.
     */
    public Cita(
            int idCita,
            int idMascota,
            int idVeterinario,
            LocalDateTime fechaHora,
            String motivo,
            Estado estado,
            String observaciones,
            LocalDateTime fechaRegistro,
            LocalDateTime horarioActivo) {

        this.idCita = idCita;
        this.idMascota = idMascota;
        this.idVeterinario = idVeterinario;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.estado = estado;
        this.observaciones = observaciones;
        this.fechaRegistro = fechaRegistro;
        this.horarioActivo = horarioActivo;
    }

    /**
     * Código visual calculado a partir del ID.
     */
    public String getCodigoVisible() {
        return String.format("CT-%04d", idCita);
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public int getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }

    public int getIdVeterinario() {
        return idVeterinario;
    }

    public void setIdVeterinario(int idVeterinario) {
        this.idVeterinario = idVeterinario;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getHorarioActivo() {
        return horarioActivo;
    }

    public void setHorarioActivo(LocalDateTime horarioActivo) {
        this.horarioActivo = horarioActivo;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public String getNombreVeterinario() {
        return nombreVeterinario;
    }

    public void setNombreVeterinario(String nombreVeterinario) {
        this.nombreVeterinario = nombreVeterinario;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    @Override
    public String toString() {
        return getCodigoVisible()
            + " - "
            + nombreMascota
            + " - "
            + fechaHora;
    }
}
