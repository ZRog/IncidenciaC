package com.example.rogport.incidenciaciudadano.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.rogport.incidenciaciudadano.Adaptadores.AdaptadorListaIncidencia;
import com.example.rogport.incidenciaciudadano.Incidencia;
import com.example.rogport.incidenciaciudadano.R;

import java.util.ArrayList;

public class ListaIncidencias extends AppCompatActivity {

    ArrayList<Incidencia> incidencias;
    private RecyclerView listaIncidencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lista_incidencias);

        Toolbar appbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listaIncidencias = (RecyclerView) findViewById(R.id.rvIncidencias);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaIncidencias.setLayoutManager(llm);
        inicializarListaIncidencias();
        inicializarAdaptador();


    }
    public void inicializarAdaptador(){
        AdaptadorListaIncidencia adaptador = new AdaptadorListaIncidencia(incidencias,this);
        listaIncidencias.setAdapter(adaptador);
    }
    public void inicializarListaIncidencias(){
        incidencias = new ArrayList<Incidencia>();
        incidencias.add(new Incidencia(1,R.drawable.banco,"calle falsa 123","Banco roto"));
        incidencias.add(new Incidencia(2,R.drawable.socavon,"calle falsa 456","Socavon"));
        incidencias.add(new Incidencia(1,R.drawable.columpio,"calle falsa 123","Columpio roto"));
    }

}
