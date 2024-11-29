package com.pxy.visaz.core.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.pxy.visaz.R
import com.pxy.visaz.databinding.VisaDropdownViewBinding


class VisaDropdownView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding =
        VisaDropdownViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.withStyledAttributes(attrs, R.styleable.VisaDropdownView) {
            with(binding) {
                val title = getString(R.styleable.VisaDropdownView_visaDropdownViewTitle)
                if (title != null) {
                    tvTitle.text = title
                } else {
                    tvTitle.visibility = GONE
                }
                val hint = getString(R.styleable.VisaDropdownView_android_hint)
                if (hint != null) {
                    autoCompleteTextView.hint = hint
                } else {
                    if (title != null) {
                        autoCompleteTextView.hint =
                            resources.getString(R.string.prefix_dropdown_hint, title)
                    }
                }
                val enabled = getBoolean(R.styleable.VisaDropdownView_android_enabled, true)
                textInputLayout.isEnabled = enabled

                val arrayId = getResourceId(R.styleable.VisaDropdownView_visaDropdownViewItems, 0)
                if (arrayId != 0) {
                    val items = resources.getStringArray(arrayId)
                    initDropDown(items)
                }
            }
        }
    }

    private fun initDropDown(items: Array<String>) {
        val arrayAdapter = ArrayAdapter(context, R.layout.item_dropdown, items)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    fun setText(text: String) {
        binding.autoCompleteTextView.setText(text)
    }

    fun getText(): String {
        return binding.autoCompleteTextView.text.toString()
    }

    fun onItemSelect(function: (selectedOption: String) -> Unit) {
        binding.autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val selectedOption = parent.getItemAtPosition(position).toString()
            function.invoke(selectedOption)
        }
    }
}