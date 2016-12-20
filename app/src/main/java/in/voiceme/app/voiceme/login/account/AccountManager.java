package in.voiceme.app.voiceme.login.account;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.regions.Regions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import in.voiceme.app.voiceme.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Project AwsCognitoTest
 * Created by jmc on 14/02/16.
 */
public class AccountManager {

    private static final String TAG = AccountManager.class.getSimpleName();
    private static final String DATASET_NAME = "test";
    private static final String KEY_WHEN = "when";
    private static final String KEY_PHONE = "phone";

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static AccountManager instance = new AccountManager();
    private Context context = getApplicationContext();

    private CognitoCachingCredentialsProvider credentialsProvider;
    private CognitoSyncManager syncManager;

    private Dataset dataset;

    private SyncObserver syncObserver;

    /**
     * Hidden default constructor (singleton)
     */
    private AccountManager() {

        // Initializing the CredentialsProvider
        credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                context.getString(R.string.aws_identity_pool), // Identity Pool ID
                Regions.US_EAST_1 // Put your own region here

        );



        // Initializing the Sync Manager
        syncManager = new CognitoSyncManager(
                context,
                Regions.US_EAST_1,
                credentialsProvider);

        dataset = syncManager.openOrCreateDataset(DATASET_NAME);

        Log.v(TAG, "Created AccountManager...");

    }

    public SyncObserver getSyncObserver() {
        return syncObserver;
    }

    public void setSyncObserver(SyncObserver syncObserver) {
        this.syncObserver = syncObserver;
    }

    /**
     * Return the singleton instance
     * @return the instance of AccountManager
     */
    public static AccountManager getInstance() {
        return instance;
    }


    /**
     * Gets a new Cognito identity for the provided login map.
     * This must NOT be called on the main thread.
     * @param logins
     * @return
     */
    public String getIdentityId(Map<String, String> logins) {
        credentialsProvider.setLogins(logins);
        return credentialsProvider.getIdentityId();
    }



    /**
     * Refresh the Cognito credentials based on the new logins map.
     * This must NOT be called from the main thread
     * @param logins the login map we shall use to refresh the credentials
     */
    public void refreshCredentials(Map<String, String> logins) {

        try {
            credentialsProvider.setLogins(logins);
            credentialsProvider.refresh();

            Log.v(TAG, String.format("Obtained new credentials (will expire %s)", SimpleDateFormat.getInstance().format(credentialsProvider.getSessionCredentitalsExpiration())));
        } catch (Exception e) {
            String message = String.format("Got %s %s while refreshing credentials...",
                    e.getClass().getSimpleName(), e.getMessage());
            Log.e(TAG, message, e);
        }
    }



    /**
     * Test whether we are bound to an identity or not
     * @return true if we have a cached identity
     */
    public boolean hasCachedIdentity() {

        String cachedIdentity = credentialsProvider.getCachedIdentityId();

        return  cachedIdentity != null && !cachedIdentity.isEmpty();
    }



    /**
     * Returns whether the current Cognito session is expired
     * @return true if the Cognito session is expired or does not exist
     */
    public boolean isSessionExpired() {
        Date now = new Date();
        Date expiration = credentialsProvider.getSessionCredentitalsExpiration();

        // the below is just log
        if (expiration != null && expiration.before(now)) {
            Log.w(TAG, String.format("Session expired at %s",
                    SimpleDateFormat.getInstance().format(expiration)));
        } else if (expiration != null) {
            Log.d(TAG, String.format("Session will expire at %s",
                    SimpleDateFormat.getInstance().format(expiration)));
        } else {
            Log.d(TAG, "Session expiration UNSET (returning expired)...");
        }

        return (expiration == null || expiration.before(now));
    }


    /**
     * Returns the Cognito credentials session expiration (might be null)
     * @return the Cognito credentials session expiration
     */
    public Date getSessionExpiration() {
        return credentialsProvider.getSessionCredentitalsExpiration();
    }

    /**
     * Put a new set of values into the dataset and pushes the results to the remote storage.
     * Ths syncObserver will be called at the end of the sync operation.
     */
    public void putValues() {
        String phone = Build.MANUFACTURER + " " + Build.MODEL;
        String when = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(new Date());
        dataset.put(KEY_PHONE, phone);
        dataset.put(KEY_WHEN, when);

        //As we might need to refresh the credentials, we synchronize the dataset asynchronously
        new SynchronizeDatasetTask(this).execute();

    }

    /**
     * Refresh the values from the remote storage and call the syncObserver at the end of the sync operations.
     */
    public void refreshValues() {
        //As we might need to refresh the credentials, we synchronize the dataset asynchronously
        new SynchronizeDatasetTask(this).execute();
    }

    public CognitoCachingCredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public CognitoSyncManager getSyncManager() {
        return syncManager;
    }

    public Dataset getDataset() {
        return dataset;
    }
}
