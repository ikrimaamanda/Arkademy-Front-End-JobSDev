package com.example.jobsdev.maincontent.listengineer

import com.example.jobsdev.maincontent.hireengineer.ItemSkillHireEngineerDataClass

data class DetailEngineerModel(val engineerId : String?,
                                val accountId : String?,
                                val engineerName : String?,
                                val engineerEmail : String?,
                                val engineerPhoneNumber : String?,
                                val engineerJobTitle : String?,
                                val engineerJobType : String?,
                                val engineerLocation : String?,
                                val engineerDescription : String?,
                                val engineerProfilePict : String?,
                                val skillEngineer : List<ItemSkillHireEngineerDataClass?>)