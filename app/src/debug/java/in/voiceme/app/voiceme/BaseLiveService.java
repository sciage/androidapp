package in.voiceme.app.voiceme;

import com.squareup.otto.Bus;

import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;

/**
 * Created by harish on 12/16/2016.
 */

public abstract class BaseLiveService {

    protected final Bus bus;
    protected final VoicemeApplication application;

    protected BaseLiveService(VoicemeApplication application) {
        this.application = application;
        bus = application.getBus();
        bus.register(this);
    }
}
