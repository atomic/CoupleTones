package cse110.team17.coupletones;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Tony Lim on 5/8/16.
 * UserAccoutn is a class to represent informations about the user of the App. These include the
 * name of the user, list of locations, and partner's phone number, regID or email
 * Singleton class
 */
public class UserAccount {

    private static String TAG = "UserAccount";

    private static String mUserPhone;

    private static boolean hasPartner = false;
    private static String mPartnerPhone;

    /**
     * favorite places that contains Markers.
     * data is public for easier retrieval
     */
    public static ArrayList<Marker> mFavoritePlaces;

    public UserAccount(){
        Log.d(TAG, "Error: UserAccount should not be instantiated");
    }


    /**
     * add partner through phone number
     * @param partnerPhone : partner's phone number
     * @return boolean to represent whether a partner already added or not
     */
    public static boolean addPartner(final String partnerPhone)
    {
        if( hasPartner) return false;
        hasPartner = true;
        mPartnerPhone = partnerPhone;
        Log.d("UesrAccount", partnerPhone);
        return true;
    }

    /**
     * Add new favorite place to user's
     * @param favoritePlace : Marker for favorite place
     */
    public static void addFavoritePlace(Marker favoritePlace)
    {
        mFavoritePlaces.add(favoritePlace);
    }

    /**
     * Getter for member variables
     */
    public static String getPartnerPhone() {
        return mPartnerPhone;
    }

    public static void setUserPhone(String userPhone) {
        mUserPhone = userPhone;
        // TODO: check if firebase have the location lists for this number

    }

    public static String getUserPhone() {
        return mUserPhone;
    }
}
