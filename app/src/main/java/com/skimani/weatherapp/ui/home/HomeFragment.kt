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
import com.skimani.weatherapp.adapters.CurrentWeatherAdapter
import com.skimani.weatherapp.databinding.FragmentHomeBinding
import com.skimani.weatherapp.db.entity.CurrentWeather
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
                    Navigation.findNavController(binding.rvCurrentWeather)
                        .navigate(R.id.navigation_notes)
                }
            })
    }

    private fun initRequests() {
        homeViewModel.localCurrentWeather()
        homeViewModel.getCurrentWeather()
    }

    private fun setUpObservers() {
        homeViewModel.localCurrentWeather.observe(viewLifecycleOwner, {
            if (it != null) {
                Timber.d("Local weather :::: $it")
                currentWeatherAdapter.submitList(it)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
