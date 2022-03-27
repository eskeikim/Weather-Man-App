package com.skimani.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skimani.weatherapp.databinding.DailyForecastItemListBinding
import com.skimani.weatherapp.db.entity.Hourly
import com.skimani.weatherapp.utils.Util

class DailyForecastAdapter :
    ListAdapter<Hourly, DailyForecastAdapter.DailyForecastAdapterViewHolder>(
        HourlyForecastDiffUtil
    ) {

    inner class DailyForecastAdapterViewHolder(private val binding: DailyForecastItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hourlyForecast: Hourly) {
            var date = hourlyForecast.date
            val time = hourlyForecast.time
            val temperature = hourlyForecast.temperature
            val weather = hourlyForecast.weatherMain
            val weatherIcon = hourlyForecast.weatherIcon
            val weatherDesc = hourlyForecast.weatherDescription
            if (position == 0) {
                date = "Today"
            } else if (position == 1) {
                date = "Tomorrow"
            } else date
            binding.tvTime.text = date
            binding.tvTemp.text = "$temperature°"

            Util.bindWeatherIcon(weather, binding.ivWeatherIcon)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DailyForecastAdapterViewHolder {
        return DailyForecastAdapterViewHolder(
            DailyForecastItemListBinding.inflate(
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
