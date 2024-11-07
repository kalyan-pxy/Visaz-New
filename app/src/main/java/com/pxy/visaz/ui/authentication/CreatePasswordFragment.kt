package com.pxy.visaz.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.pxy.visaz.R
import com.pxy.visaz.core.BaseFragment
import com.pxy.visaz.core.utils.Validator
import com.pxy.visaz.databinding.FragmentCreatePasswordBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreatePasswordFragment : BaseFragment() {

    private lateinit var binding: FragmentCreatePasswordBinding

    private val validator by lazy { Validator() }

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
    }

    private fun initViews() {
        with(binding) {
            ivBack.setOnClickListener {
                goBack()
            }
            btnSignUp.setOnClickListener {
                val userEmail = etEmail.text.toString()
                val otp = etOtp.text.toString()
                val newPassword = etNewPassword.text.toString()
                val confPassword = etConfPassword.text.toString()
                if (isValid(userEmail, otp, newPassword, confPassword)) {
                    loginViewModel.createPassword(userEmail, otp, newPassword)
                }
            }
        }
    }

    private fun isValid(
        email: String,
        otp: String,
        newPassword: String,
        confPassword: String
    ): Boolean {
        // Check if email is valid
        if (validator.isValidEmail(email)) {
            binding.tilEmail.error = null
        } else {
            binding.tilEmail.apply {
                error = validator.getEmailError(email)
                requestFocus()
            }
            return false
        }

        // Check if otp is valid
        if (validator.isValidOtp(otp)) {
            binding.tilOtp.error = null
        } else {
            binding.tilOtp.apply {
                error = validator.getOtpError(otp)
                requestFocus()
            }
            return false
        }

        // Check if password is valid
        if (validator.isValidPassword(newPassword)) {
            binding.tilNewPassword.error = null
        } else {
            binding.tilNewPassword.apply {
                error = validator.getPasswordError(newPassword)
                requestFocus()
            }
            return false
        }

        // Check if confirm password is valid
        if (newPassword == confPassword) {
            binding.tilConfPassword.error = null
        } else {
            binding.tilConfPassword.apply {
                error = getString(R.string.error_password_mismatch)
                requestFocus()
            }
            return false
        }
        return true
    }

    private fun initObservers() {
        loginViewModel.createPasswordObserver.observe(viewLifecycleOwner) {
            it?.let {
                if (it.success) {
                    toast(it.message)
                    findNavController().navigate(R.id.action_createPasswordFragment_to_loginFragment)
                }
            }
        }
        loginViewModel.errorObserver.observe(viewLifecycleOwner) {
            it?.let {
                toastError(
                    it.errorMessage
                )
            }
        }
        loginViewModel.loaderObserver.observe(viewLifecycleOwner) {
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
        binding = FragmentCreatePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }
}