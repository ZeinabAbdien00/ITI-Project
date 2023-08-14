package com.iti.itiproject

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.iti.itiproject.data.datastore.DataStoreImplementation
import javax.inject.Inject

class MyApplication : Application() {

    @Inject
    lateinit var dataStore: DataStoreImplementation

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        @SuppressLint("StaticFieldLeak")
        lateinit var instance: MyApplication
            private set

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        instance = this
    }

}