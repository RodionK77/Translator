package com.example.translator.data.DI

import com.example.translator.data.EntityRepositoryImpl
import com.example.translator.data.MeaningsRepositoryImpl
import com.example.translator.domain.EntityRepository
import com.example.translator.domain.MeaningsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMeaningsRepository(
        impl: MeaningsRepositoryImpl
    ): MeaningsRepository

    @Binds
    abstract fun bindEntityRepository(
        impl: EntityRepositoryImpl
    ): EntityRepository
}