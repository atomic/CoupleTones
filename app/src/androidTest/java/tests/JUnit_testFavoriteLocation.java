package tests;

import com.google.android.gms.maps.model.LatLng;

import junit.framework.TestCase;

import cse110.team17.coupletones.FavoriteLocation;

/**
 * Created by tylersy on 5/8/16.
 */
public class JUnit_testFavoriteLocation extends TestCase {
    public void test_create() {
        FavoriteLocation favoriteLocation = new FavoriteLocation("test", new LatLng(-15, 10));
        assertEquals(favoriteLocation.getTitle(), "test");
        assertEquals(favoriteLocation.getLocation().latitude, -15);
        assertEquals(favoriteLocation.getLocation().longitude, 10);
    }

    public void test_distance() {
        // distance between (15.9995S, 10E) and (16S, 10E) = ~55m
        FavoriteLocation favoriteLocation = new FavoriteLocation("test", new LatLng(-15.9995, 10));
        FavoriteLocation favoriteLocation2 = new FavoriteLocation("test", new LatLng(-16, 10));

        float distance = favoriteLocation.distanceTo(favoriteLocation2.getLocation());
        assertTrue(distance < 60);
        assertTrue(distance > 50);
    }
}
