package cse110.team17.coupletones;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    public static final String LOGIN = "LoginActivity";
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mPhoneView;

//    private Button mButtonRegisterId;
    private Button mButtonSignIn;           // This button is used to sign in/ or sign up for now
    private Button mButtonSignPartner;


    private UserAccount mUserAccount;

    private Constants.State mState          = Constants.State.UNREGISTERED;
    private Constants.State mStatePartner   = Constants.State.UNREGISTERED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
//        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPhoneView = (AutoCompleteTextView) findViewById(R.id.phone_number);

        mUserAccount = new UserAccount();

        mButtonSignIn = (Button) findViewById(R.id.sign_in_button);
        mButtonSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !registerUser() ) {
                    Toast.makeText(LoginActivity.this, "No inputs.", Toast.LENGTH_SHORT).show();
                } else {    // register success
                    mPhoneView.setText("");
                    mButtonSignIn.setEnabled(false);
                    mButtonSignPartner.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "User registered", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mButtonSignPartner = (Button) findViewById(R.id.sign_partner);
        mButtonSignPartner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !registerPartner() ) {
                    Toast.makeText(LoginActivity.this, "No inputs.", Toast.LENGTH_SHORT).show();
                } else {
                    mPhoneView.setEnabled(false);
                    mButtonSignPartner.setEnabled(false);
                    Toast.makeText(LoginActivity.this, "Partner registered", Toast.LENGTH_SHORT).show();

                    Utils.delay(2, new Utils.DelayCallback() {
                        @Override
                        public void afterDelay() {
                            Toast.makeText(LoginActivity.this, "Showing Map..", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Utils.delay(1, new Utils.DelayCallback() {
                        @Override
                        public void afterDelay() {
                        }
                    });

                    Intent i = MapsActivity.newIntent(LoginActivity.this, mUserAccount.getPartnerPhone());
                    // TODO: putExtra on user's phone number (firebase will push to this path

                    Log.d(LOGIN, "partner phone (before starting Map): " + mUserAccount.getPartnerPhone());
                    startActivity(i);

                }
            }
        });

    }

    private boolean registerPartner() {
        // TODO: make sure firebase has updated children path
        String partnerPhone = mPhoneView.getText().toString();
        mUserAccount.addPartner(partnerPhone);
        return true;
    }

    /**
     * Function to register the information on user
     */
    private boolean registerUser() {

        // TODO: make sure firebase has updated children path
        String phone = mPhoneView.getText().toString();
        if(phone.isEmpty())
            return false; // for now Phone is more important
        mUserAccount.setUserPhone(phone);
        return true;
    }

}

