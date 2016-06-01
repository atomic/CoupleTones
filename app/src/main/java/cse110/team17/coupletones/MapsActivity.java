package cse110.team17.coupletones;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener{

    private static final String TAG = "MapsActivity";

    private static final float DEFAULT_ZOOM = 15.0f;

    // distance within location to send notification (1/10 mile)
    private static final float NOTIFICATION_DISTANCE = 161.0f;

    // where to start the map by default
    private static final LatLng DEFAULT_LOCATION = new LatLng(40, -100);

    public static final String EXTRA_GET_USER = "cse110.team16.coupletones.user";
    public static final String EXTRA_GET_PARTNER_PHONE = "cse110.team16.coupletones.partner_phone";

    private GoogleMap mMap;
    private SharedPreferences sharedPrefs;

    private ArrayList<FavoriteLocation> favoriteLocations;
    private FavoriteLocation lastVisitedLocation;

    private String mPartnerNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Getting partner's number from previous activity
        mPartnerNumber = getIntent().getStringExtra(EXTRA_GET_PARTNER_PHONE);
        Log.d(TAG, "partner number : " + mPartnerNumber);

        // TODO: use this push to do something else,
        Utils.mFirebaseRef.child("coupletones").setValue("Going in to MapActivity");
        // Test send something to Firebase (TODO: extends)
    }

    @Override
    public void onMapLongClick(final LatLng point) {
        createAddDialog(point);
    }

    public void saveFavoriteLocation(FavoriteLocation favoriteLocation) {
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putInt("numLocations", favoriteLocations.size());

        // index of last element (marker just added)
        int i = favoriteLocations.size() - 1;
        editor.putFloat("lat" + i, (float) favoriteLocation.getLocation().latitude);
        editor.putFloat("lon" + i, (float) favoriteLocation.getLocation().longitude);
        editor.putString("title" + i, favoriteLocation.getTitle());

        editor.commit();

        favoriteLocations.add(favoriteLocation);
    }

    public void loadFavoriteLocations() {
        // load the saved locations
        int numLocations = sharedPrefs.getInt("numLocations", 0);
        if (numLocations > 0) {
            for (int i = 0; i < numLocations; i++) {
                double lat = (double) sharedPrefs.getFloat("lat" + i, 0);
                double lon = (double) sharedPrefs.getFloat("lon" + i, 0);
                String title = sharedPrefs.getString("title" + i, "NULL");

                FavoriteLocation favoriteLocation = new FavoriteLocation(title,
                        new LatLng(lat, lon));
                favoriteLocation.addToGoogleMap(mMap);

                favoriteLocations.add(favoriteLocation);
            }
        }
    }

    /**
     * Creates an alert dialog that allows the user to name and add a location.
     * @param point lat/lng of location
     */
    private void createAddDialog(final LatLng point) {
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

                        FavoriteLocation favoriteLocation = new FavoriteLocation(title, point);
                        favoriteLocation.addToGoogleMap(mMap);

                        saveFavoriteLocation(favoriteLocation);
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
        favoriteLocations = new ArrayList<>();

        sharedPrefs = getSharedPreferences("location", 0);
        loadFavoriteLocations();

        // define listener
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng position = new LatLng(location.getLatitude(), location.getLongitude());

                // check if you're close to a location here and send notification upstream
                for (FavoriteLocation favoriteLocation : favoriteLocations) {
                    if (favoriteLocation != lastVisitedLocation) {
                        float distance = favoriteLocation.distanceTo(position);

                        // if user is within distance of favorite location
                        if (distance < NOTIFICATION_DISTANCE) {
                            lastVisitedLocation = favoriteLocation;

                            sendMessage(mPartnerNumber, "Your partner visited location " +
                                    favoriteLocation.getTitle());

                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            // VIbrate for 500 ms
                            v.vibrate(500);
                        }
                    }
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

        // only update location if we move past the notification distance
        locationManager.requestLocationUpdates(locationProvider, 0,
                NOTIFICATION_DISTANCE, locationListener);

        // center at last known location
        Location lastKnownLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLoc != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLoc.getLatitude(),
                    lastKnownLoc.getLongitude()), DEFAULT_ZOOM));
        }
        else {
            // start in default location
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, DEFAULT_ZOOM));
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void sendMessage(String num, String msg) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(num, null, msg, null, null);
    }


    public static Intent newIntent(Context packageContext, String partnerNumber) {
        Intent i = new Intent(packageContext, MapsActivity.class);
        i.putExtra(EXTRA_GET_PARTNER_PHONE, partnerNumber);
        return i;
    }

}
