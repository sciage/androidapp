package in.voiceme.app.voiceme.SocialSignInHelper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * Created by multidots on 6/16/2016.
 */
public class GooglePlusSignInHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int SIGN_IN_REQUEST_CODE = 101;
    private static final int ERROR_DIALOG_REQUEST_CODE = 102;
    private static final int PERMISSIONS_REQUEST_GET_ACCOUNTS = 103;

    private GoogleResponseListener mListener;
    private boolean isResolutionInProgress;
    private Activity mContext;
    private GoogleApiClient mGoogleApiClient;
    private VoicemeApplication application;

    public GooglePlusSignInHelper(Activity context, GoogleResponseListener listener) {
        mContext = context;
        mListener = listener;

        buildGoogleApiClient();
    }


    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    public void performSignIn() {
        mGoogleApiClient.disconnect();
        if (!mGoogleApiClient.isConnecting()) mGoogleApiClient.connect();
    }

    public void disconnectApiClient() {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.GET_ACCOUNTS}, PERMISSIONS_REQUEST_GET_ACCOUNTS);
        } else {
            processUserInfo();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    private void processUserInfo() {
        Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        mListener.onGSignInSuccess(person);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (!result.hasResolution()) {
            mListener.onGSignInFail();

            //check if the error has it's own resolution??
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), mContext, ERROR_DIALOG_REQUEST_CODE).show();
        } else if (!isResolutionInProgress) {
            processSignInError(result);
        }
    }

    private void processSignInError(ConnectionResult connectionResult) {
        if (connectionResult != null && connectionResult.hasResolution()) {
            try {
                isResolutionInProgress = true;
                connectionResult.startResolutionForResult(mContext, SIGN_IN_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                isResolutionInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    public void onPermissionResult(int requestCode, int[] grantResults) {
        if (requestCode != PERMISSIONS_REQUEST_GET_ACCOUNTS) return;

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            processUserInfo();
        } else {
            mListener.onGSignInFail();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GooglePlusSignInHelper.SIGN_IN_REQUEST_CODE && resultCode == RESULT_OK) {
            if (mGoogleApiClient != null && !mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
            Timber.d("this is the intent data" + data.getDataString());
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                try {
                    if (result.getSignInAccount() != null) {
              //        application.getBus().post(new Account.GoogleAccessTokenCognito(result.getSignInAccount().getIdToken()));
                    } else {
                        Timber.i( "result.getSignInAccount is null.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void signOut() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
    }
}