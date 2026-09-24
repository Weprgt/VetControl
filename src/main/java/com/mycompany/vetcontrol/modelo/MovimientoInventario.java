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
public class MovimientoInventario {

    /**
     * Valores permitidos por el ENUM de MySQL.
     */
    public enum TipoMovimiento {
        ENTRADA,
        SALIDA,
        AJUSTE_ENTRADA,
        AJUSTE_SALIDA
    }

    private int idMovimiento;
    private int idProducto;
    private int idUsuario;
    private TipoMovimiento tipoMovimiento;
    private int cantidad;
    private String motivo;
    private LocalDateTime fechaMovimiento;

    /*
     * Datos recuperados mediante JOIN.
     * No son columnas adicionales de movimientos_inventario.
     */
    private String codigoProducto;
    private String nombreProducto;
    private String nombreUsuario;

    /**
     * Constructor vacío.
     */
    public MovimientoInventario() {
    }

    /**
     * Constructor para registrar un movimiento nuevo.
     */
    public MovimientoInventario(
            int idProducto,
            int idUsuario,
            TipoMovimiento tipoMovimiento,
            int cantidad,
            String motivo) {

        this.idProducto = idProducto;
        this.idUsuario = idUsuario;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.motivo = motivo;
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    /**
     * Determina si el movimiento aumenta las existencias.
     */
    public boolean esEntrada() {
        return tipoMovimiento == TipoMovimiento.ENTRADA
            || tipoMovimiento == TipoMovimiento.AJUSTE_ENTRADA;
    }

    /**
     * Determina si el movimiento reduce las existencias.
     */
    public boolean esSalida() {
        return tipoMovimiento == TipoMovimiento.SALIDA
            || tipoMovimiento == TipoMovimiento.AJUSTE_SALIDA;
    }

    @Override
    public String toString() {
        return tipoMovimiento
            + " - "
            + nombreProducto
            + " - "
            + cantidad;
    }
}