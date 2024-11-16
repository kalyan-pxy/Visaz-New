package com.pxy.visaz.ui.applyvisa

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.pxy.visaz.R
import com.pxy.visaz.core.AppConstants
import com.pxy.visaz.core.PopBackFragment
import com.pxy.visaz.core.model.visa.Country
import com.pxy.visaz.core.model.visa.VisaApplicationBasicDetails
import com.pxy.visaz.core.model.visa.VisaApplicationModel
import com.pxy.visaz.core.utils.Validator
import com.pxy.visaz.databinding.FragmentVisaSubmitFormBinding
import com.pxy.visaz.ui.home.VisaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class VisaSubmitFormFragment : PopBackFragment() {

    private lateinit var binding: FragmentVisaSubmitFormBinding
    private var visaApplicationModel: VisaApplicationModel? = null
    private var selectedDate: String? = null

    private val validator by lazy { Validator() }

    private val visaViewModel: VisaViewModel by viewModel()

    // List of Country objects
    private val countries = listOf(
        Country("India", "+91"),
        Country("United States", "+1"),
        Country("United Kingdom", "+44"),
        Country("Canada", "+1"),
        Country("Australia", "+61"),
        Country("Germany", "+49"),
        Country("France", "+33"),
        Country("Japan", "+81"),
        Country("China", "+86")
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
    }

    private fun initViews() {
        initToolbar()
        initGenderDropDown()
        initCountryDropDown()
        updateVisaDetails()
        initListeners()
    }

    private fun initCountryDropDown() {
        // Adapter for dropdown displaying country names
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown,
            countries.map { it.name } // Display only the name
        )
        binding.etCountry.setAdapter(adapter)
    }

    private fun initGenderDropDown() {
        val priorities = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, priorities)
        binding.etGender.setAdapter(arrayAdapter)
    }

    private fun initListeners() {
        with(binding) {
            btnSubmit.setOnClickListener {
                if (isValid(
                        etMotherName.text.toString(),
                        etFatherName.text.toString(),
                        etGender.text.toString(),
                        etCountry.text.toString()
                    )
                ) {
                    val visaApplicationBasicDetails = VisaApplicationBasicDetails(
                        etSelectedDate.text.toString(),
                        etMotherName.text.toString(),
                        etFatherName.text.toString(),
                        etGender.text.toString(),
                        etCountry.text.toString()
                    )
                    val bundle = arguments ?: Bundle()
                    bundle.putParcelable(
                        AppConstants.EXTRA_VISA_APPLICATION_BASIC_DETAILS,
                        visaApplicationBasicDetails
                    )
                    findNavController().navigate(
                        R.id.action_visaSubmitFormFragment_to_visaImagesUploadFragment,
                        bundle
                    )
                }
            }
        }
    }

    private fun isValid(
        motherName: String,
        fatherName: String,
        gender: String,
        country: String
    ): Boolean {

        with(binding) {
            with(tilMotherName) {
                if (validator.isValidName(motherName)) {
                    error = null
                } else {
                    error = validator.getNameError(motherName)
                    return false
                }
            }

            with(tilFatherName) {
                if (validator.isValidName(fatherName)) {
                    error = null
                } else {
                    error = validator.getNameError(fatherName)
                    return false
                }
            }

            with(tilGender) {
                if (validator.isValidName(gender)) {
                    error = null
                } else {
                    error = validator.getNameError(gender)
                    return false
                }
            }

            with(tilCountry) {
                if (validator.isValidName(country)) {
                    error = null
                } else {
                    error = validator.getNameError(country)
                    return false
                }
            }


        }
        return true
    }

    private fun updateVisaDetails() {
        with(binding) {
            visaApplicationModel?.let {

            }
            selectedDate?.let {
                etSelectedDate.setText(it)
            }
        }
    }

    private fun initToolbar() {
        with(binding.layoutHeader.toolbar) {
            setNavigationOnClickListener {
                goBack()
            }
            title = getString(R.string.label_submit_application)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readIntentData()
    }

    private fun readIntentData() {
        arguments?.let {
            visaApplicationModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable(
                    AppConstants.EXTRA_VISA_DETAILS,
                    VisaApplicationModel::class.java
                )
            } else {
                arguments?.getParcelable(AppConstants.EXTRA_VISA_DETAILS)
            }
            selectedDate = arguments?.getString(AppConstants.EXTRA_SELECTED_DATE)
        }
    }

    private fun initObservers() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVisaSubmitFormBinding.inflate(inflater, container, false)
        return binding.root
    }
}