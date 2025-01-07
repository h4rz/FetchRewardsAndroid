package io.github.h4rz.fetchrewardsandroid.di

import io.github.h4rz.fetchrewardsandroid.data.remote.ApiService
import io.github.h4rz.fetchrewardsandroid.data.repository.ItemRepositoryImpl
import io.github.h4rz.fetchrewardsandroid.domain.repository.ItemRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    single<ItemRepository> {
        ItemRepositoryImpl(get())
    }
}