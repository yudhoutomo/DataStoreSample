package com.crocodic.datastore.injection

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.crocodic.datastore.data.room.AppDatabase
import com.crocodic.datastore.data.room.user.UserRepository

@Module
abstract class RoomModule {

    @Module
    companion object {

        @JvmStatic
        @Singleton
        @Provides
        fun provideDatabase(context: Context) = AppDatabase.getDatabase(context)

        @JvmStatic
        @Singleton
        @Provides
        fun provideUserRepository(database: AppDatabase) = UserRepository(database.userDao())

    }
}
