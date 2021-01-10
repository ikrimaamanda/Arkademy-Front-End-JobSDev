package com.example.jobsdev.maincontent.search

import com.example.jobsdev.maincontent.listengineer.DetailEngineerModel
import com.example.jobsdev.maincontent.listengineer.ListEngineerResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class SearchPresenter(private val coroutineScope: CoroutineScope,
                      private val service: JobSDevApiService?) : SearchContract.Presenter {

    private var view : SearchContract.View? = null

    override fun bindToView(view: SearchContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun callServiceSearch(search: String?) {
        coroutineScope.launch {
            view?.showLoading()
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.searchEngineer(search = search)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideLoading()

                        when {
                            e.code() == 404 -> {
                                view?.onResultFail("No data engineer!")
                            }
                            e.code() == 400 -> {
                                view?.onResultFail("expired")
                            }
                            else -> {
                                view?.onResultFail("Server under maintenance!")
                            }
                        }
                    }
                }

            }

            if (response is ListEngineerResponse) {

                if (response.success) {
                    val list = response.data?.map {
                        DetailEngineerModel(it.engineerId, it.accountId, it.accountName, it.accountEmail, it.accountPhoneNumber, it.engineerJobTitle, it.engineerJobType, it.engineerLocation, it.engineerDescription, it.engineerProfilePict, it.skillEngineer)
                    }
                    view?.onResultSuccess(list)
                } else {
                    view?.onResultFail(response.message)
                }
            }
        }
    }

    override fun callServiceFilter(filter: Int?) {
        coroutineScope.launch {
            view?.showLoading()
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.filterEngineer(filter = filter)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideLoading()

                        when {
                            e.code() == 404 -> {
                                view?.onResultFail("No data engineer!")
                            }
                            e.code() == 400 -> {
                                view?.onResultFail("expired")
                            }
                            else -> {
                                view?.onResultFail("Server under maintenance!")
                            }
                        }
                    }
                }

            }

            if (response is ListEngineerResponse) {

                if (response.success) {
                    val list = response.data?.map {
                        DetailEngineerModel(it.engineerId, it.accountId, it.accountName, it.accountEmail, it.accountPhoneNumber, it.engineerJobTitle, it.engineerJobType, it.engineerLocation, it.engineerDescription, it.engineerProfilePict, it.skillEngineer)
                    }
                    view?.onResultSuccess(list)
                } else {
                    view?.onResultFail(response.message)
                }
            }
        }
    }
}