package com.pxy.visaz.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pxy.visaz.R
import com.pxy.visaz.core.model.visa.VisaType
import com.pxy.visaz.databinding.ItemVisaTypeBinding

class VisaTypeListAdapter :
    ListAdapter<VisaType, VisaTypeListAdapter.ItemViewHolder>(VisaTypeItemDiffCallback()) {

    private var listener: VisaTypeListListener? = null

    interface VisaTypeListListener {
        fun onItemClick(position: Int, visaType: VisaType)
    }

    fun setVisaTypeListListener(listener: VisaTypeListListener) {
        this.listener = listener
    }

    inner class ItemViewHolder(private val binding: ItemVisaTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(visaType: VisaType, position: Int) {
            with(binding) {
                ctvVisaType.text = visaType.type
                ctvVisaDescription.text = visaType.description
                ctvVisaProcessingTime.text = visaType.processingTime
                clItem.setOnClickListener {
                    if (!visaType.isSelected) {
                        listener?.onItemClick(position, visaType)
                    }
                }
                if (visaType.isSelected) {
                    clItem.background = AppCompatResources.getDrawable(
                        clItem.context,
                        R.drawable.item_visa_type_background_selected
                    )
                } else {
                    clItem.background = AppCompatResources.getDrawable(
                        clItem.context,
                        R.drawable.item_visa_type_background_default
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemVisaTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}
/*
open class VisaTypeListAdapter(
    private val visaTypes: List<VisaType>,
    private val onItemClickListener: (VisaType) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var rowBinding: ItemVisaTypeBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        rowBinding =
            ItemVisaTypeBinding.inflate(
                LayoutInflater.from(container.context),
                container,
                false
            )
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return visaTypes.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(rowBinding) {
            visaTypes[position].let { visaType ->
                ctvVisaType.text = visaType.type
                ctvVisaDescription.text = visaType.description
                ctvVisaProcessingTime.text = visaType.processingTime
                clItem.setOnClickListener {
                    if (!visaType.isSelected) {
                        onItemClickListener.invoke(visaType)
                        visaTypes.forEachApply {
                            isSelected = false
                        }
                        visaType.isSelected = true
                        notifyDataSetChanged()
                    }
                }
                if (visaType.isSelected) {
                    clItem.background = AppCompatResources.getDrawable(
                        clItem.context,
                        R.drawable.item_visa_type_background_selected
                    )
                } else {
                    clItem.background = AppCompatResources.getDrawable(
                        clItem.context,
                        R.drawable.item_visa_type_background_default
                    )
                }
            }
        }
    }

    fun updateVisaType(visaType: VisaType?) {
        */
/*visaTypes.forEachApply {
            isSelected = false
        }
        visaTypes.find { it.type == visaType?.type }?.apply {
            isSelected = true
        }
        notifyDataSetChanged()*//*

    }

    inner class ViewHolder(binding: ItemVisaTypeBinding) :
        RecyclerView.ViewHolder(binding.root)

}*/
