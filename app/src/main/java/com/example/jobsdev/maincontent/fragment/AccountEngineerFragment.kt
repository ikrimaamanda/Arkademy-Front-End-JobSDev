package com.example.jobsdev.maincontent.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.jobsdev.R
import com.example.jobsdev.databinding.FragmentAccountEngineerBinding
import com.example.jobsdev.maincontent.adapter.TabPagerAdapter
import com.example.jobsdev.maincontent.editprofile.EditAccountEngineerActivity
import com.example.jobsdev.maincontent.skillengineer.AddSkillActivity
import com.example.jobsdev.maincontent.skillengineer.ItemSkillEngineerDataClass
import com.example.jobsdev.maincontent.skillengineer.RecyclerViewSkillEngineerAdapter
import com.example.jobsdev.maincontent.webview.GitHubWebViewActivity
import com.example.jobsdev.onboard.OnBoardLoginActivity
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.PreferencesHelper
import com.google.android.material.tabs.TabLayout

class AccountEngineerFragment : Fragment() {

    private lateinit var sharedPref : PreferencesHelper
    private lateinit var pagerAdapter: TabPagerAdapter
    private lateinit var viewPager : ViewPager
    private lateinit var tabLayout : TabLayout
    private lateinit var binding : FragmentAccountEngineerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_engineer, container, false)

        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(activity, EditAccountEngineerActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            showDialogLogOut()
            showMessage("Log Out")
        }
        binding.tvGithub.setOnClickListener {
            startActivity(Intent(activity, GitHubWebViewActivity::class.java))
        }

        binding.btnAddSkill.setOnClickListener {
            startActivity(Intent(activity, AddSkillActivity::class.java))
        }

        pagerAdapter =
            TabPagerAdapter(
                childFragmentManager
            )
        addFragment(binding.root)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = PreferencesHelper(view.context)
        binding.tvEmailProfile.text = sharedPref.getValueString(Constant.prefEmail )

        val exampleListSkill = generateDummyList(10)
        binding.rvSkillEngineer.apply {
            adapter =
                RecyclerViewSkillEngineerAdapter(
                    exampleListSkill
                )
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun addFragment(view: View?) {
        viewPager = view!!.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)

        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)

    }

    private fun moveActivity() {
        val  intent = Intent(activity, OnBoardLoginActivity::class.java)
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

    private fun generateDummyList(size : Int) : List<ItemSkillEngineerDataClass> {
        val list = ArrayList<ItemSkillEngineerDataClass>()

        for (i in 0 until size) {
            val skillName = when(i%3) {
                0 -> "Kotlin"
                1 -> "Java"
                else -> "Laravel"
            }
            val item = ItemSkillEngineerDataClass("${i + 1}", skillName)
            list += item
        }
        return list
    }

}