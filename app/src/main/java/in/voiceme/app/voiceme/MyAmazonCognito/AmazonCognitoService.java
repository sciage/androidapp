package in.voiceme.app.voiceme.MyAmazonCognito;

import android.content.Context;

import com.amazonaws.auth.AWSAbstractCognitoIdentityProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.auth.CognitoCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.regions.Regions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Harish on 8/20/2016.
 */
public class AmazonCognitoService {

    private static final String TAG = "CognitoSyncClientManager";

    /**
     * Enter here the Identity Pool associated with your app and the AWS
     * region where it belongs. Get this information from the AWS console.
     */

    private static final String IDENTITY_POOL_ID = "us-east-1:bf156c88-ffa2-4d99-8be9-a95a5baba307";
    private static final Regions REGION = Regions.US_EAST_1;
    protected static CognitoCachingCredentialsProvider credentialsProvider = null;
    protected static AWSAbstractCognitoIdentityProvider developerIdentityProvider;
    private static CognitoSyncManager syncClient;
    /**
     * Set this flag to true for using developer authenticated identities
     * Make sure you configured it in DeveloperAuthenticationProvider.java.
     */
    private static boolean useDeveloperAuthenticatedIdentities = false;
    String identityId = credentialsProvider.getIdentityId();

    /**
     * Initializes the Cognito Identity and Sync clients. This must be called before getInstance().
     *
     * @param context a context of the app
     */
    public static void init(Context context) {

        if (syncClient != null) return;

        if (useDeveloperAuthenticatedIdentities) {
            credentialsProvider = new CognitoCachingCredentialsProvider(context, developerIdentityProvider,
                    REGION);
        } else {
            credentialsProvider = new CognitoCachingCredentialsProvider(context, IDENTITY_POOL_ID,
                    REGION);
        }

        syncClient = new CognitoSyncManager(context, REGION, credentialsProvider);
    }

    /**
     * Sets the login so that you can use authorized identity. This requires a
     * network request, so you should call it in a background thread.
     *
     * @param providerName the name of the external identity provider
     * @param token        openId token
     */
    public static void addLogins(String providerName, String token) {
        if (syncClient == null) {
            throw new IllegalStateException("CognitoSyncClientManager not initialized yet");
        }

        Map<String, String> logins = credentialsProvider.getLogins();
        if (logins == null) {
            logins = new HashMap<>();
        }
        logins.put(providerName, token);
        credentialsProvider.setLogins(logins);
    }

    /**
     * Gets the singleton instance of the CognitoClient. init() must be called
     * prior to this.
     *
     * @return an instance of CognitoClient
     */
    public static CognitoSyncManager getInstance() {
        if (syncClient == null) {
            throw new IllegalStateException("CognitoSyncClientManager not initialized yet");
        }
        return syncClient;
    }
    //Log.d("LogTag", "my ID is " + identityId);

    /**
     * Returns a credentials provider object
     *
     * @return
     */
    public CognitoCredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }
}
