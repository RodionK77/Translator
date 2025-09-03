package com.example.translator.data.DI

import com.example.translator.data.MeaningsRepositoryImpl
import com.example.translator.data.TranslationsRepositoryImpl
import com.example.translator.domain.MeaningsRepository
import com.example.translator.domain.TranslationsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindTranslationsRepository(
        impl: TranslationsRepositoryImpl
    ): TranslationsRepository

    @Binds
    abstract fun bindMeaningsRepository(
        impl: MeaningsRepositoryImpl
    ): MeaningsRepository
}