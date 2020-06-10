package com.example.pixag.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.pixag.DataModels.MyLocation;
import com.example.pixag.Fragments.UploadImageFragment;
import com.example.pixag.Interfaces.FragmentUploadListener;
import com.example.pixag.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class UploadImageActivity extends AppCompatActivity implements OnMapReadyCallback, FragmentUploadListener{

    private MyLocation myLocation;
    private int requestCode = 404;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        Intent intent = getIntent();
        myLocation = new MyLocation(intent.getDoubleExtra("long", 36.0818155), intent.getDoubleExtra("lat", 12.121212));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);


        FragmentManager manager = getSupportFragmentManager();
        manager.findFragmentById(R.id.container_upload);
        Fragment fragment;
        fragment = new UploadImageFragment(myLocation, this);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container_upload, fragment, "0").commit();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, requestCode);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Set focus on Israel and mark current location
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.4117257, 35.0818155), 6));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())));
    }


    @Override
    public void onUploadPerformed(Bundle bundle) {

    }
}