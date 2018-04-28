package xyz.rickygao.dishow.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.rickygao.dishow.common.Preference

internal object ServiceFactory {

    private const val HOST_NAME = "rickygao.xyz"
    //    private const val HOST_NAME = "172.23.83.95"
    private const val BASE_URL = "http://$HOST_NAME:8080/"

    val client = OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .authenticator { route, response ->
                val username = Preference.username
                val password = Preference.password

                if (username != null && password != null) {
                    response.request().newBuilder()
                            .header("username", Preference.username)
                            .header("password", Preference.password)
                            .build()
                } else null
            }
            .retryOnConnectionFailure(false)
            .build()

    val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    @JvmName("create")
    inline operator fun <reified T> invoke(): T = retrofit.create(T::class.java)
}