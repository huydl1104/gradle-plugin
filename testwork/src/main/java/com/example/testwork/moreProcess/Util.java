
package com.example.testwork.moreProcess;

import static android.os.Process.myPid;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;


public class Util {

    private static final String TAG = "Util";

    /**
     * Utility method to update given TextView with the current process string.
     */
    public static void setCurrentRunningProcess(TextView textView, Context activityContext) {
        String currentProcName;
        ActivityManager manager =
                (ActivityManager) activityContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == myPid()) {
                currentProcName = processInfo.processName;
                Log.i(TAG, currentProcName);
                textView.setText(currentProcName);
                break;
            }
        }
    }

}