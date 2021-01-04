package com.example.jobsdev.maincontent.projectcompany

import com.google.gson.annotations.SerializedName

data class ProjectResponse(val success : Boolean, val message : String, val data : List<Project>) {
    data class Project(@SerializedName("pj_id") val projectId : String,
                        @SerializedName("cn_id") val companyId : String,
                        @SerializedName("pj_project_name") val projectName : String,
                        @SerializedName("pj_description") val projectDesc : String,
                        @SerializedName("pj_deadline") val projectDeadline : String,
                        @SerializedName("pj_image") val projectImage : String,
                        @SerializedName("pj_created_at") val projectCreateAt : String,
                        @SerializedName("pj_updated_at") val projectUpdateAt : String
    )
}