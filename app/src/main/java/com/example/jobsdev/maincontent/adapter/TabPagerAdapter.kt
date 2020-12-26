package com.example.jobsdev.maincontent.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.jobsdev.maincontent.fragment.ExperienceEngineerFragment
import com.example.jobsdev.maincontent.fragment.PortfolioEngineerFragment

class TabPagerAdapter(fragment : FragmentManager) : FragmentStatePagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragment = arrayOf(
        PortfolioEngineerFragment(),
        ExperienceEngineerFragment()
    )

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

    override fun getCount(): Int = fragment.size

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "Portfolio"
            1 -> "Experience"
            else -> ""
        }
    }

}