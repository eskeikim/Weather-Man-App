package com.skimani.weatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skimani.weatherapp.R
import com.skimani.weatherapp.databinding.HourlyForecastItemListBinding
import com.skimani.weatherapp.db.entity.Hourly
import com.skimani.weatherapp.db.entity.HourlyForecast

class DailyForecastAdapter(private val context: Context) :
    ListAdapter<Hourly, DailyForecastAdapter.DailyForecastAdapterViewHolder>(
        HourlyForecastDiffUtil
    ) {
    private var onClickedListerner: onItemClickedListerner? = null

    inner class DailyForecastAdapterViewHolder(private val binding: HourlyForecastItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hourlyForecast: Hourly) {
            val date = hourlyForecast.date
            val time = hourlyForecast.time
            val temperature = hourlyForecast.temperature
            val weather = hourlyForecast.weatherMain
            val weatherIcon = hourlyForecast.weatherIcon
            val weatherDesc = hourlyForecast.weatherDescription
            binding.tvTime.text = date
            binding.tvTemp.text = "$temperature°"

            bindWeatherIcon(weather, binding.ivWeatherIcon)
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
    ): DailyForecastAdapterViewHolder {
        return DailyForecastAdapterViewHolder(
            HourlyForecastItemListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: DailyForecastAdapterViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    interface onItemClickedListerner {
        fun onFavouriteClicked(currentWeather: HourlyForecast)
        fun onClicked(currentWeather: HourlyForecast)
    }

    fun setOnItemClickListerner(listener: onItemClickedListerner) {
        onClickedListerner = listener
    }

    companion object {
        object HourlyForecastDiffUtil : DiffUtil.ItemCallback<Hourly>() {
            override fun areItemsTheSame(
                oldItem: Hourly,
                newItem: Hourly
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Hourly,
                newItem: Hourly
            ): Boolean {
                return oldItem.time == newItem.time
            }
        }
    }
}
