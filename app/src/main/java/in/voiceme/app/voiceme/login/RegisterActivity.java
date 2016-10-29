package in.voiceme.app.voiceme.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.regions.Regions;
import com.facebook.AccessToken;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.plus.model.people.Person;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import in.voiceme.app.voiceme.ActivityPage.MainActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.SocialSignInHelper.FacebookHelper;
import in.voiceme.app.voiceme.SocialSignInHelper.FacebookResponse;
import in.voiceme.app.voiceme.SocialSignInHelper.FacebookUser;
import in.voiceme.app.voiceme.SocialSignInHelper.GoogleAuthResponse;
import in.voiceme.app.voiceme.SocialSignInHelper.GoogleAuthUser;
import in.voiceme.app.voiceme.SocialSignInHelper.GooglePlusSignInHelper;
import in.voiceme.app.voiceme.SocialSignInHelper.GoogleResponseListener;
import in.voiceme.app.voiceme.SocialSignInHelper.GoogleSignInHelper;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.PrefUtil;

import static com.facebook.AccessToken.getCurrentAccessToken;


public class RegisterActivity extends BaseActivity implements View.OnClickListener, GoogleResponseListener, FacebookResponse, GoogleAuthResponse {
    private PrefUtil prefUtil;

    private static final int RC_SIGN_IN = 9001;
    private static final String IDENTITY_POOL_ID = "us-east-1:b9755bcf-4179-40ad-8a5e-07d7baa8914c";
    private static final Regions REGION = Regions.US_EAST_1;
    private static String TAG = MainActivity.class.getSimpleName();
    private CognitoCachingCredentialsProvider mCredentialsProvider;
    private CognitoSyncManager mSyncClient;

    private FacebookHelper mFbHelper;
    private GooglePlusSignInHelper mGHelper;
    private GoogleSignInHelper mGAuthHelper;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        String token = FirebaseInstanceId.getInstance().getToken();


       // Log.d("Id Generated", token);
        Toast.makeText(RegisterActivity.this, token, Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_register);

        //Google api initialization
        mGHelper = new GooglePlusSignInHelper(this, this);

        //google auth initialization
        mGAuthHelper = new GoogleSignInHelper(this, null, this);

        //fb api initialization
        mFbHelper = new FacebookHelper(this,
                "id,name,email,gender,birthday,picture,cover",
                this);

        //set sign in button
        findViewById(R.id.g_login_btn).setOnClickListener(this);
        findViewById(R.id.g_plus_login_btn).setOnClickListener(this);
        findViewById(R.id.bt_act_login_fb).setOnClickListener(this);

        initCognitoCredentialsProvider();
        initCognitoSyncClient();
        // initFacebookLogin();
     //   initGoogleLogin();

