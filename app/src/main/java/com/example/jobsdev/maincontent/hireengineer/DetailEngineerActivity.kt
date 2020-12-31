package com.example.jobsdev.maincontent.hireengineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityDetailEngineerBinding
import com.example.jobsdev.maincontent.webview.GitHubWebViewActivity
import com.example.jobsdev.sharedpreference.ConstantDetailEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper

class DetailEngineerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailEngineerBinding
    private lateinit var pagerAdapter: TabPagerDetailEngineerAdapter
    private lateinit var sharedPref : PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_engineer)
        sharedPref = PreferencesHelper(this)

        binding.tvFullName.text = sharedPref.getValueString(ConstantDetailEngineer.engineerName)
        binding.tvJobTitle.text = sharedPref.getValueString(ConstantDetailEngineer.engineerJobTitle)
        binding.tvJobType.text = sharedPref.getValueString(ConstantDetailEngineer.engineerJobType)
        binding.tvLocation.text = sharedPref.getValueString(ConstantDetailEngineer.location)
        binding.tvDesc.text = sharedPref.getValueString(ConstantDetailEngineer.description)
        binding.tvEmailProfile.text = sharedPref.getValueString(ConstantDetailEngineer.email)

        val image = intent.getStringExtra("image")
        var img = "http://54.236.22.91:4000/image/$image"
        Log.d("image: ", img!!)

        Glide.with(binding.civProfilePict)
            .load(img)
            .placeholder(R.drawable.profile_pict_base)
            .error(R.drawable.profile_pict_base)
            .into(binding.civProfilePict)

        binding.btnHireEngineer.setOnClickListener {
            startActivity(Intent(this, FormHireActivity::class.java))
        }

        val exampleListSkill = generateDummyList(10)
        binding.rvSkillEngineer.apply {
            adapter =
                RecyclerViewSkillHireEngineerAdapter(
                    exampleListSkill
                )
            layoutManager = LinearLayoutManager(this@DetailEngineerActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        pagerAdapter = TabPagerDetailEngineerAdapter(supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun generateDummyList(size : Int) : List<ItemSkillHireEngineerDataClass> {
        val list = ArrayList<ItemSkillHireEngineerDataClass>()

        for (i in 0 until size) {
            val skillName = when(i%3) {
                0 -> "Kotlin"
                1 -> "Java"
                else -> "Laravel"
            }
            val item = ItemSkillHireEngineerDataClass("${i + 1}",i, skillName)
            list += item
        }
        return list
    }

}