package com.udacity.asteroidradar.main

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidsApi
import com.udacity.asteroidradar.api.PictureOfDayApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception

class MainViewModel : ViewModel() {
    private val _picture = MutableLiveData<PictureOfDay>()
    val picture: LiveData<PictureOfDay>
        get() = _picture

    private val _asteroids = MutableLiveData<ArrayList<Asteroid>>()
    val asteroid:LiveData<ArrayList<Asteroid>>
        get() = _asteroids

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid:LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    init {
        getPictureOfDay("picture")
        getAsteroids()
    }

    fun displayAsteroidDetails(asteroid: Asteroid)
    {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete()
    {
        _navigateToSelectedAsteroid.value = null
    }

    private fun getAsteroids()
    {
        viewModelScope.launch {
            //_stringResult.value = AsteroidsApi.retrofitService.getAsteroids()
            try {
                _asteroids.value = parseAsteroidsJsonResult(JSONObject(AsteroidsApi.retrofitService.getAsteroids()))
                Log.i("asteroids",_asteroids.value!!.size.toString())
                Log.i("asteroids", _asteroids.value!![0].codename.toString())
            }
            catch (e: Exception)
            {
                Log.i("asteroids","SOMETHING WENT WRONG!")
            }
        }
    }


    private fun getPictureOfDay(type: String)
    {
        viewModelScope.launch {
            try {
                _picture.value = PictureOfDayApi.retrofitService.getPicture()
            } catch (e: Exception)
            {
                Log.i("picture","WRONG!")
            }
        }
    }
}