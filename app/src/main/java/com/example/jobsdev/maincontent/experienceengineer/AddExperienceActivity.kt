package com.example.jobsdev.maincontent.experienceengineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityAddExperienceEngineerBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class AddExperienceActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddExperienceEngineerBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : JobSDevApiService
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var viewModel : AddExperienceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_experience_engineer)
        sharedPref = PreferencesHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(JobSDevApiService::class.java)
        viewModel = ViewModelProvider(this).get(AddExperienceViewModel::class.java)

        viewModel.setService(service)
        viewModel.setSharedPref(sharedPref)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnAddExperience.setOnClickListener {
            if (binding.etPositionExperience.text.isNullOrEmpty() || binding.etCompanyExperience.text.isNullOrEmpty() || binding.etStartDateExperience.text.isNullOrEmpty() || binding.etEndDateExperience.text.isNullOrEmpty() || binding.etDescriptionExperience.text.isNullOrEmpty()) {
                showMessage("Please filled all fields")
                binding.etPositionExperience.requestFocus()
            } else {
                viewModel.callAddExperienceApi(binding.etPositionExperience.text.toString(),  binding.etCompanyExperience.text.toString(), binding.etStartDateExperience.text.toString(), binding.etEndDateExperience.text.toString(), binding.etDescriptionExperience.text.toString())
            }
        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }

        subscribeLiveData()
    }

    private fun subscribeLiveData() {
        viewModel.isAddExperienceLiveData.observe(this, Observer {
            if (it) {
                viewModel.isMessageLiveData.observe(this, Observer { it1->
                    showMessage(it1)
                    moveActivity()
                })
            } else {
                viewModel.isMessageLiveData.observe(this, Observer { it1 ->
                    if (it1 == "expired") {
                        showMessage("Please sign in again!")
                    } else {
                        showMessage(it1)
                    }
                })
            }
        })
    }

    private fun showMessage(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun moveActivity() {
        startActivity(Intent(this, MainContentActivity::class.java))
        finish()
    }
}