package com.example.jobsdev.maincontent.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R
import com.example.jobsdev.databinding.FragmentSearchBinding
import com.example.jobsdev.maincontent.hireengineer.DetailEngineerActivity
import com.example.jobsdev.maincontent.listengineer.DetailEngineerModel
import com.example.jobsdev.maincontent.listengineer.ListEngineerAdapter
import com.example.jobsdev.maincontent.listengineer.OnListEngineerClickListener
import com.example.jobsdev.maincontent.search.SearchContract
import com.example.jobsdev.maincontent.search.SearchPresenter
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantDetailEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment(),
    OnListEngineerClickListener, SearchContract.View {

    private lateinit var binding : FragmentSearchBinding
    private var listEngineer = ArrayList<DetailEngineerModel>()
    private lateinit var coroutineScope : CoroutineScope
    private lateinit var service : JobSDevApiService
    private var presenter : SearchPresenter? = null
    private lateinit var sharedPref : PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(requireContext())!!.create(JobSDevApiService::class.java)
        sharedPref = PreferencesHelper(requireContext())

        presenter = SearchPresenter(coroutineScope, service)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewSearchEngineer.adapter = ListEngineerAdapter(listEngineer,this)
        binding.recyclerViewSearchEngineer.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        setSearchView()
        setFilter()
    }

    override fun onEngineerItemClicked(position: Int) {
        sharedPref.putValue(ConstantDetailEngineer.engineerId, listEngineer[position].engineerId!!)

        val intent = Intent(requireContext(), DetailEngineerActivity::class.java)
        intent.putExtra("name", listEngineer[position].engineerName)
        intent.putExtra("jobTitle", listEngineer[position].engineerJobTitle)
        intent.putExtra("jobType", listEngineer[position].engineerJobType)
        intent.putExtra("image", listEngineer[position].engineerProfilePict)
        intent.putExtra("email", listEngineer[position].engineerEmail)
        intent.putExtra("location", listEngineer[position].engineerLocation)
        intent.putExtra("description", listEngineer[position].engineerDescription)
        intent.putExtra("phoneNumber", listEngineer[position].engineerPhoneNumber)
        startActivity(intent)
    }

    override fun onResultSuccess(list: List<DetailEngineerModel>) {
        (binding.recyclerViewSearchEngineer.adapter as ListEngineerAdapter).addListEngineer(list)
        binding.recyclerViewSearchEngineer.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.ivEmpty.visibility = View.GONE
    }

    override fun onResultFail(message: String) {
        binding.recyclerViewSearchEngineer.visibility = View.GONE
        if (message == "expired") {
            Toast.makeText(requireContext(), "Please sign in!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
        binding.ivEmpty.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    override fun showLoading() {
        binding.recyclerViewSearchEngineer.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.ivEmpty.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)
        presenter?.callServiceSearch(null)
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

    private fun setSearchView() {
        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == "") {
                    presenter?.callServiceSearch(null)
                } else {
                    if (newText?.length == 3) {
                        presenter?.callServiceSearch(newText)
                    }
                }
                return true
            }
        })
    }

    private fun setFilter() {
        binding.tvFilter.setOnClickListener {
            var popup = PopupMenu(requireContext(), binding.tvFilter)
            popup.inflate(R.menu.menu_filter)
            popup.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.item_0 -> {
                        presenter?.callServiceFilter(0)
                    }
                    R.id.item_2 -> {
                        presenter?.callServiceFilter(2)
                    }
                    R.id.item_3 -> {
                        presenter?.callServiceFilter(3)
                    }
                    R.id.item_4 -> {
                        presenter?.callServiceFilter(4)
                    }
            }
                true
            }
            popup.show()
        }
    }

}