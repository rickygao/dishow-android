package xyz.rickygao.dishow.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.rickygao.dishow.common.Preference

internal object ServiceFactory {

    private const val HOST_NAME = "rickygao.xyz"
    private const val BASE_URL = "http://$HOST_NAME:8080/"

    val client = OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .authenticator { _, response ->
                val username = Preference.username
                val password = Preference.password

                if (username != null && password != null) {
                    response.request().newBuilder()
                            .header("username", username)
                            .header("password", password)
                            .build()
                } else null
            }
            .build()

    val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    @JvmName("create")
    inline operator fun <reified T> invoke(): T = retrofit.create(T::class.java)
}