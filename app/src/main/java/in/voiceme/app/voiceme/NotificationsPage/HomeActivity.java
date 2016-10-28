package in.voiceme.app.voiceme.NotificationsPage;

import android.os.Bundle;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseAuthenticatedActivity;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;


public class HomeActivity extends BaseAuthenticatedActivity {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final int REQUEST_IMAGE_DELETED = 100;
    public static final String RESULT_EXTRA_MESSAGE_ID = "RESULT_EXTRA_MESSAGE_ID";

    @Override
    protected void onVoicemeCreate(Bundle savedState) {
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("Home");
        setNavDrawer(new MainNavDrawer(this));


    }
}
