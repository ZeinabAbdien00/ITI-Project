package com.iti.itiproject.model.api.today.all

data class TodayAll(
    val active: Long,
    val affectedCountries: Long,
    val cases: Long,
    val casesPerOneMillion: Double,
    val critical: Long,
    val deaths: Long,
    val deathsPerOneMillion: Double,
    val recovered: Long,
    val tests: Long,
    val testsPerOneMillion: Double,
    val todayCases: Long,
    val todayDeaths: Long,
    val updated: Long
)