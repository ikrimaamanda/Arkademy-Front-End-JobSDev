package com.example.jobsdev.maincontent.editprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityEditAccountCompanyBinding
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.ConstantAccountCompany
import com.example.jobsdev.sharedpreference.PreferencesHelper

class EditAccountCompanyActivity : AppCompatActivity() {

    private lateinit var sharedPref : PreferencesHelper

    private lateinit var binding : ActivityEditAccountCompanyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_account_company)
        sharedPref = PreferencesHelper(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val image = sharedPref.getValueString(Constant.prefProfilePict)
        var img = "http://54.236.22.91:4000/image/$image"

        Glide.with(binding.civEditProfilePict)
            .load(img)
            .placeholder(R.drawable.profile_pict_base)
            .error(R.drawable.profile_pict_base)
            .into(binding.civEditProfilePict)

        binding.tvNameNow.text = sharedPref.getValueString(ConstantAccountCompany.companyName)
        binding.tvPhoneNumberNow.text = sharedPref.getValueString(Constant.prefPhoneNumber)
        binding.tvPositionNow.text = sharedPref.getValueString(ConstantAccountCompany.position)
        binding.tvFieldsNow.text = sharedPref.getValueString(ConstantAccountCompany.fields)
        binding.tvLocationNow.text = sharedPref.getValueString(Constant.prefLocation)
        binding.tvDescriptionNow.text = sharedPref.getValueString(Constant.prefDescription)
        binding.tvInstagramNow.text = sharedPref.getValueString(ConstantAccountCompany.instagram)
        binding.tvLinkedinNow.text = sharedPref.getValueString(ConstantAccountCompany.linkedin)

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }
    }
}