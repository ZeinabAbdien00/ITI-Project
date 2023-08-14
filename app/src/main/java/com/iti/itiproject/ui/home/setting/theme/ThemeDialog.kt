package com.iti.itiproject.ui.home.setting.theme

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.iti.itiproject.data.datastore.DataStoreImplementation
import com.iti.itiproject.databinding.FragmentThemeDialogBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ThemeDialog : DialogFragment() {
    private lateinit var binding: FragmentThemeDialogBinding
    private lateinit var dataStore: DataStoreImplementation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThemeDialogBinding.inflate(layoutInflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requireActivity().setFinishOnTouchOutside(false)
        dataStore = DataStoreImplementation(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels

        this.dialog!!.window!!.setLayout(((9 * width) / 10), (2 * height) / 10)
        setonClickListener()

    }

    private fun setonClickListener() {
        binding.darkBtn.setOnClickListener {
            changeDarkMode(checkedButton = true)
        }
        binding.lightBtn.setOnClickListener {
            changeDarkMode(checkedButton = false)
        }
    }

    private fun changeDarkMode(checkedButton: Boolean) {
        themeSwitch(checkedButton)
    }

    private val themeSwitch: (checkedButton: Boolean) -> Unit = {
        when (it) {

            true -> {
                val job = saveDarkMode(true)
                lifecycleScope.launch {
                    if (job.isActive) {
                        job.join()
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }
            }
            false -> {
                val job = saveDarkMode(false)
                lifecycleScope.launch {
                    if (job.isActive) {
                        job.join()
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
            }
        }
    }

    private fun saveDarkMode(isDark: Boolean): Job {
        return lifecycleScope.launch {
            dataStore.setDarkMode(isDark)
        }
    }

}