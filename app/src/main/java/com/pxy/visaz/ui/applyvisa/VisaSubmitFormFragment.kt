package com.pxy.visaz.ui.applyvisa

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.firebase.util.nextAlphanumericString
import com.pxy.visaz.R
import com.pxy.visaz.core.AppConstants
import com.pxy.visaz.core.PopBackFragment
import com.pxy.visaz.core.customview.VisaDocUploadView
import com.pxy.visaz.core.extension.copyFileToAppStorage
import com.pxy.visaz.core.extension.showDatePicker
import com.pxy.visaz.core.extension.uriToFilePath
import com.pxy.visaz.core.model.visa.Country
import com.pxy.visaz.core.model.visa.VisaApplicationModel
import com.pxy.visaz.core.model.visa.VisaSubmitApplicationModel
import com.pxy.visaz.core.utils.Validator
import com.pxy.visaz.databinding.FragmentVisaSubmitFormBinding
import com.pxy.visaz.ui.home.VisaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random


class VisaSubmitFormFragment : PopBackFragment() {

    private lateinit var binding: FragmentVisaSubmitFormBinding
    private var visaApplicationModel: VisaApplicationModel? = null
    private var selectedDate: String? = null
    private var selectedViewYesNoDocUpload: VisaDocUploadView? = null

    private val validator by lazy { Validator() }

    private val visaViewModel: VisaViewModel by viewModel()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            btnSubmit.setOnClickListener {
                val travelDate = inputSelectedDate.getText()
                val nationality = inputNationality.getText()
                val nationalId = inputNationalId.getText()
                val fullName = inputFullName.getText()
                val contactNumber = inputContactNumber.getText()
                val email = inputEmail.getText()
                val gender = dropdownGender.getText()
                val maritalStatus = dropdownMaritalStatus.getText()
                val husbandName = inputHusbandName.getText()
                val motherName = inputMotherName.getText()
                val fatherName = inputFatherName.getText()
                val currentResidentialAddress = inputCurrentResidentialAddress.getText()
                val nameOfBusiness = inputNameOfBusiness.getText()
                val addressOfBusiness = inputAddressOfBusiness.getText()
                val employerDesignation = inputEmployerDesignation.getText()
                val panNumber = inputPanNumber.getText()
                val occupation = dropdownOccupation.getText()
                val religion = dropdownReligion.getText()
                val educationQualification = dropdownEducationQualification.getText()
                val mainLanguageSpoken = dropdownMainLanguageSpoken.getText()
                val dob = inputDOB.getText()
                val placeOfBirthCountry = inputPlaceOfBirthCountry.getText()
                val passportNumber = inputPassportNumber.getText()
                val purposeOfVisit = inputPurposeOfVisit.getText()

                //contacts in country
                val countryContactName = inputCountryContactName.getText()
                val relationshipWithHost = inputRelationshipWithHost.getText()
                val addressOrHotelConfirmation = inputAddressOrHotelConfirmation.getText()
                val voucherArea = inputVoucherArea.getText()

                //documents
                val profileImage = docProfilePic.getImagePath()
                val passport = docPassport.getImagePath()
                val previousPassport = docPreviousPassport.getImagePath()
                val longTermFd = docLongTermFd.getImagePath()
                val incomeTaxReturns = docIncomeTaxReturns.getImagePath()
                val letterOfInvitation = docLetterOfInvitation.getImagePath()
                val isValid = validateVisaApplication(
                    travelDate,
                    nationality,
                    nationalId,
                    fullName,
                    contactNumber,
                    email,
                    gender,
                    maritalStatus,
                    husbandName,
                    motherName,
                    fatherName,
                    currentResidentialAddress,
                    nameOfBusiness,
                    addressOfBusiness,
                    employerDesignation,
                    panNumber,
                    occupation,
                    religion,
                    educationQualification,
                    mainLanguageSpoken,
                    dob,
                    placeOfBirthCountry,
                    passportNumber,
                    purposeOfVisit,
                    countryContactName,
                    relationshipWithHost,
                    addressOrHotelConfirmation,
                    voucherArea,
                    profileImage,
                    passport,
                    previousPassport,
                    longTermFd,
                    incomeTaxReturns,
                    letterOfInvitation
                )
                if (isValid) {
                    val model = VisaSubmitApplicationModel(
                        travelDate,
                        nationality,
                        nationalId,
                        fullName,
                        contactNumber,
                        email,
                        gender,
                        maritalStatus,
                        husbandName,
                        motherName,
                        fatherName,
                        currentResidentialAddress,
                        nameOfBusiness,
                        addressOfBusiness,
                        employerDesignation,
                        panNumber,
                        occupation,
                        religion,
                        educationQualification,
                        mainLanguageSpoken,
                        dob,
                        placeOfBirthCountry,
                        passportNumber,
                        purposeOfVisit,
                        countryContactName,
                        relationshipWithHost,
                        addressOrHotelConfirmation,
                        voucherArea,
                        profileImage,
                        passport,
                        previousPassport,
                        longTermFd,
                        incomeTaxReturns,
                        letterOfInvitation
                    )
                    visaViewModel.submitVisaApplication(model)
                    toast(getString(R.string.visa_application_submitted_successfully))
                    findNavController().navigate(R.id.action_visaSubmitFormFragment_to_homeFragment)
                }

            }
            inputSelectedDate.addOnClickListener {
                showSelectedDatePicker()
            }
            inputDOB.addOnClickListener {
                showDOBDatePicker()
            }
            dropdownGender.onItemSelect {
                val genderArray = resources.getStringArray(R.array.gender)
                binding.dropdownMaritalStatus.isVisible =
                    (it == genderArray[0] || it == genderArray[1])
                checkMarriageStatus()
            }
            dropdownMaritalStatus.onItemSelect {
                checkMarriageStatus()
            }

