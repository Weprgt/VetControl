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
public class Mascota {

    // Valores permitidos por el ENUM de MySQL.
    public enum Sexo {
        MACHO,
        HEMBRA,
        DESCONOCIDO
    }

    // Identificador generado automáticamente por MySQL.
    private int idMascota;

    // Identificador del cliente propietario.
    private int idCliente;

    // Número clínico único de la mascota.
    private String numeroExpediente;
    private String nombre;
    private String especie;
    private String raza;
    private Sexo sexo;
    private LocalDate fechaNacimiento;
    private String color;
    private String observaciones;
    private boolean activo;
    private LocalDateTime fechaRegistro;

    /*
     * Este dato se obtiene mediante una consulta JOIN con clientes.
     * No representa una columna de la tabla mascotas.
     */
    private String nombrePropietario;

    /**
     * Constructor vacío.
     */
    public Mascota() {
        this.sexo = Sexo.DESCONOCIDO;
        this.activo = true;
    }

    /**
     * Constructor utilizado para registrar una mascota nueva.
     * No recibe ID porque MySQL lo genera automáticamente.
     */
    public Mascota(
            int idCliente,
            String numeroExpediente,
            String nombre,
            String especie,
            String raza,
            Sexo sexo,
            LocalDate fechaNacimiento,
            String color,
            String observaciones) {

        this.idCliente = idCliente;
        this.numeroExpediente = numeroExpediente;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.color = color;
        this.observaciones = observaciones;
        this.activo = true;
    }

    /**
     * Constructor completo, útil al recuperar datos desde MySQL.
     */
    public Mascota(
            int idMascota,
            int idCliente,
            String numeroExpediente,
            String nombre,
            String especie,
            String raza,
            Sexo sexo,
            LocalDate fechaNacimiento,
            String color,
            String observaciones,
            boolean activo,
            LocalDateTime fechaRegistro) {

        this.idMascota = idMascota;
        this.idCliente = idCliente;
        this.numeroExpediente = numeroExpediente;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.color = color;
        this.observaciones = observaciones;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
    }

    /*
     * Código utilizado únicamente para mostrar la mascota en la interfaz.
     * Ejemplo: M-0001.
     */
    public String getCodigoVisible() {
        return String.format("M-%04d", idMascota);
    }

    public int getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    /**
     * Permite mostrar el nombre de la mascota en componentes como JComboBox.
     */
    @Override
    public String toString() {
        return nombre + " - " + numeroExpediente;
    }
}
