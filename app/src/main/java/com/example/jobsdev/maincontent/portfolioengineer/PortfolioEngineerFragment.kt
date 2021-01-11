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

class PortfolioEngineerFragment : Fragment(), RecyclerViewListPortfolioAdapter.OnPortfolioClickListener, PortfolioContract.ViewPortfolio {

    private lateinit var binding : FragmentPortfolioEngineerBinding
    var listPortfolio = ArrayList<ItemPortfolioModel>()
    private lateinit var coroutineScope : CoroutineScope
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var service : JobSDevApiService
    private var presenter : PortfolioContract.PresenterPortfolio? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_portfolio_engineer, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        sharedPref = PreferencesHelper(requireContext())
        service = ApiClient.getApiClient(requireContext())!!.create(JobSDevApiService::class.java)
        presenter = PortfolioEngineerPresenter(coroutineScope, service, sharedPref)

        binding.btnAddPortfolio.setOnClickListener {
            startActivity(Intent(activity, AddPortfolioActivity::class.java))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var portfolioAdapter = RecyclerViewListPortfolioAdapter(listPortfolio, this)
        binding.recyclerViewPortfolio.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewPortfolio.adapter = portfolioAdapter

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

    override fun addListPortfolio(list: List<ItemPortfolioModel>) {
        (binding.recyclerViewPortfolio.adapter as RecyclerViewListPortfolioAdapter).addListPortfolio(list)
        binding.recyclerViewPortfolio.showOrGone(true)
        binding.ivEmptyIllustration.showOrGone(false)
        binding.progressBar.showOrGone(false)
    }

    override fun failedAdd(msg: String) {
        if (msg == "expired") {
            Toast.makeText(requireContext(), "Please sign in!", Toast.LENGTH_LONG).show()
        }
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
        binding.progressBar.showOrGone(false)
    }

    override fun showProgressBar() {
        binding.progressBar.showOrGone(true)
    }

    override fun hideProgressBar() {
        binding.progressBar.showOrGone(false)
    }

    private fun View.showOrGone(show : Boolean) {
        if (show) {
            visibility = View.VISIBLE
        } else {
            visibility = View.GONE
        }
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