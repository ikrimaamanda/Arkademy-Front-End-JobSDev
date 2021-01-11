package com.example.jobsdev.maincontent.fragment

import android.content.Intent
import android.os.Bundle
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
import com.example.jobsdev.maincontent.home.HomeContract
import com.example.jobsdev.maincontent.home.HomePresenter
import com.example.jobsdev.maincontent.listengineer.DetailEngineerModel
import com.example.jobsdev.maincontent.listengineer.EngineerApiService
import com.example.jobsdev.maincontent.listengineer.ListEngineerAdapter
import com.example.jobsdev.maincontent.listengineer.OnListEngineerClickListener
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.sharedpreference.ConstantDetailEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class HomeFragment : Fragment(),
    OnListEngineerClickListener, HomeContract.ViewHome {

    private lateinit var binding : FragmentHomeBinding
    var listEngineer = ArrayList<DetailEngineerModel>()
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

        binding.recyclerViewListEngineer.adapter = ListEngineerAdapter(listEngineer,this)
        binding.recyclerViewListEngineer.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    override fun onEngineerItemClicked(position: Int) {
        Toast.makeText(requireContext(), "${listEngineer[position].engineerName} clicked", Toast.LENGTH_SHORT).show()
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
        (binding.recyclerViewListEngineer.adapter as ListEngineerAdapter).addListEngineer(list)
        binding.recyclerViewListEngineer.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.ivEmpty.visibility = View.GONE
    }

    override fun onResultFail(message: String) {
        binding.recyclerViewListEngineer.visibility = View.GONE
        if (message == "expired") {
            Toast.makeText(requireContext(), "Please sign in!", Toast.LENGTH_LONG).show()
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