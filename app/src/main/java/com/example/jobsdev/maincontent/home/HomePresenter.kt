package com.example.jobsdev.maincontent.home

import com.example.jobsdev.maincontent.listengineer.DetailEngineerModel
import com.example.jobsdev.maincontent.listengineer.EngineerApiService
import com.example.jobsdev.maincontent.listengineer.ListEngineerResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class HomePresenter(private val coroutineScope: CoroutineScope,
                    private val service: EngineerApiService?) : HomeContract.PresenterHome {

    private var view : HomeContract.ViewHome? = null

    override fun bindToView(view: HomeContract.ViewHome) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun callListEngineerService() {
        coroutineScope.launch {

            view?.showLoading()

            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getAllEngineer()
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideLoading()

                        when {
                            e.code() == 404 -> {
                                view?.onResultFail("Not found data engineer!")
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

            if(response is ListEngineerResponse) {

                if (response.success) {
                    val list = response.data?.map {
                        DetailEngineerModel(it.engineerId, it.accountId, it.accountName, it.accountEmail, it.accountPhoneNumber, it.engineerJobTitle, it.engineerJobType, it.engineerLocation, it.engineerDescription, it.engineerProfilePict, it.skillEngineer)
                    }
                    val mutable = list.toMutableList()
                    mutable.removeAll{it.engineerJobTitle == null || it.engineerJobType == null || it.engineerProfilePict == null}
                    view?.onResultSuccess(mutable)
                } else {
                    view?.onResultFail(response.message)
                }
            }
        }

    }

}