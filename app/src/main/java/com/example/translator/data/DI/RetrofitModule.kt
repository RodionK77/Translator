package com.example.translator.data.DI

import com.example.translator.data.MeaningsAPI
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    @Qualifiers.Meanings
    fun provideMeaningsRetrofit() : Retrofit = Retrofit.Builder()
        .baseUrl("https://dictionary.skyeng.ru")
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build()

    @Provides
    @Singleton
    fun provideRetrofitMeaningsService(@Qualifiers.Meanings retrofit : Retrofit) : MeaningsAPI = retrofit.create(MeaningsAPI::class.java)


}