package com.example.jobsdev.maincontent.hireengineer

import android.content.Intent
import android.os.Bundle
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
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class PortfolioDetailEngineerFragment : Fragment(), RecyclerViewListPortfolioAdapter.OnPortfolioClickListener, PortfolioDetailEngineerContract.ViewPortfolioDetailEngineer {
    private lateinit var binding : FragmentPortfolioDetailEngineerBinding
    private var listPortfolio = ArrayList<ItemPortfolioModel>()
    private lateinit var coroutineScope : CoroutineScope
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var service : JobSDevApiService
    private var presenter : PortfolioDetailEngineerPresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_portfolio_detail_engineer, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        sharedPref = PreferencesHelper(requireContext())
        service = ApiClient.getApiClient(requireContext())!!.create(JobSDevApiService::class.java)
        presenter = PortfolioDetailEngineerPresenter(coroutineScope, service, sharedPref)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val portfolioAdapter = RecyclerViewListPortfolioAdapter(listPortfolio, this)
        binding.recyclerViewPortfolio.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewPortfolio.adapter = portfolioAdapter

    }

    override fun onPortfolioItemClicked(position: Int) {
        val intent = Intent(requireContext(), DetailPortfolioEngineerActivity::class.java)
        intent.putExtra("portfolioImage", listPortfolio[position].portfolioImage)
        intent.putExtra("appName", listPortfolio[position].appName)
        intent.putExtra("linkPub", listPortfolio[position].linkPublication)
        intent.putExtra("linkRepo", listPortfolio[position].linkRepo)
        intent.putExtra("workplace", listPortfolio[position].workPlace)
        intent.putExtra("typeApp", listPortfolio[position].portfolioType)
        intent.putExtra("desc", listPortfolio[position].portfolioDesc)

        startActivity(intent)
    }

    override fun addListPortfolioDetailEngineer(list: List<ItemPortfolioModel>) {
        (binding.recyclerViewPortfolio.adapter as RecyclerViewListPortfolioAdapter).addListPortfolio(list)
        binding.recyclerViewPortfolio.visibility = View.VISIBLE
        binding.ivEmptyIllustration.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

    }

    override fun failedAdd(msg: String) {
        if (msg == "expired") {
            Toast.makeText(requireContext(), "Please sign in!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
        }
        binding.progressBar.visibility = View.GONE
    }

    override fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindView(this)
        presenter?.callListPortfolioApi()
    }

    override fun onStop() {
        super.onStop()
        presenter?.unbind()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        presenter = null
        super.onDestroy()
    }

}