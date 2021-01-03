 package com.example.jobsdev.maincontent.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R
import com.example.jobsdev.databinding.FragmentListProjectCompanyBinding
import com.example.jobsdev.maincontent.projectcompany.*
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.sharedpreference.ConstantAccountCompany
import com.example.jobsdev.sharedpreference.ConstantProjectCompany
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class ListProjectCompanyFragment : Fragment(), ListProjectAdapter.OnListProjectCompanyClickListener {

    private lateinit var binding : FragmentListProjectCompanyBinding
    private lateinit var coroutineScope : CoroutineScope
    private var listProjectCompany = ArrayList<ProjectCompanyModel>()
    private lateinit var sharedPref : PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_project_company, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        sharedPref = PreferencesHelper(requireContext())

        binding.rvListProject.adapter = ListProjectAdapter(listProjectCompany, this)
        binding.rvListProject.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        binding.btnAddProjectCompany.setOnClickListener {
            startActivity(Intent(context, AddNewProjectActivity::class.java))
        }

        getProjectByCnId()
        return binding.root
    }

    fun getProjectByCnId() {
        val service = ApiClient.getApiClient(requireContext())?.create(ProjectsCompanyApiService::class.java)

        coroutineScope.launch {
            Log.d("ikrima", "Start: ${Thread.currentThread().name}")

            val response = withContext(Dispatchers.IO) {
                Log.d("ikrima", "CallApi: ${Thread.currentThread().name}")

                try {
                    service?.getProjectByCnId(sharedPref.getValueString(ConstantAccountCompany.companyId))
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            Log.d("Ikrima Response", response.toString())

            if(response is ProjectResponse) {
                val list = response.data?.map {
                    ProjectCompanyModel(it.projectId, it.companyId, it.projectName, it.projectDesc, it.projectDeadline, it.projectImage, it.projectCreateAt, it.projectUpdateAt)
                }
                (binding.rvListProject.adapter as ListProjectAdapter).addListProjectCompany(list)
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onProjectCompanyItemClicked(position: Int) {
        Toast.makeText(requireContext(), "${listProjectCompany[position].projectName} clicked", Toast.LENGTH_SHORT).show()
        sharedPref.putValue(ConstantProjectCompany.projectId, listProjectCompany[position].projectId)

        val intent = Intent(requireContext(), DetailProjectCompanyActivity::class.java)
        intent.putExtra("projectName", listProjectCompany[position].projectName)
        intent.putExtra("deadline", listProjectCompany[position].projectDeadline)
        intent.putExtra("createdAt", listProjectCompany[position].projectCreateAt)
        intent.putExtra("updatedAt", listProjectCompany[position].projectUpdateAt)
        intent.putExtra("description", listProjectCompany[position].projectDesc)
        intent.putExtra("image", listProjectCompany[position].projectImage)

        startActivity(intent)
    }

}