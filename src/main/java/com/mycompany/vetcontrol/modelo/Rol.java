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

    private int idRol;
    private String nombre;
    private String descripcion;

    public Rol() {
    }

    public Rol(
            int idRol,
            String nombre,
            String descripcion) {

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
     * Permite que el JComboBox muestre el nombre
     * en lugar de la dirección del objeto.
     */
    @Override
    public String toString() {
        return nombre;
    }
}