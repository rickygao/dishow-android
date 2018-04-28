package xyz.rickygao.dishow.network

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.*
import java.math.BigDecimal

interface Service {

    companion object REAL : Service by ServiceFactory()

    @GET("/users/username/{username}/password/{password}")
    fun getUserByUsernameAndPassword(@Path("username") username: String,
                                     @Path("password") password: String): Deferred<Id>

    @PUT("/users/username/{username}/password/{password}")
    fun putUserByUsernameAndPassword(@Path("username") username: String,
                                     @Path("password") password: String): Deferred<Id>

    @GET("/universities/{uid}")
    fun getUniversityById(@Path("uid") uid: Int): Deferred<University>

    @GET("/universities")
    fun getUniversities(): Deferred<List<University>>

    @GET("/canteens/{cid}")
    fun getCanteenById(@Path("cid") cid: Int): Deferred<Canteen>

    @GET("/catalogs/{cid}")
    fun getCatalogById(@Path("cid") cid: Int): Deferred<Catalog>

    @GET("/dishes/{did}")
    fun getDishById(@Path("did") cid: Int): Deferred<Dish>

    @GET("/catalogs/comments/{ccid}")
    fun getCatalogCommentById(@Path("ccid") ccid: Int): Deferred<CatalogComment>

    @GET("/catalogs/{cid}/comments")
    fun getCatalogCommentsByCatalog(@Path("cid") cid: Int): Deferred<CatalogComments>

    @POST("/catalogs/{cid}/comments")
    fun postCatalogComment(@Path("cid") cid: Int,
                           @Body body: CatalogCommentBody): Deferred<Id>
}

data class University(
        val id: Int,
        val name: String,
        val location: String?,
        val longitude: BigDecimal?,
        val latitude: BigDecimal?,
        val canteens: List<Canteen>?
)

data class Canteen(
        val id: Int,
        val name: String,
        val location: String?,
        val longitude: BigDecimal?,
        val latitude: BigDecimal?,
        val catalogs: List<Catalog>?
)

data class Catalog(
        val id: Int,
        val name: String,
        val location: String?,
        @SerializedName("avg_star")
        val avgStar: Double?,
        val dishes: List<Dish>?
)

data class Dish(
        val id: Int,
        val name: String,
        val price: BigDecimal?
)

data class CatalogComment(
        val id: Int,
        val star: Int,
        val detail: String,
        val username: String?
)

data class CatalogComments(
        @SerializedName("avg_star")
        val avgStar: Double?,
        val comments: List<CatalogComment>
)

data class CatalogCommentBody(
        val star: Int,
        val detail: String? = null,
        val anonymous: Boolean
)

data class Id(val id: Int?)