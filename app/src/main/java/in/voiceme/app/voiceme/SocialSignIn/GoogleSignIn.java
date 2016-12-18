package in.voiceme.app.voiceme.SocialSignIn;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import in.voiceme.app.voiceme.SocialSignInHelper.GoogleAuthResponse;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;

/**
 * Created by harish on 12/17/2016.
 */

public class GoogleSignIn extends BaseSocialSignIn implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 100;
    private FragmentActivity mContext;
    private GoogleAuthResponse mListener;
    private GoogleApiClient mGoogleApiClient;


    protected GoogleSignIn(VoicemeApplication application) {
        super(application);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
}
