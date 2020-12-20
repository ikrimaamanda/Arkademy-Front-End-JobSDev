package com.example.jobsdev.maincontent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.ActionBarContextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityMainContentBinding
import com.example.jobsdev.maincontent.fragment.HomeFragment
import com.example.jobsdev.maincontent.fragment.MessageFragment
import com.example.jobsdev.maincontent.fragment.AccountFragment
import com.example.jobsdev.maincontent.fragment.SearchFragment
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.android.synthetic.main.activity_main_content.*

class MainContentActivity : AppCompatActivity() {

    private lateinit var sharedPref : PreferencesHelper

    private lateinit var binding : ActivityMainContentBinding
    private lateinit var homeFragment : HomeFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var messageFragment: MessageFragment
    private lateinit var accountFragment: AccountFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_content)

        sharedPref = PreferencesHelper(this)


        setSupportActionBar(binding.toolbar)

        homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fl_container, homeFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()

        binding.bottomNavMenu.setOnNavigationItemSelectedListener{
            item -> when(item.itemId) {
            R.id.home -> {
                homeFragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fl_container, homeFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                binding.tvTitleToolbar.setText("Home")
            }
            R.id.search -> {
                searchFragment = SearchFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fl_container, searchFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                binding.tvTitleToolbar.setText("Search")
            }
            R.id.message -> {
                messageFragment = MessageFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fl_container, messageFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                binding.tvTitleToolbar.setText("Message")
            }
            R.id.account -> {
                accountFragment = AccountFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fl_container, accountFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                binding.tvTitleToolbar.setText("Profile")
            }
        }
            true
        }

    }
}