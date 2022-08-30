package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Repository (private val database: AsteroidRadarDatabase)
{
    val pictureOfDay: LiveData<PictureOfDay?> = database.asteroidRadarDao.getPicture().map {
        it?.let {
            it.asDomainModel()
        }
    }

    val asteroids: LiveData<List<Asteroid>?> = Transformations.map(database.asteroidRadarDao.getAsteroids())
    {
        it?.let {
            it.asDomainModel()
        }
    }

    suspend fun refreshData()
    {
        withContext(Dispatchers.IO)
        {
            val pictureOfDay = AsteroidRadarApi.retrofitService.getPicture()
            if(pictureOfDay.mediaType == "image") {
                database.asteroidRadarDao.insertPicture(pictureOfDay.asDatabaseModel())
            }
            val asteroids = parseAsteroidsJsonResult(JSONObject(AsteroidRadarApi.retrofitService.getAsteroids()))
            database.asteroidRadarDao.insertAll(asteroids.asDatabaseModel())
        }
    }
}