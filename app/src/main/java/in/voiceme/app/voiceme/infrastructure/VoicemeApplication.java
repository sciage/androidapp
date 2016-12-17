package in.voiceme.app.voiceme.infrastructure;

import android.app.Application;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.regions.Regions;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.squareup.otto.Bus;

import in.voiceme.app.voiceme.Module;
import in.voiceme.app.voiceme.services.ServiceFactory;
import in.voiceme.app.voiceme.services.WebService;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

import static com.facebook.GraphRequest.TAG;

/**
 * Created by Harish on 7/20/2016.
 */
public class VoicemeApplication extends Application {
    private Auth auth;
    private Bus bus;
    private WebService webService;
    protected CognitoCachingCredentialsProvider mCredentialsProvider;
    protected CognitoSyncManager mSyncClient;

    private static final String IDENTITY_POOL_ID = "us-east-1:b9755bcf-4179-40ad-8a5e-07d7baa8914c";
    private static final Regions REGION = Regions.US_EAST_1;

    public VoicemeApplication() {
        bus = new Bus();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        auth = new Auth(this);
        FacebookSdk.sdkInitialize(this);
        Module.register(this);
        webService = ServiceFactory.createRetrofitService(WebService.class);


        initCognitoCredentialsProvider();
        initCognitoSyncClient();

        Timber.plant(new Timber.DebugTree(){
            // Add the line number to the TAG
            @Override
            protected String createStackElementTag(StackTraceElement element){
                return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });

    }

    private void initCognitoCredentialsProvider() {
        mCredentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                IDENTITY_POOL_ID, // YOUR Identity Pool ID from AWS Cognito
                REGION // Region
        );

        Log.i(TAG, "mCredentialsProvider: " + mCredentialsProvider.toString());
    }

    private void initCognitoSyncClient() {
        mSyncClient = new CognitoSyncManager(
                getApplicationContext(),
                REGION,
                mCredentialsProvider
        );

        Log.i(TAG, "mSyncClient: " + mSyncClient.toString());
    }

    public CognitoCachingCredentialsProvider getmCredentialsProvider() {
        return mCredentialsProvider;
    }

    public CognitoSyncManager getmSyncClient() {
        return mSyncClient;
    }

    public WebService getWebService() {
        return webService;
    }

    public Auth getAuth() {
        return auth;
    }


    public Bus getBus() {
        return bus;
    }
}
