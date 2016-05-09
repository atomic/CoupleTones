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

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.internal.LocationRequestUpdateData;

import java.io.IOException;

import cse110.team17.coupletones.gcm.GcmIntentService;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    public static final String LOGIN = "LoginActivity";
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mPhoneView;
    private View mProgressView;
    private View mLoginFormView;

//    private Button mButtonRegisterId;
    private Button mButtonSignIn;           // This button is used to sign in/ or sign up for now
    private Button mButtonSignPartner;

    private GoogleCloudMessaging gcm;

    private String regid;

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

        // TODO: for now this sign in button is disabled (May be needed for final app)
        mButtonSignIn = (Button) findViewById(R.id.sign_in_button);
        mButtonSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !registerUser() ) {
                    Toast.makeText(LoginActivity.this, "No inputs.", Toast.LENGTH_SHORT).show();
                } else {
                    mState = Constants.State.REGISTERED;
                    //mEmailView.setHint("Partner's Email");
//                    mEmailView.setText("");
                    //mPhoneView.setHint("Partner's Phone number");
                    mPhoneView.setText("");
                    mButtonSignIn.setEnabled(false);
                    mButtonSignPartner.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "User registered", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mState = Constants.State.REGISTERED; // Only phone registration for partner is allowed
        mButtonSignPartner = (Button) findViewById(R.id.sign_partner);
        mButtonSignPartner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !registerPartner() ) {
                    Toast.makeText(LoginActivity.this, "No inputs.", Toast.LENGTH_SHORT).show();
                } else {
                    mStatePartner = Constants.State.REGISTERED;
//                    mEmailView.setEnabled(false); // TODO: may needed for final
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
                    Log.d(LOGIN, "partner phone (before starting Map): " + mUserAccount.getPartnerPhone());
                    startActivity(i);

                }
            }
        });


        // TODO: Delete this, for now, since we are using phone number instead
//        mButtonRegisterId = (Button) findViewById(R.id.reg_id_button);
//        mButtonRegisterId.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // after registered, this button should be disabled, no need for switch case
//                if( mState == Constants.State.REGISTERED)
//                    throw new AssertionError("User already registered");
//                registerUser();
////                }
//            }
//        })

        // New button to go to map activity
//        Button mTestMapButton = (Button) findViewById(R.id.test_go_to_map);             assert mTestMapButton != null;
//        mTestMapButton.setEnabled(false);
//        mTestMapButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(LoginActivity.this, MapsActivity.class);  // TODO: will need to be replaced later to allow data passing, or change to fragment
//                startActivity(i);
//            }
//        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private boolean registerPartner() {
        if( mStatePartner == Constants.State.REGISTERED )
            return false;
        String partnerPhone = mPhoneView.getText().toString();
        Log.d(LOGIN, "parterPhone : " + partnerPhone);
        mUserAccount.addPartner(partnerPhone);
//        mUserAccount.setPartnerEmail(mEmailView.getText().toString());  // NOTE: might be empty

        // TODO: register partner here, need to do all that authentication
        return true;
    }

    /**
     * Function to register the information on user
     */
    private boolean registerUser() {
        Log.d(LOGIN, "Do no call this function");

        String email = mEmailView.getText().toString();
        String phone = mPhoneView.getText().toString();
        Log.d(LOGIN, "email : " + email);
        Log.d(LOGIN, "phone : " + phone);
        if(email.isEmpty() && phone.isEmpty())
            return false;
        if(phone.isEmpty())
            return false; // for now Phone is more important

        mUserAccount.setUserEmail(email);
        mUserAccount.setUserPhone(phone);
        return true;
    }

}

