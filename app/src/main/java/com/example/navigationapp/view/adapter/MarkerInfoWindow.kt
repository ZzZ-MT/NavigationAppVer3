package com.example.navigationapp.view.adapter

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.navigationapp.databinding.MapMarkerInfoWindowBinding
import com.example.navigationapp.view.HomeFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerInfoWindow(val context: HomeFragment): GoogleMap.InfoWindowAdapter {
    private val TAG ="MarkerOptionWindow"
    private var _binding: MapMarkerInfoWindowBinding? = null
    private val binding get() = _binding

    override fun getInfoWindow(p0: Marker): View? {
        Log.i(TAG,"getInfoWindow")
        return null
    }

    override fun getInfoContents(p0: Marker): View? {
        Log.i(TAG,"getInfoContents")
        val layoutInflater = (context as Fragment).layoutInflater
        _binding = MapMarkerInfoWindowBinding.inflate(layoutInflater)
        return binding?.root
    }
}