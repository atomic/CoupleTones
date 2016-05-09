package cse110.team17.coupletones;

/**
 * Created by Derek on 5/5/2016.
 */
public interface Constants {

    String PROJECT_ID = "813916479990";

    String KEY_STATE = "keyState";
    String KEY_REG_ID = "keyRegId";
    String KEY_MSG_ID = "keyMsgId";
    String KEY_ACCOUNT = "keyAccount";
    String KEY_MESSAGE_TXT = "keyMessageTxt";
    String KEY_EVENT_TYPE = "keyEventbusType";

    String ACTION = "action";
    // very simply notification handling :-)
    int NOTIFICATION_NR = 10;

    long GCM_DEFAULT_TTL = 2 * 24 * 60 * 60 * 1000; // two days

    String PACKAGE = "cse110.team17.coupletones";
    // actions for server interaction
    String ACTION_REGISTER = PACKAGE + ".REGISTER";
    String ACTION_UNREGISTER = PACKAGE + ".UNREGISTER";
    String ACTION_ECHO = PACKAGE + ".ECHO";

    // action for notification intent
    String NOTIFICATION_ACTION = PACKAGE + ".NOTIFICATION";

    String DEFAULT_USER = "fakeUser";

    enum EventbusMessageType {
        REGISTRATION_FAILED, REGISTRATION_SUCCEEDED, UNREGISTRATION_SUCCEEDED, UNREGISTRATION_FAILED;
    }

    enum State {
        REGISTERED, UNREGISTERED;
    }
}
