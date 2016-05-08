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
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

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
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPhoneView = (AutoCompleteTextView) findViewById(R.id.phone_number);

        mUserAccount = new UserAccount();

        mButtonSignIn = (Button) findViewById(R.id.sign_in_button);
        mButtonSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !registerUser() ) {
                    Toast.makeText(LoginActivity.this, "No inputs.", Toast.LENGTH_SHORT).show();
                } else {
                    mState = Constants.State.REGISTERED;
                    mEmailView.setHint("Partner's Email");
                    mEmailView.setText("");
                    mPhoneView.setHint("Partner's Phone number");
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
                    mStatePartner = Constants.State.REGISTERED;
                    mEmailView.setEnabled(false);
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
                            Intent i = new Intent(LoginActivity.this, MapsActivity.class);  // TODO: will need to be replaced later to allow data passing, or change to fragment
                            i.putExtra(MapsActivity.EXTRA_GET_PARTNER_PHONE, mUserAccount.getPartnerPhone());
                            startActivity(i);
                        }
                    });

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
        Button mTestMapButton = (Button) findViewById(R.id.test_go_to_map);             assert mTestMapButton != null;
        mTestMapButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, MapsActivity.class);  // TODO: will need to be replaced later to allow data passing, or change to fragment
                startActivity(i);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private boolean registerPartner() {
        if( mStatePartner == Constants.State.REGISTERED )
            return false;
        String partnerPhone = mPhoneView.getText().toString();
        Log.d(LOGIN, "parterPhone : " + partnerPhone);
        mUserAccount.addPartner(partnerPhone);
        mUserAccount.setPartnerEmail(mEmailView.getText().toString());  // NOTE: might be empty

        // TODO: register partner here, need to do all that authentication
        return true;
    }

    /**
     * Function to register the information on user
     */
    private boolean registerUser() {
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



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
//            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

