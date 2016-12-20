package in.voiceme.app.voiceme.login.account;

/**
 * Interface that should be implemented by observers of the synchronisation
 */
public interface SyncObserver {
    void onDatasetDidSync(String dataset);
}
