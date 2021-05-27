package de.timbo.coracle.injection

import de.timbo.coracle.BuildConfig
import de.timbo.coracle.api.ApiType
import de.timbo.coracle.api.CoinCapApiInterface
import de.timbo.coracle.api.AuthInterceptor
import de.timbo.coracle.api.CurrencyApiInterface
import de.timbo.coracle.utils.CURRENCY_EXCHANGE_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    single(qualifier(ApiType.COIN_CAP)) { provideOkHttpClient() }
    single { provideRetrofit(get(qualifier(ApiType.COIN_CAP))) }
    single(qualifier(ApiType.CURRENCY)) { provideCurrencyOkHttpClient() }
    single { provideCurrencyExchangeRetrofit(get(qualifier(ApiType.CURRENCY))) }
}

private fun provideOkHttpClient(): OkHttpClient {
    val builder = OkHttpClient.Builder()

    if (BuildConfig.DEBUG) {
        builder.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
    }

    builder.addInterceptor(AuthInterceptor())
    builder.readTimeout(10, TimeUnit.SECONDS)
    builder.connectTimeout(10, TimeUnit.SECONDS)
    builder.writeTimeout(10, TimeUnit.SECONDS)

    return builder.build()
}

private fun provideCurrencyOkHttpClient(): OkHttpClient {
    val builder = OkHttpClient.Builder()

    if (BuildConfig.DEBUG) {
        builder.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
    }

    builder.addInterceptor(AuthInterceptor())
    builder.readTimeout(10, TimeUnit.SECONDS)
    builder.connectTimeout(10, TimeUnit.SECONDS)
    builder.writeTimeout(10, TimeUnit.SECONDS)

    return builder.build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
    .baseUrl(BuildConfig.BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(okHttpClient)
    .build()
    .create(CoinCapApiInterface::class.java)


private fun provideCurrencyExchangeRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
    .baseUrl(CURRENCY_EXCHANGE_BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(okHttpClient)
    .build()
    .create(CurrencyApiInterface::class.java)