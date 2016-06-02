package cse110.team17.coupletones;

import android.location.Location;
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
public class FavoriteLocation implements Parcelable {
    private String title;
    private LatLng location;

    public FavoriteLocation(String title, LatLng location) {
        this.title = title;
        this.location = location;
    }

    public FavoriteLocation(Parcel p) {
        title = p.readString();
        double lat = p.readLong();
        double lon = p.readLong();
        location = new LatLng(lat, lon);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeDouble(location.latitude);
        dest.writeDouble(location.longitude);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public FavoriteLocation createFromParcel(Parcel in) {
            return new FavoriteLocation(in);
        }

        public FavoriteLocation[] newArray(int size) {
            return new FavoriteLocation[size];
        }
    };
}

