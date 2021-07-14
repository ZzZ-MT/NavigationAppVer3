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
import com.example.navigationapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InformationFragment: Fragment() {
    private val TAG ="InformationFragment"
    private var _binding: FragmentInformationBinding? = null
    private val binding get() = _binding
    private lateinit var navController: NavController
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var currentFirebaseUser: FirebaseUser? = null

    private val firebaseViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
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

        navController = findNavController()

        binding?.btLogout?.setOnClickListener {
            coroutineScope.launch {
                firebaseViewModel.logOutUser()
                navController.popBackStack(R.id.tabFragment,true)
                //findNavController().navigateUp()
                //findNavController().navigate(R.id.loginFragment)
                navController.navigate(R.id.loginFragment)
            }
        }

        coroutineScope.launch {
            Log.i(TAG,"loading")

            currentFirebaseUser = firebaseViewModel.getCurrentUserInformation()
            //Log.i(TAG, currentFirebaseUser!!.uid)
            var uid = currentFirebaseUser?.uid
            var name = currentFirebaseUser?.displayName
            var email = currentFirebaseUser?.email
            binding?.tvUid?.text = uid
            binding?.tvName?.text = name
            binding?.tvEmail?.text = email
        }
        Log.i(TAG, "onViewCreated")
    }
}