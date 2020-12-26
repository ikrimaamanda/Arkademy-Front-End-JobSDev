package com.example.jobsdev.maincontent.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R
import com.example.jobsdev.databinding.FragmentListProjectCompanyBinding
import com.example.jobsdev.maincontent.projectcompany.ListProjectAdapter
import com.example.jobsdev.maincontent.projectcompany.ProjectCompanyModel
import com.example.jobsdev.maincontent.projectcompany.ProjectResponse
import com.example.jobsdev.maincontent.projectcompany.ProjectsCompanyApiService
import com.example.jobsdev.remote.ApiClient
import kotlinx.coroutines.*

class ListProjectCompanyFragment : Fragment() {

    private lateinit var binding : FragmentListProjectCompanyBinding
    private lateinit var coroutineScope : CoroutineScope

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_project_company, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        binding.rvListProject.adapter = ListProjectAdapter()
        binding.rvListProject.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        getProjectByCnId()
        return binding.root
    }

    fun getProjectByCnId() {
        val service = ApiClient.getApiClient()?.create(ProjectsCompanyApiService::class.java)

        coroutineScope.launch {
            Log.d("ikrima", "Start: ${Thread.currentThread().name}")

            val response = withContext(Dispatchers.IO) {
                Log.d("ikrima", "CallApi: ${Thread.currentThread().name}")

                try {
                    service?.getProjectByCnId()
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            Log.d("Ikrima Response", response.toString())

            if(response is ProjectResponse) {
                val list = response.data?.map {
                    ProjectCompanyModel(it.projectId, it.companyId, it.projectName, it.projectDesc, it.projectDeadline, it.projectImage, it.projectCreateAt, it.projectUpdateAt)
                }
                (binding.rvListProject.adapter as ListProjectAdapter).addList(list)
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

}