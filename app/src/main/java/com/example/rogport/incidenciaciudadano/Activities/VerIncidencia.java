package com.example.rogport.incidenciaciudadano.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rogport.incidenciaciudadano.R;
import com.squareup.picasso.Picasso;

public class VerIncidencia extends AppCompatActivity {
    TextView TVIncidencia;
    ImageView imagen;
    public static Uri idImagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ver_incidencia);

        imagen = (ImageView) findViewById(R.id.fotoIncidencia);
        Bundle parametros = getIntent().getExtras();
        String url = parametros.getString("imagen");
        Picasso.with(VerIncidencia.this)
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .into(imagen);

        Toolbar appbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //imagen.setImageURI(idImagen);*/

        TVIncidencia=(TextView)findViewById(R.id.TextIncidencia);
        TVIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(VerIncidencia.this, Reporte.class);
               startActivity(i);
            }
        });
    }
}
