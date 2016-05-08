package cse110.team17.coupletones;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by tylersy on 5/8/16.
 */
public class FavoriteLocation {
    private Marker marker;
    private String title;
    private LatLng location;

    public FavoriteLocation(String title, LatLng location) {
        this.title = title;
        this.location = location;
    }

    public void addToGoogleMap(GoogleMap map) {
        marker = map.addMarker(new MarkerOptions().title(title).position(location));
    }

    public String getTitle() {
        return marker.getTitle();
    }

    public LatLng getLocation() {
        return marker.getPosition();
    }

    public float distanceTo(LatLng location) {
        float [] results = new float[3];

        Location.distanceBetween(location.latitude, location.longitude,
                marker.getPosition().latitude, marker.getPosition().longitude,
                results);

        return results[0];
    }
}
