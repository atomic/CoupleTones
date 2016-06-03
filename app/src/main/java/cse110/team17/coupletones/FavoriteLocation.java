package cse110.team17.coupletones;

import android.location.Location;
import android.media.Ringtone;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;

/**
 * Created by tylersy on 5/8/16.
 */
public class FavoriteLocation {
    private String title;
    private LatLng location;
    private Ringtone tone;

    public FavoriteLocation(String title, LatLng location) {
        this.title = title;
        this.location = location;
    }

    public void addToGoogleMap(GoogleMap map) {
        map.addMarker(new MarkerOptions().title(title).position(location));
    }

    public String getTitle() {
        return title;
    }

    public LatLng getLocation() {
        return location;
    }

    public float distanceTo(LatLng location) {
        float [] results = new float[3];

        Location.distanceBetween(location.latitude, location.longitude,
                this.location.latitude, this.location.longitude,
                results);

        return results[0];
    }

    public void setTone(Ringtone tone) {
        this.tone = tone;
    }

    public Ringtone getTone() {
        return tone;
    }
}

