package com.pxy.visaz.core.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.pxy.visaz.R
import com.pxy.visaz.databinding.OptionCardViewBinding

class OptionCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding =
        OptionCardViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.withStyledAttributes(attrs, R.styleable.OptionCard) {
            val optionImage = getDrawable(R.styleable.OptionCard_optionImage)
            with(binding) {
                optionImage?.let {
                    imgOption.setImageDrawable(it)
                }
                val optionTitle = getString(R.styleable.OptionCard_optionTitle)
                val optionSubTitle = getString(R.styleable.OptionCard_optionSubTitle)
                if (optionTitle?.isNotEmpty() == true) {
                    txtOptionTitle.text = optionTitle
                } else {
                    txtOptionTitle.isVisible = false
                }
                if (optionSubTitle?.isNotEmpty() == true) {
                    txtOptionSubTitle.text = optionSubTitle
                } else {
                    txtOptionSubTitle.isVisible = false
                }
                val backGround = getColor(
                    R.styleable.OptionCard_optionBackground,
                    -1
                )
                if (backGround != -1) {
                    setBackGroundColor(backGround)
                }
            }
        }
    }

    fun onClick(listener: OnClickListener) {
        binding.root.setOnClickListener(listener)
    }

    fun setSubTitle(subTitle: String) {
        with(binding) {
            txtOptionSubTitle.text = subTitle
            txtOptionSubTitle.isVisible = subTitle.isNotEmpty()
        }
    }

    fun setSubTitleColor(textColor: Int) {
        with(binding) {
            txtOptionSubTitle.setTextColor(textColor)
        }
    }

    private fun setBackGroundColor(backGround: Int) {
        with(binding) {
            root.setBackgroundColor(backGround)
        }
    }

    fun hideForwardIcon() {
        with(binding) {
            imgNavigation.isVisible = false
        }
    }
}