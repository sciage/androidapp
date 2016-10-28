package in.voiceme.app.voiceme.infrastructure;

import android.util.Log;

import rx.Subscriber;

public abstract class BaseSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        Log.e("ERROR", e.getLocalizedMessage(), e);
    }
}
