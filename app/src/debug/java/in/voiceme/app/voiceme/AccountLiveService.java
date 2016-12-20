package in.voiceme.app.voiceme;

import android.util.Log;

import in.voiceme.app.voiceme.infrastructure.Auth;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;

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

    // save them inside shared preference
    public void outputCognitoCredentials() {
        Log.i(TAG, "outputCognitoCredentials");
        new Thread(new Runnable() {
            @Override
            public void run() {
  //              Timber.i("getCachedIdentityId: " + application.getmCredentialsProvider().getCachedIdentityId());
  //              Timber.i( "getIdentityId: " + application.getmCredentialsProvider().getIdentityId());
            }
        }).start();
    }

    private void refreshCredentialsProvider() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                application.getmCredentialsProvider().refresh();
            }
        }).start();
    }
}
