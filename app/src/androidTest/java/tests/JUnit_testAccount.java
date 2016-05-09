package tests;

import junit.framework.TestCase;

import cse110.team17.coupletones.FavoriteLocation;
import cse110.team17.coupletones.UserAccount;

/**
 * Created by atomic on 5/8/16.
 */
public class JUnit_testAccount extends TestCase {


    public void test_EmailPhone() {
        UserAccount mAccount = new UserAccount();
        mAccount.setUserEmail("team17@ucsd.edu");
        mAccount.setUserPhone("626-251-2414");

        assertEquals("team17@ucsd.edu", mAccount.getUserEmail());
        assertEquals("626-251-2414", mAccount.getUserPhone());
    }

    public void test_addPartner() {
        UserAccount mAccount = new UserAccount();
        mAccount.setPartnerEmail("team17@ucsd.edu");
        mAccount.addPartner("626-251-2414");

        assertEquals("team17@ucsd.edu", mAccount.getUserEmail());
        assertEquals("626-251-2414", mAccount.getUserPhone());
    }
}
