package com.example.cocabreak.models;

public class Historial {

    private String nombre;
    private String tipo;
    private long fecha;

    public Historial() {
    }

    public Historial(String nombre,
                     String tipo,
                     long fecha) {

        this.nombre = nombre;
        this.tipo = tipo;
        this.fecha = fecha;
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
}