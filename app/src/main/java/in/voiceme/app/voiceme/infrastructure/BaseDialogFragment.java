package in.voiceme.app.voiceme.infrastructure;

import android.app.DialogFragment;
import android.os.Bundle;

import com.squareup.otto.Bus;

/**
 * Created by Harish on 7/25/2016.
 */
public class BaseDialogFragment extends DialogFragment {
    protected VoicemeApplication application;
    protected Bus bus;
    protected ActionScheduler scheduler;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        application = (VoicemeApplication) getActivity().getApplication();
        scheduler = new ActionScheduler(application);

        bus = application.getBus();
        bus.register(this);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        scheduler.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        scheduler.onResume();
    }
}
