package com.example.android.map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    int lan ;
    int lan2 ;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


            }


        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        final LatLng[] currentLocation = new LatLng[1];
        locationManager =  (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mMap = googleMap;
        final Boolean[] ischanged = {true};
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mMap.clear();


                currentLocation[0] = new LatLng(location.getLatitude(), location.getLongitude());
                if (ischanged[0]) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    mMap.addMarker(new MarkerOptions().position(currentLocation[0]).title("current location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation[0], 10));
//                    ischanged[0] =false;
                }

                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    Log.i("adreses",addresses.get(0).toString());
                    String adress = "";
                    if (addresses.get(0).getCountryName() != null){
                        adress=addresses.get(0).getCountryName()+" ";

                    }
                    if (addresses.get(0).getAdminArea() != null){
                        adress+=addresses.get(0).getAdminArea()+" ";

                    }
                    if (addresses.get(0).getThoroughfare() != null){
                        adress+=addresses.get(0).getThoroughfare();

                    }

                    Toast.makeText(MapsActivity.this, adress, Toast.LENGTH_LONG).show();


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }else{
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,20000,0,locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng lastlocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            mMap.addMarker(new MarkerOptions().position(lastlocation).title("last location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastlocation,10));


        }


        // Add a marker in Sydney and move the camera

    }
}
