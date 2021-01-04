package com.example.jobsdev.maincontent.skillengineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityUpdateSkillBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class UpdateSkillActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUpdateSkillBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : JobSDevApiService
    private lateinit var sharedPref : PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_skill)
        sharedPref = PreferencesHelper(this)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(JobSDevApiService::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val name = intent.getStringExtra("skillName")
        binding.tvCurrentSkillName.text = name
        val skillId = intent.getIntExtra("skillId",0)

        binding.btnUpdateSkill.setOnClickListener {
            if(binding.etUpdateSkillName.text.isNullOrEmpty()) {
                showMessage("Please input skill name")
                binding.etUpdateSkillName.requestFocus()
            }
            callUpdateSkillApi(skillId!!, binding.etUpdateSkillName.text.toString())
        }

        binding.btnDelete.setOnClickListener {
            callDeleteSkillApi(skillId)
        }
    }

    private fun callUpdateSkillApi(skillId : Int, skillName : String) {
        coroutineScope.launch {
            val results = withContext(Dispatchers.IO){
                try {
                    service.updateSkillName(skillId, skillName)
                } catch (e:Throwable) {
                    Log.e("errorM", e.message.toString())
                    e.printStackTrace()
                }
            }
            Log.d("updateSkill", results.toString())

            if(results is GeneralResponse) {
                Log.d("updateSkill", results.toString())

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

    private fun callDeleteSkillApi(skillId : Int) {
        coroutineScope.launch {
            val results = withContext(Dispatchers.IO){
                try {
                    service.deleteSkillBySkId(skillId)
                } catch (e:Throwable) {
                    Log.e("errorM", e.message.toString())
                    e.printStackTrace()
                }
            }
            Log.d("deleteSkill", results.toString())

            if(results is GeneralResponse) {
                Log.d("deleteSkill", results.toString())

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