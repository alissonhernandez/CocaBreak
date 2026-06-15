package com.example.cocabreak.models;

public class Reto {

    private String titulo;
    private String descripcion;
    private String progreso;
    private boolean bloqueado;

    public Reto(String titulo,
                String descripcion,
                String progreso,
                boolean bloqueado) {

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.progreso = progreso;
        this.bloqueado = bloqueado;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getProgreso() {
        return progreso;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }
}