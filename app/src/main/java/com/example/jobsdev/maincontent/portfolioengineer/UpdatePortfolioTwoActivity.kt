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
import com.example.jobsdev.databinding.ActivityUpdatePortfolioTwoBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.ConstantPortfolio
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class UpdatePortfolioTwoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUpdatePortfolioTwoBinding
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : JobSDevApiService
    val typeApp = arrayOf("mobile app", "web app")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_portfolio_two)
        sharedPref = PreferencesHelper(this)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(JobSDevApiService::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val portfolioId = intent.getIntExtra("updatePortfolioId", 0)
        binding.etAppName.setText(intent.getStringExtra("appName"))
        binding.etDescriptionPortfolio.setText(intent.getStringExtra("portfolioDesc"))
        binding.etLinkPubPortfolio.setText(intent.getStringExtra("linkPub"))
        binding.etLinkRepoPortfolio.setText(intent.getStringExtra("linkRepo"))
        binding.etWorkplacePortfolio.setText(intent.getStringExtra("workPlace"))

        configSpinnerTypeApp()

        binding.btnUpdatePortfolio.setOnClickListener {
            if(binding.etAppName.text.isNullOrEmpty() || binding.etDescriptionPortfolio.text.isNullOrEmpty() || binding.etLinkPubPortfolio.text.isNullOrEmpty() || binding.etLinkRepoPortfolio.text.isNullOrEmpty() || binding.etWorkplacePortfolio.text.isNullOrEmpty()) {
                showMessage("Please filled all field")
                binding.etAppName.requestFocus()
            } else {
                callUpdatePortfolioApi(portfolioId)
            }

        }

        binding.btnDeletePortfolio.setOnClickListener {
            callDeletePortfolioApi(portfolioId)
        }
    }

    private fun callDeletePortfolioApi(portfolioId : Int) {
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.deletePortfolioByPrId(portfolioId)
                } catch (e: Throwable) {
                    Log.e("errorKah?", e.message.toString())
                    e.printStackTrace()
                }
            }

            if(response is GeneralResponse) {
                if(response.success) {
                    showMessage(response.message)
                    moveActivity()
                }
            }
            showMessage("Something wrong...")
        }
    }

    private fun configSpinnerTypeApp() {
        binding.spinnerTypeApp.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, typeApp)
        binding.spinnerTypeApp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                showMessage("None")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
//                showMessage("${typeApp[position]} clicked")
                sharedPref.putValue(ConstantPortfolio.typeApp, typeApp[position])
            }

        }
    }

    private fun callUpdatePortfolioApi(portfolioId : Int) {
        coroutineScope.launch {
            val results = withContext(Dispatchers.IO){
                try {
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

                    service.updatePortfolio(portfolioId, prAppName, prDesc, prLinkPub, prLinkRepo, prWorkplace, prType)

                } catch (e:Throwable) {
                    Log.e("errorYa", e.message.toString())
                    e.printStackTrace()
                }
            }
            Log.d("updatePortfolioReq", results.toString())

            if(results is GeneralResponse) {
                Log.d("updatePortfolioReq", results.toString())

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