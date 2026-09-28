/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.vetcontrol.modelo;

/**
 *
 * @author weprg
 */
public class ResumenInicio {

    private int clientesActivos;
    private int mascotasActivas;
    private int citasHoy;
    private int productosStockBajo;

    /**
     * Constructor vacío.
     */
    public ResumenInicio() {
    }

    /**
     * Constructor con todos los datos del resumen.
     */
    public ResumenInicio(
            int clientesActivos,
            int mascotasActivas,
            int citasHoy,
            int productosStockBajo) {

        this.clientesActivos = clientesActivos;
        this.mascotasActivas = mascotasActivas;
        this.citasHoy = citasHoy;
        this.productosStockBajo = productosStockBajo;
    }

    public int getClientesActivos() {
        return clientesActivos;
    }

    public void setClientesActivos(int clientesActivos) {
        this.clientesActivos = clientesActivos;
    }

    public int getMascotasActivas() {
        return mascotasActivas;
    }

    public void setMascotasActivas(int mascotasActivas) {
        this.mascotasActivas = mascotasActivas;
    }

    public int getCitasHoy() {
        return citasHoy;
    }

    public void setCitasHoy(int citasHoy) {
        this.citasHoy = citasHoy;
    }

    public int getProductosStockBajo() {
        return productosStockBajo;
    }

    public void setProductosStockBajo(
            int productosStockBajo) {

        this.productosStockBajo =
            productosStockBajo;
    }
}
