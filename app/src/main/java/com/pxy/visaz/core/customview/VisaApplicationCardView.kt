package com.pxy.visaz.core.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.pxy.visaz.R
import com.pxy.visaz.core.extension.loadImageFromFilePath
import com.pxy.visaz.core.extension.toBase64
import com.pxy.visaz.core.extension.toast
import com.pxy.visaz.core.model.visa.Country
import com.pxy.visaz.core.model.visa.VisaApplicationDetails
import com.pxy.visaz.core.utils.Validator
import com.pxy.visaz.databinding.ViewVisaApplicationFormBinding

class VisaApplicationCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val validator by lazy { Validator() }

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

    private var visaModel: VisaApplicationDetails? = null

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

    fun setVisaModel(visaModel: VisaApplicationDetails) {
        this.visaModel = visaModel
        with(binding) {
            tvCardHeader.text = visaModel.title
            etSelectedDate.setText(visaModel.selectedDate)
            etMotherName.setText(visaModel.motherName)
            etFatherName.setText(visaModel.fatherName)
            etGender.setText(visaModel.gender)
        }
        setProfileImageUri(visaModel.profileImageBase64)
        setPassportImageUri(visaModel.passportImageBase64)
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

    fun setProfileImageUri(uri: String?) {
        visaModel?.profileImageBase64 = uri
        with(binding.ivProfilePic) {
            if (uri == null) {
                setImageResource(R.drawable.ic_image_placeholder)
            } else {
                loadImageFromFilePath(uri)
            }
        }
    }

    fun getVisaModel(): VisaApplicationDetails? {
        if (isValid(
                binding.etSelectedDate.text.toString(),
                binding.etMotherName.text.toString(),
                binding.etFatherName.text.toString(),
                binding.etGender.text.toString(),
                binding.etCountry.text.toString()
            )
        ) {
            if (visaModel?.profileImageBase64.isNullOrEmpty()) {
                context.toast(context.getString(R.string.error_profile_image))
                return null
            }
            if (visaModel?.passportImageBase64.isNullOrEmpty()) {
                context.toast(context.getString(R.string.error_passport_image))
                return null
            }
            return visaModel.apply {
                this?.motherName = binding.etMotherName.text.toString()
                this?.fatherName = binding.etFatherName.text.toString()
                this?.gender = binding.etGender.text.toString()
                this?.selectedDate = binding.etSelectedDate.text.toString()
                this?.country = binding.etCountry.text.toString()
                //this?.profileImageBase64 = this?.profileImageBase64?.toBase64()
                //this?.passportImageBase64 = this?.passportImageBase64?.toBase64()
            }
        } else {
            return null
        }
    }

    private fun isValid(
        selectedDate: String,
        motherName: String,
        fatherName: String,
        gender: String,
        country: String
    ): Boolean {
        with(binding) {
            with(tilSelectedDate) {
                if (validator.isValidName(selectedDate)) {
                    error = null
                } else {
                    error = validator.getNameError(selectedDate)
                    return false
                }
            }

            with(tilMotherName) {
                if (validator.isValidName(motherName)) {
                    error = null
                } else {
                    error = validator.getNameError(motherName)
                    return false
                }
            }

            with(tilFatherName) {
                if (validator.isValidName(fatherName)) {
                    error = null
                } else {
                    error = validator.getNameError(fatherName)
                    return false
                }
            }

            with(tilGender) {
                if (validator.isValidName(gender)) {
                    error = null
                } else {
                    error = validator.getNameError(gender)
                    return false
                }
            }

            with(tilCountry) {
                if (validator.isValidName(country)) {
                    error = null
                } else {
                    error = validator.getNameError(country)
                    return false
                }
            }


        }
        return true
    }

    fun setPassportImageUri(uri: String?) {
        visaModel?.passportImageBase64 = uri
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

    fun getProfileImageName(): String {
        return visaModel?.title.orEmpty() + "_profile"
    }

    fun getPassportImageName(): String {
        return visaModel?.title.orEmpty() + "_passport"
    }

    fun setSelectDate(date: String) {
        with(binding) {
            etSelectedDate.setText(date)
            tilSelectedDate.error = null
        }
    }
}