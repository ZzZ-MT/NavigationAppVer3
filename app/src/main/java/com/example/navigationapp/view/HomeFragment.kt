package com.example.navigationapp.view
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.navigationapp.R
import com.example.navigationapp.databinding.FragmentHomeBinding
import com.example.navigationapp.databinding.FragmentSearchPlacesBinding
import com.example.navigationapp.view.adapter.BottomSheetDialogFragment
import com.example.navigationapp.view.adapter.MarkerInfoWindow
import com.example.navigationapp.viewmodel.UserViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.internal.ConnectionCallbacks
import com.google.android.gms.common.api.internal.OnConnectionFailedListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior

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
        ViewModelProvider(this).get(UserViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        Log.i(TAG, "onCreateView")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding?.viewmodel = firebaseViewModel
        val inflater = LayoutInflater.from(requireContext())
        binding =

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

//        navController = Navigation.findNavController(view)
//        firebaseViewModel.navigateScreen.observe(requireActivity(), EventObserver {
//            navController.navigate(it)
//        })

        firebaseViewModel.toast.observe(viewLifecycleOwner, { message ->
            message?.let {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                firebaseViewModel.onToastShown()
            }
        })

        binding?.btnCurrentLocation?.setOnClickListener {
            getCurrentLoc()
            findNavController().navigate(R.id.action_homeFragment_to_bottomSheetDialog)
//            val bottomSheet = BottomSheetDialogFragment()
//            bottomSheet.show(supportFragmentManager,"bottom sheet")
            Log.d(TAG,"btnCurrentLocation")
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
            if (location != null) {
                gotoLocation(location.latitude,location.longitude)
            } else {
                Log.i(TAG, "No GPS")
                Toast.makeText(context, "Turn on GPS", Toast.LENGTH_LONG).show()
            }

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
            map.uiSettings.isMapToolbarEnabled = false
            map.uiSettings.isZoomControlsEnabled = false
            map.setInfoWindowAdapter(MarkerInfoWindow(this))
        }

        getCurrentLoc()
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