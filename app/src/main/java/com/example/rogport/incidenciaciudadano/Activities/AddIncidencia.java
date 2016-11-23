package com.example.rogport.incidenciaciudadano.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rogport.incidenciaciudadano.Incidencia;
import com.example.rogport.incidenciaciudadano.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class AddIncidencia extends AppCompatActivity {

    int ID;
    ImageView tomarFoto;
    EditText ubicacion,descripcion;
    public static final int REQUEST_CAPTURE = 1;
    private DatabaseReference mDatabase;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_incidencia);

        tomarFoto = (ImageView) findViewById(R.id.tomarFoto);
        ubicacion = (EditText) findViewById(R.id.input_ubicacion);
        descripcion = (EditText) findViewById(R.id.input_descripcion);
        tomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, REQUEST_CAPTURE);
            }
        });

        Toolbar appbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap foto = (Bitmap) extras.get("data");
            tomarFoto.setImageBitmap(foto);
        }
    }

    public void subirDatosDB(View v){

        DatabaseReference refID2 = FirebaseDatabase.getInstance().getReference("ID");
        refID2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ID = Integer.parseInt(dataSnapshot.getValue().toString());
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl("gs://incidenciaciudadano.appspot.com");
                StorageReference bancoImagesRef = storageRef.child(String.valueOf(ID));
                tomarFoto.setDrawingCacheEnabled(true);
                tomarFoto.buildDrawingCache();
                Bitmap bitmap = tomarFoto.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                StorageReference idRef = storageRef.child(String.valueOf(ID));
                Log.d("AVISAZO",String.valueOf(ID));
                byte[] data1 = baos.toByteArray();
                UploadTask uploadTask1 = idRef.putBytes(data1);
                uploadTask1.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(getApplicationContext(),exception.toString(),Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        url = downloadUrl.toString();
                        Incidencia incidencia = new Incidencia(ID,url,ubicacion.getText().toString(),descripcion.getText().toString());
                        mDatabase.child("Incidencias").child(String.valueOf(ID)).setValue(incidencia);
                        mDatabase.child("ID").setValue(ID+1);
                        finish();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        });


    }
}
