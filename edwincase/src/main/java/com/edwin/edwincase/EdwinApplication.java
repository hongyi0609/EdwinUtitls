package com.edwin.edwincase;

import android.app.Application;
import android.content.Context;

/**
 * Created by Edwin on 2017/12/17.
 */

public class EdwinApplication extends Application {

    private static Context mContext;
    private static Application sApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        sApplication = new EdwinApplication();
    }

    public static Application getInstance() {
        return sApplication;
    }

    public static Context getContext() {
        return mContext;
    }
}
