package com.example.jobsdev.maincontent.hireengineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityDetailEngineerBinding
import com.example.jobsdev.maincontent.webview.GitHubWebViewActivity
import com.example.jobsdev.sharedpreference.Constant
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

        binding.tvFullName.text = intent.getStringExtra("name")
        binding.tvJobTitle.text = intent.getStringExtra("jobTitle")
        binding.tvJobType.text = intent.getStringExtra("jobType")
        binding.tvLocation.text = intent.getStringExtra("location")
        binding.tvDesc.text = intent.getStringExtra("description")
        binding.tvEmailProfile.text = intent.getStringExtra("email")

        val image = intent.getStringExtra("image")
        var img = "http://54.236.22.91:4000/image/$image"
        Log.d("image: ", img!!)

        Glide.with(binding.civProfilePict)
            .load(img)
            .placeholder(R.drawable.profile_pict_base)
            .error(R.drawable.profile_pict_base)
            .into(binding.civProfilePict)

        if(sharedPref.getValueInt(Constant.prefLevel) == 0) {
            binding.btnHireEngineer.showOrGone(false)
        } else {
            binding.btnHireEngineer.showOrGone(true)
            binding.btnHireEngineer.setOnClickListener {
                startActivity(Intent(this, FormHireActivity::class.java))
            }
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

    fun View.showOrGone(show: Boolean) {
        visibility = if(show) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

}