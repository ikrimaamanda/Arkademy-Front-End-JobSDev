package com.example.jobsdev.maincontent.hireengineer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsdev.R
import com.example.jobsdev.databinding.FragmentPortfolioDetailEngineerBinding
import com.example.jobsdev.maincontent.portfolioengineer.ItemPortfolioDataClass

class PortfolioDetailEngineerFragment : Fragment() {
    private lateinit var binding : FragmentPortfolioDetailEngineerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_portfolio_detail_engineer, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exampleList = generateDummyList(15)

                binding.recyclerView.apply {
            adapter =
                RecyclerViewListPortfolioDetailEngineerAdapter(
                    exampleList
                )
            layoutManager = LinearLayoutManager(activity)
        }

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

}