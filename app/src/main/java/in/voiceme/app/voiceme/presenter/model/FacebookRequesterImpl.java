package in.voiceme.app.voiceme.presenter.model;

import android.content.Context;
import android.os.Bundle;

import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;


public class FacebookRequesterImpl implements FacebookRequester  {
    Context c;
    public FacebookRequesterImpl(Context c){

        this.c = c;
    }

    @Override
    public void facebookRequest(LoginResult loginResult, GraphRequest.GraphJSONObjectCallback callback) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), callback);
        Bundle bundle = new Bundle();
        bundle.putString("fields", "name,picture.type(large),birthday,email");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
    }


}
