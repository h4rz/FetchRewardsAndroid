package io.github.h4rz.fetchrewardsandroid.domain.repository

import io.github.h4rz.fetchrewardsandroid.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    suspend fun getItems(): Flow<List<Item>>
}