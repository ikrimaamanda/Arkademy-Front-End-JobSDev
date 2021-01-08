package com.example.jobsdev.maincontent.projectcompany

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityEditProjectBinding
import com.example.jobsdev.sharedpreference.ConstantProjectCompany
import com.example.jobsdev.sharedpreference.PreferencesHelper

class EditProjectActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditProjectBinding
    private lateinit var sharedPref : PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_project)
        sharedPref = PreferencesHelper(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val image = sharedPref.getValueString(ConstantProjectCompany.projectImage)
        var img = "http://54.236.22.91:4000/image/$image"

        Glide.with(binding.ivProjectImage)
            .load(img)
            .placeholder(R.drawable.profile_pict_base)
            .error(R.drawable.profile_pict_base)
            .into(binding.ivProjectImage)

        binding.etProjectName.setText(sharedPref.getValueString(ConstantProjectCompany.projectName))
        val deadline = sharedPref.getValueString(ConstantProjectCompany.projectDeadline)!!.split("T")[0]
        binding.etDeadline.setText(deadline)
        binding.etDesc.setText(sharedPref.getValueString(ConstantProjectCompany.projectDesc))

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }
    }
}