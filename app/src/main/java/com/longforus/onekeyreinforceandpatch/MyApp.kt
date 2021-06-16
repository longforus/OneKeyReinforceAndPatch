package com.longforus.onekeyreinforceandpatch

import android.app.Application
import android.util.Log

class MyApp : Application() {
    val TAG = "MyApp"
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "my app")
    }
}