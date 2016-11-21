package com.example.rogport.incidenciaciudadano.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rogport.incidenciaciudadano.R;

public class VerIncidencia extends AppCompatActivity {
    TextView TVIncidencia;
    ImageView imagen;
    public static int idImagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ver_incidencia);

        Toolbar appbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imagen = (ImageView) findViewById(R.id.fotoIncidencia);
        imagen.setImageResource(idImagen);

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
