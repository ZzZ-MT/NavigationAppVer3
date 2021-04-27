package com.example.navigationapp.view.ui

import android.content.Context
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
import androidx.navigation.fragment.findNavController
import com.example.navigationapp.R
import com.example.navigationapp.databinding.FragmentLoginBinding
import com.example.navigationapp.utils.EventObserver
import com.example.navigationapp.viewmodel.FirebaseViewModel

class LoginFragment: Fragment() {
    private val TAG ="LoginFragment"

    private var email:String = ""
    private var password:String = ""

    //Data binding
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

    private lateinit var navController: NavController

    //ViewModel
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
            savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView")
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        //binding view model
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

        binding?.tvChangeView?.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
            Log.i(TAG,"changeView?")
        }

        binding?.btnLogin?.setOnClickListener {
            //Binding EditText
            email = binding?.etEmail?.text.toString()
            password = binding?.etPassword?.text.toString()

            if(email != "" && password != "") {
                firebaseViewModel.loginUserFromAuthWithEmailAndPassword(email,password,this)
            } else {
                Toast.makeText(activity, "Please fill all gaps",
                    Toast.LENGTH_LONG).show()
            }
        }
        Log.i(TAG, "onViewCreated")

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