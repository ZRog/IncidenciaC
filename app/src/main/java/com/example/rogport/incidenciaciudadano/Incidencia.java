package com.example.rogport.incidenciaciudadano;

/**
 * Created by RogPort on 20/11/2016.
 */

public class Incidencia {

    private int id,imagen;
    private String ubicacion,descripcion;

    public Incidencia(int id, int imagen, String ubi, String desc) {
        this.id = id;
        this.imagen = imagen;
        this.ubicacion = ubi;
        this.descripcion = desc;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
