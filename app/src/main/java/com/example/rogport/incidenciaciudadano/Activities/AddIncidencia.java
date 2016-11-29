package com.example.rogport.incidenciaciudadano.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rogport.incidenciaciudadano.FireBaseInstanceIdService;
import com.example.rogport.incidenciaciudadano.Incidencia;
import com.example.rogport.incidenciaciudadano.Mail.Config;
import com.example.rogport.incidenciaciudadano.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddIncidencia extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    int ID, size;
    ImageView tomarFoto;
    EditText ubicacion, descripcion;
    public static final int REQUEST_CAPTURE = 1;
    private DatabaseReference mDatabase;
    private String url,city,ubiBuena;
    private boolean hechoFoto = false, calleInv = false;
    GoogleApiClient mGoogleApiClient;
    Geocoder geocoder;
    private Double lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        geocoder = new Geocoder(getApplicationContext());

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


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap foto = (Bitmap) extras.get("data");
            tomarFoto.setImageBitmap(foto);
            hechoFoto = true;
        }
    }

    public void obtenerUbi(View v) throws IOException {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Location ultimaLoc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            String Calle = geocoder.getFromLocation(ultimaLoc.getLatitude(), ultimaLoc.getLongitude(), 1).get(0).getAddressLine(0).toString();
            ubicacion.setText(Calle);
            //LatLng latlong = new LatLng(geocoder.getFromLocationName("Carrer Cervantes,Badalona",1).get(0).getLatitude(),geocoder.getFromLocationName("Carrer Cervantes,Badalona",1).get(0).getLongitude());

            //Calle = String.valueOf(geocoder.getFromLocationName("Carrer Cervantes,Badalona",1).get(0).getLatitude());
            //Calle = String.valueOf(geocoder.getFromLocationName("Carrer Cervantes,Badalona",1).get(0).getLongitude());

        }

    }

    public void subirDatosDB(View v) {
        String ubi = ubicacion.getText().toString();
        String desc = descripcion.getText().toString();
        if (!ubi.matches("") && !desc.matches("") && hechoFoto) {
            DatabaseReference refSize = FirebaseDatabase.getInstance().getReference("size");
            DatabaseReference refID2 = FirebaseDatabase.getInstance().getReference("ID");
            refSize.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    size = Integer.parseInt(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
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
                    byte[] data1 = baos.toByteArray();
                    UploadTask uploadTask1 = idRef.putBytes(data1);
                    uploadTask1.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            url = downloadUrl.toString();
                            try {
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                }
                            Location ultimaLoc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            city = geocoder.getFromLocation(ultimaLoc.getLatitude(), ultimaLoc.getLongitude(), 1).get(0).getLocality().toString();
                            if(geocoder.getFromLocationName(ubicacion.getText().toString() + ", " + city,1).size()==0){
                                    calleInv=true;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(!calleInv) {
                            try {
                                lat = geocoder.getFromLocationName(ubicacion.getText().toString() + ", " + city,1).get(0).getLatitude();
                                lon = geocoder.getFromLocationName(ubicacion.getText().toString() + ", " + city,1).get(0).getLongitude();
                                ubiBuena = geocoder.getFromLocationName(ubicacion.getText().toString() + ", " + city,1).get(0).getAddressLine(0);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            List<Double> listaTemp = new ArrayList<Double>();
                            listaTemp.add(lat);
                            listaTemp.add(lon);
                            Calendar calendar = Calendar.getInstance();
                            String mes = String.valueOf(calendar.get(Calendar.MONTH));
                            String dia = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                            String year = String.valueOf(calendar.get(Calendar.YEAR));
                            Incidencia incidencia = new Incidencia(ID, url, ubiBuena, descripcion.getText().toString(),listaTemp,dia+"/"+mes+"/"+year);
                            Log.d("AVISOINC",incidencia.getFecha() + "no null");
                            mDatabase.child("Incidencias").child(String.valueOf(ID)).setValue(incidencia);
                            mDatabase.child("ID").setValue(ID + 1);
                            mDatabase.child("size").setValue(size + 1);
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.incidenciaSend),Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                            Toast.makeText(AddIncidencia.this,getResources().getString(R.string.calleInvalida),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        });
    hechoFoto = false;

    }
        else {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.faltanCampos),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }


}
