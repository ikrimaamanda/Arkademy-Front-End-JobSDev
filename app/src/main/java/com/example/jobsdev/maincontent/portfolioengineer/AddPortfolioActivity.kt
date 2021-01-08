package com.example.jobsdev.maincontent.portfolioengineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityAddPortfolioBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.maincontent.hireengineer.HireApiService
import com.example.jobsdev.maincontent.hireengineer.HireResponse
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.*
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class AddPortfolioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPortfolioBinding
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : JobSDevApiService
    val typeApp = arrayOf("mobile app", "web app")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_portfolio)
        sharedPref = PreferencesHelper(this)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(JobSDevApiService::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        configSpinnerTypeApp()

        binding.btnAddPortfolio.setOnClickListener {
            if(binding.etAppName.text.isNullOrEmpty() || binding.etDescriptionPortfolio.text.isNullOrEmpty() || binding.etLinkPubPortfolio.text.isNullOrEmpty() || binding.etLinkRepoPortfolio.text.isNullOrEmpty() || binding.etWorkplacePortfolio.text.isNullOrEmpty()) {
                showMessage("Please filled all field")
                binding.etAppName.requestFocus()
            } else {
                callAddPortfolioApi()
            }

        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }

    }

    private fun configSpinnerTypeApp() {
        binding.spinnerTypeApp.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, typeApp)
        binding.spinnerTypeApp.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@AddPortfolioActivity, "None", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(this@AddPortfolioActivity, "${typeApp[position]} clicked", Toast.LENGTH_SHORT).show();
                sharedPref.putValue(ConstantPortfolio.typeApp, typeApp[position])
            }

        }
    }

    private fun callAddPortfolioApi() {
        coroutineScope.launch {
            val results = withContext(Dispatchers.IO){
                try {
                    val enId = sharedPref.getValueString(ConstantAccountEngineer.engineerId)!!.toRequestBody("text/plain".toMediaTypeOrNull())
                    val prAppName = binding.etAppName.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val prDesc = binding.etDescriptionPortfolio.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val prLinkPub = binding.etLinkPubPortfolio.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val prLinkRepo = binding.etLinkRepoPortfolio.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val prWorkplace = binding.etWorkplacePortfolio.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val prType = sharedPref.getValueString(ConstantPortfolio.typeApp)!!
                        .toRequestBody("text/plain".toMediaTypeOrNull())

                    service.addPortfolio(prAppName, prDesc, prLinkPub, prLinkRepo, prWorkplace, prType, enId!!)
                } catch (e:Throwable) {
                    Log.e("errorM", e.message.toString())
                    e.printStackTrace()
                }
            }
            Log.d("addPortfolioReq", results.toString())

            if(results is GeneralResponse) {
                Log.d("addPortfolioReq", results.toString())

                if(results.success) {
                    showMessage(results.message)
                    moveActivity()
                } else {
                    showMessage(results.message)
                }
            }
            showMessage("Something wrong...")
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