            docProfilePic.addDocImageClickListener {
                selectedViewYesNoDocUpload = docProfilePic
                pickImage.launch("image/*")
            }
            docPassport.addDocImageClickListener {
                selectedViewYesNoDocUpload = docPassport
                pickImage.launch("image/*")
            }
            docPreviousPassport.addDocImageClickListener {
                selectedViewYesNoDocUpload = docPreviousPassport
                pickImage.launch("image/*")
            }
            docLongTermFd.addDocImageClickListener {
                selectedViewYesNoDocUpload = docLongTermFd
                pickImage.launch("image/*")
            }
            docIncomeTaxReturns.addDocImageClickListener {
                selectedViewYesNoDocUpload = docIncomeTaxReturns
                pickImage.launch("image/*")
            }
            docLetterOfInvitation.addDocImageClickListener {
                selectedViewYesNoDocUpload = docLetterOfInvitation
                pickImage.launch("image/*")
            }
        }
    }

    private fun validateVisaApplication(
        travelDate: String,
        nationality: String,
        nationalId: String,
        fullName: String,
        contactNumber: String,
        email: String,
        gender: String,
        maritalStatus: String,
        husbandName: String,
        motherName: String,
        fatherName: String,
        currentResidentialAddress: String,
        nameOfBusiness: String,
        addressOfBusiness: String,
        employerDesignation: String,
        panNumber: String,
        occupation: String,
        religion: String,
        educationQualification: String,
        mainLanguageSpoken: String,
        dob: String,
        placeOfBirthCountry: String,
        passportNumber: String,
        purposeOfVisit: String,
        countryContactName: String,
        relationshipWithHost: String,
        addressOrHotelConfirmation: String,
        voucherArea: String,
        profileImage: String,
        passport: String,
        previousPassport: String,
        longTermFd: String,
        incomeTaxReturns: String,
        letterOfInvitation: String
    ): Boolean {
        with(binding) {
            if (validator.isInputValid(travelDate)) {
                inputSelectedDate.setError(null)
            } else {
                inputSelectedDate.setError(getString(R.string.error_empty_travel_date))
                return false
            }

            /* if (validator.isInputValid(nationality)) {
            inputNationality.setError(null)
        } else {
            inputNationality.setError(validator.getNameError(nationality))
            return false
        }*/

            if (validator.isValidAadhaarNumber(nationalId)) {
                inputNationalId.setError(null)
            } else {
                inputNationalId.setError(validator.getAadhaarError(nationalId))
                return false
            }

            if (validator.isInputValid(fullName)) {
                inputFullName.setError(null)
            } else {
                inputFullName.setError(validator.getInputError(fullName))
                return false
            }

            if (validator.isValidPhoneNumber(contactNumber)) {
                inputContactNumber.setError(null)
            } else {
                inputContactNumber.setError(validator.getPhoneNumberError(contactNumber))
                return false
            }

            if (validator.isValidEmail(email)) {
                inputEmail.setError(null)
            } else {
                inputEmail.setError(validator.getEmailError(email))
                return false
            }

            if (validator.isInputValid(gender)) {
                dropdownGender.setError(null)
            } else {
                dropdownGender.setError(getString(R.string.error_empty_gender))
                return false
            }

            with(dropdownMaritalStatus) {
                if (isVisible) {
                    if (validator.isInputValid(maritalStatus)) {
                        setError(null)
                    } else {
                        setError(getString(R.string.error_empty_marital_status))
                        return false
                    }
                }
            }

            with(inputHusbandName) {
                if (isVisible) {
                    if (validator.isInputValid(husbandName)) {
                        setError(null)
                    } else {
                        setError(getString(R.string.error_empty_husband_name))
                        return false
                    }
                }
            }

            if (validator.isInputValid(motherName)) {
                inputMotherName.setError(null)
            } else {
                inputMotherName.setError(getString(R.string.error_empty_mother_name))
                return false
            }

            if (validator.isInputValid(fatherName)) {
                inputFatherName.setError(null)
            } else {
                inputFatherName.setError(getString(R.string.error_empty_father_name))
                return false
            }

            if (validator.isInputValid(currentResidentialAddress)) {
                inputCurrentResidentialAddress.setError(null)
            } else {
                inputCurrentResidentialAddress.setError(
                    getString(R.string.error_empty_current_residential_address)
                )
                return false
            }

            if (validator.isInputValid(nameOfBusiness)) {
                inputNameOfBusiness.setError(null)
            } else {
                inputNameOfBusiness.setError(getString(R.string.error_empty_name_of_business))
                return false
            }

            if (validator.isInputValid(addressOfBusiness)) {
                inputAddressOfBusiness.setError(null)
            } else {
                inputAddressOfBusiness.setError(getString(R.string.error_empty_address_of_business))
                return false
            }

            if (validator.isInputValid(employerDesignation)) {
                inputEmployerDesignation.setError(null)
            } else {
                inputEmployerDesignation.setError(
                    getString(R.string.error_empty_employer_designation)
                )
                return false
            }

            if (validator.isValidPAN(panNumber)) {
                inputPanNumber.setError(null)
            } else {
                inputPanNumber.setError(validator.getPanError(panNumber))
                return false
            }

            if (validator.isInputValid(occupation)) {
                dropdownOccupation.setError(null)
            } else {
                dropdownOccupation.setError(getString(R.string.error_empty_occupation))
                return false
            }

            if (validator.isInputValid(religion)) {
                dropdownReligion.setError(null)
            } else {
                dropdownReligion.setError(getString(R.string.error_empty_religion))
                return false
            }

            if (validator.isInputValid(educationQualification)) {
                dropdownEducationQualification.setError(null)
            } else {
                dropdownEducationQualification.setError(
                    getString(R.string.error_empty_education_qualification)
                )
                return false
            }

            if (validator.isInputValid(mainLanguageSpoken)) {
                dropdownMainLanguageSpoken.setError(null)
            } else {
                dropdownMainLanguageSpoken.setError(
                    getString(R.string.error_empty_main_language_spoken)
                )
                return false
            }

            if (validator.isInputValid(dob)) {
                inputDOB.setError(null)
            } else {
                inputDOB.setError(getString(R.string.error_empty_dob))
                return false
            }

            /*if (validator.isInputValid(placeOfBirthCountry)) {
                inputPlaceOfBirthCountry.setError(null)
            } else {
                inputPlaceOfBirthCountry.setError(
                    validator.getInputError(
                        placeOfBirthCountry
                    )
                )
                return false
            }*/

            if (validator.isValidIndianPassport(passportNumber)) {
                inputPassportNumber.setError(null)
            } else {
                inputPassportNumber.setError(validator.getPassportError(passportNumber))
                return false
            }

            if (validator.isInputValid(purposeOfVisit)) {
                inputPurposeOfVisit.setError(null)
            } else {
                inputPurposeOfVisit.setError(getString(R.string.error_empty_purpose_of_visit))
                return false
            }

            if (validator.isInputValid(countryContactName)) {
                inputCountryContactName.setError(null)
            } else {
                inputCountryContactName.setError(getString(R.string.error_empty_country_contact_name))
                return false
            }

            if (validator.isInputValid(relationshipWithHost)) {
                inputRelationshipWithHost.setError(null)
            } else {
                inputRelationshipWithHost.setError(
                    getString(R.string.error_empty_relationship_with_host)
                )
                return false
            }

            if (validator.isInputValid(addressOrHotelConfirmation)) {
                inputAddressOrHotelConfirmation.setError(null)
            } else {
                inputAddressOrHotelConfirmation.setError(
                    getString(R.string.error_empty_address_or_hotel_confirmation)
                )
                return false
            }

            if (validator.isInputValid(voucherArea)) {
                inputVoucherArea.setError(null)
            } else {
                inputVoucherArea.setError(getString(R.string.error_empty_voucher_area))
                return false
            }

            with(docProfilePic) {
                if (isYes()) {
                    if (validator.isInputValid(profileImage)) {
                        setError(null)
                    } else {
                        setError(getString(R.string.error_profile_image))
                        return false
                    }
                }
            }

            with(docPassport) {
                if (isYes()) {
                    if (validator.isInputValid(passport)) {
                        setError(null)
                    } else {
                        setError(getString(R.string.error_passport_image))
                        return false
                    }
                }
            }

            with(docPreviousPassport) {
                if (isYes()) {
                    if (validator.isInputValid(previousPassport)) {
                        setError(null)
                    } else {
                        setError(getString(R.string.error_previous_passport_image))
                        return false
                    }
                }
            }

            with(docLongTermFd) {
                if (isYes()) {
                    if (validator.isInputValid(longTermFd)) {
                        setError(null)
                    } else {
                        setError(getString(R.string.error_long_term_fd))
                        return false
                    }
                }
            }

            with(docIncomeTaxReturns) {
                if (isYes()) {
                    if (validator.isInputValid(incomeTaxReturns)) {
                        setError(null)
                    } else {
                        setError(getString(R.string.error_income_tax_returns))
                        return false
                    }
                }
            }

            with(docLetterOfInvitation) {
                if (isYes()) {
                    if (validator.isInputValid(letterOfInvitation)) {
                        setError(null)
                    } else {
                        setError(getString(R.string.error_letter_of_invitation))
                        return false
                    }
                }
            }
        }

        return true
    }

    private fun FragmentVisaSubmitFormBinding.checkMarriageStatus() {
        val marriedArray = resources.getStringArray(R.array.marital_status)
        val genderArray = resources.getStringArray(R.array.gender)

        val selectedGenderStatus = dropdownGender.getText()
        val selectedMaritalStatus = dropdownMaritalStatus.getText()
        if ((selectedGenderStatus == genderArray[0] || selectedGenderStatus == genderArray[1])) {
            if (selectedMaritalStatus == marriedArray[1]) {
                inputHusbandName.visibility = View.VISIBLE
                inputHusbandName.setTitle(
                    if (dropdownGender.getText() == genderArray[0]) {
                        getString(R.string.wife_name)
                    } else {
                        getString(R.string.husband_name)
                    }
                )
            } else {
                inputHusbandName.visibility = View.GONE
            }
        } else {
            dropdownMaritalStatus.isVisible = false
            inputHusbandName.isVisible = false
        }
    }

    private fun showSelectedDatePicker() {
        showDatePicker {
            binding.inputSelectedDate.setText(it)
        }
    }

    private fun showDOBDatePicker() {
        // Set constraints to disable past dates
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())

        showDatePicker(constraintsBuilder) {
            binding.inputDOB.setText(it)
        }
    }

    private fun initViews() {
        initToolbar()
        visaApplicationModel?.let {
            with(binding) {
                tvContactInCountry.text = getString(R.string.contact_in_country, it.name)
            }
        }
    }

    private fun initCountryDropDown() {
        // Adapter for dropdown displaying country names
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown,
            countries.map { it.name } // Display only the name
        )
        //binding.etCountry.setAdapter(adapter)
    }

    private fun initToolbar() {
        with(binding.layoutHeader.toolbar) {
            setNavigationOnClickListener {
                goBack()
            }
            title = getString(R.string.label_submit_application)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readIntentData()
    }

    private fun readIntentData() {
        arguments?.let {
            visaApplicationModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable(
                    AppConstants.EXTRA_VISA_DETAILS,
                    VisaApplicationModel::class.java
                )
            } else {
                arguments?.getParcelable(AppConstants.EXTRA_VISA_DETAILS)
            }
            selectedDate = arguments?.getString(AppConstants.EXTRA_SELECTED_DATE)
        }
    }

    private fun initObservers() {

    }


    // Register for activity result to get an image from gallery
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { imageUri ->
                val randomImageName = Random.nextAlphanumericString(10)
                val profileImageUri = uriToFilePath(imageUri)?.let {
                    copyFileToAppStorage(
                        requireContext(),
                        it,
                        randomImageName
                    )
                }
                selectedViewYesNoDocUpload?.setImageUri(profileImageUri, randomImageName)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVisaSubmitFormBinding.inflate(inflater, container, false)
        return binding.root
    }
}