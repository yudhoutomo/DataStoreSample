package com.crocodic.datastore.ui

import androidx.lifecycle.MutableLiveData
import com.crocodic.datastore.api.ApiResponse
import com.crocodic.datastore.api.ApiService
import com.crocodic.datastore.api.ApiStatus
import com.crocodic.datastore.data.Session
import com.crocodic.datastore.data.room.AppDatabase
import com.crocodic.datastore.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val apiService: ApiService,
    private val database: AppDatabase,
    private val session: Session
) : BaseViewModel() {


    val apiResponse = MutableLiveData<ApiResponse>()


    fun login() {

        compositeDisposable.add(
            apiService.login("username", "password", "regid")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val response = it.string()

                    val responseJson = JSONObject(response)

                    val apiStatus = responseJson.getInt("api_status")
                    val apiMessage = responseJson.getString("api_message")

                    if (apiStatus == 1) {

                        apiResponse.postValue(
                            ApiResponse().apply {
                                status = ApiStatus.SUCCESS
                                body = apiMessage
                            }
                        )

                    } else {
                        apiResponse.postValue(
                            ApiResponse().apply {
                                status = ApiStatus.WRONG
                                body = apiMessage
                            }
                        )
                        //Log.e(Cons.TAG, "error status: $apiMessage")
                    }
                }, {
                    apiResponse.postValue(
                        ApiResponse().apply {
                            status = ApiStatus.ERROR
                            body = it.localizedMessage
                        }
                    )

                })
        )
    }

}