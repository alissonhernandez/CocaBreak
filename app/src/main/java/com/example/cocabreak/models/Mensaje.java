package com.example.cocabreak.models;

public class Mensaje {

    private String texto;
    private boolean enviadoPorMi;

    private String nombre;
    private String hora;

    public Mensaje() {
    }

    public Mensaje(
            String texto,
            boolean enviadoPorMi,
            String nombre,
            String hora
    ) {
        this.texto = texto;
        this.enviadoPorMi = enviadoPorMi;
        this.nombre = nombre;
        this.hora = hora;
    }

    public String getTexto() {
        return texto;
    }

    public boolean isEnviadoPorMi() {
        return enviadoPorMi;
    }

    public String getNombre() {
        return nombre;
    }

    public String getHora() {
        return hora;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setEnviadoPorMi(boolean enviadoPorMi) {
        this.enviadoPorMi = enviadoPorMi;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}