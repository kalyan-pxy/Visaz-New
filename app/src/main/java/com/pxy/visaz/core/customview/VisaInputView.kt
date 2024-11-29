package com.pxy.visaz.core.customview

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.pxy.visaz.R
import com.pxy.visaz.databinding.VisaInputViewBinding

class VisaInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding =
        VisaInputViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.withStyledAttributes(attrs, R.styleable.VisaInputView) {
            with(binding) {
                val title = getString(R.styleable.VisaInputView_visaInputViewTitle)
                if (title != null) {
                    tvTitle.text = title
                } else {
                    tvTitle.visibility = GONE
                }
                val hint = getString(R.styleable.VisaInputView_android_hint)
                if (hint != null) {
                    textInputEditText.hint = hint
                } else {
                    if (title != null) {
                        textInputEditText.hint =
                            resources.getString(R.string.prefix_input_hint, title)
                    }
                }
                val text = getString(R.styleable.VisaInputView_android_text)
                if (text != null) {
                    textInputEditText.setText(text)
                }
                val enabled = getBoolean(R.styleable.VisaInputView_android_enabled, true)
                textInputLayout.isEnabled = enabled
                val maxLength = getInt(R.styleable.VisaInputView_android_maxLength, 0)
                if (maxLength != 0) {
                    textInputEditText.filters = arrayOf(InputFilter.LengthFilter(maxLength))
                }
                val inputType = getInt(R.styleable.VisaInputView_android_inputType, 0)
                if (inputType != 0) {
                    textInputEditText.inputType = inputType
                }
                val minLines = getInt(R.styleable.VisaInputView_android_minLines, 0)
                if (minLines != 0) {
                    textInputEditText.minLines = minLines
                }
                val gravity = getInt(R.styleable.VisaInputView_android_gravity, 0)
                if (gravity != 0) {
                    textInputEditText.gravity = gravity
                }
            }
        }
    }

    fun setText(text: String) {
        binding.textInputEditText.setText(text)
    }

    fun setTitle(title: String) {
        binding.tvTitle.text = title
        binding.textInputEditText.hint =
            resources.getString(R.string.prefix_input_hint, title)
    }
}