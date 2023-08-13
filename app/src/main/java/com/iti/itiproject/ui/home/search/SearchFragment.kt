package com.iti.itiproject.ui.home.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.iti.itiproject.adapter.all.AllDataRVAdapter
import com.iti.itiproject.databinding.FragmentSearchBinding
import com.iti.itiproject.model.api.today.TodayResponseItem
import com.iti.itiproject.ui.home.BaseRepository


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var repository: BaseRepository
    private lateinit var searchAdapter: AllDataRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        repository = BaseRepository()
        viewModel = SearchViewModel(repository)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTestListeners()

    }

    private fun editTestListeners() {
        binding.etSearch.addTextChangedListener {
            observation(binding.etSearch.text.toString())
        }
    }

    private fun observation(text: String) {
        viewModel.getCountryData(text)
        viewModel.countryData.observe(viewLifecycleOwner, Observer { response ->

            if (response != null) {
                setupRecyclerView(response)
            }

        })
    }

    private fun setupRecyclerView(searchQuoteResponse: TodayResponseItem) {

        binding.apply {
            tvNothing.visibility = View.GONE
            countryLayout.apply {
                lol.visibility = View.VISIBLE

                tvCountryName.text = searchQuoteResponse.country
                tvContinent.text = searchQuoteResponse.continent.toString()
                recovered.text = searchQuoteResponse.recovered.toString()
                tvCases.text = searchQuoteResponse.cases.toString()
                tvDeaths.text = searchQuoteResponse.deaths.toString()
                tvActive.text = searchQuoteResponse.active.toString()
                tvTests.text = searchQuoteResponse.tests.toString()
                tvCritical.text = searchQuoteResponse.critical.toString()
                recovered.text = searchQuoteResponse.recovered.toString()
            }


        }

    }

}