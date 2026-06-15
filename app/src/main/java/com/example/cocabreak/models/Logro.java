package com.example.cocabreak.models;

public class Logro {

    private String titulo;
    private String descripcion;
    private boolean desbloqueado;

    public Logro(String titulo,
                 String descripcion,
                 boolean desbloqueado) {

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.desbloqueado = desbloqueado;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isDesbloqueado() {
        return desbloqueado;
    }
}
