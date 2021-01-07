package com.example.jobsdev.maincontent.experienceengineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityAddExperienceEngineerBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class AddExperienceActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddExperienceEngineerBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : JobSDevApiService
    private lateinit var sharedPref : PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_experience_engineer)
        sharedPref = PreferencesHelper(this)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(JobSDevApiService::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnAddExperience.setOnClickListener {
            val enId = sharedPref.getValueString(ConstantAccountEngineer.engineerId)

            if (binding.etPositionExperience.text.isNullOrEmpty() || binding.etCompanyExperience.text.isNullOrEmpty() || binding.etStartDateExperience.text.isNullOrEmpty() || binding.etEndDateExperience.text.isNullOrEmpty() || binding.etDescriptionExperience.text.isNullOrEmpty()) {
                showMessage("Please filled all fields")
                binding.etPositionExperience.requestFocus()
            }
            callAddExperienceApi(binding.etPositionExperience.text.toString(),  binding.etCompanyExperience.text.toString(), binding.etStartDateExperience.text.toString(), binding.etEndDateExperience.text.toString(), binding.etDescriptionExperience.text.toString(), enId!!.toInt())
        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }
    }

    private fun callAddExperienceApi(exPosition : String, exCompany : String, exStartDate : String, exEndDate : String, exDesc : String, enId : Int) {
        coroutineScope.launch {
            val results = withContext(Dispatchers.IO){
                try {
                    service.addExperience(exPosition, exCompany, exStartDate, exEndDate, exDesc, enId)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if(results is GeneralResponse) {
                Log.d("addExp", results.toString())

                if(results.success) {
                    showMessage(results.message)
                    moveActivity()
                } else {
                    showMessage(results.message)
                }
            }
            showMessage("Something wrong ...")
        }
    }

    private fun showMessage(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun moveActivity() {
        startActivity(Intent(this, MainContentActivity::class.java))
        finish()
    }
}