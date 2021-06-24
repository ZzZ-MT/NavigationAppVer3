package com.example.navigationapp.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.navigationapp.R
import com.example.navigationapp.databinding.FragmentHomeBinding
import com.example.navigationapp.model.directions.DirectionResponses
import com.example.navigationapp.repo.retrofit.RetrofitClient
import com.example.navigationapp.view.adapter.MarkerInfoWindow
import com.example.navigationapp.viewmodel.UserViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.PolyUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment: Fragment(),
        OnMapReadyCallback{

    private val TAG ="HomeFragment"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    //Google Map
    private lateinit var map: GoogleMap
    private lateinit var sunrise: LatLng
    private lateinit var lotte: LatLng

    //Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //Navigation component
//    private lateinit var navController: NavController

    //Bottom Sheet Behavior
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>



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
        initView()
        return binding?.root
    }

    private fun initView() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding!!.bottomSheetMap)
        bottomSheetBehavior.addBottomSheetCallback(object:BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> Log.i(TAG, "STATE_COLLAPSED")
                    BottomSheetBehavior.STATE_EXPANDED -> Log.i(TAG, "STATE_EXPANDED")
                    BottomSheetBehavior.STATE_DRAGGING -> Log.i(TAG,"STATE_DRAGGING")
                    BottomSheetBehavior.STATE_SETTLING -> Log.i(TAG,"STATE_SETTLING")
                    BottomSheetBehavior.STATE_HIDDEN -> Log.i(TAG,"STATE_HIDDEN")
                    else -> Log.i(TAG,"OTHER_STATE")
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.i(TAG,"onSlide")
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        //Google Map direction point
        sunrise = LatLng(10.7217076873311, 106.70363313849708)
        lotte = LatLng(10.740228324485326, 106.70186219107477)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        firebaseViewModel.toast.observe(viewLifecycleOwner, { message ->
            message?.let {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                firebaseViewModel.onToastShown()
            }
        })

        binding?.btnCurrentLocation?.setOnClickListener {
            getCurrentLoc()
            //findNavController().navigate(R.id.bottomSheetDialog)
            map.clear()
            Log.d(TAG,"btnCurrentLocation")

        }

    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLoc() {
    fusedLocationClient.lastLocation.addOnCompleteListener {
            var location = it.result
            if (location != null) {
                //gotoLocation(location.latitude,location.longitude)
            } else {
                Log.i(TAG, "No GPS")
                Toast.makeText(context, "Turn on GPS", Toast.LENGTH_LONG).show()
            }

        }
    }

//    private fun gotoLocation(latitude: Double, longitude: Double) {
//        var latLng= LatLng(latitude,longitude)
//        var cameraUpdate:CameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18F)
//        map.moveCamera(cameraUpdate)
//        map.addMarker(MarkerOptions().position(latLng).title("Your Location"))
//        map.addCircle(CircleOptions().center(latLng).radius(100.0).strokeColor(Color.BLUE))
//    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            map = googleMap
            getCurrentLoc()
            map.uiSettings.isMapToolbarEnabled = false
            map.uiSettings.isZoomControlsEnabled = false
            map.setInfoWindowAdapter(MarkerInfoWindow(this))
            map.setOnInfoWindowClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

            }
            val markerFkip = MarkerOptions()
                .position(sunrise)
                .title("FKIP")
            val markerMonas = MarkerOptions()
                .position(lotte)
                .title("Monas")

            map.addMarker(markerFkip)
            map.addMarker(markerMonas)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(sunrise, 11.6f))

            val fromFKIP = sunrise.latitude.toString() + "," + sunrise.longitude.toString()
            val toMonas = lotte.latitude.toString() + "," + lotte.longitude.toString()

            val apiServices = context?.let { RetrofitClient.apiServices(it) }
            apiServices?.getDirection(fromFKIP, toMonas, getString(R.string.api_key))
                ?.enqueue(object : Callback<DirectionResponses> {
                    override fun onResponse(call: Call<DirectionResponses>, response: Response<DirectionResponses>) {
                        drawPolyline(response)
                        Log.d("ok", response.message())
                    }

                    override fun onFailure(call: Call<DirectionResponses>, t: Throwable) {
                        Log.e("thua", t.localizedMessage)
                    }
                })

            map.setOnInfoWindowCloseListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            map.setOnMapClickListener { latlng -> // Clears the previously touched position
                map.clear();
                // Animating to the touched position
                map.animateCamera(CameraUpdateFactory.newLatLng(latlng));
                val location = LatLng(latlng.latitude, latlng.longitude)
                map.addMarker(MarkerOptions().position(location))
            }
        }
    }

    private fun drawPolyline(response: Response<DirectionResponses>) {
        val shape = response.body()?.routes?.get(0)?.overviewPolyline?.points
        val polyline = PolylineOptions()
            .addAll(PolyUtil.decode(shape))
            .width(8f)
            .color(Color.BLUE)
        map.addPolyline(polyline)
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