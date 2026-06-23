package com.example.cocabreak.network;

public class ConsumoRequest {
    public String tipo;
    public int cantidad_ml;
    public String fecha;
    public ConsumoRequest(String tipo, int cantidad_ml, String fecha) {
        this.tipo = tipo;
        this.cantidad_ml = cantidad_ml;
        this.fecha = fecha;
    }
}