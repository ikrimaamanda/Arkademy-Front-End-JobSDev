package com.example.jobsdev.maincontent.hireengineer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityFormHireBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.maincontent.projectcompany.ProjectCompanyModel
import com.example.jobsdev.maincontent.projectcompany.ProjectResponse
import com.example.jobsdev.maincontent.projectcompany.ProjectsCompanyApiService
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.sharedpreference.ConstantAccountCompany
import com.example.jobsdev.sharedpreference.ConstantProjectCompany
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class FormHireActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFormHireBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : HireApiService
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var viewModel : FormHireViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_form_hire)
        sharedPref = PreferencesHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(HireApiService::class.java)

        viewModel = ViewModelProvider(this).get(FormHireViewModel::class.java)
        viewModel.setService(service)
        viewModel.setSharedPref(sharedPref)

        configSpinnerProject()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        Toast.makeText(this, "${intent.getStringExtra("enId")}", Toast.LENGTH_SHORT).show()
        
        binding.btnHire.setOnClickListener {
            if(binding.spinnerProject.equals("none") || binding.etHireMessage.text.isEmpty() || binding.etPrice.text.isEmpty()) {
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
                    moveActivity()
                })
            } else {
                viewModel.isMessage.observe(this, Observer { it1 ->
                    showMessage(it1)
                })
            }
        })
    }

    private fun configSpinnerProject() {
        val service = ApiClient.getApiClient(context = this)?.create(ProjectsCompanyApiService::class.java)

        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getProjectByCnId(sharedPref.getValueString(ConstantAccountCompany.companyId))
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if(response is ProjectResponse) {
                val list = response.data?.map {
                    ProjectCompanyModel(it.projectId, it.companyId, it.projectName, it.projectDesc, it.projectDeadline, it.projectImage, it.projectCreateAt, it.projectUpdateAt)
                }

                val projectName =
                    arrayOfNulls<String>(list.size)
                val projectId =
                    arrayOfNulls<String>(list.size)

                for (i in 0 until list.size) {
                    projectName[i] = list.get(i).projectName
                    projectId[i] = list.get(i).projectId
                }

                binding.spinnerProject.adapter = ArrayAdapter<String>(this@FormHireActivity, R.layout.support_simple_spinner_dropdown_item, projectName)

                binding.spinnerProject.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Toast.makeText(this@FormHireActivity, "None", Toast.LENGTH_SHORT).show()
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        Toast.makeText(this@FormHireActivity, "${projectName[position]} clicked", Toast.LENGTH_SHORT).show();
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