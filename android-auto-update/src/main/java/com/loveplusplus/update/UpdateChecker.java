package com.loveplusplus.update;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class UpdateChecker {


    public static void checkForDialog(Context context, FragmentManager fragmentManager) {
        if (context != null) {
            new CheckUpdateTask(context, fragmentManager, Constants.TYPE_DIALOG, true).execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }
    }


    public static void checkForNotification(Context context, FragmentManager fragmentManager) {
        if (context != null) {
            new CheckUpdateTask(context, fragmentManager, Constants.TYPE_NOTIFICATION, false).execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }

    }


}
