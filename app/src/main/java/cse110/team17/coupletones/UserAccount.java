package cse110.team17.coupletones;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Tony Lim on 5/8/16.
 * UserAccoutn is a class to represent informations about the user of the App. These include the
 * name of the user, list of locations, and partner's phone number, regID or email
 */
public class UserAccount {

    private String mUsername;
    private String mUserEmail;
    private Integer mUserPhone;

    private boolean hasPartner;
    private String mPartnerEmail;
    private String mPartnerRegId;
    private Integer mPartnerPhone;

    /**
     * favorite places that contains Markers.
     * data is public for easier retrieval
     */
    public ArrayList<Marker> mFavoritePlaces;

    public UserAccount(){
        hasPartner = false;
    }


    /**
     * add partner through phone number
     * @param partnerPhone : partner's phone number
     * @return boolean to represent whether a partner already added or not
     */
    public boolean addPartner(final Integer partnerPhone)
    {
        if( !hasPartner) return false;
        hasPartner = true;
        mPartnerPhone = partnerPhone;
        return true;
    }

    /**
     * Add new favorite place to user's
     * @param favoritePlace : Marker for favorite place
     */
    public void addFavoritePlace(Marker favoritePlace)
    {
        mFavoritePlaces.add(favoritePlace);
    }


    /**
     * Getter for member variables
     */
    public Integer getPartnerPhone() {
        return mPartnerPhone;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public String getPartnerEmail() {
        return mPartnerEmail;
    }

    public String getPartnerRegId() {
        return mPartnerRegId;
    }

    public void setPartnerEmail(String partnerEmail) {
        mPartnerEmail = partnerEmail;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public void setUserEmail(String userEmail) {
        mUserEmail = userEmail;
    }

    public void setUserPhone(Integer userPhone) {
        mUserPhone = userPhone;
    }

    public Integer getUserPhone() {
        return mUserPhone;
    }
}
