package com.pxy.visaz.core.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.pxy.visaz.R
import com.pxy.visaz.core.extension.loadImageFromFilePath
import com.pxy.visaz.databinding.ViewYesNoDocUploadBinding

class ViewYesNoDocUpload @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding =
        ViewYesNoDocUploadBinding.inflate(LayoutInflater.from(context), this, true)

    private var docName = ""

    fun setData(header: String, docHeader: String) {
        with(binding) {
            tvOptionHeader.text = header
            tvDocHeader.text = docHeader

            rgOption.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rbYes -> {
                        clDocContainer.visibility = VISIBLE
                    }

                    R.id.rbNo -> {
                        clDocContainer.visibility = GONE
                    }
                }
            }
        }
    }

    fun setImageUri(uri: String?, randomImageName: String) {
        docName = randomImageName
        with(binding.ivDocImage) {
            if (uri == null) {
                setImageResource(R.drawable.ic_image_placeholder)
            } else {
                loadImageFromFilePath(uri)
            }
        }
    }

    fun getDocName(): String {
        return docName
    }

    fun addDocImageClickListener(listener: OnClickListener) {
        binding.ivDocImage.setOnClickListener(listener)
    }
}