package com.example.navigationapp.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.navigationapp.R
import com.example.navigationapp.databinding.FragmentInformationBinding
import com.example.navigationapp.utils.EventObserver
import com.example.navigationapp.viewmodel.FirebaseViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InformationFragment: Fragment() {
    private val TAG ="InformationFragment"
    private var _binding: FragmentInformationBinding? = null
    private val binding get() = _binding
    //private lateinit var navController: NavController
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var currentFirebaseUser: FirebaseUser? = null

    private val firebaseViewModel by lazy {
        ViewModelProvider(this).get(FirebaseViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.i(TAG, "onCreateView")
        binding?.viewmodel = firebaseViewModel
        _binding = FragmentInformationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Navigation Controller
//        navController = Navigation.findNavController(view)
//        firebaseViewModel.navigateScreen.observe(requireActivity(), EventObserver {
//            navController.navigate(it)
//        })

//        firebaseViewModel.toast.observe(viewLifecycleOwner, { message ->
//            message?.let {
//                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
//                firebaseViewModel.onToastShown()
//            }
//        })

        binding?.btLogout?.setOnClickListener {
            coroutineScope.launch {
                firebaseViewModel.logOutUser()
                findNavController().navigate(R.id.loginFragment)
            }
        }

        coroutineScope.launch {
            currentFirebaseUser = firebaseViewModel.checkUserLoggedIn()

            currentFirebaseUser?.let { firebaseUser ->
                Log.i(TAG, firebaseUser.uid)
                val user = currentFirebaseUser

                var name = user?.displayName
                var email = user?.email

                binding?.tvName?.text = name
                binding?.tvEmail?.text = email

            }
        }
        Log.i(TAG, "onViewCreated")
    }
}