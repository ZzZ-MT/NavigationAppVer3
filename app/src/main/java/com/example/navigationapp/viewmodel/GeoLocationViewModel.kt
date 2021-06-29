package com.example.navigationapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.navigationapp.model.LatLong

class GeoLocationViewModel: ViewModel() {
    private val TAG = "GeoLocationViewModel"
    private val _location = MutableLiveData<LatLong?>()
    val location: LiveData<LatLong?>
        get() = _location

    fun getCurrentLocation() {

    }
}