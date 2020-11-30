package com.crocodic.datastore.injection

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.crocodic.datastore.BuildConfig
import com.crocodic.datastore.data.Session
import com.crocodic.datastore.api.ApiService

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build().create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(application: Application, session: Session): OkHttpClient {


        val interceptors = HttpLoggingInterceptor()
        interceptors.level = HttpLoggingInterceptor.Level.HEADERS

        return OkHttpClient().newBuilder()
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()


                val cm =
                    application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                val isWifi =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cm.getNetworkCapabilities(cm.activeNetwork)
                            ?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    } else {
                        cm.activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI
                    } ?: false

                val requestBuilder = original.newBuilder()
                    //.header("Accept-Encoding", "identity")
                    .method(original.method(), original.body())


                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .addInterceptor(interceptors)
            .build()
    }
}
