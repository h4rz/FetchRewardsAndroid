package io.github.h4rz.fetchrewardsandroid.viewmodel

import io.github.h4rz.fetchrewardsandroid.domain.model.Item
import io.github.h4rz.fetchrewardsandroid.domain.repository.ItemRepository
import io.github.h4rz.fetchrewardsandroid.ui.viewmodel.MainViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private lateinit var repository: ItemRepository
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val testData = listOf(
        Item(6, 1, "Apple"),
        Item(4, 1, "Banana"),
        Item(5, 2, "Apple"),
        Item(3, 2, "Zebra"),
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = MainViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchItemsSuccess() = runTest {
        coEvery { repository.getItems() } returns flowOf(testData)
        viewModel.fetchItems()
        testDispatcher.scheduler.advanceUntilIdle()

        val currentState = viewModel.uiState.value
        assertEquals(false, currentState.isLoading)
        assertEquals(null, currentState.error)
        assertEquals(testData, currentState.items)
        assertEquals(listOf(1, 2), currentState.availableListIds)
        assertEquals(null, currentState.selectedListId)
        assertEquals(testData, currentState.filteredItems)
    }

    @Test
    fun fetchItemsError() = runTest {
        val errorMessage = "Some error occurred"
        coEvery { repository.getItems() } returns flow { throw Exception(errorMessage)}
        viewModel.fetchItems()
        testDispatcher.scheduler.advanceUntilIdle()

        val currentState = viewModel.uiState.value
        assertEquals(false, currentState.isLoading)
        assertEquals(errorMessage, currentState.error)
        assertEquals(emptyList<Item>(), currentState.items)
        assertEquals(emptyList<Item>(), currentState.availableListIds)
        assertEquals(null, currentState.selectedListId)
        assertEquals(emptyList<Item>(), currentState.filteredItems)
    }

    @Test
    fun selectedListIdChanged() = runTest {
        coEvery { repository.getItems() } returns flowOf(testData)
        viewModel.fetchItems()
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onSelectListId(2)

        val currentState = viewModel.uiState.value
        assertEquals(false, currentState.isLoading)
        assertEquals(null, currentState.error)
        assertEquals(testData, currentState.items)
        assertEquals(listOf(1, 2), currentState.availableListIds)
        assertEquals(2, currentState.selectedListId)
        assertEquals(testData.filter { it.listId == 2 }, currentState.filteredItems)
    }

    @Test
    fun triggerRetry() = runTest {
        coEvery { repository.getItems() } returns flow { throw Exception("First Try Error") }
        viewModel.fetchItems()
        testDispatcher.scheduler.advanceUntilIdle()

        coEvery { repository.getItems() } returns flowOf(testData)
        viewModel.retry()
        testDispatcher.scheduler.advanceUntilIdle()

        val currentState = viewModel.uiState.value
        assertEquals(false, currentState.isLoading)
        assertEquals(null, currentState.error)
        assertEquals(testData, currentState.items)
    }

}