      //   outputCognitoCredentials();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.g_login_btn:
                mGAuthHelper.performSignIn(this);
                break;
            case R.id.g_plus_login_btn:
                mGHelper.performSignIn();
                break;
            case R.id.bt_act_login_fb:
                mFbHelper.performSignIn(this);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //handle results
        mFbHelper.onActivityResult(requestCode, resultCode, data);
        mGHelper.onActivityResult(requestCode, resultCode, data);
        mGAuthHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGHelper.disconnectApiClient();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        //handle permissions
        mGHelper.onPermissionResult(requestCode, grantResults);
    }

    private void finishLogin() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
          //  handleGoogleSignInResult(result);
        } else {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    } */

    // -- AWS Cognito Related Methods

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
    private void outputCognitoCredentials() {
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

    /**
     * <h2>refreshCredentialsProvider</h2>
     * <p>This calls the refresh() method for the AWS Credentials Provider on a background thread.
     * This should be called after updating login information (such as calling setLogins()).</p>
     */



    private void refreshCredentialsProvider() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mCredentialsProvider.refresh();
            }
        }).start();
    }

    /*
    // -- Facebook SDK Related Methods
    private void initFacebookLogin() {
        if (mFacebookCallbackManager == null) {
            mFacebookCallbackManager = CallbackManager.Factory.create();
        }

        LoginButton fbLogin = (LoginButton) findViewById(R.id.fbLogin);
        fbLogin.setReadPermissions(Arrays.asList("public_profile", "email"));

        fbLogin.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "fbLogin onSuccess");
                Log.i(TAG, "accessToken: " + loginResult.getAccessToken().getToken());

                addFacebookLoginToCognito(loginResult.getAccessToken());

                application.getAuth().setAuthToken(loginResult.getAccessToken().toString());
                application.getAuth().getUser().setLoggedIn(true);
                setResult(RESULT_OK);
                finishLogin();
            }


            @Override
            public void onCancel() {
                Log.i(TAG, "fbLogin onCancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i(TAG, "FacebookException: " + e.toString());
            }
        });

    } */


    private void addFacebookLoginToCognito(AccessToken facebookAccessToken) {
        Log.i(TAG, "addFacebookLoginToCognito");
        Log.i(TAG, "AccessToken: " + facebookAccessToken.getToken());

        addDataToSampleDataset("facebook_token", facebookAccessToken.getToken()); // please don't do this in a production app...

        Map<String, String> logins = mCredentialsProvider.getLogins();
        logins.put("graph.facebook.com", facebookAccessToken.getToken());
        Log.i(TAG, "logins: " + logins.toString());

        mCredentialsProvider.setLogins(logins);

       refreshCredentialsProvider();


    }


    // -- Google Sign-In Related Methods
    /*
    private void initGoogleLogin() {
        if (mGoogleSignInOptions == null) {
            mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.GOOGLE_SERVER_CLIENT_ID))
                    .build();
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        }
                    })
                    .addApi(com.google.android.gms.auth.api.Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                    .build();
        }

        gmsLogin = (SignInButton) findViewById(R.id.gmsLogin);

        gmsLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        Log.i(TAG, "handleSignInResult: " + result.isSuccess());
        if (result.isSuccess()) {
            try {
                if (result.getSignInAccount() != null) {
                    addGoogleLoginToCognito(result.getSignInAccount().getIdToken());
                } else {
                    Log.i(TAG, "result.getSignInAccount is null.");
                }
            } catch (GoogleAuthException | IOException e) {
                e.printStackTrace();
            }
        }
    } */

    private void addGoogleLoginToCognito(String token) throws GoogleAuthException, IOException {
        Log.i(TAG, "addGoogleLoginToCognito");
        Log.i(TAG, "token: " + token);

        addDataToSampleDataset("google_token", token); // please don't do this in a production app...

        Map<String, String> logins = mCredentialsProvider.getLogins();
        logins.put("accounts.google.com", token);
        Log.i(TAG, "logins: " + logins.toString());

        mCredentialsProvider.setLogins(logins);
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

    @Override
    public void onFbSignInFail() {
        Toast.makeText(this, "Facebook sign in failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFbSignInSuccess() {
        Toast.makeText(this, "Facebook sign in success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFbProfileReceived(FacebookUser facebookUser) {
        Toast.makeText(this, "Facebook user data: name= " + facebookUser.name + " email= " + facebookUser.email, Toast.LENGTH_SHORT).show();

        Log.d("Person name: ", facebookUser.name + "");
        Log.d("Person gender: ", facebookUser.gender + "");
        Log.d("Person email: ", facebookUser.email + "");
        Log.d("Person image: ", facebookUser.facebookID + "");

        addFacebookLoginToCognito(getCurrentAccessToken());
        application.getAuth().setAuthToken("token");
        application.getAuth().getUser().setLoggedIn(true);
        finishLogin();
    }

    @Override
    public void onGSignInFail() {
        Toast.makeText(this, "Google sign in failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGSignInSuccess(Person person) {
        Toast.makeText(this, "Google sign in success", Toast.LENGTH_SHORT).show();
        Log.d("Person display name: ", person.getDisplayName() + "");
        Log.d("Person birth date: ", person.getBirthday() + "");
        Log.d("Person gender: ", person.getGender() + "");
        Log.d("Person name: ", person.getName() + "");
        Log.d("Person id: ", person.getImage() + "");
    }

    @Override
    public void onGoogleAuthSignIn(GoogleAuthUser user) {
        Toast.makeText(this, "Google user data: name= " + user.name + " email= " + user.email, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGoogleAuthSignInFailed() {
        Toast.makeText(this, "Google sign in failed.", Toast.LENGTH_SHORT).show();
    }

}
