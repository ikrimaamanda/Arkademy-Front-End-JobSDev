package com.example.jobsdev.maincontent.projectcompany.listhirebyprojectid

data class HireByProjectIdModel(val hireId : String?, val cn_id : String?,
                                val enId : String?, val enJobTitle : String?,
                                val enJobType : String?, val enProfilePict : String?,
                                val projectId : String?, val projectName : String?,
                                val projectDesc : String?, val projectDeadline : String?,
                                val projectImage : String?,
                                val hirePrice : String?, val hireMessage : String?, val hireStatus : String?,
                                val dateConfirm : String?, val hireCreatedAt : String?,
                                val projectCreateAt : String?, val projectUpdateAt : String?,
                                val acName : String?, val acEmail : String?, val acPhoneNumber : String?
)