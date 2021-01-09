package com.example.jobsdev.maincontent.portfolioengineer

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
import com.example.jobsdev.R
import com.example.jobsdev.databinding.FragmentPortfolioEngineerBinding
import com.example.jobsdev.maincontent.experienceengineer.ItemExperienceModel
import com.example.jobsdev.maincontent.experienceengineer.RecyclerViewListExperienceAdapter
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.GetExperienceByEnIdResponse
import com.example.jobsdev.retfrofit.GetPortfolioByEnIdResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.ConstantPortfolio
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class PortfolioEngineerFragment : Fragment(), RecyclerViewListPortfolioAdapter.OnPortfolioClickListener {

    private lateinit var binding : FragmentPortfolioEngineerBinding
    var listPortfolio = ArrayList<ItemPortfolioModel>()
    private lateinit var coroutineScope : CoroutineScope
    private lateinit var sharedPref : PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_portfolio_engineer, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        sharedPref = PreferencesHelper(requireContext())

        binding.btnAddPortfolio.setOnClickListener {
            startActivity(Intent(activity, AddPortfolioActivity::class.java))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val enId = sharedPref.getValueString(ConstantAccountEngineer.engineerId)
        getListPortfolio(enId!!.toInt())

        var portfolioAdapter = RecyclerViewListPortfolioAdapter(listPortfolio, this)
        binding.recyclerViewPortfolio.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewPortfolio.adapter = portfolioAdapter

    }

    private fun getListPortfolio(enId : Int) {
        val service = ApiClient.getApiClient(requireContext())?.create(JobSDevApiService::class.java)

        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getListPortfolioByEnId(enId)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if(response is GetPortfolioByEnIdResponse) {
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
        Toast.makeText(requireContext(), "${listPortfolio[position].portfolioId} clicked", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), UpdatePortfolioTwoActivity::class.java)
        sharedPref.putValue(ConstantPortfolio.portfolioImage, listPortfolio[position].portfolioImage!!)
        intent.putExtra("updatePortfolioId", listPortfolio[position].portfolioId)
        intent.putExtra("appName", listPortfolio[position].appName)
        intent.putExtra("portfolioDesc", listPortfolio[position].portfolioDesc)
        intent.putExtra("linkPub", listPortfolio[position].linkPublication)
        intent.putExtra("linkRepo", listPortfolio[position].linkRepo)
        intent.putExtra("workPlace", listPortfolio[position].workPlace)
        startActivity(intent)
    }
}