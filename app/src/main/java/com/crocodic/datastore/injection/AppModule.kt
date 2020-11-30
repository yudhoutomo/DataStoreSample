package com.crocodic.datastore.injection

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.crocodic.datastore.data.Session
import com.crocodic.datastore.base.ViewModelFactory
import com.crocodic.datastore.data.datastore.preference.DataStoreManager

@Module
abstract class AppModule {

    @Binds
    abstract fun provideContext(application: Application): Context

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Module
    companion object {
        @JvmStatic
        @Provides
        @Singleton
        fun provideSession(context: Context) = Session(context)

    }

//    @Module
//    object T {
//        @JvmStatic
//        @Provides
//        @Singleton
//        fun provideStorePreference(context: Context) = DataStoreManager(context)
//
//    }
}
