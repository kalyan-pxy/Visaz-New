package com.pxy.visaz.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.pxy.visaz.R
import com.pxy.visaz.core.AppConstants
import com.pxy.visaz.databinding.DialogObservationEntryBinding
import com.pxy.visaz.domain.model.InspectionDetails

class ObservationEntryDialog : DialogFragment() {
    private lateinit var binding: DialogObservationEntryBinding

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
            inspectionDetails: InspectionDetails,
            positiveListener: ((InspectionDetails) -> Unit?)? = null,
            negativeListener: (() -> Unit?)? = null
        ) {
            val generalAppDialogFragment = ObservationEntryDialog().apply {
                arguments = bundleOf(
                    AppConstants.EXTRA_INSPECTION_DETAILS to inspectionDetails
                )
            }
            generalAppDialogFragment.positiveListener = positiveListener
            generalAppDialogFragment.negativeListener = negativeListener
            generalAppDialogFragment.show(
                fragmentManager,
                ObservationEntryDialog::class.java.simpleName
            )
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            manager.beginTransaction().apply {
                add(this@ObservationEntryDialog, tag)
                commitAllowingStateLoss()
            }
        } catch (e: IllegalStateException) {
            e.run { printStackTrace() }
        }
    }

    private var positiveListener: ((InspectionDetails) -> Unit?)? = null
    private var negativeListener: (() -> Unit?)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme)
        binding = DialogObservationEntryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogSlideAnimation
        val inspectionDetails = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(
                AppConstants.EXTRA_INSPECTION_DETAILS,
                InspectionDetails::class.java
            )
        } else {
            arguments?.getParcelable(AppConstants.EXTRA_INSPECTION_DETAILS)
        }
        with(binding) {
            inspectionDetails?.let {
                dialog?.setCancelable(false)
                etObservation.setText(it.observation)
                etRemarks.setText(it.remarks)
                tvDescription.text =
                    getString(R.string.observation_dialog_description, it.details, it.required)
                btnSubmit.setOnClickListener {
                    val inspectionDetails = inspectionDetails.copy(
                        observation = etObservation.text.toString(),
                        remarks = etRemarks.text.toString()
                    )
                    positiveListener?.invoke(inspectionDetails)
                    dismiss()
                }
                btnCancel.setOnClickListener {
                    negativeListener?.invoke()
                    dismiss()
                }
            }
        }
    }
}