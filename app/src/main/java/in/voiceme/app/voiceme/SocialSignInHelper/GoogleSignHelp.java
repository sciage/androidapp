package in.voiceme.app.voiceme.SocialSignInHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import in.voiceme.app.voiceme.infrastructure.Account;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;

public class GoogleSignHelp extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 100;
    private FragmentActivity mContext;
    private GoogleAuthResponse mListener;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


public GoogleSignHelp(FragmentActivity context, @Nullable String serverClientId, @NonNull GoogleAuthResponse listener) {
    mContext = context;
    mListener = listener;

    //noinspection ConstantConditions
    if (listener == null){
        throw new RuntimeException("GoogleAuthResponse listener cannot be null.");
    }

    //build api client
    buildGoogleApiClient(buildSignInOptions(serverClientId));
}

    private GoogleSignInOptions buildSignInOptions(@Nullable String serverClientId) {
        GoogleSignInOptions.Builder gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestId();
        if (serverClientId != null) gso.requestIdToken(serverClientId);
        return gso.build();
    }

    private void buildGoogleApiClient(@NonNull GoogleSignInOptions gso) {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(mContext, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void performSignIn(Activity activity){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void performSignIn(Fragment activity){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                bus.post(new Account.GoogleAccessTokenCognito(result.getSignInAccount().getIdToken()));
                mListener.onGoogleAuthSignIn(parseToGoogleUser(acct));
            } else {
                mListener.onGoogleAuthSignInFailed();
            }
        }
    }

    private GoogleAuthUser parseToGoogleUser(GoogleSignInAccount account){
        GoogleAuthUser user = new GoogleAuthUser();
        user.name = account.getDisplayName();
        user.familyName = account.getFamilyName();
        user.idToken =account.getIdToken();
        user.email = account.getEmail();
        user.photoUrl = account.getPhotoUrl();
        return user;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mListener.onGoogleAuthSignInFailed();
    }

    }