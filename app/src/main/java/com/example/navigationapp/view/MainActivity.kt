package com.example.navigationapp.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.navigationapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val TAG="MainActivity"
    //Register Navigation Component Architecture
    //private lateinit var navController: NavController
    //Register ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG,"onCreate")
//        if (!AppPreferences.firstRun) {
//            AppPreferences.firstRun = true
//            Log.d("SpinKotlin", "The value of our pref is: ${AppPreferences.firstRun}")
//        }
//        if (AppPreferences.changeUserName == null) {
//            AppPreferences.changeUserName = "Dat"
//        }
//
//        if(!AppPreferences.changeUserStatus) {
//            AppPreferences.changeUserStatus = true
//        }


        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission()
        }
        // Re-check before enabling. You can add an else statement to warn the user about the lack of functionality if it's disabled.
        // "or" is used instead of "and" as per the error. If it requires both, flip it over to &&. (I'm not sure, I haven't used GPS stuff before)
        // Re-check before enabling. You can add an else statement to warn the user about the lack of functionality if it's disabled.
        // "or" is used instead of "and" as per the error. If it requires both, flip it over to &&. (I'm not sure, I haven't used GPS stuff before)

    }
    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }

    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) { //Can add more as per requirement
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
                123)
        }
    }
}