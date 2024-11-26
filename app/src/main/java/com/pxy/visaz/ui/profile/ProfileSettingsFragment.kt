package com.pxy.visaz.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.pxy.visaz.R
import com.pxy.visaz.core.BaseFragment
import com.pxy.visaz.core.utils.Validator
import com.pxy.visaz.databinding.FragmentProfileSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileSettingsFragment : BaseFragment() {

    private lateinit var binding: FragmentProfileSettingsBinding

    private val validator by lazy { Validator() }
    private var selectedImageUri: Uri? = null

    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
    }

    private fun initViews() {
        with(binding) {
            with(layoutHeader.toolbar) {
                title = getString(R.string.account_settings)
                setNavigationOnClickListener {
                    goBack()
                }
            }


            /*AppPreferenceHelper.student?.let {
                tvName.text = it.name
                tvEmail.text = it.email
                if (it.profilePicUrl.isNullOrEmpty().not()) {
                    ivProfilePic.loadImageFromFilePath(it.profilePicUrl!!)
                }
                etName.setText(it.name.orEmpty())
                etPhone.setText(it.phone.orEmpty())
                etBiography.setText(it.biography.orEmpty())
                etGender.setText(it.gender.orEmpty())
                etWebsiteURL.setText(it.websiteUrl.orEmpty())
                etTwitter.setText(it.twitter.orEmpty())
                etFacebook.setText(it.facebook.orEmpty())
                etLinkedin.setText(it.linkedin.orEmpty())
                etYoutube.setText(it.youtube.orEmpty())
            }*/

            ivEditProfilePic.setOnClickListener {
                pickImageFromGallery()
            }

            btnUpdate.setOnClickListener {
                val name = etName.text.toString()
                val phone = etPhone.text.toString()
                val biography = etBiography.text.toString()
                val gender = etGender.text.toString()
                val websiteUrl = etWebsiteURL.text.toString()
                val twitter = etTwitter.text.toString()
                val facebook = etFacebook.text.toString()
                val linkedin = etLinkedin.text.toString()
                val youtube = etYoutube.text.toString()
                if (isValid(
                        name,
                        phone,
                        biography,
                        gender,
                        websiteUrl,
                        twitter,
                        facebook,
                        linkedin,
                        youtube
                    )
                ) {
                    /*profileViewModel.updateProfile(
                        name,
                        phone,
                        biography,
                        gender,
                        websiteUrl,
                        twitter,
                        facebook,
                        linkedin,
                        youtube,
                        selectedImageUri?.let {
                            uriToFilePath(it)?.let {
                                copyFileToAppStorage(requireContext(),it)
                            }
                        }
                    )*/
                }
            }
        }
    }

    // Register for activity result to get an image from gallery
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                binding.ivProfilePic.setImageURI(it)  // Display the selected image
            }
        }

    private fun pickImageFromGallery() {
        pickImage.launch("image/*")  // Launch gallery for selecting images
    }

    private fun isValid(
        name: String,
        phone: String,
        biography: String,
        gender: String,
        websiteUrl: String,
        twitter: String,
        facebook: String,
        linkedin: String,
        youtube: String
    ): Boolean {
        // Check if name is valid
        if (validator.isValidName(name)) {
            binding.tilName.error = null
        } else {
            binding.tilName.error = validator.getNameError(name)
            return false
        }

        // Check if phone is valid
        if (validator.isValidPhoneNumber(phone)) {
            binding.tilPhone.error = null
        } else {
            binding.tilPhone.error = validator.getPhoneNumberError(phone)
            return false
        }

        if (validator.isValidPhoneNumber(phone)) {
            binding.tilPhone.error = null
        } else {
            binding.tilPhone.error = validator.getPhoneNumberError(phone)
            return false
        }

        if (validator.isInputValid(biography)) {
            binding.tilBiography.error = null
        } else {
            binding.tilBiography.error = validator.getInputError(biography)
            return false
        }

        if (validator.isInputValid(gender)) {
            binding.tilGender.error = null
        } else {
            binding.tilGender.error = validator.getInputError(gender)
            return false
        }

        /*if (validator.isInputValid(websiteUrl)) {
            binding.tilWebsiteURL.error = null
        } else {
            binding.tilWebsiteURL.error = validator.getInputError(websiteUrl)
            return false
        }

        if (validator.isInputValid(twitter)) {
            binding.tilTwitter.error = null
        } else {
            binding.tilTwitter.error = validator.getInputError(twitter)
            return false
        }

        if (validator.isInputValid(facebook)) {
            binding.tilFacebook.error = null
        } else {
            binding.tilFacebook.error = validator.getInputError(facebook)
            return false
        }

        if (validator.isInputValid(linkedin)) {
            binding.tilLinkedin.error = null
        } else {
            binding.tilLinkedin.error = validator.getInputError(linkedin)
            return false
        }

        if (validator.isInputValid(youtube)) {
            binding.tilYoutube.error = null
        } else {
            binding.tilYoutube.error = validator.getInputError(youtube)
            return false
        }*/

        return true
    }

    private fun initObservers() {
        profileViewModel.updateProfileObserver.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    toast("Profile Updated Successfully")
                    findNavController().navigate(R.id.action_profileSettingsFragment_profileFragment)
                }
            }
        }
        profileViewModel.errorObserver.observe(viewLifecycleOwner) {
            it?.let {
                toastError(
                    it.errorMessage
                )
            }
        }
        profileViewModel.loaderObserver.observe(viewLifecycleOwner) {
            it?.let { loading ->
                if (loading) {
                    showProgressDialog()
                } else {
                    hideProgressDialog()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
}