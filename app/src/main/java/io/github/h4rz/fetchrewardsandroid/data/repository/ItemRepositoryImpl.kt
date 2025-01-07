package io.github.h4rz.fetchrewardsandroid.data.repository

import io.github.h4rz.fetchrewardsandroid.data.remote.ApiService
import io.github.h4rz.fetchrewardsandroid.domain.model.Item
import io.github.h4rz.fetchrewardsandroid.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ItemRepositoryImpl(private val apiService: ApiService) : ItemRepository {
    override suspend fun getItems(): Flow<List<Item>> = flow {
        val response = apiService.getItems()
        val items = response.mapNotNull { Item.fromDTO(it) }
            .sortedWith(compareBy({it.listId}, {it.name}))
        emit(items)
    }
}