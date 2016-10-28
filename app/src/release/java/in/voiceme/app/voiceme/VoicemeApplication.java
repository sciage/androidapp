package in.voiceme.app.voiceme;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.squareup.otto.Bus;

import in.voiceme.app.voiceme.infrastructure.Auth;
import in.voiceme.app.voiceme.services.ServiceFactory;
import in.voiceme.app.voiceme.services.WebService;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by Harish on 7/20/2016.
 */
public class VoicemeApplication extends Application {
    private Auth auth;
    private Bus bus;
    private WebService webService;

    public VoicemeApplication() {
        bus = new Bus();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        auth = new Auth(this);
        FacebookSdk.sdkInitialize(this);
        webService = ServiceFactory.createRetrofitService(WebService.class);

        Fabric.with(this, new Crashlytics());
        Timber.plant(new ReleaseTree());
    }

    public WebService getWebService() {
        return webService;
    }

    public Auth getAuth() {
        return auth;
    }

    public Bus getBus() {
        return bus;
    }
}
