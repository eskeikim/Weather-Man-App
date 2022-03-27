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
import com.skimani.weatherapp.databinding.DetailFragmentBinding
import com.skimani.weatherapp.db.entity.CurrentWeather
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
        initViews()
    }

    private fun initViews() {
        binding.apply {
            ivBack.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(R.id.navigation_home)
            }
        }
    }

    fun onFavouriteClicked(currentWeather: CurrentWeather) {
        Timber.d("favourite!!! ${currentWeather.city}")
        var isFavourite = false
        isFavourite = !currentWeather.favourite
        viewModel.addFavourite(
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
