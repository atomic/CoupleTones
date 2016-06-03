package cse110.team17.coupletones;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class SetPartnerLocationToneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_partner_location_tone);
        final ListView partnerLocationsView = (ListView) findViewById(R.id.partner_locations_view);

//        Bundle b = getIntent().getExtras();
        HashMap<String, FavoriteLocation> partnerLocations = MapsActivity.partnerLocations;
        ArrayList<String> list = new ArrayList<>();
        if (partnerLocations != null) {
            for (HashMap.Entry<String, FavoriteLocation> entry : partnerLocations.entrySet()) {
                list.add(entry.getKey() + ": " + entry.getValue().getLocation());
            }
        }
        partnerLocationsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));

        partnerLocationsView.setClickable(true);
        partnerLocationsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = partnerLocationsView.getItemAtPosition(position);
            }
        });
    }
}
