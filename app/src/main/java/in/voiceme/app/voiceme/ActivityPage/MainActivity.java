package in.voiceme.app.voiceme.ActivityPage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseAuthenticatedActivity;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.login.Constants;
import in.voiceme.app.voiceme.login.LoginActivity;
import in.voiceme.app.voiceme.login.RefreshTokenTask;
import timber.log.Timber;

public class MainActivity extends BaseAuthenticatedActivity implements GoogleApiClient.OnConnectionFailedListener, Constants {
    private static Context applicationContext;

    public static Context getStaticApplicationContext() {
        return applicationContext;
    }


    @Override
    protected void onVoicemeCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Activity");
        setNavDrawer(new MainNavDrawer(this));

        applicationContext = this.getApplicationContext();

        Timber.v( "Configuration");
        Timber.v( "  > Amazon Identity Pool:          " + getString(R.string.aws_identity_pool));
        Timber.v( "  > Google Service ID:             " + getString(R.string.google_server_client_id));
        Timber.v( "  > Facebook Application ID:       " + getString(R.string.facebook_app_id));

        final FloatingActionButton textStatus = (FloatingActionButton) findViewById(R.id.action_a);
        textStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Button 01", Toast.LENGTH_SHORT).show();
            }
        });

        final FloatingActionButton audioStatus = (FloatingActionButton) findViewById(R.id.action_b);
        audioStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "button 02", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Initialize Facebook SDK
         */
        FacebookSdk.sdkInitialize(getApplicationContext());


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_main_activity);
        this.addPages(viewPager);

        // Give the PagerSlidingTabStrip the ViewPager
        SmartTabLayout tabsStrip = (SmartTabLayout) findViewById(R.id.tabs_main_activity);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

    }



    //add all pages
    private void addPages(ViewPager pager) {
        MainActivityFragmentPagerAdapter adapter = new MainActivityFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addPage(new ActivityYourFeedFragment());
        adapter.addPage(new ActivityInteractionFragment());

        //set adapter to pager
        pager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_text) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Refresh the Cognito credentials based on the last used provider and run onCompletion once valid credentials have been obtained
     * @param onCompletion a runnable that will be executed once the credentials have been successfully renewed
     */
    private void refreshCredentialsAndRun(Runnable onCompletion) {

        SharedPreferences settings = getSharedPreferences(PREF_FILE, Activity.MODE_PRIVATE);
        String lastUsedProvider = settings.getString(KEY_LAST_USED_PROVIDER, null);

        // The below might produce an NPE, but should not. Left as is on purpose (would highlight a flow issue)
        if (lastUsedProvider.equals(GOOGLE_LOGIN)) {
            this.refreshGoogleCredentials(onCompletion);
        } else if (lastUsedProvider.equals(FACEBOOK_LOGIN)) {
            this.refreshFacebookCredentials(onCompletion);
        } else {
            Timber.e( "Unknown previous identity provider " + lastUsedProvider);
        }
    }


    /**
     * Refresh cognito credentials based on identity token previously obtained from Facebook
     * @param onCompletion a runnable that will be executed once the credentials have been successfully renewed
     */
    private void refreshFacebookCredentials(Runnable onCompletion) {

        Timber.v( "Will try to refresh Facebook token as session has expired...");

        final AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (!accessToken.isExpired()) {

            Timber.v( String.format("  -> Facebook token is not expired (expiration %s)...", SimpleDateFormat.getInstance().format(accessToken.getExpires())));
            String token = accessToken.getToken();

            Map<String, String> logins = new HashMap<>();
            logins.put(FACEBOOK_LOGIN, token);

            Timber.v( "  -> will refresh login for " + FACEBOOK_LOGIN);

            // Refreshing session credentials (must be asynchronous)...
            new RefreshTokenTask(onCompletion).execute(logins);

            // We use the opportunity to refresh the Facebook token
            Date now = new Date();
            if (accessToken.getLastRefresh().getTime() < (now.getTime() - 7 * 86400)) {
                Timber.v("  -> triggering async facebook token refresh");
                AccessToken.refreshCurrentAccessTokenAsync();
            }

        } else {

            Timber.v( String.format("  -> Facebook token is expired (expiration %s)...", SimpleDateFormat.getInstance().format(accessToken.getExpires())));
            //User has to sign in to refresh an elapsed facebook token
            // TODO onCompletion is ignored (should be dealt with in onActivityResult)
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);

        }
    }


    /**
     * Refresh cognito credentials based on identity token previously obtained from Google
     * @param onCompletion a runnable that will be executed once the credentials have been successfully renewed
     */
    private void refreshGoogleCredentials(final Runnable onCompletion) {
        Timber.v("Will try to refresh Google token as session has expired...");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_server_client_id))
                .build();

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .build();

        final OptionalPendingResult<GoogleSignInResult> pendingResult = Auth.GoogleSignInApi.silentSignIn(googleApiClient);

        if (pendingResult.isDone()) {
            Timber.v("  -> got immediate result...");
            // There's immediate result available.
            GoogleSignInResult result = pendingResult.get();
            GoogleSignInAccount acct = result.getSignInAccount();
            String token = acct.getIdToken();
            Map<String, String> logins = new HashMap<>();
            logins.put(GOOGLE_LOGIN, token);

            Timber.v( "  -> will refresh login for " + GOOGLE_LOGIN);

            // Refreshing session credentials (must be asynchronous)...
            new RefreshTokenTask(onCompletion).execute(logins);

        } else {

            // There's no immediate result ready, displays some progress indicator and waits for the
            // async callback.
            Timber.v( "  -> got NO immediate result...");

            this.setProgressBarIndeterminateVisibility(true);
            pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {

                    if (result.isSuccess()) {

                        Timber.v( "  -> got successful PENDING result...");
                        GoogleSignInAccount acct = result.getSignInAccount();
                        String token = acct.getIdToken();
                        Map<String, String> logins = new HashMap<>();
                        logins.put(GOOGLE_LOGIN, token);

                        Timber.v( "  -> will refresh login for " + GOOGLE_LOGIN);

                        new RefreshTokenTask(onCompletion).execute(logins);

                    } else {
                        Timber.v( "  -> got failed PENDING result: redirecting user to sign in...");
                        // It didn't work, we show the sign in, but things will get messy...
                        // TODO onCompletion is ignored (should be dealt with in onActivityResult)
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        MainActivity.this.startActivity(intent);                    }
                }
            });
        }
    }

    /* Implements GoogleApiClient.OnConnectionFailedListener */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        String message = String.format("Failed to connect to Google [error #%d, %s]...", connectionResult.getErrorCode(), connectionResult.getErrorMessage());
        Timber.e(message);
    }
}
