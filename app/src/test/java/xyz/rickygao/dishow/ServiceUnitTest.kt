package xyz.rickygao.dishow

import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import xyz.rickygao.dishow.network.CatalogCommentBody
import xyz.rickygao.dishow.network.Service

@RunWith(JUnit4::class)
class ServiceUnitTest {

    @Test
    fun testGetUniversityById() = runBlocking {
        val response = Service.getUniversityById(1).await()
        assertNotNull(response)
        assertEquals(1, response.id)
    }

    @Test
    fun testGetUniversities() = runBlocking {
        val response = Service.getUniversities().await()
        assertNotNull(response)
        assertNotEquals(0, response.size)
    }

    @Test
    fun testGetCanteensById() = runBlocking {
        val response = Service.getCanteenById(101).await()
        assertNotNull(response)
        assertEquals(101, response.id)
    }

    @Test
    fun testGetCatalogById() = runBlocking {
        val response = Service.getCatalogById(10301).await()
        assertNotNull(response)
        assertEquals(10301, response.id)
    }

    @Test
    fun testGetDishById() = runBlocking {
        val response = Service.getDishById(1030101).await()
        assertNotNull(response)
        assertEquals(1030101, response.id)
    }

    @Test
    fun testGetCatalogCommentById() = runBlocking {
        val response = Service.getCatalogCommentById(1).await()
        assertNotNull(response)
        assertEquals(1, response.id)
    }

    @Test
    fun testGetCatalogCommentsByCatalog() = runBlocking {
        val response = Service.getCatalogCommentsByCatalog(10301).await()
        assertNotNull(response)
    }

    @Test
    fun testPostCatalogComment() = runBlocking {
        val response = Service.postCatalogComment(10301, CatalogCommentBody(4)).await()
        assertNotNull(response)
        assert(response.id >= 0)
    }
}
