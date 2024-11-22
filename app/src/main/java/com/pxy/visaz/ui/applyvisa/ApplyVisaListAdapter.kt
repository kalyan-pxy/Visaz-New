package com.pxy.visaz.ui.applyvisa

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pxy.visaz.core.model.visa.VisaApplicationBasicDetails
import com.pxy.visaz.databinding.ItemApplyVisaBinding

class ApplyVisaListAdapter :
    ListAdapter<VisaApplicationBasicDetails, ApplyVisaListAdapter.ItemViewHolder>(ItemDiffCallback()) {

    private var listener: ApplyVisaListListener? = null

    interface ApplyVisaListListener {
        fun onProfileImageUploadClick(position: Int, visaModel: VisaApplicationBasicDetails)
        fun onPassportImageUploadClick(position: Int, visaModel: VisaApplicationBasicDetails)
        fun onSelectDateClick(position: Int, visaModel: VisaApplicationBasicDetails)
    }

    fun setApplyVisaListListener(listener: ApplyVisaListListener) {
        this.listener = listener
    }

    inner class ItemViewHolder(private val binding: ItemApplyVisaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: VisaApplicationBasicDetails, position: Int) {
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

/*

open class ApplyVisaListAdapter(
    private val visaModels: ArrayList<VisaApplicationBasicDetails>,
    private val listener: ApplyVisaListListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var rowBinding: ItemApplyVisaBinding

    interface ApplyVisaListListener {
        fun onProfileImageUploadClick(position: Int, visaModel: VisaApplicationBasicDetails)
        fun onPassportImageUploadClick(position: Int, visaModel: VisaApplicationBasicDetails)
    }

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        rowBinding =
            ItemApplyVisaBinding.inflate(
                LayoutInflater.from(container.context),
                container,
                false
            )
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return visaModels.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(rowBinding) {
            visaModels[position].let { visaModel ->
                visaApplicationCardView.setVisaModel(visaModel)
                visaApplicationCardView.onProfileImageClick {
                    listener.onProfileImageUploadClick(position, visaModel)
                }
                visaApplicationCardView.onPassportImageClick {
                    listener.onPassportImageUploadClick(position, visaModel)
                }
            }
        }
    }

    fun updateProfileImage(selectedPosition: Int, updatedModel: VisaApplicationBasicDetails) {
        visaModels[selectedPosition] = updatedModel
        notifyItemChanged(selectedPosition)
    }

    fun updatePassportImage(selectedPosition: Int, updatedModel: VisaApplicationBasicDetails) {
        visaModels[selectedPosition] = updatedModel
        notifyItemChanged(selectedPosition)
    }

    inner class ViewHolder(binding: ItemApplyVisaBinding) :
        RecyclerView.ViewHolder(binding.root)

}*/
