package com.skimani.weatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skimani.weatherapp.R
import com.skimani.weatherapp.databinding.CurrentWeatherItemListBinding
import com.skimani.weatherapp.db.entity.CurrentWeather
import timber.log.Timber

class CurrentWeatherAdapter(private val context: Context) :
    ListAdapter<CurrentWeather, CurrentWeatherAdapter.CurrentWeatherAdapterViewHolder>(
        CurrentWeatherDiffUtil
    ) {
    private var onClickedListerner: onItemClickedListerner? = null

    inner class CurrentWeatherAdapterViewHolder(private val binding: CurrentWeatherItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentWeather: CurrentWeather) {
            val favourite = currentWeather.favourite
            val cityName = currentWeather.city
            val date = currentWeather.date
            val time = currentWeather.time
            val temperature = currentWeather.temperature
            val weather = currentWeather.weatherMain
            val weatherIcon = currentWeather.weatherIcon
            val weatherDesc = currentWeather.weatherDescription
            binding.tvCityName.text = cityName
            binding.tvDate.text = date
            binding.tvTime.text = time
            binding.tvTemp.text = "$temperature°"
            if (favourite) {
                binding.ivFavourite.setBackgroundDrawable(context.getDrawable(R.drawable.ic_favorite_button))
            } else {
                binding.ivFavourite.setBackgroundDrawable(context.getDrawable(R.drawable.ic_favorite_button))
            }
            bindWeatherIcon(weather, binding.ivWeatherIcon)
            binding.ivFavourite.setOnClickListener {
                Timber.d("CLICKED:::::")
//                Toast.makeText(context, "!!! ${currentWeather.city}", Toast.LENGTH_SHORT).show()
                onClickedListerner?.onFavouriteClicked(currentWeather)
            }
            binding.root.setOnClickListener {
                Timber.d("::::CLICKED:::::")
                onClickedListerner?.onFavouriteClicked(currentWeather)
            }
        }
    }

    private fun bindWeatherIcon(weather: String, ivWeatherIcon: AppCompatImageView) {
        if (weather == "") {
            ivWeatherIcon.setBackgroundDrawable(context.getDrawable(R.drawable.night))
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrentWeatherAdapterViewHolder {
        return CurrentWeatherAdapterViewHolder(
            CurrentWeatherItemListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: CurrentWeatherAdapterViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    interface onItemClickedListerner {
        fun onFavouriteClicked(currentWeather: CurrentWeather)
        fun onClicked(position: Int)
    }

    fun setOnItemClickListerner(listener: onItemClickedListerner) {
        onClickedListerner = listener
    }

    companion object {
        object CurrentWeatherDiffUtil : DiffUtil.ItemCallback<CurrentWeather>() {
            override fun areItemsTheSame(
                oldItem: CurrentWeather,
                newItem: CurrentWeather
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CurrentWeather,
                newItem: CurrentWeather
            ): Boolean {
                return oldItem.city == newItem.city || oldItem.cityId == newItem.cityId || oldItem.countryCode == newItem.countryCode
            }
        }
    }
}
