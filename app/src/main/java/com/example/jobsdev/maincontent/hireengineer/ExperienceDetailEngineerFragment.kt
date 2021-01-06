package com.example.jobsdev.maincontent.hireengineer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R
import com.example.jobsdev.databinding.FragmentExperienceDetailEngineerBinding
import com.example.jobsdev.maincontent.experienceengineer.ItemExperienceModel
import com.example.jobsdev.maincontent.experienceengineer.RecyclerViewListExperienceAdapter
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.GetExperienceByEnIdResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantDetailEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class ExperienceDetailEngineerFragment : Fragment(), RecyclerViewListExperienceAdapter.OnListExperienceClickListener {
    private lateinit var binding : FragmentExperienceDetailEngineerBinding
    private val listExperience = ArrayList<ItemExperienceModel>()
    private lateinit var coroutineScope : CoroutineScope
    private lateinit var sharedPref : PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_experience_detail_engineer, container, false)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        sharedPref = PreferencesHelper(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val enId = sharedPref.getValueString(ConstantDetailEngineer.engineerId)
        getListExperience(enId!!.toInt())

        binding.recyclerViewExperience.adapter = RecyclerViewListExperienceAdapter(listExperience, this)
        binding.recyclerViewExperience.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

    }

    private fun getListExperience(enId : Int) {
        val service = ApiClient.getApiClient(requireContext())?.create(JobSDevApiService::class.java)

        coroutineScope.launch {

            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getListExperienceByEnId(enId)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            Log.d("ExpResponse", response.toString())

            if(response is GetExperienceByEnIdResponse) {
                Log.d("ExpResponse", response.toString())

                val list = response.data?.map {
                    ItemExperienceModel(it.enId, it.exId, it.exPosition, it.exCompany, it.exStartDate, it.exEndDate, it.exDesc)
                }
                (binding.recyclerViewExperience.adapter as RecyclerViewListExperienceAdapter).addListExperience(list)
            } else {
                Toast.makeText(requireContext(), "Hello, your list experience is empty!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onExperienceItemClicked(position: Int) {
        Toast.makeText(requireContext(), "${listExperience[position].position} clicked", Toast.LENGTH_SHORT).show()
    }

}