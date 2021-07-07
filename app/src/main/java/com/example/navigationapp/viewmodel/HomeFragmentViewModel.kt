package com.example.navigationapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.navigationapp.model.LatLong

class HomeFragmentViewModel:ViewModel() {
    private val TAG = "HomeFragmentViewMode"
    // ghi nho current location,
    private val _currentLocation = MutableLiveData<LatLong?>()
    val currentLocation: LiveData<LatLong?>
        get() = _currentLocation


}