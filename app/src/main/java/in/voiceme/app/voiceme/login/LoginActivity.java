package in.voiceme.app.voiceme.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.ActivityPage.MainActivity;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_REGISTER = 2;

    private View registerButton;


    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.activity_login);

        registerButton = findViewById(R.id.activity_login_register);

        registerButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == registerButton)
            startActivityForResult(new Intent(this, RegisterActivity.class), REQUEST_REGISTER);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

    }

    private void finishLogin() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
