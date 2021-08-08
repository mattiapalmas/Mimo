package com.mimo.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mimo.common.Constants.MIMO_API_URL
import com.mimo.data.api.MimoApi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { provideLessonsApi() }
}

fun provideGson(): Gson = GsonBuilder()
    .setLenient()
    .create()

fun provideConverterFactory(
    gson: Gson = provideGson()
): Converter.Factory = GsonConverterFactory.create(gson)

fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder().build()

fun provideLessonsApi(
    domain: String = MIMO_API_URL,
    okHttpClient: OkHttpClient = provideOkHttp(),
    converterFactory: Converter.Factory = provideConverterFactory()
): MimoApi = Retrofit.Builder()
    .baseUrl(domain)
    .client(okHttpClient)
    .addConverterFactory(converterFactory)
    .build()
    .create(MimoApi::class.java)