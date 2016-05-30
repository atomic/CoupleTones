package cse110.team17.coupletones;

import com.firebase.client.Firebase;

/**
 * Created by atomic on 5/30/16.
 */
public class StarterApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}

