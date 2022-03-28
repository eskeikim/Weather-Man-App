package com.skimani.weatherapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.skimani.weatherapp.R
import com.skimani.weatherapp.adapters.CurrentWeatherAdapter
import com.skimani.weatherapp.databinding.FragmentHomeBinding
import com.skimani.weatherapp.db.entity.CurrentWeather
import com.skimani.weatherapp.utils.Constants
import com.skimani.weatherapp.utils.NotificationWorkManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var currentWeatherAdapter: CurrentWeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initviews()
        setupAdapter()
        initRequests()
        setUpObservers()
        return root
    }

    private fun initviews() {
        binding.apply {
            ivBackground.setOnClickListener {
                if (cdBgSelector.visibility == View.VISIBLE) {
                    hideSelectorLayout()
                } else {
                    cdBgSelector.visibility = View.VISIBLE
                }
            }
            tvClearDay.setOnClickListener {
                ivBackgroundImage.setImageDrawable(requireContext().getDrawable(R.drawable.clear_day))
                hideSelectorLayout()
            }
            tvRainyDay.setOnClickListener {
                ivBackgroundImage.setImageDrawable(requireContext().getDrawable(R.drawable.rainy_day_bg))
                hideSelectorLayout()
            }
            tvClearNight.setOnClickListener {
                ivBackgroundImage.setImageDrawable(requireContext().getDrawable(R.drawable.clear_night_bg))
                hideSelectorLayout()
            }
            tvRainyNight.setOnClickListener {
                ivBackgroundImage.setImageDrawable(requireContext().getDrawable(R.drawable.rainy_night_bg))
                hideSelectorLayout()
            }
            ivSearchWeatherIcon.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    currentWeatherAdapter.filter.filter(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    currentWeatherAdapter.filter.filter(newText)
                    return true
                }
            })
        }
    }

    private fun hideSelectorLayout() {
        binding.cdBgSelector.visibility = View.GONE
    }

    private fun setupAdapter() {
        currentWeatherAdapter = CurrentWeatherAdapter(requireContext())
        binding.rvCurrentWeather.adapter = currentWeatherAdapter
        currentWeatherAdapter.setOnItemClickListerner(object :
                CurrentWeatherAdapter.onItemClickedListerner {

                override fun onFavouriteClicked(currentWeather: CurrentWeather) {
                    Timber.d("favourite!!! ${currentWeather.city}")
                    var isFavourite = false
                    isFavourite = !currentWeather.favourite
                    homeViewModel.addFavourite(
                        currentWeather.city,
                        currentWeather.countryCode,
                        isFavourite
                    )
                    val message =
                        if (isFavourite) " ${currentWeather.city} added to favourites" else " ${currentWeather.city} removed from favourites"
                    Toast.makeText(
                        requireContext(),
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onClicked(currentWeather: CurrentWeather) {
                    val directions =
                        HomeFragmentDirections.actionNavigationHomeToDetailFragment(currentWeather)
                    findNavController().navigate(directions)
                }
            })
    }

    private fun initRequests() {
        homeViewModel.localCurrentWeather()
        homeViewModel.getCurrentWeather()
        initPeriodicWork()
    }

    private fun setUpObservers() {
        homeViewModel.localCurrentWeather.observe(viewLifecycleOwner, {
            if (it != null) {
                Timber.d("Local weather :::: $it")
                currentWeatherAdapter.setData(it)
                binding.ivNoData.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        })
    }

    private fun initPeriodicWork() {
        val syncDatesWork =
            PeriodicWorkRequest.Builder(NotificationWorkManager::class.java, 1, TimeUnit.HOURS)
                .setInitialDelay(2, TimeUnit.MINUTES)
                .addTag(Constants.TAG_NOTIFICATION_TIME_WORKER)
                .build()
        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            Constants.NAME_NOTIFICATION_TIME_WORKER,
            ExistingPeriodicWorkPolicy.KEEP,
            syncDatesWork
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
