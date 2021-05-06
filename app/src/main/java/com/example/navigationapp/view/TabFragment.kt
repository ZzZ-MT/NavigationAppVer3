package com.example.navigationapp.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.navigationapp.databinding.FragmentTabBinding
import com.example.navigationapp.view.adapter.ViewPagerAdapter

class TabFragment: Fragment() {
    private val TAG ="TabFragment"
    private var _binding: FragmentTabBinding? = null
    private val binding get() = _binding

//    private val tabLayout = binding?.tabLayout
//    private val viewPager2 = binding?.viewPager2

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewPager2?.isSaveEnabled = false
        val adapter = ViewPagerAdapter(childFragmentManager,lifecycle)
        adapter.createFragment(1)
        Log.i(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.i(TAG, "onCreateView")
        _binding = FragmentTabBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}