package com.buddy.growfarm.di

import com.buddy.growfarm.network.api.GrowFarmAPI
import com.buddy.growfarm.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class Modules {
    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesGrowFarmApi(retrofit: Retrofit) : GrowFarmAPI {
        return retrofit.create(GrowFarmAPI::class.java)
    }
}