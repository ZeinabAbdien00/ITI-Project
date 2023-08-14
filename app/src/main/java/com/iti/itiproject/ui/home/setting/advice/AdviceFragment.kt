package com.iti.itiproject.ui.home.setting.advice

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.iti.itiproject.R
import com.iti.itiproject.databinding.FragmentAdviceBinding
import com.ramotion.paperonboarding.PaperOnboardingFragment
import com.ramotion.paperonboarding.PaperOnboardingPage

class AdviceFragment : DialogFragment() {

    private lateinit var binding: FragmentAdviceBinding
    private lateinit var paperOnBoardingFragment: PaperOnboardingFragment
    private lateinit var fragmentTransaction: FragmentTransaction
    private var fragmentManager: FragmentManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAdviceBinding.inflate(layoutInflater)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requireActivity().setFinishOnTouchOutside(false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels

        this.dialog!!.window!!.setLayout(((9 * width) / 10), (9 * height) / 10)

        setUpOnBoarding()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnAdviceBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpOnBoarding() {

        fragmentManager = childFragmentManager

        paperOnBoardingFragment = PaperOnboardingFragment.newInstance(getDataForOnBoarding())
        fragmentTransaction = fragmentManager!!.beginTransaction()

        fragmentTransaction.add(
            R.id.frame_layout_main,
            paperOnBoardingFragment
        )

        fragmentTransaction.commit()
    }


    private fun getDataForOnBoarding(): ArrayList<PaperOnboardingPage> {

        val source0 = PaperOnboardingPage(
            "Coronavirus Protection",
            "Wear a suitable mask when you are in a poorly ventilated area.",
            Color.parseColor("#e14285"),
            R.drawable.ic_covid_in_air,
            R.drawable.ic_covid_in_air
        )
        val source1 = PaperOnboardingPage(
            "Covid Symptoms",
            "If you have a fever, cough and difficulty breathing, seek medical attention immediately",
            Color.parseColor("#e3e3ea"),
            R.drawable.ic_covid_symptoms,
            R.drawable.ic_covid_symptoms
        )
        val source2 = PaperOnboardingPage(
            "Wash Hands",
            "Clean your hands regularly and wash them with soap and water.",
            Color.parseColor("#d1f0fc"),
            R.drawable.ic_wash_hand,
            R.drawable.ic_wash_hand
        )
        val source3 = PaperOnboardingPage(
            "Using Alcohol",
            "Regularly use an alcohol-based hand sanitizer when touching anything",
            Color.parseColor("#b7cde6"),
            R.drawable.ic_using_alcohol,
            R.drawable.ic_using_alcohol
        )
        val source4 = PaperOnboardingPage(
            "Stay Home",
            "During isolation or group siege to preserve your safety and the safety of others",
            Color.parseColor("#E18D44"),
            R.drawable.ic_work_from_home,
            R.drawable.ic_work_from_home
        )
        val source5 = PaperOnboardingPage(
            "Temperature Measurement",
            "To find out if you have been infected with Corona, measure the temperature daily",
            Color.parseColor("#7497F8"),
            R.drawable.ic_temperature_control,
            R.drawable.ic_temperature_control
        )
        val source6 = PaperOnboardingPage(
            "Required Distance",
            "Maintain a distance of at least one meter from others for your safety",
            Color.parseColor("#d1f0fc"),
            R.drawable.ic_spase_svg,
            R.drawable.ic_spase_svg
        )
        val source8 = PaperOnboardingPage(
            "Medical Rules",
            "Get vaccinated as soon as it's your turn and stick to local vaccination guidelines",
            Color.parseColor("#8F89F6"),
            R.drawable.ic_medical_rolls,
            R.drawable.ic_medical_rolls
        )

        val elements: ArrayList<PaperOnboardingPage> = ArrayList()

        elements.add(source0)
        elements.add(source1)
        elements.add(source2)
        elements.add(source3)
        elements.add(source4)
        elements.add(source5)
        elements.add(source6)
        elements.add(source8)
        return elements
    }

}