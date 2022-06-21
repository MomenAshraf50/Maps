package com.example.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.maps.databinding.ActivityMapsBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final String TAG = "MapsActivity";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Geocoder geocoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //getServicesLocation();
        getMomenLocation();
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull @NotNull LatLng latLng) {
                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    if (addressList.isEmpty()){
                        Toast.makeText(MapsActivity.this, "no information"
                                , Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Address address = addressList.get(0);
                    String streetAddress = address.getAddressLine(0);

                    mMap.addMarker(new MarkerOptions().position(latLng)
                    .title(streetAddress));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //OnClickMarker

        /* mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull @NotNull LatLng latLng) {
                mMap.clear();
                MarkerOptions markerOptions =new MarkerOptions()
                        .position(latLng);
                mMap.addMarker(markerOptions);
            }
        });*/


        /*mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setTrafficEnabled(true);
        //30.326655,31.7065161
        LatLng latLng = new LatLng(30.326655,31.7065161);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("my location");
        mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,15);
        mMap.animateCamera(cameraUpdate);*/

        //LocationByName
        /*try {
            List<Address> addressList = geocoder.getFromLocationName("Cairo",1);
            Address address = addressList.get(0);
            Log.i(TAG, "onMapReady: "+address.toString());

            LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(address.getLocality());
            mMap.addMarker(markerOptions);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,10);
            mMap.animateCamera(cameraUpdate);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "onMapReady: ",e );
        }*/


        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

    }
    //return data from firebase
    private void getServicesLocation(){
        firestore.collection("serviceLocation")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        for (DocumentSnapshot snapshot:value.getDocuments()){
                            ServicesLocation servicesLocation = snapshot.toObject(ServicesLocation.class);
                            Log.i(TAG, "onEvent: "+servicesLocation.toString());
                            LatLng latLng = new LatLng(servicesLocation.getLat(),servicesLocation.getLng());
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(latLng);

                            mMap.addMarker(markerOptions);
                        }
                    }
                });
    }
    private void getMomenLocation(){
        firestore.collection("locations")
                .document("momen")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                            ServicesLocation servicesLocation = value.toObject(ServicesLocation.class);
                            if (servicesLocation == null) return;
                            Log.i(TAG, "onEvent: "+servicesLocation.toString());
                            LatLng latLng = new LatLng(servicesLocation.getLat(),servicesLocation.getLng());
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(latLng);

                            mMap.addMarker(markerOptions);
                    }
                });
    }
}