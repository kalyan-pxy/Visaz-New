package com.pxy.visaz.ui.applyvisa

import androidx.recyclerview.widget.DiffUtil
import com.pxy.visaz.core.model.visa.VisaApplicationBasicDetails

class ItemDiffCallback : DiffUtil.ItemCallback<VisaApplicationBasicDetails>() {
    override fun areItemsTheSame(
        oldItem: VisaApplicationBasicDetails,
        newItem: VisaApplicationBasicDetails
    ): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(
        oldItem: VisaApplicationBasicDetails,
        newItem: VisaApplicationBasicDetails
    ): Boolean {
        return oldItem == newItem
    }
}

