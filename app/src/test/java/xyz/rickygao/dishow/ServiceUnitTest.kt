package xyz.rickygao.dishow

import org.junit.Assert.*
import org.junit.Test

class ServiceUnitTest {

    @Test
    fun testGetUniversityById() {
        val response = Service.getUniversityById(1).execute()
        assertNotNull(response)
        assertNotNull(response.body())
        assertEquals(1, response.body()!!.id)
    }

    @Test
    fun testGetUniversities() {
        val response = Service.getUniversities().execute()
        assertNotNull(response)
        assertNotNull(response.body())
        assertNotEquals(0, response.body()!!.size)
    }

    @Test
    fun testGetUniversityByName() {
        val response = Service.getUniversityByName("天津大学").execute()
        assertNotNull(response)
        assertNotNull(response.body())
        assertEquals(2, response.body()!!.size)
        assertEquals(1, response.body()!![0].id)
        assertEquals(2, response.body()!![1].id)
    }

    @Test
    fun testGetCanteensById() {
        val response = Service.getCanteenById(101).execute()
        assertNotNull(response)
        assertNotNull(response.body())
        assertEquals(101, response.body()!!.id)
    }

    @Test
    fun testGetCanteensByUniversity() {
        val response = Service.getCanteensByUniversity(1).execute()
        assertNotNull(response)
        assertNotNull(response.body())
        assertNotEquals(0, response.body()!!.size)
    }

    @Test
    fun testGetCanteenByUniversityAndName() {
        val response = Service.getCanteenByUniversityAndName(1, "梅园").execute()
        assertNotNull(response)
        assertNotNull(response.body())
        assertEquals(1, response.body()!!.size)
        assertEquals(101, response.body()!![0].id)
    }

    @Test
    fun testGetCatalogById() {
        val response = Service.getCatalogById(10301).execute()
        assertNotNull(response)
        assertNotNull(response.body())
        assertEquals(10301, response.body()!!.id)
    }

    @Test
    fun testGetCatalogsByCanteen() {
        val response = Service.getCatalogsByCanteen(103).execute()
        assertNotNull(response)
        assertNotNull(response.body())
        assertNotEquals(0, response.body()!!.size)
    }

    @Test
    fun testGetCatalogsByCanteenAndName() {
        val response = Service.getCatalogsByCanteenAndName(103, "贾福记骨汤麻辣烫").execute()
        assertNotNull(response)
        assertNotNull(response.body())
        assertEquals(1, response.body()!!.size)
        assertEquals(10301, response.body()!![0].id)
    }

    @Test
    fun testGetDishById() {
        val response = Service.getDishById(1030101).execute()
        assertNotNull(response)
        assertNotNull(response.body())
        assertEquals(1030101, response.body()!!.id)
    }

    @Test
    fun testGetDishesByCatalog() {
        val response = Service.getDishesByCatalog(10301).execute()
        assertNotNull(response)
        assertNotNull(response.body())
        assertNotEquals(0, response.body()!!.size)
    }

    @Test
    fun testGetDishesByCatalogAndName() {
        val response = Service.getDishesByCatalogAndName(10301, "骨汤麻辣烫（荤素同价称斤）").execute()
        assertNotNull(response)
        assertNotNull(response.body())
        assertEquals(1, response.body()!!.size)
        assertEquals(1030101, response.body()!![0].id)
    }

    @Test
    fun testGetCatalogCommentById() {
        val response = Service.getCatalogCommentById(1).execute()
        assertNotNull(response)
        assertNotNull(response.body())
        assertEquals(1, response.body()!!.id)
    }

    @Test
    fun testGetCatalogCommentsByCatalog() {
        val response = Service.getCatalogCommentsByCatalog(10301).execute()
        assertNotNull(response)
        assertNotNull(response.body())
        assert(response.body()!!.avgStar >= 0)
    }

    @Test
    fun testPostCatalogComment() {
        val response = Service.postCatalogComment(10301, CatalogCommentBody(4)).execute()
        assertNotNull(response)
        assertNotNull(response.body())
        assert(response.body()!!.id >= 0)
    }
}
