package in.voiceme.app.voiceme.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;

import in.voiceme.app.voiceme.ActivityPage.MainActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseAuthenticatedActivity;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;

public class LoginUserDetails extends BaseAuthenticatedActivity implements View.OnClickListener {
    private Button button;

    @Override
    protected void onVoicemeCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login_user_details);
        getSupportActionBar().setTitle("User Details");
        setNavDrawer(new MainNavDrawer(this));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        button = (Button) findViewById(R.id.submit_user_data_button);
        button.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.submit_user_data_button){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
