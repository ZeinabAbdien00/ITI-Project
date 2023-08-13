package com.iti.itiproject.ui.home.setting.setting

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iti.itiproject.R
import com.iti.itiproject.data.datastore.DataStoreImplementation
import com.iti.itiproject.databinding.FragmentSettingDetailsBinding
import com.iti.itiproject.ui.home.BaseRepository
import com.iti.itiproject.ui.home.setting.SettingViewModel
import com.iti.itiproject.ui.setup.MainActivity
import com.iti.itiproject.utils.convertStringToBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class SettingDetailsFragment : Fragment() {

    private lateinit var viewModel: SettingViewModel
    private lateinit var repository: BaseRepository
    private lateinit var dataStore: DataStoreImplementation
    private lateinit var navController: NavController

    private lateinit var binding: FragmentSettingDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingDetailsBinding.inflate(layoutInflater)
        repository = BaseRepository()
        dataStore = DataStoreImplementation(context)
        viewModel = SettingViewModel(repository, dataStore)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        setOnClickListener()
    }


    private fun logOutUser(dataStore: DataStoreImplementation) {
        lifecycleScope.launch {
            viewModel.logOut(dataStore)

            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()

        }
    }

    private fun setOnClickListener() {
        binding.apply {
            profileLinear.setOnClickListener {
                lifecycleScope.launch(Dispatchers.Main) {
                    findNavController().navigate(SettingDetailsFragmentDirections.actionSettingDetailsFragmentToProfileFragment2())
                }
            }
            adviceLinear.setOnClickListener {
                findNavController().navigate(SettingDetailsFragmentDirections.actionSettingDetailsFragmentToAdviceFragment2())
            }
            themeLinear.setOnClickListener {
                findNavController().navigate(SettingDetailsFragmentDirections.actionSettingDetailsFragmentToThemeDialog())
            }
            logoutLinear.setOnClickListener {
                showDialog(
                    "Are you sure you want to log out?",
                    "Logout",
                    "Cancel",
                    logOutLogic
                )
            }
        }
    }

    private val logOutLogic: () -> Unit = { logOutUser(dataStore = dataStore) }


    private fun showDialog(
        message: String,
        positiveTxt: String,
        negativeTxt: String,
        logic: () -> Unit,
    ) {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogCustom)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(positiveTxt) { _, _ ->
                logic()
            }
            .setNegativeButton(negativeTxt) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()

    }

    private fun setData() {
        lifecycleScope.launch {
            binding.apply {
                if (dataStore.getUserImageUri() != "") {

                    binding.ivUser.setImageBitmap(
                        convertStringToBitmap(dataStore.getUserImageUri())!!,
                    )

                } else {
                    ivUser.setImageResource(R.drawable.ic_person)
                }

                tvUserName.text = dataStore.getUser().fullName.toString()
            }
        }
    }
}