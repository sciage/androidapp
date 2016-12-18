package in.voiceme.app.voiceme;

import android.util.Log;

import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.google.android.gms.auth.GoogleAuthException;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.infrastructure.Account;
import in.voiceme.app.voiceme.infrastructure.Auth;
import timber.log.Timber;

import static com.facebook.GraphRequest.TAG;

/**
 * Created by harish on 12/16/2016.
 */

public class AccountLiveService extends BaseLiveService {
    private final Auth auth;

    protected AccountLiveService(VoicemeApplication application) {
        super(application);
        auth = application.getAuth();
    }


    @Subscribe
    public void addGoogleLoginToCognito(Account.GoogleAccessTokenCognito token) throws GoogleAuthException, IOException {
        Timber.i("addGoogleLoginToCognito");
        Timber.i("token: " + token.getToken());

        addDataToSampleDataset("google_token", token.getToken()); // please don't do this in a production app...

        Map<String, String> logins = application.getmCredentialsProvider().getLogins();
        logins.put("accounts.google.com", token.getToken());
        Timber.i( "logins: " + logins.toString());

        application.getmCredentialsProvider().setLogins(logins);
        refreshCredentialsProvider();
    }

    @Subscribe
    public void addFacebookLoginToCognito(Account.FacebookAccessTokenCognito facebookAccessToken) {
        Timber.i( "addFacebookLoginToCognito");
        Timber.i( "AccessToken: " + facebookAccessToken.fbToken);

        addDataToSampleDataset("facebook_token", facebookAccessToken.fbToken); // please don't do this in a production app...

        Map<String, String> logins = application.getmCredentialsProvider().getLogins();
        logins.put("graph.facebook.com", facebookAccessToken.fbToken);
        Timber.i( "logins: " + logins.toString());

        application.getmCredentialsProvider().setLogins(logins);
        refreshCredentialsProvider();
    }

    private void addDataToSampleDataset(String key, String value) {
        Dataset dataset = application.getmSyncClient().openOrCreateDataset("SampleDataset");
        dataset.put(key, value);
        dataset.synchronize(new DefaultSyncCallback() {
            @Override
            public void onSuccess(Dataset dataset, List newRecords) {
                Timber.i( "addDataToSampleDataset onSuccess");
                Timber.i( dataset.toString());

            }
        });
    }

    // save them inside shared preference
    public void outputCognitoCredentials() {
        Log.i(TAG, "outputCognitoCredentials");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Timber.i("getCachedIdentityId: " + application.getmCredentialsProvider().getCachedIdentityId());
                Timber.i( "getIdentityId: " + application.getmCredentialsProvider().getIdentityId());
            }
        }).start();
    }

    private void refreshCredentialsProvider() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                application.getmCredentialsProvider().refresh();
            }
        }).start();
    }
}
