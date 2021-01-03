package com.example.jobsdev.maincontent.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R
import com.example.jobsdev.databinding.FragmentListHireEngineerBinding
import com.example.jobsdev.maincontent.listengineer.DetailEngineerModel
import com.example.jobsdev.maincontent.listengineer.EngineerApiService
import com.example.jobsdev.maincontent.listengineer.ListEngineerAdapter
import com.example.jobsdev.maincontent.listengineer.ListEngineerResponse
import com.example.jobsdev.maincontent.listhireengineer.*
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class ListHireEngineerFragment : Fragment(), ListHireEngineerAdapter.OnListHireEngineerClickListener {

    private lateinit var binding : FragmentListHireEngineerBinding
    private var listHireEngineer = ArrayList<DetailHireEngineerModel>()
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var sharedPref : PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_hire_engineer, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        sharedPref = PreferencesHelper(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getListHireEngineer()

        binding.recyclerViewHireEngineer.adapter = ListHireEngineerAdapter(listHireEngineer, this)
        binding.recyclerViewHireEngineer.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    private fun getListHireEngineer() {
        val service = ApiClient.getApiClient(requireContext())?.create(HireEngineerApiService::class.java)

        coroutineScope.launch {
            Log.d("listHireEngineer", "Start: ${Thread.currentThread().name}")

            val response = withContext(Dispatchers.IO) {
                Log.d("listHireEngineer", "CallApi: ${Thread.currentThread().name}")

                try {
                    service?.getHireByEngineerId(sharedPref.getValueString(ConstantAccountEngineer.engineerId))
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            Log.d("HireResponse", response.toString())

            if(response is ListHireEngineerResponse) {
                Log.d("HireResponse", response.toString())

                val list = response.data?.map {
                    DetailHireEngineerModel(it.hireId, it.companyId, it.companyName, it.position,
                        it.companyFields, it.companyCity, it.companyDescription, it.companyInstagram,
                        it.companyLinkedIn, it.engineerId, it.projectId, it.projectName, it.projectDescription,
                        it.projectDeadline, it.projectImage, it.price, it.message, it.status, it.dateConfirm)
                }
                (binding.recyclerViewHireEngineer.adapter as ListHireEngineerAdapter).addListHireEngineer(list)
            } else {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onHireEngineerItemClicked(position: Int) {
        Toast.makeText(requireContext(), "${listHireEngineer[position].projectName} clicked", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), DetailHireEngineerActivity::class.java)
        intent.putExtra("hireId", listHireEngineer[position].hireId)
        intent.putExtra("projectName", listHireEngineer[position].projectName)
        intent.putExtra("companyName", listHireEngineer[position].companyName)
        intent.putExtra("location", listHireEngineer[position].companyCity)
        intent.putExtra("hirePrice", listHireEngineer[position].price)
        intent.putExtra("deadline", listHireEngineer[position].projectDeadline)
        intent.putExtra("message", listHireEngineer[position].message)
        intent.putExtra("description", listHireEngineer[position].projectDescription)
        intent.putExtra("projectImage", listHireEngineer[position].projectImage)
        intent.putExtra("status", listHireEngineer[position].status)

        startActivity(intent)
    }
}