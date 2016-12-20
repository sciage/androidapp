package in.voiceme.app.voiceme.login;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Map;

import in.voiceme.app.voiceme.login.account.AccountManager;

/**
 * Created by harish on 12/20/2016.
 */
class CreateIdentityTask extends AsyncTask<Map<String, String>, Integer, Boolean> {

    private RegisterActivity signInActivity;

    public CreateIdentityTask(RegisterActivity signInActivity) {
        this.signInActivity = signInActivity;
    }

    @Override
    protected Boolean doInBackground(Map<String, String>... logins) {

        String identity = AccountManager.getInstance().getIdentityId(logins[0]);

        if (identity != null && !identity.isEmpty()) {

            Log.v("Sign In Tag", String.format("Obtained AWS identity %s", identity));

            // Storing last used provider & token (will be used when refreshing)
            String provider = (String) logins[0].keySet().toArray()[0];
            String token = logins[0].get(provider);
            SharedPreferences settings = signInActivity.getSharedPreferences(Constants.PREF_FILE, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(Constants.KEY_LAST_USED_PROVIDER, provider);
            editor.putString(Constants.KEY_PROVIDER_TOKEN, token);
            editor.apply();

            return Boolean.TRUE;
        }

        Log.e("Sign In Tag", "Failed to obtain a valid identity... In all likelihood, this will never show... An exception happened above...");
        return Boolean.FALSE;
    }


    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (result) {
            // We successfully logged in... Can bo back to main activity
            signInActivity.finish();

        }
    }
}
