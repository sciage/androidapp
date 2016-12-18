package in.voiceme.app.voiceme.SocialSignIn;

import com.squareup.otto.Bus;

import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;

/**
 * Created by harish on 12/17/2016.
 */

public abstract class BaseSocialSignIn {
    protected final Bus bus;
    protected final VoicemeApplication application;

    protected BaseSocialSignIn(VoicemeApplication application) {
        this.application = application;
        bus = application.getBus();
        bus.register(this);
    }
}
