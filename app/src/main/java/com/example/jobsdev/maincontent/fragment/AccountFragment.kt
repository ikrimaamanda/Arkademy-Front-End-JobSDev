package com.example.jobsdev.maincontent.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.example.jobsdev.LoginEngineerActivity
import com.example.jobsdev.R
import com.example.jobsdev.adapter.TabPagerAdapter
import com.example.jobsdev.databinding.FragmentAccountBinding
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.PreferencesHelper
import com.google.android.material.tabs.TabLayout

class AccountFragment : Fragment() {

    private lateinit var sharedPref : PreferencesHelper

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

        binding.btnLogout.setOnClickListener {
            showDialogLogOut()
            showMessage("Log Out")
        }

        pagerAdapter = TabPagerAdapter(childFragmentManager)
        addFragment(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = PreferencesHelper(view.context)
        binding.tvEmailProfile.text = sharedPref.getValueString(Constant.prefEmail )
    }

    private fun addFragment(view: View?) {
        pagerAdapter = TabPagerAdapter(childFragmentManager)
//        binding.viewPager
//        binding.tabLayout
        viewPager = view!!.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)

        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)

    }

    private fun moveActivity() {
        val  intent = Intent(activity, LoginEngineerActivity::class.java)
        activity!!.startActivity(intent)
    }

    private fun showMessage(message : String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showDialogLogOut() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Log Out")
        builder.setMessage("Do you want to log out?")
        builder.setPositiveButton("Yes", { dialogInterface : DialogInterface, i : Int -> sharedPref.putValue(Constant.prefIsLogin, false)
        moveActivity()
            sharedPref.clear()
            activity!!.finish()
        })
        builder.setNegativeButton("No", {dialogInterface : DialogInterface, i : Int ->})
        builder.show()
    }

}