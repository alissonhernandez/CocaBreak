package com.example.cocabreak.models;

public class Conversacion {

    private String nombre;
    private String ultimoMensaje;
    private String hora;
    private int imagen;

    public Conversacion(
            String nombre,
            String ultimoMensaje,
            String hora,
            int imagen) {

        this.nombre = nombre;
        this.ultimoMensaje = ultimoMensaje;
        this.hora = hora;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public String getHora() {
        return hora;
    }

    public int getImagen() {
        return imagen;
    }
}