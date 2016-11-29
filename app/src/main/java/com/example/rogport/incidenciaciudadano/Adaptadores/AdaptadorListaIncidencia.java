package com.example.rogport.incidenciaciudadano.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rogport.incidenciaciudadano.Activities.VerIncidencia;
import com.example.rogport.incidenciaciudadano.Incidencia;
import com.example.rogport.incidenciaciudadano.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by RogPort on 20/11/2016.
 */

public class AdaptadorListaIncidencia extends RecyclerView.Adapter<AdaptadorListaIncidencia.IncidenciaViewHolder>{

    private ArrayList<Incidencia> incidencias;
   // private Incidencia[] incidencias;
    Activity activity;
    private static Context cont;

    public AdaptadorListaIncidencia(ArrayList<Incidencia> incidencias, Activity activity){
        this.incidencias = incidencias;
        this.activity = activity;
    }

    @Override
    public IncidenciaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_incidencia,parent,false);
        return new IncidenciaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(IncidenciaViewHolder holder, int position) {
        final Incidencia incidencia = incidencias.get(position);
        Picasso.with(activity)
                .load(incidencia.getImagen())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgFoto);
        holder.txtUbicacion.setText(incidencia.getUbicacion());
        holder.txtDescripcion.setText(incidencia.getFecha());
        holder.panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               VerIncidencia.idImagen = Uri.parse(incidencia.getImagen());
                Intent i = new Intent(cont, VerIncidencia.class);
                i.putExtra("imagen",incidencia.getImagen());
                i.putExtra("id",incidencia.getId());
                i.putExtra("ubicacion",incidencia.getUbicacion());
                i.putExtra("descripcion",incidencia.getDescripcion());
                i.putExtra("likes",incidencia.getLikes());
               cont.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return incidencias.size();
    }

    public static class IncidenciaViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgFoto;
        private TextView txtUbicacion;
        private TextView txtDescripcion;
        private RelativeLayout panel;

    public IncidenciaViewHolder(View itemView){
        super(itemView);
        imgFoto = (ImageView) itemView.findViewById(R.id.fotoIncidencia);
        txtUbicacion = (TextView) itemView.findViewById(R.id.cardTxtUbicacion);
        txtDescripcion = (TextView) itemView.findViewById(R.id.cardTxtDescripcion);
        panel = (RelativeLayout) itemView.findViewById(R.id.panelCV);
        cont = itemView.getContext();
    }
    }
}
