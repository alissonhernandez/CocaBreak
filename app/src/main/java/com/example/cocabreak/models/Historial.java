package com.example.cocabreak.models;

public class Historial {

    private String nombre;
    private String tipo;
    private long fecha;
    private int cantidad;

    public Historial() {
    }

    public Historial(
            String nombre,
            String tipo,
            long fecha,
            int cantidad) {

        this.nombre = nombre;
        this.tipo = tipo;
        this.fecha = fecha;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public long getFecha() {
        return fecha;
    }

    public int getCantidad() {
        return cantidad;
    }
}