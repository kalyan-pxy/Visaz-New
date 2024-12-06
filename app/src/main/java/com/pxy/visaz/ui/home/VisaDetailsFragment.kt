package com.pxy.visaz.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.pxy.visaz.R
import com.pxy.visaz.core.utils.AppConstants
import com.pxy.visaz.core.PopBackFragment
import com.pxy.visaz.core.extension.forEachApply
import com.pxy.visaz.core.extension.formatPrice
import com.pxy.visaz.core.extension.loadImage
import com.pxy.visaz.core.model.visa.VisaApplicationModel
import com.pxy.visaz.core.model.visa.VisaType
import com.pxy.visaz.databinding.FragmentVisaDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class VisaDetailsFragment : PopBackFragment() {

    private lateinit var binding: FragmentVisaDetailsBinding
    private var visaApplicationModel: VisaApplicationModel? = null

    private val visaViewModel: VisaViewModel by viewModel()
    private lateinit var visaTypeListAdapter: VisaTypeListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
    }

    private fun initViews() {
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
                val bundle = arguments ?: Bundle()
                bundle.putInt(AppConstants.EXTRA_TRAVELLER_COUNT, visaViewModel.getTravellerCount())
                findNavController().navigate(
                    R.id.action_visaDetailsFragment_to_visaSubmitFormFragment,
                    bundle
                )
            }
        }
    }

    private fun updateVisaDetails() {
        with(binding) {
            ivBack.setOnClickListener {
                goBack()
            }
            visaApplicationModel?.let {
                ivCountyHeader.loadImage(it.bannerImageUrl)
                tvHeader.text = it.name
                tvInfoMessage.text =
                    getString(R.string.label_visa_info, getString(R.string.app_name), it.name)
                tvVisaDescription.text = it.description
                //tvVisaOnDate.text = getString(R.string.label_visa_on, it.processingTime)
                //tvVisaPrice.text = it.applicationFee.visaFee.formatPrice()
                //tvOurPrice.text = it.applicationFee.companyFeeActual.formatPrice()
                /*tvPriceNote.text = getString(
                    R.string.label_visa_price_note,
                    it.applicationFee.companyFeeActual.formatPrice()
                )*/
                initVisaTypes(it.visaTypes)
            }
        }
    }

    private fun initVisaTypes(visaTypes: List<VisaType>?) {
        visaTypes?.let {
            if (it.isNotEmpty()) {
                visaViewModel.selectVisaType(it.first())
                it.first().isSelected = true
            }
            with(binding.rgVisaType) {
                visaTypeListAdapter = VisaTypeListAdapter()
                visaTypeListAdapter.setVisaTypeListListener(object :
                    VisaTypeListAdapter.VisaTypeListListener {
                    override fun onItemClick(position: Int, visaType: VisaType) {
                        visaViewModel.selectVisaType(visaType)
                        visaTypes.forEachApply {
                            isSelected = false
                        }
                        visaType.isSelected = true
                        visaTypeListAdapter.submitList(visaTypes)
                        visaTypeListAdapter.notifyDataSetChanged()
                    }
                })
                adapter = visaTypeListAdapter
                visaTypeListAdapter.submitList(visaTypes)
            }
        }
    }

    /*private fun initVisaTypes() {
        val items = listOf("Option 1", "Option 2", "Option 3")
        with(binding.rgVisaType) {
            items.forEachIndexed { _, text ->
                val customItem = LayoutInflater.from(context)
                    .inflate(R.layout.radio_visa_type, this, false)
                val radioButton = customItem.findViewById<RadioButton>(R.id.radio_button)
                val icon = customItem.findViewById<ImageView>(R.id.icon)

                radioButton.text = text
                icon.setImageResource(R.drawable.ic_launcher_foreground) // Set your icon dynamically

                // Ensure only one RadioButton is checked at a time
                radioButton.setOnClickListener {
                    clearCheck()
                    radioButton.isChecked = true
                }

                addView(customItem)
            }

            setOnCheckedChangeListener { group, checkedId ->
                val selectedButton = group.findViewById<RadioButton>(checkedId)
                selectedButton?.let {
                    toast("Selected: ${it.text}")
                }
            }
        }
    }*/

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
            calculatePrice(
                it,
                visaViewModel.selectedVisaTypeObserver.value?.fees?.applicationFee ?: 0,
                visaViewModel.selectedVisaTypeObserver.value?.fees?.serviceFee ?: 0
            )
        }
        visaViewModel.selectedVisaTypeObserver.observe(viewLifecycleOwner) {
            calculatePrice(
                visaViewModel.totalTravelers.value ?: 0,
                it?.fees?.applicationFee ?: 0,
                it?.fees?.serviceFee ?: 0
            )
        }
    }

    private fun calculatePrice(travellers: Int, applicationFee: Int, serviceFee: Int) {
        with(binding) {

            tvVisaPriceTitle.text = getString(R.string.label_visa_fee, travellers)
            tvOurPriceTitle.text = getString(R.string.label_our_fee, travellers)

            tvVisaPrice.text = (applicationFee * travellers).formatPrice()
            tvOurPrice.text = (serviceFee * travellers).formatPrice()
            val totalPrice = (applicationFee * travellers) + (serviceFee * travellers)
            tvTotalPrice.text = totalPrice.formatPrice()
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