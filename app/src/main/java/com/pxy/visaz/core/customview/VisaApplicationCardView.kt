package com.pxy.visaz.core.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.pxy.visaz.R
import com.pxy.visaz.core.extension.loadImageFromFilePath
import com.pxy.visaz.core.model.visa.Country
import com.pxy.visaz.core.model.visa.VisaApplicationBasicDetails
import com.pxy.visaz.databinding.ViewVisaApplicationFormBinding

class VisaApplicationCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    // List of Country objects
    private val countries = listOf(
        Country("India", "+91"),
        Country("United States", "+1"),
        Country("United Kingdom", "+44"),
        Country("Canada", "+1"),
        Country("Australia", "+61"),
        Country("Germany", "+49"),
        Country("France", "+33"),
        Country("Japan", "+81"),
        Country("China", "+86")
    )

    private var visaModel: VisaApplicationBasicDetails? = null

    private val binding =
        ViewVisaApplicationFormBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.withStyledAttributes(attrs, R.styleable.VisaApplicationCardView) {
            with(binding) {
                val title = getString(R.styleable.VisaApplicationCardView_visa_application_title)
                if (title != null) {
                    tvCardHeader.text = title
                }
                initGenderDropDown()
                initCountryDropDown()
            }
        }
    }

    fun setVisaModel(visaModel: VisaApplicationBasicDetails) {
        this.visaModel = visaModel
        with(binding) {
            tvCardHeader.text = visaModel.title
            etSelectedDate.setText(visaModel.selectedDate)
            etMotherName.setText(visaModel.motherName)
            etFatherName.setText(visaModel.fatherName)
            etGender.setText(visaModel.gender)
        }
        setProfileImageUri(visaModel.profileImageUri)
        setPassportImageUri(visaModel.passportImageUri)
    }

    fun onProfileImageClick(listener: OnClickListener) {
        binding.ivProfilePic.setOnClickListener(listener)
    }

    fun onPassportImageClick(listener: OnClickListener) {
        binding.ivPassportImage.setOnClickListener(listener)
    }

    fun onDateSelectClick(listener: OnClickListener) {
        binding.tilSelectedDate.setOnClickListener(listener)
        binding.etSelectedDate.setOnClickListener(listener)
    }

    private fun setProfileImageUri(uri: String?) {
        with(binding.ivProfilePic) {
            if (uri == null) {
                setImageResource(R.drawable.ic_image_placeholder)
            } else {
                loadImageFromFilePath(uri)
            }
        }
    }

    private fun setPassportImageUri(uri: String?) {
        with(binding.ivPassportImage) {
            if (uri == null) {
                setImageResource(R.drawable.ic_image_placeholder)
            } else {
                loadImageFromFilePath(uri)
            }
        }
    }

    private fun initCountryDropDown() {
        // Adapter for dropdown displaying country names
        val adapter = ArrayAdapter(
            context,
            R.layout.item_dropdown,
            countries.map { it.name } // Display only the name
        )
        binding.etCountry.setAdapter(adapter)
    }

    private fun initGenderDropDown() {
        val priorities = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(context, R.layout.item_dropdown, priorities)
        binding.etGender.setAdapter(arrayAdapter)
    }
}