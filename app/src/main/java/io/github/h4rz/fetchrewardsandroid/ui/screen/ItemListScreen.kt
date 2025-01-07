package io.github.h4rz.fetchrewardsandroid.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.h4rz.fetchrewardsandroid.ui.components.ErrorComponent
import io.github.h4rz.fetchrewardsandroid.ui.components.ItemCard
import io.github.h4rz.fetchrewardsandroid.ui.components.ListIdDropdown
import io.github.h4rz.fetchrewardsandroid.ui.components.Loader
import io.github.h4rz.fetchrewardsandroid.ui.viewmodel.MainViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    LaunchedEffect(uiState.selectedListId) {
        lazyListState.scrollToItem(0)
    }
    val errorMessage = uiState.error

    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        modifier = modifier,
        onRefresh = viewModel::retry
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
        ) {
            when {
                uiState.isLoading -> {
                    Loader()
                }

                errorMessage != null -> {
                    ErrorComponent(
                        message = errorMessage,
                        onRetry = viewModel::retry
                    )
                }

                else -> {
                    if (uiState.availableListIds.isNotEmpty()) {
                        ListIdDropdown(
                            availableListIds = uiState.availableListIds,
                            selectedListId = uiState.selectedListId,
                            onListIdSelected = viewModel::onSelectListId
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(
                            state = lazyListState,
                            contentPadding = PaddingValues(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = uiState.filteredItems,
                                key = { it.id }
                            ) { item ->
                                ItemCard(item = item)
                            }
                        }
                    } else {
                        ErrorComponent(
                            message = "No items available",
                            onRetry = viewModel::retry
                        )
                    }
                }
            }
        }
    }
}