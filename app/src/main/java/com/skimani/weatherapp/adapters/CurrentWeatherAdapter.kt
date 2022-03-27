package com.skimani.weatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skimani.weatherapp.R
import com.skimani.weatherapp.databinding.CurrentWeatherItemListBinding
import com.skimani.weatherapp.db.entity.CurrentWeather
import com.skimani.weatherapp.utils.Util
import timber.log.Timber

class CurrentWeatherAdapter(private val context: Context) :
    ListAdapter<CurrentWeather, CurrentWeatherAdapter.CurrentWeatherAdapterViewHolder>(
        CurrentWeatherDiffUtil
    ),
    Filterable {
    private var onClickedListerner: onItemClickedListerner? = null
    private var currentWeather = mutableListOf<CurrentWeather>()

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
                binding.ivFavourite.setImageDrawable(context.getDrawable(R.drawable.ic_filled_favourite))
            } else {
                binding.ivFavourite.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_button))
            }
            Util.bindWeatherIcon(weather, binding.ivWeatherIcon)
            binding.ivFavourite.setOnClickListener {
                onClickedListerner?.onFavouriteClicked(currentWeather)
            }
            binding.root.setOnClickListener {
                Timber.d("::::CLICKED:::::")
                onClickedListerner?.onClicked(currentWeather)
            }
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
        fun onClicked(currentWeather: CurrentWeather)
    }

    fun setOnItemClickListerner(listener: onItemClickedListerner) {
        onClickedListerner = listener
    }

    private val filterList = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<CurrentWeather>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(currentList)
            } else {
                for (item in currentList) {
                    if (item.city.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results == null) submitList(currentWeather) else submitList(results?.values as MutableList<CurrentWeather>)
        }
    }

    fun setData(list: List<CurrentWeather>) {
        this.currentWeather = list.toMutableList()
        submitList(list)
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

    override fun getFilter(): Filter {
        return filterList
    }
}
