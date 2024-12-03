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
        /*val model = VisaSubmitApplicationModel(
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
        )*/
        if (validator.isInputValid(travelDate)) {
            binding.inputSelectedDate.setError(null)
        } else {
            binding.inputSelectedDate.setError(validator.getNameError(travelDate))
            return false
        }

        if (validator.isInputValid(nationality)) {
            binding.inputNationality.setError(null)
        } else {
            binding.inputNationality.setError(validator.getNameError(nationality))
            return false
        }

        if (validator.isInputValid(nationalId)) {
            binding.inputNationalId.setError(null)
        } else {
            binding.inputNationalId.setError(validator.getInputError(nationalId))
            return false
        }

        if (validator.isInputValid(fullName)) {
            binding.inputFullName.setError(null)
        } else {
            binding.inputFullName.setError(validator.getInputError(fullName))
            return false
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