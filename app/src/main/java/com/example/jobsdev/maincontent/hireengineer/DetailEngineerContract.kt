package com.example.jobsdev.maincontent.hireengineer

import com.example.jobsdev.maincontent.skillengineer.ItemSkillEngineerModel
import com.example.jobsdev.retfrofit.DetailEngineerByAcIdResponse

interface DetailEngineerContract {

    interface ViewDetailEngineer{
        fun addListSkill(list : List<ItemSkillEngineerModel>)
        fun failedAddSkill(msg : String)
        fun showProgressBarSkill()
        fun hideProgressBarSkill()
    }

    interface PresenterDetailEngineer {
        fun bindView(view : ViewDetailEngineer)
        fun unbind()
        fun callListSkillApi()
    }
}