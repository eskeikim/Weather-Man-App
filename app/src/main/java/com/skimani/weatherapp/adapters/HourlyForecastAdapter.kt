package com.skimani.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skimani.weatherapp.databinding.HourlyForecastItemListBinding
import com.skimani.weatherapp.db.entity.Hourly
import com.skimani.weatherapp.utils.Util

class HourlyForecastAdapter :
    ListAdapter<Hourly, HourlyForecastAdapter.HourlyForecastAdapterViewHolder>(
        HourlyForecastDiffUtil
    ) {

    inner class HourlyForecastAdapterViewHolder(private val binding: HourlyForecastItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hourlyForecast: Hourly) {
            val date = hourlyForecast.date
            val time = hourlyForecast.time
            val temperature = hourlyForecast.temperature
            val weather = hourlyForecast.weatherMain
            val weatherIcon = hourlyForecast.weatherIcon
            val weatherDesc = hourlyForecast.weatherDescription
            binding.tvTime.text = time
            binding.tvTemp.text = "$temperature°"

            Util.bindWeatherIcon(weather, binding.ivWeatherIcon)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HourlyForecastAdapterViewHolder {
        return HourlyForecastAdapterViewHolder(
            HourlyForecastItemListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: HourlyForecastAdapterViewHolder, position: Int) {
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
