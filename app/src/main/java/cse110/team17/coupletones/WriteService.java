package cse110.team17.coupletones;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.firebase.client.Firebase;

public class WriteService extends Service {
    private static final String TAG = "WriteService";
    Firebase mFirebasePush;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mFirebasePush = new Firebase("https://vivid-inferno-5451.firebaseio.com/second");
//        mFirebasePush.setValue( mMessageText.getText().toString());

//        Thread th = new Thread(new MyThread(startId));
//        th.start();
        return super.onStartCommand(intent, flags, startId);
    }

    public WriteService() {
    }

    public void pushMessage(String message) {
//        mFirebasePush.setValue( mMessageText.getText().toString());
        mFirebasePush.setValue( message );
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "destroying Service");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
