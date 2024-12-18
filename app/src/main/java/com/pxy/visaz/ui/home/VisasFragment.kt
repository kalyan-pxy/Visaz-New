package com.pxy.visaz.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bluboy.android.ui.support.VisaListAdapter
import com.pxy.visaz.R
import com.pxy.visaz.core.utils.AppConstants
import com.pxy.visaz.core.BaseFragment
import com.pxy.visaz.core.extension.initBackNavigationHandler
import com.pxy.visaz.core.model.visa.VisaApplicationModel
import com.pxy.visaz.databinding.FragmentVisasBinding
import com.pxy.visaz.ui.authentication.PreAuthActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class VisasFragment : BaseFragment() {

    private lateinit var binding: FragmentVisasBinding
    private var doubleBackToExitPressedOnce = false
    private var userName: String? = null

    private val visaViewModel: VisaViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initListeners()
        with(binding) {
            layoutHeader.tvTitle.text = getString(R.string.label_visa_list_title)
            requireActivity().initBackNavigationHandler {
                handleBackPress()
            }
        }
    }

    private fun initListeners() {
        with(binding) {
            tilSearch.setEndIconOnClickListener {
                val searchText = binding.etSearch.text
                // Handle search logic here
                toast("Searching for: $searchText")
                visaViewModel.searchVisas(searchText.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readIntentData()
    }

    private fun readIntentData() {
        arguments?.let {
            userName = it.getString(AppConstants.EXTRA_USER_NAME)
        }
    }

    private fun initVisaList(list: List<VisaApplicationModel>) {
        with(binding) {
            val adpate = VisaListAdapter(list) {
                findNavController().navigate(R.id.action_visasFragment_to_details,
                    Bundle().apply {
                        putParcelable(AppConstants.EXTRA_VISA_DETAILS, it)
                    })
            }
            rvVisas.adapter = adpate
        }
    }

    private fun initObservers() {
        visaViewModel.logoutObserver.observe(viewLifecycleOwner) {
            if (it) {
                startActivity(
                    Intent(requireContext(), PreAuthActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }
        }
        visaViewModel.visasObserver.observe(viewLifecycleOwner) {
            it?.let {
                initVisaList(it)
            }
        }
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
        binding = FragmentVisasBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun handleBackPress() {
        if (doubleBackToExitPressedOnce) {
            requireActivity().finish()
            return
        }
        this.doubleBackToExitPressedOnce = true
        toast(getString(R.string.back_to_exit_message))
        Handler(Looper.getMainLooper()).postDelayed(
            { doubleBackToExitPressedOnce = false },
            AppConstants.BACK_BUTTON_DELAY_TIMER
        )
    }
}