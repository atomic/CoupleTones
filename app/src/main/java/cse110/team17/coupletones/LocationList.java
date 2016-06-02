package cse110.team17.coupletones;

import android.content.SharedPreferences;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tylersy on 6/1/16.
 */
public class LocationList {
    //private ArrayList<FavoriteLocation> favoriteLocations = new ArrayList<>();
    private Map<String, FavoriteLocation> favoriteLocations = new HashMap<>();
    Map<String, String> mLocationInfo = new HashMap<>();
    Map<String, Map<String, String>> places = new HashMap<>();

    private SharedPreferences sharedPrefs;

    private Firebase mUserLocationListRef;

    public LocationList(SharedPreferences sharedPrefs, Firebase userLocationListRef) {
        this.sharedPrefs = sharedPrefs;
        mUserLocationListRef = userLocationListRef;
    }

    public void addLocation(FavoriteLocation favoriteLocation) {
        //favoriteLocations.add(favoriteLocation);
        favoriteLocations.put(favoriteLocation.getTitle(), favoriteLocation);
        saveToSharedPrefs(favoriteLocation);
        saveToFirebase(favoriteLocation);
    }

    public void loadFromSharedPrefs(GoogleMap mMap) {
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

                favoriteLocations.put(favoriteLocation.getTitle(), favoriteLocation);

                mLocationInfo.put("lon", Double.toString(favoriteLocation.getLocation().longitude) );
                mLocationInfo.put("lat", Double.toString(favoriteLocation.getLocation().latitude) );
                places.put(favoriteLocation.getTitle(), mLocationInfo);
            }

            mUserLocationListRef.setValue(places);
        }
    }

    public Map<String, FavoriteLocation> getLocations() {
        return favoriteLocations;
    }

    private void saveToSharedPrefs(FavoriteLocation favoriteLocation) {
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putInt("numLocations", favoriteLocations.size());

        // index of last element (marker just added)
        int i = favoriteLocations.size() - 1;
        editor.putFloat("lat" + i, (float) favoriteLocation.getLocation().latitude);
        editor.putFloat("lon" + i, (float) favoriteLocation.getLocation().longitude);
        editor.putString("title" + i, favoriteLocation.getTitle());

        editor.commit();
    }

    private void saveToFirebase(FavoriteLocation favoriteLocation) {
        String key = favoriteLocation.getTitle();

        mLocationInfo.put("lon", Double.toString(favoriteLocation.getLocation().longitude) );
        mLocationInfo.put("lat", Double.toString(favoriteLocation.getLocation().latitude) );
        places.put(key, mLocationInfo);
        mUserLocationListRef.setValue(places);
    }
}
