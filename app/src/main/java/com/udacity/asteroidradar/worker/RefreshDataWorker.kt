package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.repository.Repository
import retrofit2.HttpException

class RefreshDataWorker (appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params)
{
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = AsteroidRadarDatabase.getInstance(applicationContext)
        val repository = Repository(database)

        return try {
            repository.refreshData()
            Result.success()
        }
        catch (e: HttpException)
        {
            Result.retry()
        }
    }

}