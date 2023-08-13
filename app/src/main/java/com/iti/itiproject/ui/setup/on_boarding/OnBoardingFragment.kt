package com.iti.itiproject.ui.setup.on_boarding

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.iti.itiproject.R
import com.iti.itiproject.data.datastore.DataStoreImplementation
import com.iti.itiproject.databinding.FragmentOnBoardingBinding
import com.ramotion.paperonboarding.PaperOnboardingFragment
import com.ramotion.paperonboarding.PaperOnboardingPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class OnBoardingFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardingBinding
    private lateinit var paperOnBoardingFragment: PaperOnboardingFragment
    private lateinit var fragmentTransaction: FragmentTransaction
    private var fragmentManager: FragmentManager? = null

    @Inject
    lateinit var dataStore: DataStoreImplementation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStore = DataStoreImplementation(appContext = context)
        GlobalScope.launch(Dispatchers.Main) {
            if (dataStore.isPassedOnBoarding()) {
                findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardingBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            setUpOnBoarding()
        }
        setOnclickListener()
    }

    private fun setOnclickListener() {
        binding.tvSkip.setOnClickListener {
            lifecycleScope.launch {
                dataStore.setPassedOnBoarding(true)
                findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
            }
        }
    }

    private fun setUpOnBoarding() {
        fragmentManager = requireActivity().supportFragmentManager

        paperOnBoardingFragment = PaperOnboardingFragment.newInstance(getDataForOnBoarding())
        fragmentTransaction = fragmentManager!!.beginTransaction()

        fragmentTransaction.add(
            R.id.frame_layout_main,
            paperOnBoardingFragment
        )

        fragmentTransaction.commit()
    }


    private fun getDataForOnBoarding(): ArrayList<PaperOnboardingPage>? {

        val source = PaperOnboardingPage(
            "Coronavirus Protection",
            "The appropriate vaccine for corona around the world and recovery rates",
            Color.parseColor("#28C2AD"),
            R.drawable.on_boarding_1,
            R.drawable.on_boarding_1
        )
        val source1 = PaperOnboardingPage(
            "Welcome to \n Corona and the world",
            "Corona around the world, statistics of cities and countries of recovery, infection and death",
            Color.parseColor("#8F89F6"),
            R.drawable.on_boarding_2,
            R.drawable.on_boarding_2
        )
        val source2 = PaperOnboardingPage(
            "Coronavirus Tips",
            "Tips for avoiding corona infection and dealing with infected people",
            Color.parseColor("#7497F8"),
            R.drawable.on_boarding_3,
            R.drawable.on_boarding_3
        )

        val elements: ArrayList<PaperOnboardingPage> = ArrayList()

        elements.add(source1)
        elements.add(source)
        elements.add(source2)
        return elements
    }

}