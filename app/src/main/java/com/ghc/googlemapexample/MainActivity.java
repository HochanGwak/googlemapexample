package com.ghc.googlemapexample;

import android.Manifest;
import android.app.VoiceInteractor;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button btn_gallery;
    private static final int REQUEST_IMAGE_CODE = 1002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_MEDIA_LOCATION}, 1);
        }


        btn_gallery = findViewById(R.id.btn_gallery);

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, REQUEST_IMAGE_CODE);
//                // Bottom 클래스에서 복붙함. 하지만 밑에 onActivityResult 오버라이드 부분은 복붙안함 추후 생각
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK && data != null && data.getClipData() != null) {

            ClipData clipData = data.getClipData();

            if (clipData != null) {

                try {
                    SlideBoard bottomSheet = new SlideBoard(clipData, getContentResolver());
                    bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");

                } catch (Exception e) {
                    Log.v("asdf", "MainActivity");
                    Log.v("asdf", e.toString());
                }


            } else {

            }
        } else {
            Log.v("asdf", resultCode + " : " + ((data == null) ? "dataIsNull" : "notNull") + " : " + ((data.getData() == null) ? "dataIsNull" : "notNull"));
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        Intent intent = getIntent();

        ArrayList<String> latList = intent.getStringArrayListExtra("lat");
        ArrayList<String> lngList = intent.getStringArrayListExtra("lng");
        String et_tx = intent.getStringExtra("et_tx");
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(10);

        if (latList != null) {

            Log.v("asdf", latList.size() + "개 위치");

            for (int i = 0; i < latList.size(); i++) {
                Log.v("asd", latList.get(i) + " / " + lngList.get(i));

                LatLng snowqualmie = new LatLng(Double.valueOf(latList.get(i)), Double.valueOf(lngList.get(i)));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(snowqualmie)

//                    .title("사진")
//                    .snippet("Snoqualmie Falls is located 25 miles east of Seattle.")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                InfoWindowData info = new InfoWindowData();
                info.setImage("snowqualmie");
                info.setHotel(et_tx);
                info.setUri(((ClipData)intent.getParcelableExtra("pic")).getItemAt(i).getUri());

                // info.setFood("Food : all types of restaurants available");
                // info.setTransport("Reach the site by bus, car and train.");

                CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this, ((ClipData)intent.getParcelableExtra("pic")).getItemAt(i).getUri());
                mMap.setInfoWindowAdapter(customInfoWindow);

                Marker m = mMap.addMarker(markerOptions);


                m.setTag(info);// 클릭했을때 실행되게
                //m.showInfoWindow();//

                mMap.moveCamera(CameraUpdateFactory.newLatLng(snowqualmie));

            }

        } else {

            LatLng snowqualmie = new LatLng(35.149955976812464, 126.9197760539669);


            mMap.moveCamera(CameraUpdateFactory.newLatLng(snowqualmie));
        }
    }
}
