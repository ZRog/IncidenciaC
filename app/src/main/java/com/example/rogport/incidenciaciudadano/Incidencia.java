package com.example.rogport.incidenciaciudadano;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by RogPort on 20/11/2016.
 */

public class Incidencia {

    private int id;
    private String imagen;
    private String ubicacion,descripcion;

    public Incidencia(int id, String imagen, String ubi, String desc) {
        this.id = id;
        this.imagen = imagen;
        this.ubicacion = ubi;
        this.descripcion = desc;
    }

    public Incidencia(){}

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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
