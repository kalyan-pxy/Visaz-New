package com.pxy.visaz.core.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.pxy.visaz.R
import com.pxy.visaz.databinding.VisaDateInputViewBinding

class VisaDateInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding =
        VisaDateInputViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.withStyledAttributes(attrs, R.styleable.VisaDateInputView) {
            with(binding) {
                val title = getString(R.styleable.VisaDateInputView_visaDateInputViewTitle)
                if (title != null) {
                    tvTitle.text = title
                } else {
                    tvTitle.visibility = GONE
                }
                val hint = getString(R.styleable.VisaDateInputView_android_hint)
                if (hint != null) {
                    textInputEditText.hint = hint
                } else {
                    if (title != null) {
                        textInputEditText.hint =
                            resources.getString(R.string.prefix_dropdown_hint, title)
                    }
                }
                val text = getString(R.styleable.VisaDateInputView_android_text)
                if (text != null) {
                    textInputEditText.setText(text)
                }
                val enabled = getBoolean(R.styleable.VisaDateInputView_android_enabled, true)
                textInputLayout.isEnabled = enabled
            }
        }
    }

    fun setError(error: String?) {
        binding.textInputLayout.error = error
    }

    fun setText(text: String) {
        binding.textInputEditText.setText(text)
    }

    fun getText(): String {
        return binding.textInputEditText.text.toString()
    }

    fun addOnClickListener(listener: OnClickListener) {
        binding.textInputEditText.setOnClickListener(listener)
        binding.textInputLayout.setOnClickListener(listener)
    }
}