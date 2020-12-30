package com.example.jobsdev.maincontent.hireengineer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityFormHireBinding
import com.example.jobsdev.maincontent.projectcompany.ListProjectAdapter
import com.example.jobsdev.maincontent.projectcompany.ProjectCompanyModel
import com.example.jobsdev.maincontent.projectcompany.ProjectResponse
import com.example.jobsdev.maincontent.projectcompany.ProjectsCompanyApiService
import com.example.jobsdev.remote.ApiClient
import kotlinx.coroutines.*

class FormHireActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFormHireBinding
    private lateinit var coroutineScope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_form_hire)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        configSpinnerProject()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    private fun configSpinnerProject() {
        binding.spinnerProject

        val service = ApiClient.getApiClient(context = this)?.create(ProjectsCompanyApiService::class.java)

        coroutineScope.launch {
            Log.d("projectCom", "Start : ${Thread.currentThread().name}")

            val response = withContext(Dispatchers.IO) {
                Log.d("projectCom", "CallApi : ${Thread.currentThread().name}")

                try {
                    service?.getProjectByCnId()
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            Log.d("projectResponse", response.toString())

            if(response is ProjectResponse) {
                val list = response.data?.map {
                    ProjectCompanyModel(it.projectId, it.companyId, it.projectName, it.projectDesc, it.projectDeadline, it.projectImage, it.projectCreateAt, it.projectUpdateAt)
                }
                list as ArrayList<ProjectCompanyModel>
                var data : MutableList<String> = ArrayList()
                list.forEach {
                    data.add(0, it.projectName)
                }
                binding.spinnerProject. adapter = ArrayAdapter<String>(this@FormHireActivity, R.layout.support_simple_spinner_dropdown_item, data)
            }

        }

    }

}