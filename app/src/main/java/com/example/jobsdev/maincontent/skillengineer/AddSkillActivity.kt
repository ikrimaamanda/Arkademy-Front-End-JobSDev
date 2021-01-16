package com.example.jobsdev.maincontent.skillengineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityAddSkillBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class AddSkillActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSkillBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : JobSDevApiService
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var viewModel : AddSkillViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_skill)
        sharedPref = PreferencesHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(JobSDevApiService::class.java)
        viewModel = ViewModelProvider(this).get(AddSkillViewModel::class.java)

        viewModel.setService(service)
        viewModel.setSharedPref(sharedPref)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnAddSkill.setOnClickListener {
            if(binding.etAddSkillName.text.isNullOrEmpty()) {
                showMessage("Please input skill name")
                binding.etAddSkillName.requestFocus()
            } else {
                viewModel.callAddSkillApi(binding.etAddSkillName.text.toString())
            }
        }

        subscribeLoadingLiveData()
        subscribeAddSkillLiveData()
    }

    private fun subscribeLoadingLiveData() {
        viewModel.isLoadingLiveData.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun subscribeAddSkillLiveData() {
        viewModel.isAddSkillLiveData.observe(this, Observer {
            if (it) {
                viewModel.isMessage.observe(this, Observer { it1->
                    showMessage(it1)
                })
                moveActivity()
            } else {
                viewModel.isMessage.observe(this, Observer { it1->
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