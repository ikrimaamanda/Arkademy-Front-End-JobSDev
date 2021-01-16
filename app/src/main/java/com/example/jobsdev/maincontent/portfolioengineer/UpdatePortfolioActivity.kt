package com.example.jobsdev.maincontent.portfolioengineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityUpdatePortfolioTwoBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantPortfolio
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class UpdatePortfolioActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUpdatePortfolioTwoBinding
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : JobSDevApiService
    val typeApp = arrayOf("mobile app", "web app")
    private lateinit var viewModel : UpdatePortfolioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_portfolio_two)
        sharedPref = PreferencesHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(JobSDevApiService::class.java)
        viewModel = ViewModelProvider(this).get(UpdatePortfolioViewModel::class.java)
        viewModel.setService(service)
        viewModel.setSharedPref(sharedPref)

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
            val prAppName = binding.etAppName.text.toString()
            val prDesc = binding.etDescriptionPortfolio.text.toString()
            val prLinkPub = binding.etLinkPubPortfolio.text.toString()
            val prLinkRepo = binding.etLinkRepoPortfolio.text.toString()
            val prWorkplace = binding.etWorkplacePortfolio.text.toString()

            if(binding.etAppName.text.isNullOrEmpty() || binding.etDescriptionPortfolio.text.isNullOrEmpty() || binding.etLinkPubPortfolio.text.isNullOrEmpty() || binding.etLinkRepoPortfolio.text.isNullOrEmpty() || binding.etWorkplacePortfolio.text.isNullOrEmpty()) {
                showMessage("Please filled all field")
                binding.etAppName.requestFocus()
            } else {
                viewModel.callUpdatePortfolioApi(portfolioId, prAppName, prDesc, prLinkPub, prLinkRepo, prWorkplace)
//                callUpdatePortfolioApi(intent.getIntExtra("updatePortfolioId", 0))
            }

        }

        binding.btnDeletePortfolio.setOnClickListener {
            viewModel.callDeletePortfolioApi(portfolioId)
        }

        subscribeUpdateLiveData()
        subscribeDeleteLivedata()
        subsriveLoadingLiveData()
    }

    private fun subscribeDeleteLivedata() {
        viewModel.isDeletePortfolioLiveData.observe(this, Observer {
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

    private fun subscribeUpdateLiveData() {
        viewModel.isUpdatePortfolioLiveData.observe(this, Observer {
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

    private fun subsriveLoadingLiveData() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
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
                sharedPref.putValue(ConstantPortfolio.typeApp, typeApp[position])
            }

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