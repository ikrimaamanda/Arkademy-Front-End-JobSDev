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
import com.example.jobsdev.R
import com.example.jobsdev.databinding.FragmentPortfolioDetailEngineerBinding
import com.example.jobsdev.maincontent.portfolioengineer.ItemPortfolioModel
import com.example.jobsdev.maincontent.portfolioengineer.RecyclerViewListPortfolioAdapter
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.GetPortfolioByEnIdResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.ConstantDetailEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class PortfolioDetailEngineerFragment : Fragment(), RecyclerViewListPortfolioAdapter.OnPortfolioClickListener {
    private lateinit var binding : FragmentPortfolioDetailEngineerBinding
    var listPortfolio = ArrayList<ItemPortfolioModel>()
    private lateinit var coroutineScope : CoroutineScope
    private lateinit var sharedPref : PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_portfolio_detail_engineer, container, false)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        sharedPref = PreferencesHelper(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val enId = sharedPref.getValueString(ConstantDetailEngineer.engineerId)
        getListPortfolio(enId!!.toInt())

        var portfolioAdapter = RecyclerViewListPortfolioAdapter(listPortfolio, this)
        binding.recyclerViewPortfolio.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewPortfolio.adapter = portfolioAdapter

    }

    private fun getListPortfolio(enId : Int) {
        val service = ApiClient.getApiClient(requireContext())?.create(JobSDevApiService::class.java)

        coroutineScope.launch {
            Log.d("listPort", "Start: ${Thread.currentThread().name}")

            val response = withContext(Dispatchers.IO) {
                Log.d("listPort", "CallApi: ${Thread.currentThread().name}")

                try {
                    service?.getListPortfolioByEnId(enId)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            Log.d("PortResponse", response.toString())

            if(response is GetPortfolioByEnIdResponse) {
                Log.d("PortResponse", response.toString())

                val list = response.data?.map {
                    ItemPortfolioModel(it.enId, it.portfolioId, it.portfolioprAppName, it.portfolioDesc, it.portfolioLinkPub, it.portfolioLinkRepo, it.portfolioWorkPlace, it.portfolioType, it.portfolioImage)
                }
                (binding.recyclerViewPortfolio.adapter as RecyclerViewListPortfolioAdapter).addListPortfolio(list)
            } else {
                Toast.makeText(requireContext(), "Hello, your list portfolio is empty!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onPortfolioItemClicked(position: Int) {
        Toast.makeText(requireContext(), "${listPortfolio[position].appName} clicked", Toast.LENGTH_SHORT).show()

    }

}