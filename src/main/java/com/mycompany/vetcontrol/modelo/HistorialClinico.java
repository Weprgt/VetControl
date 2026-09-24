/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author weprg
 */
public class HistorialClinico {

    /**
     * Valores permitidos por el ENUM de MySQL.
     */
    public enum TipoRegistro {
        CONSULTA,
        DIAGNOSTICO,
        TRATAMIENTO,
        VACUNA,
        CONTROL,
        OTRO
    }

    private int idHistorial;
    private int idMascota;
    private int idVeterinario;

    /*
     * Integer permite guardar null cuando el registro clínico
     * no está asociado directamente con una cita.
     */
    private Integer idCita;

    private LocalDateTime fechaAtencion;
    private TipoRegistro tipoRegistro;
    private String motivoConsulta;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
    private String nombreVacuna;
    private String loteVacuna;
    private LocalDate proximaDosis;
    private LocalDateTime fechaRegistro;

    /*
     * Datos obtenidos mediante JOIN.
     * No son columnas adicionales del historial.
     */
    private String nombreMascota;
    private String numeroExpediente;
    private String nombreVeterinario;
    private String nombrePropietario;

    /**
     * Constructor vacío.
     */
    public HistorialClinico() {
        this.tipoRegistro = TipoRegistro.CONSULTA;
    }

    /**
     * Constructor para registrar una entrada clínica nueva.
     */
    public HistorialClinico(
            int idMascota,
            int idVeterinario,
            Integer idCita,
            LocalDateTime fechaAtencion,
            TipoRegistro tipoRegistro,
            String motivoConsulta,
            String diagnostico,
            String tratamiento,
            String observaciones,
            String nombreVacuna,
            String loteVacuna,
            LocalDate proximaDosis) {

        this.idMascota = idMascota;
        this.idVeterinario = idVeterinario;
        this.idCita = idCita;
        this.fechaAtencion = fechaAtencion;
        this.tipoRegistro = tipoRegistro;
        this.motivoConsulta = motivoConsulta;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.observaciones = observaciones;
        this.nombreVacuna = nombreVacuna;
        this.loteVacuna = loteVacuna;
        this.proximaDosis = proximaDosis;
    }

    public String getCodigoVisible() {
        return String.format("HC-%04d", idHistorial);
    }

    public int getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
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

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public LocalDateTime getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(LocalDateTime fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    public TipoRegistro getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(TipoRegistro tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNombreVacuna() {
        return nombreVacuna;
    }

    public void setNombreVacuna(String nombreVacuna) {
        this.nombreVacuna = nombreVacuna;
    }

    public String getLoteVacuna() {
        return loteVacuna;
    }

    public void setLoteVacuna(String loteVacuna) {
        this.loteVacuna = loteVacuna;
    }

    public LocalDate getProximaDosis() {
        return proximaDosis;
    }

    public void setProximaDosis(LocalDate proximaDosis) {
        this.proximaDosis = proximaDosis;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
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
            + tipoRegistro;
    }
}
