package in.voiceme.app.voiceme.presenter.gplus;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

public interface GplusPresenter {

    void allowLogin(GoogleApiClient gApiClient);
    void onResult(GoogleSignInResult result);
}
