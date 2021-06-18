package com.example.navigationapp.utils

import android.app.Application
import android.util.Log
import com.example.navigationapp.repo.UserPreferences

class NavigationApp:Application() {
    private val TAG = "NavigationApp"
    override fun onCreate() {
        super.onCreate()
        UserPreferences.init(this)
        Log.i(TAG, "onCreate")
    }
}