package com.iti.itiproject.adapter.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.iti.itiproject.R
import com.iti.itiproject.databinding.CountriesItemBinding
import com.iti.itiproject.model.api.today.TodayResponse
import com.iti.itiproject.model.api.today.TodayResponseItem
import org.eazegraph.lib.models.PieModel

class CountriesRVAdapter(private var countriesArrayList: TodayResponse?) :
    RecyclerView.Adapter<CountriesRVAdapter.CountriesAdapter>() {

    inner class CountriesAdapter(private val binding: CountriesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val countryName = binding.tvCountry
        val pieChart = binding.piechart
        val flag = binding.ivFlag
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesAdapter {
        val binding =
            CountriesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountriesAdapter(binding)
    }

    override fun getItemCount(): Int {
        return countriesArrayList!!.size
    }

    override fun onBindViewHolder(holder: CountriesAdapter, position: Int) {
        val item = countriesArrayList!![position]
        holder.countryName.text = item.country

        Glide.with(holder.flag)
            .load(item.countryInfo.flag)
            .error(R.drawable.ic_no_flag)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .fitCenter()
            .into(holder.flag)

        addPieSlices(holder = holder, item)

    }

    private fun addPieSlices(
        holder: CountriesAdapter,
        item: TodayResponseItem
    ) {
        holder.pieChart.clearChart()

        holder.pieChart.addPieSlice(
            PieModel(
                "recover",
                item.todayRecovered.toFloat(),
                Color.parseColor("#21AF30")
            )
        )
        holder.pieChart.addPieSlice(
            PieModel(
                "cases",
                item.todayCases.toFloat(),
                Color.parseColor("#CBD122")
            )
        )
        holder.pieChart.addPieSlice(
            PieModel(
                "death",
                item.todayDeaths.toFloat(),
                Color.parseColor("#DD2020")
            )
        )

        holder.pieChart.startAnimation()

    }
}