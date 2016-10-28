package in.voiceme.app.voiceme.RecyclerViewDetails;

import android.os.Bundle;
import android.view.View;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseAuthenticatedActivity;


public class UserFeelingActivity extends BaseAuthenticatedActivity {

    @Override
    protected void onVoicemeCreate(Bundle savedState) {
        setContentView(R.layout.activity_user_feeling);
        getSupportActionBar().setTitle("User Feelings");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
