package com.example.rogport.incidenciaciudadano.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rogport.incidenciaciudadano.R;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class VerIncidencia extends AppCompatActivity {
    TextView TVIncidencia,TVubicacion,TVdescripcion;
    ImageView imagen;
    int ID,likes,size;

    public static Uri idImagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ver_incidencia);

        TVdescripcion = (TextView) findViewById(R.id.textoDescripcion);
        TVubicacion = (TextView) findViewById(R.id.textoLocalizacion);
        imagen = (ImageView) findViewById(R.id.fotoIncidencia);
        Bundle parametros = getIntent().getExtras();
        String url = parametros.getString("imagen");
        ID = parametros.getInt("id");
        final String ubicacion = parametros.getString("ubicacion");
        String descripcion = parametros.getString("descripcion");
        TVdescripcion.setText(descripcion);
        TVubicacion.setText(ubicacion);
        likes = parametros.getInt("likes");

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
                i.putExtra("ID",ID);
                i.putExtra("Ubicacion",ubicacion);
               startActivity(i);
            }
        });
    }

    public void darLike(View v){
        DatabaseReference refLikes = FirebaseDatabase.getInstance().getReference("Incidencias");
        final DatabaseReference refSize = FirebaseDatabase.getInstance().getReference("size");
        final DatabaseReference refID = refLikes.child(String.valueOf(ID));
        refSize.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                size = Integer.parseInt(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        refID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference refL = refID.child("likes");
                refL.setValue(likes + 1);
                if(likes >= 2) {
                    refID.removeValue();
                    refSize.setValue(size-1);
                    finish();
                }else {
                    finish();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
