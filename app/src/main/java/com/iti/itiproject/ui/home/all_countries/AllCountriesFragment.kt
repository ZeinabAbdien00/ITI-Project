package com.iti.itiproject.ui.home.all_countries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.iti.itiproject.adapter.all.AllDataRVAdapter
import com.iti.itiproject.databinding.FragmentAllCountriesBinding
import com.iti.itiproject.model.api.today.TodayResponse
import com.iti.itiproject.ui.home.BaseRepository
import com.iti.itiproject.ui.home.today.TodayViewModel


class AllCountriesFragment : Fragment() {

    private lateinit var binding: FragmentAllCountriesBinding
    private lateinit var allCountriesAdapter: AllDataRVAdapter

    private lateinit var viewModel: TodayViewModel
    private lateinit var repository: BaseRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllCountriesBinding.inflate(layoutInflater)
        repository = BaseRepository()
        viewModel = TodayViewModel(repository)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observation()
    }

    private fun observation() {

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.allProgressBar.isVisible = it
        })

        viewModel.getCountries()
        viewModel.countriesData.observe(viewLifecycleOwner, Observer { response ->
            if (response != null)
                setupRecyclerView(response)
        })
    }

    private fun setupRecyclerView(savedQuoteList: TodayResponse) {
        binding.rvAll.apply {
            allCountriesAdapter =
                AllDataRVAdapter(savedQuoteList)

            val messageLayoutManager =
                LinearLayoutManager(requireContext(), GridLayoutManager.VERTICAL, false)

            layoutManager = messageLayoutManager
            adapter = allCountriesAdapter
        }
    }

}