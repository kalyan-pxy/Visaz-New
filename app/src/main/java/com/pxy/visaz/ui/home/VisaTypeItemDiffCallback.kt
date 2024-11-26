package com.pxy.visaz.ui.home

import androidx.recyclerview.widget.DiffUtil
import com.pxy.visaz.core.model.visa.VisaType

class VisaTypeItemDiffCallback : DiffUtil.ItemCallback<VisaType>() {
    override fun areItemsTheSame(
        oldItem: VisaType,
        newItem: VisaType
    ): Boolean {
        return oldItem.type == newItem.type
    }

    override fun areContentsTheSame(
        oldItem: VisaType,
        newItem: VisaType
    ): Boolean {
        return oldItem == newItem
    }
}

