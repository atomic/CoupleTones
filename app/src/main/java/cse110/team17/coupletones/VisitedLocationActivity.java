package cse110.team17.coupletones;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class VisitedLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        ArrayDeque<FavoriteLocation> visitedLocations = MapsActivity.visitedPartnerLocations;

        setContentView(R.layout.activity_visited_location_acitivity);

        ListView partnerLocationsView = (ListView) findViewById(R.id.partner_locations_view);

        ArrayList<String> list = new ArrayList<>();
        if (visitedLocations != null) {
            for (FavoriteLocation location : visitedLocations) {
                list.add(location.getTitle() + ": " + location.getLocation());
            }
        }

        partnerLocationsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));
    }
}
