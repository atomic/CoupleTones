package cse110.team17.coupletones;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class PartnerLocationActivity extends AppCompatActivity {
    private ArrayList<String> list = new ArrayList<>();
    private HashSet<Integer> requests = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        final HashMap<String, FavoriteLocation> partnerLocations = MapsActivity.partnerLocations;

        setContentView(R.layout.activity_partner_location_acitivity);

        ListView partnerLocationsView = (ListView) findViewById(R.id.partner_locations_view);
        partnerLocationsView.setClickable(true);

        Log.d("intent", "received intent, partnerLocations: " + partnerLocations.toString());

        if (partnerLocations != null) {
            for (HashMap.Entry<String, FavoriteLocation> entry : partnerLocations.entrySet()) {
                list.add(entry.getKey() + ": " + entry.getValue().getLocation());
            }
        }

        partnerLocationsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));

        partnerLocationsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = null;

                String entry = list.get(position);
                String title = entry.substring(0, entry.indexOf(":"));
                FavoriteLocation favoriteLocation = MapsActivity.partnerLocations.get(title);
                if (favoriteLocation != null) {
                    uri = favoriteLocation.getUri();
                }

                requests.add(position);
                Intent i = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                i.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                i.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
                i.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, uri);
                startActivityForResult(i, position);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requests.contains(requestCode) && resultCode == RESULT_OK) {
            String entry = list.get(requestCode);
            String title = entry.substring(0, entry.indexOf(":"));

            Log.d("ringtone", "setting ringtone for location: " + title);

            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            Ringtone tone = RingtoneManager.getRingtone(getApplicationContext(), uri);

            FavoriteLocation favoriteLocation = MapsActivity.partnerLocations.get(title);
            if (favoriteLocation != null) {
                favoriteLocation.setTone(tone);
                favoriteLocation.setUri(uri);
            }
        }
    }
}
