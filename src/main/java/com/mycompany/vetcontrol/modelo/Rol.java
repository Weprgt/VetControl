/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.modelo;

/**
 *
 * @author weprg
 */
public class Rol {

    // Identificador generado por MySQL.
    private int idRol;

    // Ejemplos: ADMINISTRADOR, VETERINARIO y RECEPCIONISTA.
    private String nombre;

    // Explicación general de las funciones del rol.
    private String descripcion;

    /**
     * Constructor vacío.
     */
    public Rol() {
    }

    /**
     * Constructor utilizado al recuperar un rol de la base de datos.
     */
    public Rol(int idRol, String nombre, String descripcion) {
        this.idRol = idRol;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Permite mostrar directamente el nombre del rol
     * dentro de componentes como JComboBox.
     */
    @Override
    public String toString() {
        return nombre;
    }
}
