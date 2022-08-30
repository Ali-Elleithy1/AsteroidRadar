package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.ArrayList

@Dao
interface AsteroidRadarDao {
    @Query("SELECT * FROM databaseasteroid ORDER BY closeApproachDate ASC")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>?>

    @Query("SELECT * FROM databaseasteroid WHERE closeApproachDate = :today")
    fun getTodayAsteroids(today: String): LiveData<List<DatabaseAsteroid>?>

    @Query("SELECT * FROM databaseasteroid WHERE closeApproachDate IN (:sevenDaysDates) ORDER BY closeApproachDate ASC")
    fun getWeekAsteroids(sevenDaysDates: ArrayList<String>): LiveData<List<DatabaseAsteroid>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: List<DatabaseAsteroid>)

    @Query("select * from databasepictureofday")
    fun getPicture(): LiveData<DatabasePictureOfDay?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPicture(picture: DatabasePictureOfDay)
}