package xyz.rickygao.dishow

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.math.BigDecimal

object ServiceFactory {

    private const val HOST_NAME = "192.168.31.246"
    private const val BASE_URL = "http://$HOST_NAME:8080/"

    val client = OkHttpClient.Builder().build()
    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @JvmName("create")
    inline operator fun <reified T> invoke(): T = retrofit.create(T::class.java)
}

interface Service {

    companion object REAL : Service by ServiceFactory()

    @GET("/universities/{uid}")
    fun getUniversityById(@Path("uid") uid: Int): Call<University>

    @GET("/universities")
    fun getUniversities(): Call<List<University>>

    @GET("/universities/name/{name}")
    fun getUniversityByName(@Path("name") name: String): Call<List<University>>

    @GET("/canteens/{cid}")
    fun getCanteenById(@Path("cid") cid: Int): Call<Canteen>

    @GET("/universities/{uid}/canteens")
    fun getCanteensByUniversity(@Path("uid") uid: Int): Call<List<Canteen>>

    @GET("/universities/{uid}/canteens/name/{name}")
    fun getCanteenByUniversityAndName(@Path("uid") uid: Int,
                                      @Path("name") name: String): Call<List<Canteen>>

    @GET("/catalogs/{cid}")
    fun getCatalogById(@Path("cid") cid: Int): Call<Catalog>

    @GET("/canteens/{cid}/catalogs")
    fun getCatalogsByCanteens(@Path("cid") cid: Int): Call<List<Catalog>>

    @GET("/canteens/{cid}/catalogs/name/{name}")
    fun getCatalogsByCanteenAndName(@Path("cid") uid: Int,
                                    @Path("name") name: String): Call<List<Catalog>>

    @GET("/dishes/{did}")
    fun getDishById(@Path("did") cid: Int): Call<Dish>

    @GET("/catalogs/{cid}/dishes")
    fun getDishesByCatalog(@Path("cid") cid: Int): Call<List<Dish>>

    @GET("/catalogs/{cid}/dishes/name/{name}")
    fun getDishesByCatalogAndName(@Path("cid") uid: Int,
                                    @Path("name") name: String): Call<List<Dish>>

}

data class University(
        val id: Int,
        val name: String,
        val location: String,
        val longitude: BigDecimal,
        val latitude: BigDecimal
)

data class Canteen(
        val id: Int,
        val name: String,
        val location: String,
        val longitude: BigDecimal,
        val latitude: BigDecimal,
        val uid: Int
)

data class Catalog(
        val id: Int,
        val name: String,
        val location: String,
        val cid: Int
)

data class Dish(
        val id: Int,
        val name: String,
        val price: BigDecimal,
        val cid: Int
)