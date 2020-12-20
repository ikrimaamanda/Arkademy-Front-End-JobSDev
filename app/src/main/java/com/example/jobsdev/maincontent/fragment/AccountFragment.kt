package com.example.jobsdev.maincontent.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.jobsdev.R
import com.example.jobsdev.adapter.TabPagerAdapter
import com.example.jobsdev.databinding.FragmentAccountBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.google.android.material.tabs.TabLayout

class AccountFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var pagerAdapter: TabPagerAdapter
    private lateinit var viewPager : ViewPager
    private lateinit var tabLayout : TabLayout
    private lateinit var binding : FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_account, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)

        pagerAdapter = TabPagerAdapter(childFragmentManager)
        addFragment(rootView)
        return rootView
    }

    private fun addFragment(view: View?) {
        pagerAdapter = TabPagerAdapter(childFragmentManager)
        viewPager = view!!.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)

        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)

    }
}