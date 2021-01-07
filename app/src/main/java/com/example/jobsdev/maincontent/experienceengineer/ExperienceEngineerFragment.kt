package com.example.jobsdev.maincontent.experienceengineer

import android.content.Intent
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
import com.example.jobsdev.databinding.FragmentExperienceEngineerBinding
import com.example.jobsdev.maincontent.listhireengineer.DetailHireEngineerModel
import com.example.jobsdev.maincontent.listhireengineer.HireEngineerApiService
import com.example.jobsdev.maincontent.listhireengineer.ListHireEngineerAdapter
import com.example.jobsdev.maincontent.listhireengineer.ListHireEngineerResponse
import com.example.jobsdev.maincontent.portfolioengineer.UpdatePortfolioTwoActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.GetExperienceByEnIdResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class ExperienceEngineerFragment : Fragment(), RecyclerViewListExperienceAdapter.OnListExperienceClickListener {

    private lateinit var binding : FragmentExperienceEngineerBinding
    private val listExperience = ArrayList<ItemExperienceModel>()
    private lateinit var coroutineScope : CoroutineScope
    private lateinit var sharedPref : PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_experience_engineer, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        sharedPref = PreferencesHelper(requireContext())

        binding.btnAddExperience.setOnClickListener {
            startActivity(Intent(activity, AddExperienceActivity::class.java))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val enId = sharedPref.getValueString(ConstantAccountEngineer.engineerId)
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
                    ItemExperienceModel(it.enId, it.exId, it.exPosition, it.exCompany, it.exStartDate, it.exEndDate, it?.exDesc)
                }
                (binding.recyclerViewExperience.adapter as RecyclerViewListExperienceAdapter).addListExperience(list)
            } else {
                Toast.makeText(requireContext(), "Hello, your list experience is empty!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onExperienceItemClicked(position: Int) {
        Toast.makeText(requireContext(), "${listExperience[position].exId} clicked", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), UpdateExperienceActivity::class.java)
        intent.putExtra("exId", listExperience[position].exId)
        startActivity(intent)
    }
}