package com.example.jobsdev.maincontent.projectcompany.listhirebyprojectid

import com.google.gson.annotations.SerializedName

data class HireByProjectResponse(val success : Boolean, val message : String, val data : List<HireByProject>) {
    data class HireByProject( @SerializedName("hr_id") val hrId : String,
                              @SerializedName("cn_id") val cnId : String,
                              @SerializedName("en_id") val enId : String,
                              @SerializedName("en_job_title") val enJobTitle : String,
                              @SerializedName("en_job_type") val enJobType : String,
                              @SerializedName("en_profile_pict") val enProfilePict : String,
                              @SerializedName("pj_id") val projectId : String,
                              @SerializedName("pj_project_name") val projectName : String,
                              @SerializedName("pj_description") val projectDesc : String,
                              @SerializedName("pj_deadline") val projectDeadline : String,
                              @SerializedName("pj_image") val projectImage : String,
                              @SerializedName("hr_price") val hirePrice : String,
                              @SerializedName("hr_message") val hireMessage : String,
                              @SerializedName("hr_status") val hireStatus : String,
                              @SerializedName("hr_date_confirm") val hireDateConfirm : String,
                              @SerializedName("hr_created_at") val hireCreatedAt : String,
                              @SerializedName("pj_created_at") val projectCreateAt : String,
                              @SerializedName("pj_updated_at") val projectUpdateAt : String,
                              @SerializedName("ac_name") val name : String,
                              @SerializedName("ac_email") val email : String,
                              @SerializedName("ac_phone_number") val pHoneNumber : String
    )
}