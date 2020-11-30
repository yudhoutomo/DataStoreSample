package com.crocodic.datastore.injection

import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.crocodic.datastore.ui.MainActivity
import com.crocodic.datastore.ui.MainModule

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun bindMainActivity(): MainActivity

}