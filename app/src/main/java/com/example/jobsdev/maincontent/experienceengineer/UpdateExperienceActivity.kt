package com.example.jobsdev.maincontent.experienceengineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityUpdateExperienceBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import kotlinx.coroutines.*

class UpdateExperienceActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUpdateExperienceBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : JobSDevApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_experience)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(JobSDevApiService::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val exId = intent.getIntExtra("exId", 0)
        binding.tvPositionNow.text = intent.getStringExtra("exPosition")
        binding.tvCompanyNow.text = intent.getStringExtra("exCompany")
        binding.tvStartDateNow.text = intent.getStringExtra("exStartDate")!!.split("T")[0]
        binding.tvEndDateNow.text = intent.getStringExtra("exEndDate")!!.split("T")[0]
        binding.tvDescriptionNow.text = intent.getStringExtra("exDesc")

        binding.btnUpdateExperience.setOnClickListener {
            callUpdateExperienceApi(exId, binding.etUpdatePositionExperience.text.toString(), binding.etUpdateCompanyExperience.text.toString(), binding.etUpdateStartDateExperience.text.toString(),binding.etUpdateEndDateExperience.text.toString(), binding.etUpdateDescriptionExperience.text.toString())
        }

        binding.btnDeleteExp.setOnClickListener {
            callDeleteExpApi(exId)
        }

    }

    private fun callUpdateExperienceApi(exId : Int, exPosition : String, exCompany : String, exStartDate : String, exEndDate : String, exDesc : String) {
        coroutineScope.launch {
            val results = withContext(Dispatchers.IO){
                try {
                    service.updateExperienceByExId(exId, exPosition, exCompany, exStartDate, exEndDate, exDesc)
                } catch (e:Throwable) {
                    Log.e("errorM", e.message.toString())
                    e.printStackTrace()
                }
            }
            Log.d("updateExp", results.toString())

            if(results is GeneralResponse) {
                Log.d("updateExp", results.toString())

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

    private fun callDeleteExpApi(exId : Int) {
        coroutineScope.launch {
            val results = withContext(Dispatchers.IO){
                try {
                    service.deleteExperienceByExId(exId)
                } catch (e:Throwable) {
                    Log.e("errorM", e.message.toString())
                    e.printStackTrace()
                }
            }
            Log.d("deleteExp", results.toString())

            if(results is GeneralResponse) {
                Log.d("deleteExp", results.toString())

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