package in.voiceme.app.voiceme.login.account;

import android.os.AsyncTask;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by harish on 12/20/2016.
 */
public class SynchronizeDatasetTask extends AsyncTask<Void, Void, Boolean> {

    private AccountManager accountManager;
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public SynchronizeDatasetTask(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        Date now = new Date();
        Date expiration = accountManager.getCredentialsProvider().getSessionCredentitalsExpiration();

        if (now.after(expiration)) {
            String message = String.format("Session expired since %s... Will try to refresh credentials.", new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(expiration));
            Log.e("Account Manager", message);

        } else {
            String message = String.format("Session expiration is %s... No need to refresh.", new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(expiration));
            Log.v("Account Manager", message);
        }

        CustomSyncCallBack callBack = new CustomSyncCallBack(accountManager.getSyncObserver());
        accountManager.getDataset().synchronizeOnConnectivity(callBack);

        return Boolean.TRUE;
    }
}
