package in.voiceme.app.voiceme.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.presenter.AccountActivity;
import in.voiceme.app.voiceme.presenter.facebook.FbPresenter;
import in.voiceme.app.voiceme.presenter.facebook.FbPresenterImpl;
import in.voiceme.app.voiceme.presenter.gplus.GplusPresenter;
import in.voiceme.app.voiceme.presenter.gplus.GplusPresenterImpl;
import in.voiceme.app.voiceme.presenter.model.data.LoginUser;
import in.voiceme.app.voiceme.presenter.view.MainView;


public class RegisterActivity extends BaseActivity implements MainView, GoogleApiClient.OnConnectionFailedListener {

    private CallbackManager fbManager;
    private FbPresenter fbPresenter;
    private GplusPresenter gplusPresenter;
    private LoginButton fBtn;
    private SignInButton gBtn;
    Context c = this;
    private GoogleApiClient account;
    private static final int GOOGLE_SIGNIN_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        String token = FirebaseInstanceId.getInstance().getToken();


       // Log.d("Id Generated", token);
        Toast.makeText(RegisterActivity.this, token, Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_register);

        fbManager = CallbackManager.Factory.create();
        fBtn = (LoginButton) findViewById(R.id.fb_btn);
        fBtn.setReadPermissions(Arrays.asList("public_profile","email","user_birthday"));

        fbPresenter = new FbPresenterImpl(this);
        fbLogin();

        gplusPresenter = new GplusPresenterImpl(this);
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        final GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                .build();

        gBtn = (SignInButton) findViewById(R.id.google_btn);
        gBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gplusPresenter.allowLogin(googleApiClient);
            }
        });


        // initFacebookLogin();
     //   initGoogleLogin();

      //   outputCognitoCredentials();


    }

    private void fbLogin(){
        fBtn.registerCallback(fbManager, (FacebookCallback<LoginResult>) fbPresenter);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGNIN_REQUEST){
            gplusPresenter.onResult(Auth.GoogleSignInApi.getSignInResultFromIntent(data));
        }
    }

    @Override
    public void setUser(LoginUser user) {
        if (user != null){
            sendIntent(user);
        } else {
            Log.d("GPLUS", "GPLUS ERROR");
        }
    }

    private void sendIntent(LoginUser user){
        Intent i = new Intent(this, AccountActivity.class);
        i.putExtra("user", user);
        startActivity(i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("GPLUS", "GPLUS ERROR");
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

    @Subscribe
    public void addGoogleLoginToCognito(Account.GoogleAccessTokenCognito token) throws GoogleAuthException, IOException {
        Timber.i("addGoogleLoginToCognito");
        Timber.i("token: " + token.accessToken);

        addDataToSampleDataset("google_token", token.accessToken); // please don't do this in a production app...

        Map<String, String> logins = application.getmCredentialsProvider().getLogins();
        logins.put("accounts.google.com", token.accessToken);
        Timber.i( "logins: " + logins.toString());

        application.getmCredentialsProvider().setLogins(logins);
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
