package com.example.navigationapp.view

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.navigationapp.R
import com.example.navigationapp.databinding.FragmentHomeBinding
import com.example.navigationapp.utils.EventObserver
import com.example.navigationapp.viewmodel.FirebaseViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HomeFragment: Fragment(), OnMapReadyCallback {
    private val TAG ="HomeFragment"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    //Google Map
    private lateinit var map: GoogleMap
    private val LOCATION_PERMISSION_REQUEST = 1

    private lateinit var navController: NavController


    private val firebaseViewModel by lazy {
        ViewModelProvider(this).get(FirebaseViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.i(TAG, "onCreateView")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding?.viewmodel = firebaseViewModel


        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        mapView.onCreate(savedInstanceState)
//        MapsInitializer.initialize(this)
//        mapView.getMapAsync(this)

//        map.setMinZoomPreference(6.0f)
//        map.setMaxZoomPreference(14.0f)

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)


        navController = Navigation.findNavController(view)
        firebaseViewModel.navigateScreen.observe(requireActivity(), EventObserver {
            navController.navigate(it)
        })

        firebaseViewModel.toast.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                firebaseViewModel.onToastShown()
            }
        })

//        binding?.btnLogout?.setOnClickListener {
//            firebaseViewModel.logOutUser()
//            findNavController().navigate(R.id.loginFragment)
//        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            map = googleMap
        }
//        getLocationAccess()
        val zoomLevel = 10f
        val sydney = LatLng(-33.852, 151.211)
        map.apply {
            addMarker(
                MarkerOptions()
                    .position(sydney)
                    .title("Marker in Sydney")
            )
            moveCamera(CameraUpdateFactory
                .newLatLngZoom(sydney,zoomLevel))
        }
    }

//    private fun getLocationAccess() {
//        if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            map.isMyLocationEnabled = true
//        }
//        else
//            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)
//    }
}