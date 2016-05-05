package tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import cse110.team17.coupletones.MapsActivity;
import cse110.team17.coupletones.R;

/**
 * Created by atomic on 5/4/16.
 */
public class JUnit_testMap extends ActivityInstrumentationTestCase2<MapsActivity> {
    MapsActivity mMapsActivity;
    public JUnit_testMap() {
        super(MapsActivity.class);
    }
    public void test_first() {
        mMapsActivity = getActivity();
//        TextView textView = (TextView) mMapsActivity.findViewById(R.id.);
//        String tester = textView.getText().toString();

        assertEquals(true, true);
    }
}
