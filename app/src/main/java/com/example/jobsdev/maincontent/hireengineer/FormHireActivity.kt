package com.example.jobsdev.maincontent.hireengineer

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityFormHireBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.maincontent.listhireengineer.ListHireEngineerResponse
import com.example.jobsdev.maincontent.projectcompany.ProjectCompanyModel
import com.example.jobsdev.maincontent.projectcompany.ProjectResponse
import com.example.jobsdev.maincontent.projectcompany.ProjectsCompanyApiService
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.sharedpreference.*
import kotlinx.coroutines.*

class FormHireActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFormHireBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : HireApiService
    private lateinit var service2 : ProjectsCompanyApiService
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var viewModel : FormHireViewModel

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_form_hire)
        sharedPref = PreferencesHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(HireApiService::class.java)
        service2 = ApiClient.getApiClient(context = this)!!.create(ProjectsCompanyApiService::class.java)

        viewModel = ViewModelProvider(this).get(FormHireViewModel::class.java)
        viewModel.setService(service)
        viewModel.setSharedPref(sharedPref)

        configSpinnerProject()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnHire.setOnClickListener {
            if(binding.etHireMessage.text.isEmpty() || binding.etPrice.text.isEmpty()) {
                showMessage("Please filled all field")
                binding.etHireMessage.requestFocus()
            } else {
                viewModel.callHireApi(binding.etPrice.text.toString(), binding.etHireMessage.text.toString())
            }
        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }

        subscribeHireLiveData()

    }

    private fun subscribeHireLiveData() {
        viewModel.isHireLiveData.observe(this, Observer {
            if (it) {
                viewModel.isMessage.observe(this, Observer { it1 ->
                    showMessage(it1)
                })
                moveActivity()
            } else {
                viewModel.isMessage.observe(this, Observer { it1 ->
                    if (it1 == "expired") {
                        showMessage("Please sign in again!")
                    } else {
                        showMessage(it1)
                    }
                })
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun configSpinnerProject() {
        coroutineScope.launch {
            val cnId = sharedPref.getValueString(ConstantAccountCompany.companyId)
            val enId = sharedPref.getValueString(ConstantDetailEngineer.engineerId)
            val listProjectIdDoHire = mutableListOf<Long>()

            val result1 = withContext(Dispatchers.IO) {
                try {
                    service.getHireByEngineerId(enId)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            val result2 = withContext(Dispatchers.IO) {
                try {
                    service2.getProjectByCnId(cnId)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (result1 is ListHireEngineerResponse) {
                result1.data.map {
                    if (it.companyId == cnId!!.toInt() && it.engineerId == enId!!.toInt()) {
                        listProjectIdDoHire.add(it.projectId!!.toLong())
                    }
                }
            }


            if(result2 is ProjectResponse) {
                val list = result2.data.map {
                    ProjectCompanyModel(it.projectId, it.companyId, it.projectName, it.projectDesc, it.projectDeadline, it.projectImage, it.projectCreateAt, it.projectUpdateAt)
                }

                val mutableListProject = list.toMutableList()

                for (i in 0 until listProjectIdDoHire.size) {
                    mutableListProject.removeIf{it.projectId.toInt() == listProjectIdDoHire[i].toInt()}
                }

                val projectName =
                    arrayOfNulls<String>(mutableListProject.size)
                val projectId =
                    arrayOfNulls<String>(mutableListProject.size)

                for (i in 0 until mutableListProject.size) {
                    projectName[i] = mutableListProject.get(i).projectName
                    projectId[i] = mutableListProject.get(i).projectId
                }
                if (mutableListProject.isNullOrEmpty()) {
                    binding.btnHire.visibility = View.GONE
                    Toast.makeText(this@FormHireActivity, "You don't have any project to hire this engineer!", Toast.LENGTH_SHORT).show()
                }

                binding.spinnerProject.adapter = ArrayAdapter<String>(this@FormHireActivity, R.layout.support_simple_spinner_dropdown_item, projectName)

                binding.spinnerProject.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        sharedPref.putValue(ConstantProjectCompany.projectId, projectId[position].toString())
                    }
                }
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