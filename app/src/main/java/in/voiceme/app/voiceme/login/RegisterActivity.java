package in.voiceme.app.voiceme.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;


public class RegisterActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, Constants {

    private static String TAG = RegisterActivity.class.getSimpleName();

    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String EMAIL = "email";
    //Activity requests
    private static final int GOOGLE_SIGN_IN = 1;

    // Google
    private SignInButton googleSignInBtn;
    private GoogleApiClient googleApiClient;

    // Facebook
    private LoginButton facebookSignInBtn;
    private CallbackManager callbackManager;


    /* Implements GoogleApiClient.OnConnectionFailedListener */

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        String message = String.format("Failed to connect to Google [error #%d, %s]...", connectionResult.getErrorCode(), connectionResult.getErrorMessage());
        Log.e(TAG, message);
    }


    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        String token = FirebaseInstanceId.getInstance().getToken();


       // Log.d("Id Generated", token);
        Toast.makeText(RegisterActivity.this, token, Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_register);

        googleSignInBtn = (SignInButton) this.findViewById(R.id.signin_with_google_btn);
        facebookSignInBtn = (LoginButton) this.findViewById(R.id.signin_with_facebook_btn);

        this.setUpGoogleSignIn();

        this.setUpFacebookSignIn();




        // initFacebookLogin();
     //   initGoogleLogin();

      //   outputCognitoCredentials();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            // We are coming back from the Google login activity
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            this.handleGoogleSignInResult(result);
        } else {
            // We are coming back from the Google login activity
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Configures (or hides) the Facebook sign in button
     */
    private void setUpFacebookSignIn() {

        String facebookAppId = getString(R.string.facebook_app_id);

        // The Facebook application ID must be defined in res/values/configuration_strings.xml
        if (facebookAppId.isEmpty()) {
            facebookSignInBtn.setVisibility(View.GONE);
            findViewById(R.id.signin_no_facebook_signin_txt).setVisibility(View.VISIBLE);
            return;
        }

        callbackManager = CallbackManager.Factory.create();

        facebookSignInBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                RegisterActivity.this.handleFacebookLogin(loginResult);
            }


            @Override
            public void onCancel() {
                Log.v(TAG, "User cancelled log in with Facebook");
            }

            @Override
            public void onError(FacebookException error) {
                RegisterActivity.this.handleFacebookError(error);
            }
        });
    }

    /**
     * Configures (or hides) the Google sign in button
     */
    private void setUpGoogleSignIn() {

        String serverClientId = getString(R.string.google_server_client_id);

        // The Google server client ID must be defined in res/values/configuration_strings.xml
   /*     if (serverClientId.isEmpty()) {
            googleSignInBtn.setVisibility(View.GONE);
            findViewById(R.id.signin_no_google_signin_txt).setVisibility(View.VISIBLE);
            return;
        } */

        // We configure the Google Sign in button
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(serverClientId)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleSignInBtn.setSize(SignInButton.SIZE_WIDE);

        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "Signing in with Google...");

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                RegisterActivity.this.startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
            }
        });
    }



    /**
     * Handle Google sign in result
     */
    private void handleGoogleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {

            Log.v(TAG, "Successfully logged in with Google...");
            // We can request some Cognito Credentials
            GoogleSignInAccount acct = result.getSignInAccount();
            Map<String, String> logins = new HashMap<>();
            logins.put(GOOGLE_LOGIN, acct.getIdToken());
            application.getAuth().setAuthToken("token");
            application.getAuth().getUser().setLoggedIn(true);
            Log.v(TAG, String.format("Google token <<<\n%s\n>>>", logins.get(GOOGLE_LOGIN)));

            // The identity must be created asynchronously
            new CreateIdentityTask(this).execute(logins);
            setResult(RESULT_OK);
            finish();

        } else {

            Log.w(TAG, String.format("Failed to authenticate against Google #%d - %s", result.getStatus().getStatusCode(), result.getStatus().getStatusMessage()));

        }
    }


    /**
     * Handle a Facebook login error
     * @param error the error that should be handled.
     */
    private void handleFacebookError(FacebookException error) {

        String message = String.format("Failed to authenticate against Facebook %s - \"%s\"", error.getClass().getSimpleName(), error.getLocalizedMessage());
        Log.e(TAG, message, error);

    }

    /**
     * Handle a Facebook login success
     * @param loginResult the successful login result
     */
    private void handleFacebookLogin(LoginResult loginResult) {

        Log.v(TAG, "Successfully logged in with Facebook...");

        final Map<String, String> logins = new HashMap<>();
        logins.put(FACEBOOK_LOGIN, AccessToken.getCurrentAccessToken().getToken());
        application.getAuth().setAuthToken("token");
        application.getAuth().getUser().setLoggedIn(true);
        Log.v(TAG, String.format("Facebook token <<<\n%s\n>>>", logins.get(FACEBOOK_LOGIN)));


        // The identity must be created asynchronously
        new CreateIdentityTask(this).execute(logins);
        setResult(RESULT_OK);
        finish();
    }








    /**
     * <h2>refreshCredentialsProvider</h2>
     * <p>This calls the refresh() method for the AWS Credentials Provider on a background thread.
     * This should be called after updating login information (such as calling setLogins()).</p>
     */





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

    /*
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

        application.getAuth().setAuthToken("token");
        application.getAuth().getUser().setLoggedIn(true);
        setResult(RESULT_OK);
        finish();
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
        application.getAuth().setAuthToken("token");
        application.getAuth().getUser().setLoggedIn(true);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onGoogleAuthSignIn(GoogleAuthUser user) {
        Toast.makeText(this, "Google user data: name= " + user.name + " email= " + user.email, Toast.LENGTH_SHORT).show();
        application.getAuth().setAuthToken("token");
        application.getAuth().getUser().setLoggedIn(true);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onGoogleAuthSignInFailed() {
        Toast.makeText(this, "Google sign in failed.", Toast.LENGTH_SHORT).show();
    }*/

}
