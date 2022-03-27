package com.skimani.weatherapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.skimani.weatherapp.R
import com.skimani.weatherapp.adapters.DailyForecastAdapter
import com.skimani.weatherapp.adapters.HourlyForecastAdapter
import com.skimani.weatherapp.databinding.DetailFragmentBinding
import com.skimani.weatherapp.db.entity.CurrentWeather
import com.skimani.weatherapp.db.entity.Hourly
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DetailFragment : Fragment() {

    companion object {
        fun newInstance() = DetailFragment()
    }

    private lateinit var viewModel: DetailViewModel
    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var hourlyForecastAdapter: HourlyForecastAdapter
    private lateinit var dailyForecastAdapter: DailyForecastAdapter

    val currentWeather by lazy {
        arguments?.let { DetailFragmentArgs.fromBundle(it).currentWeather }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        initRequests()
        initViews()
    }

    private fun initRequests() {
//        viewModel.localCurrentWeatherDetails()
        val city = "${currentWeather?.city}, ${currentWeather?.countryCode}"
        currentWeather?.city?.let { viewModel.getLocalHourlyForecast(it) }
        viewModel.getHourlyForecast(city)
    }

    private fun initViews() {
        setUpObservers()
        binding.apply {
            tvCityName.text = "${currentWeather?.city}, ${currentWeather?.countryCode}"
            tvDate.text = currentWeather?.date
            tvTime.text = currentWeather?.time
            tvWeatherText.text = currentWeather?.weatherMain
            tvHumidity.text = "${currentWeather?.humidity}%"
            tvWind.text = "${currentWeather?.windSpeed} km/h"
            tvPressure.text = "${currentWeather?.pressure} mb"
            tvVisibility.text = "${currentWeather?.visibility} km"
            tvTemp.text = "${currentWeather?.temperature}°"
            ivBack.setOnClickListener {
                Navigation.findNavController(it).navigateUp()
            }
            ivFavourite.setOnClickListener {
                currentWeather?.let { onFavouriteClicked(it) }
            }
        }
        initAdapter()
    }

    private fun initAdapter() {
        hourlyForecastAdapter = HourlyForecastAdapter(requireContext())
        binding.rvHourlyForecast.adapter = hourlyForecastAdapter
        dailyForecastAdapter = DailyForecastAdapter(requireContext())
        binding.rvDayForecast.adapter = dailyForecastAdapter
    }

    private fun onFavouriteClicked(currentWeather: CurrentWeather) {
        Timber.d("favourite!!! ${currentWeather.city}")
        val isFavourite = currentWeather.favourite
        if (isFavourite) {
            viewModel.addFavourite(
                currentWeather.city,
                currentWeather.countryCode,
                false
            )
        } else {
            viewModel.addFavourite(
                currentWeather.city,
                currentWeather.countryCode,
                true
            )
        }
        val message =
            if (!isFavourite) " ${currentWeather.city} added to favourites" else " ${currentWeather.city} removed from favourites"
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setUpObservers() {
        currentWeather?.let {
            viewModel.localCurrentWeatherDetails(it).observe(viewLifecycleOwner, {
                if (it != null) {
                    Timber.d("Local weather :::: $it")
                    val favourite = currentWeather?.favourite
                    if (favourite == true) {
                        binding.ivFavourite.setBackgroundDrawable(requireContext().getDrawable(R.drawable.ic_filled_favourite))
                    } else {
                        binding.ivFavourite.setBackgroundDrawable(requireContext().getDrawable(R.drawable.ic_favorite_button))
                    }
                }
            })
        }
        currentWeather?.let {
            viewModel.getLocalHourlyForecast(it.city)
                .observe(viewLifecycleOwner, { hourlyForecast ->
                    if (hourlyForecast != null) {
                        val data = hourlyForecast.list
                        hourlyForecastAdapter.submitList(data?.subList(0, 6))
                        filterDataToDays(data)
                    }
                })
        }
    }

    private fun filterDataToDays(data: List<Hourly>?) {
        dailyForecastAdapter.submitList(data?.subList(0, 6))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
