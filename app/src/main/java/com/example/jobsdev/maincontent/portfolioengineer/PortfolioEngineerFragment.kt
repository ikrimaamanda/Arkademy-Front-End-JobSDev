package com.example.jobsdev.maincontent.portfolioengineer

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
import com.example.jobsdev.databinding.FragmentPortfolioEngineerBinding

class PortfolioEngineerFragment : Fragment(), RecyclerViewListPortfolioAdapter.OnPortfolioClickListener {
    private lateinit var binding : FragmentPortfolioEngineerBinding
    var listPortfolio = ArrayList<ItemPortfolioDataClass>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_portfolio_engineer, container, false)

        binding.btnAddPortfolio.setOnClickListener {
            startActivity(Intent(activity, AddPortfolioActivity::class.java))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listPortfolio = generateDummyList(15)

        var portfolioAdapter = RecyclerViewListPortfolioAdapter(listPortfolio, this)
        binding.recyclerViewPortfolio.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewPortfolio.adapter = portfolioAdapter

//        binding.recyclerView.apply {
//            adapter =
//                RecyclerViewListPortfolioAdapter(
//                    exampleListPortfolio
//                )
//            layoutManager = LinearLayoutManager(activity)
//        }
    }

    private fun generateDummyList(size : Int) : ArrayList<ItemPortfolioDataClass> {
        val list = ArrayList<ItemPortfolioDataClass>()

        for (i in 0 until size) {
            val drawable = when(i%2) {
                0 -> R.drawable.porto1
                else -> R.drawable.porto2
            }

            val item =
                ItemPortfolioDataClass(
                    drawable
                )
            list += item
        }
        return list
    }

    override fun onPortfolioItemClicked(position: Int) {
        Toast.makeText(requireContext(), "Portfolio $position clicked", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), UpdatePortfolioTwoActivity::class.java)
        startActivity(intent)
    }
}