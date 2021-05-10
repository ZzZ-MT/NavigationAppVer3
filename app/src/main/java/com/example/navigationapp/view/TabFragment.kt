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
import com.google.android.material.tabs.TabLayoutMediator

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
        Log.i(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.i(TAG, "onCreateView")
        _binding = FragmentTabBinding.inflate(inflater, container, false)
        initView()
        return binding?.root
    }

    private fun initView() {
        val tabLayout = binding?.tabLayout
        val viewPager2= binding?.viewPager2


        val adapter = ViewPagerAdapter(childFragmentManager,lifecycle)

        viewPager2?.adapter = adapter


        if (tabLayout != null && viewPager2 != null) {
            TabLayoutMediator(tabLayout,viewPager2){tab,position ->
                when (position) {
                    0 -> tab.text = "Information"
                    1 -> tab.text = "Map"
                }
            }.attach()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}