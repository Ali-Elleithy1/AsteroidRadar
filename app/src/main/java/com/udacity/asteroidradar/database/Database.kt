package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatabaseAsteroid::class,DatabasePictureOfDay::class], version = 1)
abstract class AsteroidRadarDatabase: RoomDatabase()
{
    abstract val asteroidRadarDao: AsteroidRadarDao

    companion object {
        @Volatile
        private var Instance: AsteroidRadarDatabase? = null

        fun getInstance(context:Context): AsteroidRadarDatabase
        {
            synchronized(this)
            {
                var instance = Instance
                if(instance == null)
                {
                    instance = Room.databaseBuilder(context.applicationContext,AsteroidRadarDatabase::class.java,"asteroids_db").fallbackToDestructiveMigration().build()
                    Instance = instance
                }
                return instance
            }
        }
    }
}