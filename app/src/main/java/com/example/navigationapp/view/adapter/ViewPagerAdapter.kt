package com.example.navigationapp.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.navigationapp.view.HomeFragment
import com.example.navigationapp.view.InformationFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):FragmentStateAdapter(
    fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                InformationFragment()
            }
            1 -> {
                HomeFragment()
            }
            else -> Fragment()
        }
    }
}