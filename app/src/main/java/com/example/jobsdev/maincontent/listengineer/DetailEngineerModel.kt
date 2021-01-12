package com.example.jobsdev.maincontent.listengineer

import com.example.jobsdev.maincontent.hireengineer.ItemSkillHireEngineerDataClass

data class DetailEngineerModel(val engineerId : String?,
                                val accountId : String?,
                                val engineerName : String?,
                                val engineerEmail : String?,
                                val engineerPhoneNumber : String?,
                                val engineerJobTitle : String? = null,
                                val engineerJobType : String? = null,
                                val engineerLocation : String? = null,
                                val engineerDescription : String? = null,
                                val engineerProfilePict : String? = null,
                                val skillEngineer : List<ItemSkillHireEngineerDataClass?>)