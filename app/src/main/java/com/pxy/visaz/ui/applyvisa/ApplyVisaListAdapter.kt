package com.pxy.visaz.ui.applyvisa

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pxy.visaz.core.model.visa.VisaApplicationDetails
import com.pxy.visaz.databinding.ItemApplyVisaBinding

class ApplyVisaListAdapter :
    ListAdapter<VisaApplicationDetails, ApplyVisaListAdapter.ItemViewHolder>(ItemDiffCallback()) {

    private var listener: ApplyVisaListListener? = null

    interface ApplyVisaListListener {
        fun onProfileImageUploadClick(position: Int, visaModel: VisaApplicationDetails)
        fun onPassportImageUploadClick(position: Int, visaModel: VisaApplicationDetails)
        fun onSelectDateClick(position: Int, visaModel: VisaApplicationDetails)
    }

    fun setApplyVisaListListener(listener: ApplyVisaListListener) {
        this.listener = listener
    }

    inner class ItemViewHolder(private val binding: ItemApplyVisaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: VisaApplicationDetails, position: Int) {
            with(binding) {
                visaApplicationCardView.setVisaModel(item)
                visaApplicationCardView.onProfileImageClick {
                    listener?.onProfileImageUploadClick(position, item)
                }
                visaApplicationCardView.onPassportImageClick {
                    listener?.onPassportImageUploadClick(position, item)
                }
                visaApplicationCardView.onDateSelectClick {
                    listener?.onSelectDateClick(position, item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemApplyVisaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}

