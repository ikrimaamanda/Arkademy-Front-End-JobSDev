package com.example.jobsdev.maincontent.portfolioengineer

interface PortfolioContract {

    interface ViewPortfolio{
        fun addListPortfolio(list : List<ItemPortfolioModel>)
        fun failedAdd(msg : String)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface PresenterPortfolio {
        fun bindView(view : ViewPortfolio)
        fun unbind()
        fun callListPortfolioApi()
    }
}