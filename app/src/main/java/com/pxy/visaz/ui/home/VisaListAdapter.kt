package com.bluboy.android.ui.support

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pxy.visaz.R
import com.pxy.visaz.core.extension.loadImage
import com.pxy.visaz.core.model.visa.VisaApplicationModel
import com.pxy.visaz.databinding.HomeVisaListItemBinding

open class VisaListAdapter(
    private val visaModels: List<VisaApplicationModel>,
    private val onItemClickListener: (VisaApplicationModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: HomeVisaListItemBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        rowBinding =
            HomeVisaListItemBinding.inflate(
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
                visaImage.loadImage(visaModel.imageUrl.orEmpty())
                visaTitle.text = "${visaModel.visaType} - ${visaModel.country}"
                visaSubTitle.apply {
                    text = context.getString(R.string.label_get_on, visaModel.getOnDate)
                }
                root.setOnClickListener {
                    onItemClickListener.invoke(visaModel)
                }
            }
        }
    }

    inner class ViewHolder(binding: HomeVisaListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}