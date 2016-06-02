package cse110.team17.coupletones;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;

public class PartnerLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        HashMap<String, FavoriteLocation> partnerLocations = MapsActivity.partnerLocations;

        setContentView(R.layout.activity_partner_location_acitivity);

        ListView partnerLocationsView = (ListView) findViewById(R.id.partner_locations_view);

        Log.d("intent", "received intent, partnerLocations: " + partnerLocations.toString());

        ArrayList<String> list = new ArrayList<>();
        if (partnerLocations != null) {
            for (HashMap.Entry<String, FavoriteLocation> entry : partnerLocations.entrySet()) {
                list.add(entry.getKey() + ": " + entry.getValue().getLocation());
            }
        }

        partnerLocationsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));
    }
}
