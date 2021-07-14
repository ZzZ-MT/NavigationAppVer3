package com.example.navigationapp.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.navigationapp.R
import com.example.navigationapp.databinding.FragmentSplashBinding
import com.example.navigationapp.utils.EventObserver
import com.example.navigationapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.*


class SplashFragment : Fragment() {
    private val TAG = "SplashFragment"
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding

    private lateinit var navController: NavController

    private val firebaseViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }

    private var currentFirebaseUser: FirebaseUser? = null

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Navigation Controller
        navController = Navigation.findNavController(view)
        firebaseViewModel.navigateScreen.observe(requireActivity(), EventObserver {
            navController.popBackStack(R.id.splashFragment, true)
            navController.navigate(it)
        })

        coroutineScope.launch(Dispatchers.Main) {
            currentFirebaseUser = firebaseViewModel.getCurrentUser()
            delay(3000)
//            if(currentFirebaseUser != null) {
//                navController.navigate(SplashFragmentDirections.actionSplashFragmentToMainNavGraph())
//            } else {
//                navController.navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
//            }
//        }

            Log.i(TAG, "onViewCreated")
        }
    }
}