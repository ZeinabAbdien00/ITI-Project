package com.iti.itiproject.adapter.all

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.itiproject.databinding.AllDataItemBinding
import com.iti.itiproject.model.api.today.TodayResponse

class AllDataRVAdapter(private var countriesArrayList: TodayResponse?) :
    RecyclerView.Adapter<AllDataRVAdapter.AllCountriesAdapter>() {

    inner class AllCountriesAdapter(private val binding: AllDataItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val countryName = binding.tvCountryName
        val continent = binding.tvContinent
        val recovered = binding.recovered
        val cases = binding.tvCases
        val death = binding.tvDeaths
        val active = binding.tvActive
        val tests = binding.tvTests
        val critical = binding.tvCritical
        val population = binding.tvPopulation
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCountriesAdapter {
        val binding =
            AllDataItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllCountriesAdapter(binding)
    }

    override fun getItemCount(): Int {
        return countriesArrayList!!.size
    }

    override fun onBindViewHolder(holder: AllCountriesAdapter, position: Int) {
        val item = countriesArrayList!![position]
        holder.countryName.text = item.country.toString()
        holder.active.text = item.active.toString()
        holder.death.text = item.deaths.toString()
        holder.cases.text = item.cases.toString()
        holder.tests.text = item.tests.toString()
        holder.continent.text = item.continent.toString()
        holder.critical.text = item.critical.toString()
        holder.population.text = item.population.toString()
        holder.recovered.text = item.recovered.toString()
    }

}