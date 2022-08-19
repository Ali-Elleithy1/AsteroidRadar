package com.udacity.asteroidradar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.leanback.widget.DiffCallback
import androidx.leanback.widget.GuidedActionAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidItemBinding

class AsteroidsAdapter(val onClickListener: OnClickListener): ListAdapter<Asteroid,AsteroidsAdapter.AsteroidViewHolder>(DiffCallback) {
    class AsteroidViewHolder(private var binding: AsteroidItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid)
        {
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): AsteroidViewHolder
            {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: AsteroidItemBinding = DataBindingUtil.inflate(layoutInflater,R.layout.asteroid_item,parent,false)
                return AsteroidViewHolder(binding)
            }
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Asteroid>()
    {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        //return AsteroidViewHolder(AsteroidItemBinding.inflate(LayoutInflater.from(parent.context)))
        return AsteroidViewHolder.from(parent)

    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(asteroid)
        }
        holder.bind(asteroid)
    }

    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit ) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}
