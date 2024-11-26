package com.pxy.visaz.ui.applyvisa

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.children
import com.pxy.visaz.R
import com.pxy.visaz.core.AppConstants
import com.pxy.visaz.core.PopBackFragment
import com.pxy.visaz.core.customview.VisaApplicationCardView
import com.pxy.visaz.core.extension.copyFileToAppStorage
import com.pxy.visaz.core.extension.showDatePicker
import com.pxy.visaz.core.extension.uriToFilePath
import com.pxy.visaz.core.model.visa.VisaApplicationDetails
import com.pxy.visaz.core.model.visa.VisaApplicationModel
import com.pxy.visaz.databinding.FragmentApplyVisasList1Binding
import com.pxy.visaz.ui.home.VisaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ApplyVisasListFragment1 : PopBackFragment() {

    private lateinit var binding: FragmentApplyVisasList1Binding
    private var travelerCount = 1
    private var list: ArrayList<VisaApplicationDetails> = arrayListOf()

    private val visaViewModel: VisaViewModel by viewModel()
    private var selectedCardView: VisaApplicationCardView? = null

    private var visaApplicationModel: VisaApplicationModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            btnSubmit.setOnClickListener {
                val updatedList: ArrayList<VisaApplicationDetails> = arrayListOf()
                llContainer.children.forEach {
                    if (it is VisaApplicationCardView) {
                        val model = it.getVisaModel()
                        if (model == null) {
                            //toast(getString(R.string.error_visa_input))
                            return@setOnClickListener
                        } else {
                            updatedList.add(model)
                        }
                    }
                }
                Log.e("visa list", "===>>> updated list: $updatedList")
                visaApplicationModel?.let {
                    val visaType = it.visaTypes?.get(0)?.type ?: ""
                    visaViewModel.submitVisaApplication(it.id, visaType, updatedList)
                }
                //toast(getString(R.string.visa_application_submitted_successfully))
                //findNavController().navigate(R.id.action_applyVisaSubmitFormFragment_to_homeFragment)
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
                    VisaApplicationDetails(
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
            visaApplicationModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable(
                    AppConstants.EXTRA_VISA_DETAILS,
                    VisaApplicationModel::class.java
                )
            } else {
                arguments?.getParcelable(AppConstants.EXTRA_VISA_DETAILS)
            }
        }
    }

    private fun initVisaList(list: ArrayList<VisaApplicationDetails>) {
        with(binding) {
            list.forEach {
                val visaApplicationCardView = VisaApplicationCardView(requireContext())
                visaApplicationCardView.setVisaModel(it)
                visaApplicationCardView.onProfileImageClick {
                    selectedCardView = visaApplicationCardView
                    pickProfileImage.launch("image/*")
                }
                visaApplicationCardView.onPassportImageClick {
                    selectedCardView = visaApplicationCardView
                    pickPassportImage.launch("image/*")
                }
                visaApplicationCardView.onDateSelectClick {
                    selectedCardView = visaApplicationCardView
                    showDatePicker {
                        visaApplicationCardView.setSelectDate(it)
                    }
                }
                llContainer.addView(visaApplicationCardView)
            }
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
        binding = FragmentApplyVisasList1Binding.inflate(inflater, container, false)
        return binding.root
    }

    // Register for activity result to get an image from gallery
    private val pickProfileImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { imageUri ->
                selectedCardView?.let { cardView ->
                    val profileImageUri = uriToFilePath(imageUri)?.let {
                        copyFileToAppStorage(
                            requireContext(),
                            it,
                            cardView.getProfileImageName()
                        )
                    }
                    cardView.setProfileImageUri(profileImageUri)
                }
            }
        }

    private val pickPassportImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { imageUri ->
                selectedCardView?.let { cardView ->
                    val passportImageUri = uriToFilePath(imageUri)?.let {
                        copyFileToAppStorage(
                            requireContext(),
                            it,
                            cardView.getPassportImageName()
                        )
                    }
                    cardView.setPassportImageUri(passportImageUri)
                }
            }
        }

    private fun pickPassportImageFromGallery() {
        pickPassportImage.launch("image/*")  // Launch gallery for selecting images
    }
}