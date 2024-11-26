package com.pxy.visaz.ui.applyvisa

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.pxy.visaz.R
import com.pxy.visaz.core.AppConstants
import com.pxy.visaz.core.PopBackFragment
import com.pxy.visaz.core.model.visa.VisaApplicationDetails
import com.pxy.visaz.core.model.visa.VisaApplicationModel
import com.pxy.visaz.databinding.FragmentVisaImagesUploadBinding
import com.pxy.visaz.ui.home.VisaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class VisaImagesUploadFragment : PopBackFragment() {

    private lateinit var binding: FragmentVisaImagesUploadBinding
    private var visaApplicationModel: VisaApplicationModel? = null
    private var visaApplicationBasicDetails: VisaApplicationDetails? = null

    private var selectedProfileImageUri: Uri? = null
    private var selectedPassportImageUri: Uri? = null

    private val visaViewModel: VisaViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
    }

    private fun initViews() {
        initToolbar()
        updateVisaDetails()
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            btnSubmit.setOnClickListener {
                toast("Visa application submitted successfully")
                findNavController().navigate(R.id.action_visaImagesUploadFragment_to_homeFragment)
            }
            btnProfileImageUpload.setOnClickListener {
                pickProfileImageFromGallery()
            }
            btnPassportImageUpload.setOnClickListener {
                pickPassportImageFromGallery()
            }
        }
    }

    private fun updateVisaDetails() {
        with(binding) {
            visaApplicationModel?.let {

            }
            visaApplicationBasicDetails?.let {
                tvVisaBasicDetails.text = getString(
                    R.string.visa_basic_details_display,
                    it.selectedDate,
                    it.motherName,
                    it.fatherName,
                    it.gender,
                    it.country
                )
            }
        }
    }

    private fun initToolbar() {
        with(binding.layoutHeader.toolbar) {
            setNavigationOnClickListener {
                goBack()
            }
            title = getString(R.string.label_upload_visa_required_images)
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
            visaApplicationBasicDetails =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arguments?.getParcelable(
                        AppConstants.EXTRA_VISA_APPLICATION_BASIC_DETAILS,
                        VisaApplicationDetails::class.java
                    )
                } else {
                    arguments?.getParcelable(AppConstants.EXTRA_VISA_APPLICATION_BASIC_DETAILS)
                }
        }
    }

    private fun initObservers() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVisaImagesUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Register for activity result to get an image from gallery
    private val pickProfileImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedProfileImageUri = it
                with(binding.ivProfilePreview) {
                    setImageURI(it)
                    visibility = View.VISIBLE
                }
            }
        }

    private fun pickProfileImageFromGallery() {
        pickProfileImage.launch("image/*")  // Launch gallery for selecting images
    }

    // Register for activity result to get an image from gallery
    private val pickPassportImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedPassportImageUri = it
                with(binding.ivPassportPreview) {
                    setImageURI(it)
                    visibility = View.VISIBLE
                }
            }
        }

    private fun pickPassportImageFromGallery() {
        pickPassportImage.launch("image/*")  // Launch gallery for selecting images
    }

}