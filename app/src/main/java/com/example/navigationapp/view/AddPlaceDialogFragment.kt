package com.example.navigationapp.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.navigationapp.R
import com.example.navigationapp.databinding.FragmentAddPlaceDialogBinding


class AddPlaceDialogFragment : DialogFragment() {
    private val TAG = "AddPlaceDialogFragment"

    private var _binding: FragmentAddPlaceDialogBinding? = null
    private val binding get() = _binding

    private lateinit var navController: NavController


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView")
        _binding = FragmentAddPlaceDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG,"onViewCreated")
        navController = findNavController()

        binding?.btnBack?.setOnClickListener {
            dismiss()
        }

        val places = resources.getStringArray(R.array.type_of_place)

        val spinner = binding?.spinnerPlaces
        if (spinner != null) {
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, places)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(requireActivity(),
                        getString(R.string.selected_item) + " " +
                                "" + places[position], Toast.LENGTH_SHORT).show()

                    adapter.setNotifyOnChange(true)
                    adapter.notifyDataSetChanged()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }
    }

}