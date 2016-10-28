package in.voiceme.app.voiceme;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import timber.log.Timber;

/**
 * Created by harish on 10/18/2016.
 */

public class ReleaseTree extends Timber.Tree {
    private static final int MAX_LOG_LENGTH = 4000;

    @Override
    protected boolean isLoggable(int priority){
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return false;
        }
        //only when WARN, WTF, ERROR
        return true;
    }


    protected void log(int priority, String tag, String message, Throwable t) {
        if(isLoggable(priority)){

            //Report caught exceptions to Crashlytics
            if (priority == Log.ERROR && t != null){
                Crashlytics.logException(t);
            } else if (priority == Log.WARN){
                Crashlytics.log(message);
            } else if (t != null){
                //record the exception if timber is not able to detect it.
                String formattedMessage = LogMessageHelper.format(priority, tag, message);
                Crashlytics.logException(new StackTraceRecorder(formattedMessage));
            }

            //message is short enough, does not need to be broken down into chunks
            if (message.length() < MAX_LOG_LENGTH){
                if (priority == Log.ASSERT){
                    Log.wtf(tag, message);
                }else{
                    Log.println(priority, tag, message);
                }
                return;
            }

            //split by line , then ensure each line can fit into log's max length
            for (int i = 0, length = message.length(); i < length; i++){
                    int newLine = message.indexOf('\n', i);
                    newLine = newLine != -1 ? newLine : length;

                do {
                    int end = Math.min(newLine, i + MAX_LOG_LENGTH);
                    String part = message.substring(i, end);
                    if (priority == Log.ASSERT){
                        Log.wtf(tag, part);
                    } else {
                        Log.println(priority, tag, part);
                    }
                    i= end;
                } while (i < newLine);
            }
        }



    }

}
