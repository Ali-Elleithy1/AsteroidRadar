package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidRadarApi
import com.udacity.asteroidradar.api.AsteroidsApiFilter
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Exception

enum class ApiStatus { LOADING, DONE, ERROR }
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var today: String
    private val calendar = Calendar.getInstance()
    private val currentTime = calendar.time
    private val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())


    private val database = AsteroidRadarDatabase.getInstance(application)
    private val repository = Repository(database)

//    private val _picture = MutableLiveData<PictureOfDay>()
//    val picture: LiveData<PictureOfDay>
//        get() = _picture
//
//    private val _asteroids1 = MutableLiveData<List<Asteroid>>()
//    val asteroid1:LiveData<List<Asteroid>>
//        get() = _asteroids1

    private val _asteroidsTime = MutableLiveData<String>("saved")
    val asteroidTime:LiveData<String>
        get() = _asteroidsTime

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid:LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    init {
        viewModelScope.launch {
            try {
                //_status.value = ApiStatus.LOADING
                repository.refreshData()
                //_status.value = ApiStatus.DONE
            }
            catch (e: Exception)
            {
                //_status.value = ApiStatus.ERROR
                Log.i("refresh", "No Internet Connection.")
            }
        }
//        _asteroids.value = repository.asteroids
//        _picture.value = repository.pictureOfDay
//        getPictureOfDay()
//        getAsteroids()
    }

    var asteroids = repository.asteroids
    var pictureOfDay = repository.pictureOfDay
    var pictureOfDayContent = repository.pictureOfDay.value?.title?:"Empty image"


    fun displayAsteroidDetails(asteroid: Asteroid)
    {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete()
    {
        _navigateToSelectedAsteroid.value = null
    }

    fun setAsteroidType(filter: AsteroidsApiFilter)
    {
        _asteroidsTime.value = filter.value
    }

    fun updateFilter() {
        //setAsteroidType(filter)
        asteroids = Transformations.switchMap(_asteroidsTime) { type ->
            when (type) {
                AsteroidsApiFilter.DAY.value -> {
                    today = dateFormat.format(currentTime)
                    database.asteroidRadarDao.getTodayAsteroids(today).map {
                        it.asDomainModel()
                    }
                }
                AsteroidsApiFilter.WEEK.value -> {
                    database.asteroidRadarDao.getWeekAsteroids(getNextSevenDaysFormattedDates())
                        .map {
                            it.asDomainModel()
                        }
                }
                else -> {
                    database.asteroidRadarDao.getAsteroids().map {
                        it.asDomainModel()
                    }
                }
            }
        }
    }

    private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
        val formattedDateList = ArrayList<String>()

        val calendar = Calendar.getInstance()
        for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
            val currentTime = calendar.time
            val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            formattedDateList.add(dateFormat.format(currentTime))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return formattedDateList
    }
}