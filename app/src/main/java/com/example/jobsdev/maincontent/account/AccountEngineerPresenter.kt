package com.example.jobsdev.maincontent.account

import com.example.jobsdev.maincontent.skillengineer.ItemSkillEngineerModel
import com.example.jobsdev.retfrofit.DetailEngineerByAcIdResponse
import com.example.jobsdev.retfrofit.GetSkillByEnIdResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AccountEngineerPresenter(private val coroutineScope: CoroutineScope,
                               private val service: JobSDevApiService?,
                               private val sharedPref : PreferencesHelper) : AccountEngineerContract.PresenterAcEngineer {

    private var view : AccountEngineerContract.ViewAcEngineer? = null

    override fun bindView(view: AccountEngineerContract.ViewAcEngineer) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun callEngineerIdApi() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getEngineerByAcId(sharedPref.getValueString(Constant.prefAccountId))
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideProgressBar()

                        when {
                            e.code() == 404 -> {
                                view?.failedSetData("Data not found!")
                            }
                            e.code() == 400 -> {
                                view?.failedSetData("expired")
                            }
                            else -> {
                                view?.failedSetData("Server under maintenance!")
                            }
                        }
                    }
                }
            }

            if (result is DetailEngineerByAcIdResponse) {
                if (result.success) {
                    view?.setDataEngineer(result.data)
                    if (result.data.enProfilePict != null) {
                        sharedPref.putValue(Constant.prefProfilePict, result.data.enProfilePict!!)
                    }
                } else {
                    view?.failedSetData(result.message)
                }
            } else {
                view?.failedSetData("Hello, your data is empty!")
            }
        }
    }

    override fun callSkillApi() {
        coroutineScope.launch {
            view?.showProgressBar()

            val response = withContext(Dispatchers.IO) {

                try {
                    service?.getSkillByEnId(sharedPref.getValueString(ConstantAccountEngineer.engineerId)!!.toInt())
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideProgressBar()

                        when {
                            e.code() == 404 -> {
                                view?.failedSetData("Data not found!")
                            }
                            e.code() == 400 -> {
                                view?.failedSetData("expired")
                            }
                            else -> {
                                view?.failedSetData("Server under maintenance!")
                            }
                        }
                    }
                }
            }

            if(response is GetSkillByEnIdResponse) {
                if (response.success) {
                    val list = response.data.map {
                        ItemSkillEngineerModel(it?.skId, it?.enId, it?.skillName)
                    }
                    view?.addSkill(list)
                } else {
                    view?.failedAddSkill(response.message)
                }
            } else {
                view?.failedAddSkill("Hello, your list skill is empty!")
            }
        }
    }


}