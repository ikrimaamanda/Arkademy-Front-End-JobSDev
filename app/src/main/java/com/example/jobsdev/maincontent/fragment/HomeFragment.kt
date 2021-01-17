package com.example.jobsdev.maincontent.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.jobsdev.R
import com.example.jobsdev.databinding.FragmentHomeBinding
import com.example.jobsdev.maincontent.hireengineer.DetailEngineerActivity
import com.example.jobsdev.maincontent.home.HomeContract
import com.example.jobsdev.maincontent.home.HomePresenter
import com.example.jobsdev.maincontent.listengineer.*
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.sharedpreference.ConstantDetailEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class HomeFragment : Fragment(),
    OnListEngineerClickListener, HomeContract.ViewHome {

    private lateinit var binding : FragmentHomeBinding
    private var listEngineer = ArrayList<DetailEngineerModel>()
    private lateinit var coroutineScope : CoroutineScope
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var service : EngineerApiService
    private var presenter : HomePresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        sharedPref = PreferencesHelper(requireContext())
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(requireContext())!!.create(EngineerApiService::class.java)

        presenter = HomePresenter(coroutineScope, service)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewListEngineer.adapter = ListEngineerHomeAdapter(listEngineer,this)
        binding.recyclerViewListEngineer.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun onEngineerItemClicked(position: Int) {
        sharedPref.putValue(ConstantDetailEngineer.engineerId, listEngineer[position].engineerId!!)

        val intent = Intent(requireContext(), DetailEngineerActivity::class.java)
        intent.putExtra("image", listEngineer[position].engineerProfilePict)
        intent.putExtra("enId", listEngineer[position].engineerId)
        intent.putExtra("name", listEngineer[position].engineerName)
        intent.putExtra("jobTitle", listEngineer[position].engineerJobTitle)
        intent.putExtra("jobType", listEngineer[position].engineerJobType)
        intent.putExtra("image", listEngineer[position].engineerProfilePict)
        intent.putExtra("location", listEngineer[position].engineerLocation)
        intent.putExtra("engId", listEngineer[position].engineerId)
        intent.putExtra("email", listEngineer[position].engineerEmail)
        intent.putExtra("description", listEngineer[position].engineerDescription)
        intent.putExtra("phoneNumber", listEngineer[position].engineerPhoneNumber)

        startActivity(intent)
    }

    override fun onResultSuccess(list: List<DetailEngineerModel>) {
        (binding.recyclerViewListEngineer.adapter as ListEngineerHomeAdapter).addListEngineerHome(list)
        binding.recyclerViewListEngineer.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.ivEmpty.visibility = View.GONE
    }

    override fun onResultFail(message: String) {
        binding.recyclerViewListEngineer.visibility = View.GONE
        if (message == "expired") {
            Toast.makeText(requireContext(), "Please sign in!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
        binding.ivEmpty.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    override fun showLoading() {
        binding.recyclerViewListEngineer.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.ivEmpty.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)
        presenter?.callListEngineerService()
    }

    override fun onStop() {
        super.onStop()
        presenter?.unbind()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
        presenter = null
    }

}