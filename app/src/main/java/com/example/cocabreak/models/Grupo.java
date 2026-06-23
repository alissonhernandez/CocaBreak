package com.example.cocabreak.models;

import java.util.HashMap;
import java.util.Map;

public class Grupo {

    private String id;
    private String nombre;
    private String descripcion;
    private String creador;
    private Map<String, Boolean> miembros;

    public Grupo() {}

    public Grupo(String id, String nombre, String descripcion, String creador) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creador = creador;
        this.miembros = new HashMap<>();
    }

    public String getId()          { return id; }
    public String getNombre()      { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getCreador()     { return creador; }
    public Map<String, Boolean> getMiembros() { return miembros; }

    public void setId(String id)                         { this.id = id; }
    public void setNombre(String nombre)                 { this.nombre = nombre; }
    public void setDescripcion(String descripcion)       { this.descripcion = descripcion; }
    public void setCreador(String creador)               { this.creador = creador; }
    public void setMiembros(Map<String, Boolean> m)      { this.miembros = m; }

    public boolean esMiembro(String uid) {
        return miembros != null && Boolean.TRUE.equals(miembros.get(uid));
    }
}
