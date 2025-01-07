package io.github.h4rz.fetchrewardsandroid.data.remote

import io.github.h4rz.fetchrewardsandroid.data.model.ItemDTO
import retrofit2.http.GET

interface ApiService {
    @GET("/hiring.json")
    suspend fun getItems(): List<ItemDTO>
}