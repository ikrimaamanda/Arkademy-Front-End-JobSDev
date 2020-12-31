package com.example.jobsdev.maincontent.fragment

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
import com.example.jobsdev.databinding.FragmentHomeBinding
import com.example.jobsdev.maincontent.hireengineer.DetailEngineerActivity
import com.example.jobsdev.maincontent.listengineer.DetailEngineerModel
import com.example.jobsdev.maincontent.listengineer.EngineerApiService
import com.example.jobsdev.maincontent.listengineer.ListEngineerAdapter
import com.example.jobsdev.maincontent.listengineer.ListEngineerResponse
import com.example.jobsdev.maincontent.recyclerview.OnListEngineerClickListener
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.sharedpreference.ConstantDetailEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class HomeFragment : Fragment(), OnListEngineerClickListener {

    private lateinit var binding : FragmentHomeBinding
    var listEngineer = ArrayList<DetailEngineerModel>()
    private lateinit var coroutineScope : CoroutineScope
    private lateinit var sharedPref : PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        sharedPref = PreferencesHelper(requireContext())
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        getListEngineer()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewListEngineer.adapter = ListEngineerAdapter(listEngineer,this)
        binding.recyclerViewListEngineer.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    fun getListEngineer() {
        val service = ApiClient.getApiClient(requireContext())?.create(EngineerApiService::class.java)

        coroutineScope.launch {
            Log.d("listengineer", "Start: ${Thread.currentThread().name}")

            val response = withContext(Dispatchers.IO) {
                Log.d("listengineer", "CallApi: ${Thread.currentThread().name}")

                try {
                    service?.getAllEngineer()
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            Log.d("listengineer Response", response.toString())

            if(response is ListEngineerResponse) {
                val list = response.data?.map {
                    DetailEngineerModel(it.engineerId, it.accountId, it.accountName, it.accountEmail, it.accountPhoneNumber, it.engineerJobTitle, it.engineerJobType, it.engineerLocation, it.engineerDescription, it.engineerProfilePict, it.skillEngineer)
                }
                (binding.recyclerViewListEngineer.adapter as ListEngineerAdapter).addListEngineer(list)
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onEngineerItemClicked(position: Int) {
        Toast.makeText(requireContext(), "${listEngineer[position].accountName} clicked", Toast.LENGTH_SHORT).show()
        sharedPref.putValue(ConstantDetailEngineer.engineerId, listEngineer[position].engineerId!!)
        sharedPref.putValue(ConstantDetailEngineer.engineerName, listEngineer[position].accountName!!)
        sharedPref.putValue(ConstantDetailEngineer.email, listEngineer[position].accountEmail!!)

        if(listEngineer[position].engineerJobTitle != null && listEngineer[position].engineerJobType != null && listEngineer[position].engineerLocation != null && listEngineer[position].engineerDescription != null) {
            sharedPref.putValue(ConstantDetailEngineer.engineerJobTitle, listEngineer[position].engineerJobTitle!!)
            sharedPref.putValue(ConstantDetailEngineer.engineerJobType, listEngineer[position].engineerJobType!!)
            sharedPref.putValue(ConstantDetailEngineer.location, listEngineer[position].engineerLocation!!)
            sharedPref.putValue(ConstantDetailEngineer.description, listEngineer[position].engineerDescription!!)
        }

        val intent = Intent(requireContext(), DetailEngineerActivity::class.java)
        intent.putExtra("image", listEngineer[position].engineerProfilePict)

        startActivity(intent)
    }
}