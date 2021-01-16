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
import com.example.jobsdev.databinding.ActivityUpdateExperienceBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.JobSDevApiService
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class UpdateExperienceActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUpdateExperienceBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : JobSDevApiService
    private lateinit var viewModel : UpdateExperienceViewModel
    private lateinit var updateStartDate: DatePickerDialog.OnDateSetListener
    private lateinit var updateEndDate: DatePickerDialog.OnDateSetListener
    private lateinit var c: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_experience)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(JobSDevApiService::class.java)
        viewModel = ViewModelProvider(this).get(UpdateExperienceViewModel::class.java)

        viewModel.setService(service)

        c = Calendar.getInstance()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val exId = intent.getIntExtra("exId", 0)
        binding.etUpdatePositionExperience.setText(intent.getStringExtra("exPosition"))
        binding.etUpdateCompanyExperience.setText(intent.getStringExtra("exCompany"))
        val startDate = intent.getStringExtra("exStartDate")!!.split("T")[0]
        binding.etUpdateStartDateExperience.setText(startDate)
        val endDate = intent.getStringExtra("exEndDate")!!.split("T")[0]
        binding.etUpdateEndDateExperience.setText(endDate)
        binding.etUpdateDescriptionExperience.setText(intent.getStringExtra("exDesc"))

        binding.etUpdateStartDateExperience.setOnClickListener {
            DatePickerDialog(
                this, updateStartDate, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.etUpdateEndDateExperience.setOnClickListener {
            DatePickerDialog(
                this, updateEndDate, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        startDateExperience()
        endDateExperience()

        binding.btnUpdateExperience.setOnClickListener {
            viewModel.callUpdateExperienceApi(exId, binding.etUpdatePositionExperience.text.toString(),
                binding.etUpdateCompanyExperience.text.toString(),
                binding.etUpdateStartDateExperience.text.toString(),binding.etUpdateEndDateExperience.text.toString(),
                binding.etUpdateDescriptionExperience.text.toString())
        }

        binding.btnDeleteExp.setOnClickListener {
            viewModel.callDeleteExpApi(exId)
        }

        subscribeLoadingLiveData()
        subscribeUpdateLiveData()
        subscribeDeleteLiveData()

    }

    private fun startDateExperience() {
        updateStartDate = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val day = findViewById<TextView>(R.id.et_update_start_date_experience)
            val formatDate = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(formatDate, Locale.US)

            day.text = sdf.format(c.time)
        }
    }

    private fun endDateExperience() {
        updateEndDate = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val day = findViewById<TextView>(R.id.et_update_end_date_experience)
            val formatDate = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(formatDate, Locale.US)

            day.text = sdf.format(c.time)
        }
    }

    private fun subscribeLoadingLiveData() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun subscribeDeleteLiveData() {
        viewModel.isDeleteLivedata.observe(this, Observer {
            if (it) {
                viewModel.isMessage.observe(this, Observer { it1->
                    showMessage(it1)
                    moveActivity()
                })
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

    private fun subscribeUpdateLiveData() {
        viewModel.isUpdateLivedata.observe(this, Observer {
            if (it) {
                viewModel.isMessage.observe(this, Observer { it1->
                    showMessage(it1)
                    moveActivity()
                })
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