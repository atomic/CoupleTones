package cse110.team17.coupletones;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener{

    private static final float defaultZoom = 14.0f;

    private GoogleMap mMap;
    private SharedPreferences sharedPrefs;
    private ArrayList<Marker> markers;
    private boolean isMapCentered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapLongClick(final LatLng point) {
        View alertView = LayoutInflater.from(MapsActivity.this).inflate(R.layout.alert_add, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(alertView);

        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Save",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // name of the marker
                        String title = ((EditText) alertDialog.findViewById(R.id.etTitle)).
                                getText().toString();

                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(point)
                                .title(title));
                        markers.add(marker);

                        // save the marker
                        SharedPreferences.Editor editor = sharedPrefs.edit();

                        editor.putInt("numLocations", markers.size());

                        // index of last element (marker just added)
                        int i = markers.size() - 1;
                        editor.putFloat("lat" + i, (float) point.latitude);
                        editor.putFloat("lon" + i, (float) point.longitude);
                        editor.putString("title" + i, title);

                        editor.commit();
                    }
                });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }
                });

        alertDialog.show();
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
        mMap = googleMap;
        markers = new ArrayList<>();

        // load the saved locations
        sharedPrefs = getSharedPreferences("location", 0);
        int numLocations = sharedPrefs.getInt("numLocations", 0);
        if (numLocations > 0) {
            for (int i = 0; i < numLocations; i++) {
                double lat = (double) sharedPrefs.getFloat("lat" + i, 0);
                double lon = (double) sharedPrefs.getFloat("lon" + i, 0);
                String title = sharedPrefs.getString("title" + i, "NULL");

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lon))
                        .title(title));
                markers.add(marker);
            }
        }

        // define listener
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (!isMapCentered) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                            location.getLongitude()), defaultZoom));
                    isMapCentered = true;
                }

                // check if you're close to a location here
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

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
            Log.d("test1", "ins");
            return;
        } else if (mMap != null) {
            Log.d("test2", "outs");
            mMap.setMyLocationEnabled(true);
            mMap.setOnMapLongClickListener(this);
        }
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);

        // center at last known location
        Location lastKnownLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLoc != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLoc.getLatitude(),
                    lastKnownLoc.getLongitude()), defaultZoom));
            isMapCentered = true;
        }
        else {
            // start in default location
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), defaultZoom));
            isMapCentered = false;
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
