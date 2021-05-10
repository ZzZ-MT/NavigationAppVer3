package com.example.navigationapp.view


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.navigationapp.R
import com.example.navigationapp.databinding.FragmentHomeBinding
import com.example.navigationapp.databinding.FragmentSearchPlacesBinding
import com.example.navigationapp.utils.EventObserver
import com.example.navigationapp.utils.PermissionUtils.isPermissionGranted
import com.example.navigationapp.utils.PermissionUtils.requestPermission
import com.example.navigationapp.viewmodel.FirebaseViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.common.api.internal.ConnectionCallbacks
import com.google.android.gms.common.api.internal.OnConnectionFailedListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class HomeFragment: Fragment(),
        OnMapReadyCallback,
        ConnectionCallbacks,
        OnConnectionFailedListener{

    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private val TAG ="HomeFragment"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    //Google Map
    private lateinit var map: GoogleMap
    private var permissionDenied = false
    //private var isPermissionGrated:Boolean = false

    //Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //Navigation component
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

//        if(isPermissionGrated) {
//
//        }
        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

//        navController = Navigation.findNavController(view)
//        firebaseViewModel.navigateScreen.observe(requireActivity(), EventObserver {
//            navController.navigate(it)
//        })

        firebaseViewModel.toast.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                firebaseViewModel.onToastShown()
            }
        })

        binding?.btnLogout?.setOnClickListener {
            firebaseViewModel.logOutUser()
            findNavController().navigate(R.id.loginFragment)
        }

        binding?.btnCurrentLocation?.setOnClickListener {
            getCurrentLoc()
            Log.d(TAG,"btnCurrentLocation")
        }

        binding?.btnSearch?.setOnClickListener {
//            findNavController().navigate(R.id.searchPlaceFragment)
            showSearchPlacesFragment()
        }

    }

    private fun showSearchPlacesFragment() {
        val searchPlacesBinding:FragmentSearchPlacesBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
        R.layout.fragment_search_places,null,false)
        val dialogView = context?.let {
            AlertDialog.Builder(it,0).create()
        }
        dialogView?.apply{
            setView(searchPlacesBinding.root)
            setCancelable(false)
        }?.show()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLoc() {
    fusedLocationClient.lastLocation.addOnCompleteListener {
            var location = it.result
            gotoLocation(location.latitude,location.longitude)
        }
    }

    private fun gotoLocation(latitude: Double, longitude: Double) {
        var latLng= LatLng(latitude,longitude)
        var cameraUpdate:CameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18F)
        map.moveCamera(cameraUpdate)
        map.addMarker(MarkerOptions().position(latLng).title("Your Location"))
        map.addCircle(CircleOptions().center(latLng).radius(100.0).strokeColor(Color.BLUE))
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            map = googleMap
        }
        getCurrentLoc()

        val zoomLevel = 10f
        val sydney = LatLng(-33.852, 151.211)
//        map.apply {
//            addMarker(
//                    MarkerOptions()
//                            .position(sydney)
//                            .title("Marker in Sydney")
//            )
//            moveCamera(CameraUpdateFactory
//                    .newLatLngZoom(sydney, zoomLevel))
//        }
    }

    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            requestPermission(requireActivity() as AppCompatActivity, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()

        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true
        }
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.i(TAG, "onViewStateRestored")
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i(TAG, "onDetach")
    }


}