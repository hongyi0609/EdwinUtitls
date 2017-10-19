package com.edwin.utils

import android.app.Application
import android.content.Context

/**
 * Created by hongy_000 on 2017/10/19.
 */
class EdwinApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        edwinApplication = this
    }

    companion object {
        private var edwinApplication :EdwinApplication? = null
        fun getContext(): Context {
            return edwinApplication as Context
        }
    }
}