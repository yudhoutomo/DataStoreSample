package com.crocodic.datastore.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class ApiResponse {

    @Expose
    @SerializedName("status")
    var status: ApiStatus? = ApiStatus.SUCCESS

    @Expose
    @SerializedName("body")
    var body: String? = null
}