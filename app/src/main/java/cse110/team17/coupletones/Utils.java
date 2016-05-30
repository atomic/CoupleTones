package cse110.team17.coupletones;

import android.os.Handler;

import com.firebase.client.Firebase;

/**
 * Created by atomic on 5/8/16.
 */
public class Utils {

    // Delay mechanism
    public static Firebase mFirebaseRef = new Firebase("https://vivid-inferno-5451.firebaseio.com/");
    // TODO: find a way to store GPX on firebase and make it singleton here


    public interface DelayCallback{
        void afterDelay();
    }

    public static void delay(int secs, final DelayCallback delayCallback){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayCallback.afterDelay();
            }
        }, secs * 1000); // afterDelay will be executed after (secs*1000) milliseconds.
    }
}
