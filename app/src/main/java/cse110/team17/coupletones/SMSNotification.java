package cse110.team17.coupletones;

import android.telephony.SmsManager;

/**
 * Created by tylersy on 6/1/16.
 */
public class SMSNotification implements Notification {
    private final String phoneNumber;
    private final String msg;

    public SMSNotification(String phoneNumber, String msg) {
        this.phoneNumber = phoneNumber;
        this.msg = msg;
    }

    public void send() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, msg, null, null);
    }
}
