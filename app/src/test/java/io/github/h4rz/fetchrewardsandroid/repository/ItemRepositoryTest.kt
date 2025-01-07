package io.github.h4rz.fetchrewardsandroid.repository

import io.github.h4rz.fetchrewardsandroid.data.model.ItemDTO
import io.github.h4rz.fetchrewardsandroid.data.remote.ApiService
import io.github.h4rz.fetchrewardsandroid.data.repository.ItemRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ItemRepositoryTest {

    private lateinit var apiService: ApiService
    private lateinit var repository: ItemRepositoryImpl

    private val testData = listOf(
        ItemDTO(1, 1, null),
        ItemDTO(2, 2, ""),
        ItemDTO(3, 2, "Zebra"),
        ItemDTO(4, 1, "Banana"),
        ItemDTO(5, 2, "Apple"),
        ItemDTO(6, 1, "Apple"),
    )

    @Before
    fun setup() {
        apiService = mockk(relaxed = true)
        repository = ItemRepositoryImpl(apiService)
    }

    @Test
    fun testFilterNullOrBlankItemNames() = runTest {
        coEvery { apiService.getItems() } returns testData

        val result = repository.getItems().first()

        assertEquals(4, result.size)
        assertEquals("Apple", result[0].name)
        assertEquals("Banana", result[1].name)
        assertEquals("Apple", result[2].name)
    }

    @Test
    fun testSortItemsByListIdAndThenName() = runTest {
        coEvery { apiService.getItems() } returns testData

        val result = repository.getItems().first()

        assertEquals(4, result.size)
        assertEquals("Apple", result[0].name)
        assertEquals("Banana", result[1].name)
        assertEquals("Apple", result[2].name)
        assertEquals("Zebra", result[3].name)
    }
}