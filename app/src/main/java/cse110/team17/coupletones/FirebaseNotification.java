package cse110.team17.coupletones;

import com.firebase.client.Firebase;

/**
 * Created by tylersy on 6/1/16.
 */
public class FirebaseNotification implements Notification {
    private final String msg;
    Firebase partnerRef;

    public FirebaseNotification(Firebase partnerRef, String msg) {
        this.partnerRef = partnerRef;
        this.msg = msg;
    }

    public void send() {
        partnerRef.setValue(msg);
    }
}
