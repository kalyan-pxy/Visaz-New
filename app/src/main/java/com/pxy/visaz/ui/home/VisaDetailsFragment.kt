package com.pxy.visaz.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.pxy.visaz.R
import com.pxy.visaz.core.AppConstants
import com.pxy.visaz.core.PopBackFragment
import com.pxy.visaz.core.extension.formatPrice
import com.pxy.visaz.core.extension.loadImage
import com.pxy.visaz.core.extension.showDatePicker
import com.pxy.visaz.core.model.visa.VisaApplicationModel
import com.pxy.visaz.databinding.FragmentVisaDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class VisaDetailsFragment : PopBackFragment() {

    private lateinit var binding: FragmentVisaDetailsBinding
    private var visaApplicationModel: VisaApplicationModel? = null

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
            ivAddTravellers.setOnClickListener {
                visaViewModel.addTraveller()
            }
            ivRemoveTravellers.setOnClickListener {
                visaViewModel.removeTraveller()
            }
            btnStartVisa.setOnClickListener {
                showDatePicker {
                    toast("Selected date: $it")
                    val bundle = arguments ?: Bundle()
                    bundle.putString(AppConstants.EXTRA_SELECTED_DATE, it)
                    findNavController().navigate(
                        R.id.action_visaDetailsFragment_to_visaSubmitFormFragment,
                        bundle
                    )
                }
            }
        }
    }

    private fun updateVisaDetails() {
        with(binding) {
            visaApplicationModel?.let {
                ivCountyHeader.loadImage(it.imageUrl)
                tvInfoMessage.text =
                    getString(R.string.label_visa_info, getString(R.string.app_name), it.country)
                tvVisaDescription.text = it.description
                tvVisaOnDate.text = getString(R.string.label_visa_on, it.processingTime)
                tvVisaPriceTitle.text = getString(R.string.label_visa_fee, "1")
                tvOurPriceTitle.text = getString(R.string.label_our_fee, "1")
                tvVisaPrice.text = it.applicationFee.visaFee.formatPrice()
                tvOurPrice.text = it.applicationFee.companyFeeActual.formatPrice()
                tvPriceNote.text = getString(
                    R.string.label_visa_price_note,
                    it.applicationFee.companyFeeActual.formatPrice()
                )
            }
        }
    }

    private fun initToolbar() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                goBack()
            }
            var isShow = true
            var scrollRange = -1
            appbar.addOnOffsetChangedListener { barLayout, verticalOffset ->
                if (scrollRange == -1) {
                    scrollRange = barLayout?.totalScrollRange!!
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbar.title = visaApplicationModel?.country
                    isShow = true
                    tvHeader.isVisible = false
                } else if (isShow) {
                    toolbar.title = ""
                    isShow = false
                    tvHeader.text = visaApplicationModel?.country
                    tvHeader.isVisible = true
                }
            }
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
        }
    }

    private fun initObservers() {
        visaViewModel.totalTravelers.observe(viewLifecycleOwner) {
            binding.tvTotalTravellers.text = it.toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVisaDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
}