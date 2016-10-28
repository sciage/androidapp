package in.voiceme.app.voiceme.infrastructure;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.squareup.otto.Subscribe;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.ActivityPage.MainActivity;
import in.voiceme.app.voiceme.login.LoginActivity;
import in.voiceme.app.voiceme.ProfilePage.Account;

/**
 * Created by Harish on 7/26/2016.
 */
public class AuthenticationActivity extends BaseActivity {
    private CognitoCachingCredentialsProvider mCredentialsProvider;
    public static final String EXTRA_RETURN_TO_ACTIVITY = "EXTRA_RETURN_TO_ACTIVITY";
    private static String TAG = MainActivity.class.getSimpleName();
    private Auth auth;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_autentication);

        auth = application.getAuth();

        if (!auth.hasAuthToken()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }


     //   bus.post(new Account.LoginWithLocalTokenRequest(auth.getAuthToken()));
    }



    @Subscribe
    public void onLoginWithLocalToken(Account.LoginWithLocalTokenResponse response) {
        if (!response.didSucceed()) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            auth.setAuthToken(null);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        Intent intent;
        String returnTo = getIntent().getStringExtra(EXTRA_RETURN_TO_ACTIVITY);
        if (returnTo != null) {
            try {
                intent = new Intent(this, Class.forName(returnTo));
            } catch (Exception ignored) {
                intent = new Intent(this, MainActivity.class);
            }
        } else {
            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
