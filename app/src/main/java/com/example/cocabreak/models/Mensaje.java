package com.example.cocabreak.models;

public class Mensaje {

    private String texto;
    private boolean enviadoPorMi;

    public Mensaje(String texto, boolean enviadoPorMi) {
        this.texto = texto;
        this.enviadoPorMi = enviadoPorMi;
    }

    public String getTexto() {
        return texto;
    }

    public boolean isEnviadoPorMi() {
        return enviadoPorMi;
    }
}