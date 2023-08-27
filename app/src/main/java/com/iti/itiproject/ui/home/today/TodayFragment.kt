package com.iti.itiproject.ui.home.today

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.iti.itiproject.adapter.home.CountriesRVAdapter
import com.iti.itiproject.databinding.FragmentTodayBinding
import com.iti.itiproject.model.api.today.TodayResponse
import com.iti.itiproject.model.api.today.all.TodayAll
import com.iti.itiproject.ui.home.BaseRepository
import org.eazegraph.lib.models.PieModel


class TodayFragment : Fragment() {

    private lateinit var binding: FragmentTodayBinding
    private lateinit var countriesAdapter: CountriesRVAdapter
    private lateinit var viewModel: TodayViewModel
    private lateinit var repository: BaseRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodayBinding.inflate(layoutInflater)
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
            binding.todayProgressBar.isVisible = it
        })

        viewModel.getCountries()
        viewModel.countriesData.observe(viewLifecycleOwner, Observer { response ->
            if (response != null)
                setupRecyclerView(response)
        })

        viewModel.getAll()
        viewModel.allData.observe(viewLifecycleOwner, Observer { response ->
            if (response != null)
                setData(response)
        })
    }

    private fun setData(it: TodayAll?) {

        binding.allPieChart.clearChart()

        binding.apply {

            allPieChart.addPieSlice(
                PieModel(
                    "TodayCases",
                    (it!!.todayCases * 150).toFloat(),
                    Color.parseColor("#CBD122")
                )
            )
            allPieChart.addPieSlice(
                PieModel(
                    "TodayDeath",
                    (it.todayDeaths * 10090).toFloat(),
                    Color.parseColor("#DD2020")
                )
            )
            allPieChart.addPieSlice(
                PieModel(
                    "Recovered",
                    (it.recovered / 100).toFloat(),
                    Color.parseColor("#21AF30")
                )
            )
            allPieChart.addPieSlice(
                PieModel(
                    "Critical",
                    (it.critical * 100).toFloat(),
                    Color.parseColor("#E14934")
                )
            )
            allPieChart.addPieSlice(
                PieModel(
                    "AllCases",
                    (it.cases / 100).toFloat(),
                    Color.parseColor("#929718")
                )
            )
            allPieChart.addPieSlice(
                PieModel(
                    "AllDeaths",
                    it.deaths.toFloat(),
                    Color.parseColor("#951313")
                )
            )
            allPieChart.addPieSlice(
                PieModel(
                    "Active",
                    (it.active / 10).toFloat(),
                    Color.parseColor("#EE65F3")
                )
            )
            allPieChart.addPieSlice(
                PieModel(
                    "Tests",
                    (it.tests / 1000).toFloat(),
                    Color.parseColor("#213CEF")
                )
            )

            allPieChart.startAnimation()

        }
    }

    private fun setupRecyclerView(savedQuoteList: TodayResponse) {
        binding.rvSavedQuotes.apply {
            countriesAdapter =
                CountriesRVAdapter(savedQuoteList)

            val messageLayoutManager =
                LinearLayoutManager(requireContext(), GridLayoutManager.VERTICAL, false)

            layoutManager = messageLayoutManager
            adapter = countriesAdapter
        }
    }

}