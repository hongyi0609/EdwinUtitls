package com.edwin.edwincase;

import android.app.Application;
import android.content.Context;

/**
 * Created by hongy_000 on 2017/12/17.
 */

public class EdwinApplication extends Application {

    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
