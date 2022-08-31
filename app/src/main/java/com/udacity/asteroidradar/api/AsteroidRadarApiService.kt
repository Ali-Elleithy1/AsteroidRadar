package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

enum class AsteroidsApiFilter(val value: String) { WEEK(value = "week"), DAY(value = "day"), SAVED(value = "saved")}
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL)
    .build()

interface AsteroidRadarApiService {
    @GET("neo/rest/v1/feed?api_key=0NfGUIj9pkFvQS4UMYekGYCgu04ARFQReH5dNbf2")
    suspend fun getAsteroids(): String

    @GET("planetary/apod?api_key=0NfGUIj9pkFvQS4UMYekGYCgu04ARFQReH5dNbf2")
    suspend fun getPicture(): PictureOfDay
}

object AsteroidRadarApi{
    val retrofitService: AsteroidRadarApiService by lazy { retrofit.create(AsteroidRadarApiService::class.java) }
}