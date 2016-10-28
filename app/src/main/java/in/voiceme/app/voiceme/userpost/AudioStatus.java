package in.voiceme.app.voiceme.userpost;

import android.os.Bundle;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseAuthenticatedActivity;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;

public class AudioStatus extends BaseAuthenticatedActivity {

    @Override
    protected void onVoicemeCreate(Bundle savedState) {
        setContentView(R.layout.activity_audio_status);
        getSupportActionBar().setTitle("Audio Status");
        setNavDrawer(new MainNavDrawer(this));
    }
}
