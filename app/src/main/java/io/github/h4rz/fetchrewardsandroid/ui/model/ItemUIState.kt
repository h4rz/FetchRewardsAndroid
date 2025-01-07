package io.github.h4rz.fetchrewardsandroid.ui.model

import io.github.h4rz.fetchrewardsandroid.domain.model.Item

/**
 * UI state for the Item list screen.
 * - isLoading: Boolean indicating whether the data is being loaded.
 * - items: List of Item objects.
 * - error: String containing an error message, if any.
 * - availableListIds: List of unique listIds.
 * - selectedListId: Currently selected listId (dropdown).
 * - filteredItems: List of Item objects based on the selectedListId.
 * */
data class ItemUIState(
    val isLoading: Boolean = false,
    val items: List<Item> = emptyList(),
    val error: String? = null,
    val availableListIds: List<Int> = emptyList(),
    val selectedListId: Int? = null,
    val filteredItems: List<Item> = emptyList()
)