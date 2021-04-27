package com.example.navigationapp.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.navigationapp.R
import com.example.navigationapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG="MainActivity"
    //Register Navigation Component Architecture
    //private lateinit var navController: NavController
    //Register ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val navHostFragment = supportFragmentManager
//            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
//        // Instantiate the navController using the NavHostFragment
//        navController = navHostFragment.navController
//        // Make sure actions in the ActionBar get propagated to the NavController
//        setupActionBarWithNavController(navController)
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
}