package com.example.navigationapp.view.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.navigationapp.databinding.FragmentHomeBinding
import com.example.navigationapp.databinding.FragmentRegisterBinding
import com.example.navigationapp.utils.EventObserver
import com.example.navigationapp.viewmodel.FirebaseViewModel

class HomeFragment: Fragment() {
    private val TAG ="HomeFragment"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

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

    }
}