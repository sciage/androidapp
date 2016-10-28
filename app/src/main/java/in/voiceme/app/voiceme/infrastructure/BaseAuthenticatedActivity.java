package in.voiceme.app.voiceme.infrastructure;

import android.content.Intent;
import android.os.Bundle;

import in.voiceme.app.voiceme.login.LoginActivity;

public abstract class BaseAuthenticatedActivity extends BaseActivity {

    @Override
    protected final void onCreate(Bundle savedState) {
        super.onCreate(savedState);

         if (!application.getAuth().hasAuthToken()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

         /* if (!application.getAuth().getUser().isLoggedIn()) {
            if (application.getAuth().hasAuthToken()) {
                Intent intent = new Intent(this, AuthenticationActivity.class);
                intent.putExtra(AuthenticationActivity.EXTRA_RETURN_TO_ACTIVITY, getClass().getName());
                startActivity(intent);
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }

            finish();
            return;
        } */

        onVoicemeCreate(savedState);
    }

    protected abstract void onVoicemeCreate(Bundle savedState);
}
