package in.voiceme.app.voiceme;

import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;

/**
 * Created by harish on 12/16/2016.
 */

public class Module {

    public static void register(VoicemeApplication application) {
        new AccountLiveService(application);
    }

}
