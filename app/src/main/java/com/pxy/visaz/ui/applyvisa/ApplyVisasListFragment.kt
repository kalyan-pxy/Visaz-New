package com.pxy.visaz.ui.applyvisa

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.pxy.visaz.R
import com.pxy.visaz.core.AppConstants
import com.pxy.visaz.core.PopBackFragment
import com.pxy.visaz.core.extension.copyFileToAppStorage
import com.pxy.visaz.core.extension.showDatePicker
import com.pxy.visaz.core.extension.uriToFilePath
import com.pxy.visaz.core.model.visa.VisaApplicationBasicDetails
import com.pxy.visaz.databinding.FragmentApplyVisasListBinding
import com.pxy.visaz.ui.home.VisaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ApplyVisasListFragment : PopBackFragment() {

    private lateinit var binding: FragmentApplyVisasListBinding
    private var travelerCount = 1
    private var list: ArrayList<VisaApplicationBasicDetails> = arrayListOf()

    private var selectedPosition = 0
    private val visaViewModel: VisaViewModel by viewModel()

    private lateinit var adapter: ApplyVisaListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            btnSubmit.setOnClickListener {
                toast(getString(R.string.visa_application_submitted_successfully))
                findNavController().navigate(R.id.action_applyVisaSubmitFormFragment_to_homeFragment)
            }
        }
    }

    private fun initViews() {
        with(binding) {
            with(layoutHeader.toolbar) {
                setNavigationOnClickListener {
                    goBack()
                }
            }

            for (i in 1..travelerCount) {
                list.add(
                    VisaApplicationBasicDetails(
                        title = getString(R.string.header_traveller, i)
                    )
                )
            }
            initVisaList(list)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readIntentData()
    }

    private fun readIntentData() {
        arguments?.let {
            travelerCount = it.getInt(AppConstants.EXTRA_TRAVELLER_COUNT)
        }
    }

    private fun initVisaList(list: ArrayList<VisaApplicationBasicDetails>) {
        with(binding) {
            adapter = ApplyVisaListAdapter()
            adapter.setApplyVisaListListener(object : ApplyVisaListAdapter.ApplyVisaListListener {
                override fun onProfileImageUploadClick(
                    position: Int,
                    visaModel: VisaApplicationBasicDetails
                ) {
                    selectedPosition = position
                    pickProfileImageFromGallery()
                }

                override fun onPassportImageUploadClick(
                    position: Int,
                    visaModel: VisaApplicationBasicDetails
                ) {
                    selectedPosition = position
                    pickPassportImageFromGallery()
                }

                override fun onSelectDateClick(
                    position: Int,
                    visaModel: VisaApplicationBasicDetails
                ) {
                    selectedPosition = position
                    showDatePicker()
                }
            })
            rvVisas.adapter = adapter
            adapter.submitList(list)
        }
    }

    private fun initObservers() {
        visaViewModel.loaderObserver.observe(viewLifecycleOwner) {
            it?.let { loading ->
                if (loading) {
                    showProgressDialog()
                } else {
                    hideProgressDialog()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApplyVisasListBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Register for activity result to get an image from gallery
    private val pickProfileImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { imageUri ->
                list[selectedPosition].apply {
                    profileImageUri = uriToFilePath(imageUri)?.let {
                        copyFileToAppStorage(
                            requireContext(),
                            it,
                            "${this.title.orEmpty()}_profile"
                        )
                    }
                }
                adapter.submitList(list)
                adapter.notifyItemChanged(selectedPosition)
            }
        }

    private fun pickProfileImageFromGallery() {
        pickProfileImage.launch("image/*")  // Launch gallery for selecting images
    }

    // Register for activity result to get an image from gallery
    private val pickPassportImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { imageUri ->
                list[selectedPosition].apply {
                    passportImageUri = uriToFilePath(imageUri)?.let {
                        copyFileToAppStorage(
                            requireContext(),
                            it,
                            "${this.title.orEmpty()}_passport"
                        )
                    }
                }
                adapter.submitList(list)
                adapter.notifyItemChanged(selectedPosition)
            }
        }

    private fun pickPassportImageFromGallery() {
        pickPassportImage.launch("image/*")  // Launch gallery for selecting images
    }

    private fun showDatePicker() {
        showDatePicker {
            list[selectedPosition].apply {
                selectedDate = it
            }
            adapter.submitList(list)
            adapter.notifyItemChanged(selectedPosition)
        }
    }
}