package net.tyzen.io;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public class SessionHandler {
  private static final String PREF_NAME = "UserSession";
  private static final String WORD = "username";
  private static final String KEY_EXPIRES = "expires";
  private static final String KEY_FULL_NAME = "full_name";
  private static final String KEY = "api_key";
  private static final String SECRET = "api_secret";
  private static final String KEYB = "api_keyb";
  private static final String SECRETB = "api_secretb";
  private static final String KEYC = "api_keyc";
  private static final String SECRETC = "api_secretc";
  private static final String OTP = "code";
  private static final String KEY_EMPTY = "";
  private Context mContext;
  private SharedPreferences.Editor mEditor;
  private SharedPreferences mPreferences;

  public SessionHandler(Context mContext) {
    this.mContext = mContext;
    mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    this.mEditor = mPreferences.edit();
  }

  /**
   * Logs in the user by saving user details and setting session
   *
   * @param word
   * @param keys
   * @param secrets
   */
  public void loginUser(String word, String fullName, String keys, String secrets,  String keysb, String secretsb, String otp, String keyc, String secretc ) {
    mEditor.putString(WORD, word);
    mEditor.putString(KEY_FULL_NAME, fullName);
    mEditor.putString(KEY, keys);
    mEditor.putString(SECRET, secrets);
    mEditor.putString(KEYB, keysb);
    mEditor.putString(SECRETB, secretsb);
    mEditor.putString(OTP, otp);
    mEditor.putString(KEYC, keyc);
    mEditor.putString(SECRETC, secretc);
    Date date = new Date();

    //Set user session for next 7 days
    long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
    mEditor.putLong(KEY_EXPIRES, millis);
    mEditor.commit();
  }

  /**
   * Checks whether user is logged in
   *
   * @return
   */
  public boolean isLoggedIn() {
    Date currentDate = new Date();

    long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* If shared preferences does not have a value
         then user is not logged in
         */
    if (millis == 0) {
      return false;
    }
    Date expiryDate = new Date(millis);

        /* Check if session is expired by comparing
        current date and Session expiry date
        */
    //return currentDate.before(expiryDate);
    return true;
  }

  /**
   * Fetches and returns user details
   *
   * @return user details
   */
  public User getUserDetails() {
    //Check if user is logged in first
    if (!isLoggedIn()) {
      return null;
    }
    User user = new User();
    user.word(mPreferences.getString(WORD, KEY_EMPTY));
    user.setFullName(mPreferences.getString(KEY_FULL_NAME, KEY_EMPTY));
    user.setKeys(mPreferences.getString(KEY, KEY_EMPTY));
    user.setSecrets(mPreferences.getString(SECRET, KEY_EMPTY));
    user.setBKeys(mPreferences.getString(KEYB, KEY_EMPTY));
    user.setBSecrets(mPreferences.getString(SECRETB, KEY_EMPTY));
    user.setOTP(mPreferences.getString(OTP, KEY_EMPTY));
    user.setCKeys(mPreferences.getString(KEYC, KEY_EMPTY));
    user.setCSecrets(mPreferences.getString(SECRETC, KEY_EMPTY));
    user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));

    return user;
  }

  /**
   * Logs out user by clearing the session
   */
  public void logoutUser(){
    mEditor.clear();
    mEditor.commit();
  }

}
