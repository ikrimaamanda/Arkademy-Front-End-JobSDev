package com.example.jobsdev.maincontent.experienceengineer

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
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
import java.text.SimpleDateFormat
import java.util.*

class AddExperienceActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddExperienceEngineerBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : JobSDevApiService
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var viewModel : AddExperienceViewModel
    private lateinit var startDate: DatePickerDialog.OnDateSetListener
    private lateinit var endDate: DatePickerDialog.OnDateSetListener
    private lateinit var c: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_experience_engineer)
        sharedPref = PreferencesHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(JobSDevApiService::class.java)
        viewModel = ViewModelProvider(this).get(AddExperienceViewModel::class.java)

        viewModel.setService(service)
        viewModel.setSharedPref(sharedPref)

        c = Calendar.getInstance()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.etStartDateNewExperience.setOnClickListener {
            DatePickerDialog(
                this, startDate, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.etEndDateNewExperience.setOnClickListener {
            DatePickerDialog(
                this, endDate, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        startDateExperience()
        endDateExperience()

        binding.btnAddExperience.setOnClickListener {
            if (binding.etPositionExperience.text.isNullOrEmpty() || binding.etCompanyExperience.text.isNullOrEmpty() || binding.etStartDateNewExperience.text.isNullOrEmpty() || binding.etEndDateNewExperience.text.isNullOrEmpty() || binding.etDescriptionExperience.text.isNullOrEmpty()) {
                showMessage("Please filled all fields")
                binding.etPositionExperience.requestFocus()
            } else {
                viewModel.callAddExperienceApi(binding.etPositionExperience.text.toString(),  binding.etCompanyExperience.text.toString(), binding.etStartDateNewExperience.text.toString(), binding.etEndDateNewExperience.text.toString(), binding.etDescriptionExperience.text.toString())
            }
        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }

        subscribeLoadingLiveData()
        subscribeLiveData()
    }

    private fun startDateExperience() {
        startDate = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val day = findViewById<TextView>(R.id.et_start_date_new_experience)
            val formatDate = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(formatDate, Locale.US)

            day.text = sdf.format(c.time)
        }
    }

    private fun endDateExperience() {
        endDate = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val day = findViewById<TextView>(R.id.et_end_date_new_experience)
            val formatDate = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(formatDate, Locale.US)

            day.text = sdf.format(c.time)
        }
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