package io.github.h4rz.fetchrewardsandroid.domain.model

import io.github.h4rz.fetchrewardsandroid.data.model.ItemDTO

data class Item(
    val id: Int,
    val listId: Int,
    val name: String
) {
    companion object {
        fun fromDTO(item: ItemDTO): Item? {
            return if (item.name.isNullOrBlank()) {
                null
            } else {
                Item(id = item.id, listId = item.listId, name = item.name)
            }
        }
    }
}