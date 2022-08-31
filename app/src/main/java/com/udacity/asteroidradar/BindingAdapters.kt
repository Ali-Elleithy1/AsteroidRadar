package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.ApiStatus

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = imageView.context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = imageView.context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("asteroidsStatus")
fun asteroidsStatus(progressBar: ProgressBar, apiStatus: ApiStatus?)
{
    apiStatus.let {
        when (apiStatus)
        {
            ApiStatus.LOADING -> {
                progressBar.visibility = View.VISIBLE
            }
            ApiStatus.DONE -> {
                progressBar.visibility = View.GONE
            }
        }
    }
}

@BindingAdapter("imgOfDayContentDescription")
fun imgOfDayContentDescription(imageView: ImageView, content: String?)
{
    content?.let {
        imageView.contentDescription = content
    }
}

@BindingAdapter("listData")
fun bindAsteroids(recyclerView: RecyclerView, data: List<Asteroid>?)
{
    data?.let {
        val adapter = recyclerView.adapter as AsteroidsAdapter
        adapter.submitList(data)
    }
}

@BindingAdapter("imageOfDay")
fun bindImage(imageView: ImageView, imgOfDay: PictureOfDay?)
{
    imgOfDay?.let {
        if(imgOfDay.mediaType == "image")
        {
            val imgUri = imgOfDay.url.toUri().buildUpon().scheme("https").build()
            Glide.with(imageView.context)
                .load(imgUri)
                .apply(RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_error))
                .into(imageView)
        }
        else
        {
            Glide.with(imageView.context)
            .load(R.drawable.ic_error)
            .into(imageView)
        }
    }
}
