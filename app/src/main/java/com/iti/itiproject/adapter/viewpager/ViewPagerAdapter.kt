package com.iti.itiproject.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iti.itiproject.ui.home.all_countries.AllCountriesFragment
import com.iti.itiproject.ui.home.search.SearchFragment
import com.iti.itiproject.ui.home.setting.SettingFragment
import com.iti.itiproject.ui.home.today.TodayFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TodayFragment()
            1 -> AllCountriesFragment()
            2 -> SearchFragment()
            else -> SettingFragment()
        }
    }
}