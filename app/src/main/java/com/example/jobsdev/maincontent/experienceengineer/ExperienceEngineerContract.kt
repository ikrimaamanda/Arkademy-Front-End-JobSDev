package com.example.jobsdev.maincontent.experienceengineer

interface ExperienceEngineerContract {
    interface ViewExperienceEngineer {
        fun addListExperience(list : List<ItemExperienceModel>)
        fun failedAdd(msg : String)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface PresenterExperienceEngineer {
        fun bindView(view : ViewExperienceEngineer)
        fun unbind()
        fun callListExperienceApi()
    }
}