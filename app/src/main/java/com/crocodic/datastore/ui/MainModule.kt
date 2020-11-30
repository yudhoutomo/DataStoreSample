package com.crocodic.datastore.ui


import androidx.lifecycle.ViewModel
import com.crocodic.datastore.injection.anotation.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindViewModel(mainViewModel: MainViewModel): ViewModel
}