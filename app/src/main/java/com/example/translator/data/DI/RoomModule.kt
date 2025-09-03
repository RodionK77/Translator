package com.example.translator.data.DI

import android.app.Application
import androidx.room.Room
import com.example.translator.data.Room.TranslationsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideUsersDb(app: Application): TranslationsDatabase{
        return Room.databaseBuilder(
            app,
            TranslationsDatabase::class.java,
            "translations.db"
        ).build()
    }
}