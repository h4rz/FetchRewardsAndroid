package io.github.h4rz.fetchrewardsandroid.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.h4rz.fetchrewardsandroid.domain.repository.ItemRepository
import io.github.h4rz.fetchrewardsandroid.ui.model.ItemUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ItemUIState())
    val uiState: StateFlow<ItemUIState> = _uiState.asStateFlow()

    init {
        fetchItems()
    }

    /**
     * Fetches item from the repository & updates the UI state accordingly.
     * Initially, it shows all items.
     * If there's an error, it updates the UI state with the error message.
     * */
    fun fetchItems() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            repository.getItems()
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Something went wrong. Please try again."
                        )
                    }
                }
                .collect { items ->
                    val availableListIds = items.map { it.listId }.distinct().sorted()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            items = items,
                            availableListIds = availableListIds,
                            selectedListId = null, // Initially show all items
                            filteredItems = items
                        )
                    }
                }
        }
    }

    /**
     * Filters the items based on the selected listId.
     * If listId is null, it shows all items else it shows filtered items.
     * */
    fun onSelectListId(listId: Int?) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedListId = listId,
                filteredItems = if (listId == null) {
                    currentState.items
                } else {
                    currentState.items.filter { it.listId == listId }
                }
            )
        }
    }

    /**
     * Retries fetching items from the repository.
     * */
    fun retry() {
        fetchItems()
    }
}