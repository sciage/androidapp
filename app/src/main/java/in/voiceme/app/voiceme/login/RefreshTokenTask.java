package in.voiceme.app.voiceme.login;

import android.os.AsyncTask;

import java.util.Map;

import in.voiceme.app.voiceme.login.account.AccountManager;

/**
 * Created by harish on 12/20/2016.
 */
public class RefreshTokenTask extends AsyncTask<Map<String, String>, Integer, Boolean> {

    private Runnable onCompletion;

    public RefreshTokenTask(Runnable onCompletion) {
        this.onCompletion = onCompletion;
    }

    @Override
    protected Boolean doInBackground(Map<String, String>... logins) {

        AccountManager.getInstance().refreshCredentials(logins[0]);
        return Boolean.TRUE;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (onCompletion != null) {
            onCompletion.run();
        }
    }
}
