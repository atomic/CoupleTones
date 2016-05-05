package tests;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import cse110.team17.coupletones.LoginActivity;
import cse110.team17.coupletones.R;

/**
 * Created by atomic on 5/4/16.
 */
public class JUnit_testLogin extends ActivityInstrumentationTestCase2<LoginActivity> {

    LoginActivity loginActivity;

    public JUnit_testLogin() {
        super(LoginActivity.class);
    }

    public void test_first() {
        loginActivity = getActivity();
        TextView textView = (TextView) loginActivity.findViewById(R.id.email);
        String tester = textView.getText().toString();

        assertEquals("", tester);
    }
}
