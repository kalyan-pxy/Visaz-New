package com.pxy.visaz.ui.applyvisa

import androidx.recyclerview.widget.DiffUtil
import com.pxy.visaz.core.model.visa.VisaApplicationDetails

class ItemDiffCallback : DiffUtil.ItemCallback<VisaApplicationDetails>() {
    override fun areItemsTheSame(
        oldItem: VisaApplicationDetails,
        newItem: VisaApplicationDetails
    ): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(
        oldItem: VisaApplicationDetails,
        newItem: VisaApplicationDetails
    ): Boolean {
        return oldItem == newItem
    }
}

