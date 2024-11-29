package com.pxy.visaz.core.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.pxy.visaz.R
import com.pxy.visaz.core.extension.loadImageFromFilePath
import com.pxy.visaz.databinding.VisaDocUploadViewBinding

class VisaDocUploadView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding =
        VisaDocUploadViewBinding.inflate(LayoutInflater.from(context), this, true)

    private var docName = ""

    init {
        context.withStyledAttributes(attrs, R.styleable.VisaDocUploadView) {
            with(binding) {
                val title = getString(R.styleable.VisaDocUploadView_visaDocUploadViewTitle)
                if (title != null) {
                    tvOptionHeader.text = title
                } else {
                    tvOptionHeader.visibility = GONE
                }
                val conditionRequired = getBoolean(R.styleable.VisaDocUploadView_conditionRequired, true)
                if (conditionRequired) {
                    rgOption.visibility = VISIBLE
                    clDocContainer.visibility = GONE
                }else{
                    rgOption.visibility = GONE
                    clDocContainer.visibility = VISIBLE
                }
                setOnCheckedChangeListener()
            }
        }
    }

    private fun setOnCheckedChangeListener() {
        with(binding) {
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
        binding.clDocContainer.setOnClickListener(listener)
    }
}