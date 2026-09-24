/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author weprg
 */
public class Producto {

    /**
     * Categorías permitidas por el ENUM de MySQL.
     */
    public enum Categoria {
        MEDICAMENTO,
        VACUNA,
        ALIMENTO,
        INSUMO,
        OTRO
    }

    private int idProducto;
    private String codigo;
    private String nombre;
    private Categoria categoria;
    private String descripcion;
    private int stockActual;
    private int stockMinimo;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private LocalDate fechaVencimiento;
    private boolean activo;
    private LocalDateTime fechaRegistro;

    /**
     * Constructor vacío.
     */
    public Producto() {
        this.stockActual = 0;
        this.stockMinimo = 0;
        this.activo = true;
    }

    /**
     * Constructor para registrar un producto nuevo.
     *
     * El stock inicial será cero. Para aumentarlo se registrará
     * posteriormente un movimiento de inventario.
     */
    public Producto(
            String codigo,
            String nombre,
            Categoria categoria,
            String descripcion,
            int stockMinimo,
            BigDecimal precioCompra,
            BigDecimal precioVenta,
            LocalDate fechaVencimiento) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.stockActual = 0;
        this.stockMinimo = stockMinimo;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.fechaVencimiento = fechaVencimiento;
        this.activo = true;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
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

    /**
     * Indica si el producto alcanzó o bajó del stock mínimo.
     */
    public boolean tieneStockBajo() {
        return stockActual <= stockMinimo;
    }

    /**
     * Indica si la fecha de vencimiento ya pasó.
     */
    public boolean estaVencido() {
        return fechaVencimiento != null
            && fechaVencimiento.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
