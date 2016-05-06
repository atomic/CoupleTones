package cse110.team17.coupletones;

/**
 * Created by Derek on 5/5/2016.
 */
public interface Constants {


    String PACKAGE = "cse110.team17.coupletones";
    // actions for server interaction
    String ACTION_REGISTER = PACKAGE + ".REGISTER";
    String ACTION_UNREGISTER = PACKAGE + ".UNREGISTER";
    String ACTION_ECHO = PACKAGE + ".ECHO";

    enum EventbusMessageType {
        REGISTRATION_FAILED, REGISTRATION_SUCCEEDED, UNREGISTRATION_SUCCEEDED, UNREGISTRATION_FAILED;
    }

    enum State {
        REGISTERED, UNREGISTERED;
    }
}
