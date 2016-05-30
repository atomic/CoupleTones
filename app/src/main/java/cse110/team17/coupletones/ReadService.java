package cse110.team17.coupletones;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class ReadService extends Service {
    private static final String TAG = "ReadService";
    Firebase mFirebaseRef;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "starting ReadService");
        Toast.makeText(ReadService.this, "read Service Started", Toast.LENGTH_SHORT).show();

//        th.start();
        mFirebaseRef = new Firebase("https://vivid-inferno-5451.firebaseio.com/first");
        mFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String data = dataSnapshot.getValue(String.class);
//                mMessageDisplay.setText(data);
            Toast.makeText(ReadService.this, data, Toast.LENGTH_SHORT).show();
                // TODO: use thread to automate message at a gap of 3 seconds here from /first
                //      problem: can't use Toast in Thread
//                Thread th = new Thread(new MyThread(data));
//                Toast.makeText( getBaseContext(), data, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    public ReadService() {
    }


    final class MyThread implements Runnable {
        private static final String TAG = "MyThread";

        String mMessage;
        public MyThread(String message) {
            mMessage = message;
        }

        @Override
        public void run() {
            Log.d(TAG, "running thread");
            // problem: can't use Toast in thread
//            Toast.makeText(ReadService.this, mMessage, Toast.LENGTH_SHORT).show();
            synchronized (this) {
                try {
                    wait(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stopSelf();
            }
        }

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "destroying service");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
