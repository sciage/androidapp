package in.voiceme.app.voiceme.infrastructure;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.regions.Regions;
import com.facebook.AccessToken;
import com.google.android.gms.auth.GoogleAuthException;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import in.voiceme.app.voiceme.ActivityPage.MainActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.VoicemeApplication;


public abstract class BaseActivity extends AppCompatActivity {
    protected VoicemeApplication application;
    protected Toolbar toolbar;
    protected NavDrawer navDrawer;
    protected Bus bus;
    protected ActionScheduler scheduler;
    private boolean isRegisterdWithBus;
    private CognitoCachingCredentialsProvider mCredentialsProvider;
    private CognitoSyncManager mSyncClient;
    private static String TAG = MainActivity.class.getSimpleName();

    private static final String IDENTITY_POOL_ID = "us-east-1:b9755bcf-4179-40ad-8a5e-07d7baa8914c";
    private static final Regions REGION = Regions.US_EAST_1;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        application = (VoicemeApplication) getApplication();
        bus = application.getBus();
        scheduler = new ActionScheduler(application);

        bus.register(this);
        isRegisterdWithBus = true;

        initCognitoCredentialsProvider();
        initCognitoSyncClient();

        /**
         * Initialize Facebook SDK

         FacebookSdk.sdkInitialize(getApplicationContext());   */

        /**
         * Initializes the sync client. This must be call before you can use it.

         AmazonCognitoService.init(this); */
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

    // save them inside shared preference
    public void outputCognitoCredentials() {
        Log.i(TAG, "outputCognitoCredentials");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "getCachedIdentityId: " + mCredentialsProvider.getCachedIdentityId());
                Log.i(TAG, "getIdentityId: " + mCredentialsProvider.getIdentityId());
            }
        }).start();
    }


    private void addDataToSampleDataset(String key, String value) {
        Dataset dataset = mSyncClient.openOrCreateDataset("SampleDataset");
        dataset.put(key, value);
        dataset.synchronize(new DefaultSyncCallback() {
            @Override
            public void onSuccess(Dataset dataset, List newRecords) {
                Log.i(TAG, "addDataToSampleDataset onSuccess");
                Log.i(TAG, dataset.toString());

            }
        });
    }

    private void refreshCredentialsProvider() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mCredentialsProvider.refresh();
            }
        }).start();
    }

    public void addFacebookLoginToCognito(AccessToken facebookAccessToken) {
        Log.i(TAG, "addFacebookLoginToCognito");
        Log.i(TAG, "AccessToken: " + facebookAccessToken.getToken());

        addDataToSampleDataset("facebook_token", facebookAccessToken.getToken()); // please don't do this in a production app...

        Map<String, String> logins = mCredentialsProvider.getLogins();
        logins.put("graph.facebook.com", facebookAccessToken.getToken());
        Log.i(TAG, "logins: " + logins.toString());

        mCredentialsProvider.setLogins(logins);

        refreshCredentialsProvider();


    }

    private void addGoogleLoginToCognito(String token) throws GoogleAuthException, IOException {
        Log.i(TAG, "addGoogleLoginToCognito");
        Log.i(TAG, "token: " + token);

        addDataToSampleDataset("google_token", token); // please don't do this in a production app...

        Map<String, String> logins = mCredentialsProvider.getLogins();
        logins.put("accounts.google.com", token);
        Log.i(TAG, "logins: " + logins.toString());

        mCredentialsProvider.setLogins(logins);
    }

    public ActionScheduler getScheduler() {
        return scheduler;
    }

    @Override
    protected void onResume() {
        super.onResume();
        scheduler.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        scheduler.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isRegisterdWithBus) {
            bus.unregister(this);
            isRegisterdWithBus = false;
        }

        if (navDrawer != null)
            navDrawer.destroy();
    }

    @Override
    public void finish() {
        super.finish();

        if (isRegisterdWithBus) {
            bus.unregister(this);
            isRegisterdWithBus = false;
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        super.setContentView(layoutResId);

        toolbar = (Toolbar) findViewById(R.id.include_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

    }

    public void fadeOut(final FadeOutListener listener) {
        View rootView = findViewById(android.R.id.content);
        rootView.animate()
                .alpha(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        listener.onFadeOutEnd();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .setDuration(300)
                .start();
    }

    protected void setNavDrawer(NavDrawer drawer) {
        this.navDrawer = drawer;
        this.navDrawer.create();

        overridePendingTransition(0, 0);

        View rootView = findViewById(android.R.id.content);
        rootView.setAlpha(0);
        rootView.animate().alpha(1).setDuration(450).start();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public VoicemeApplication getVoicemeApplication() {
        return application;
    }

    public interface FadeOutListener {
        void onFadeOutEnd();
    }

}
