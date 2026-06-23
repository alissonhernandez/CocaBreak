package com.example.cocabreak.models;

public class NotificacionAgua {

    private String titulo;
    private String mensaje;
    private long fecha;
    private String tipo;

    public NotificacionAgua() {
    }

    public NotificacionAgua(String titulo, String mensaje, long fecha, String tipo) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public String getTitulo() { return titulo; }
    public String getMensaje() { return mensaje; }
    public long getFecha() { return fecha; }
    public String getTipo() { return tipo; }
}