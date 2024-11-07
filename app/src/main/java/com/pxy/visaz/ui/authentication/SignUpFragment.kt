package com.pxy.visaz.ui.authentication

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.pxy.visaz.R
import com.pxy.visaz.core.BaseFragment
import com.pxy.visaz.core.extension.highlightTextMatches
import com.pxy.visaz.core.utils.Validator
import com.pxy.visaz.databinding.FragmentSignUpBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class SignUpFragment : BaseFragment() {

    private lateinit var binding: FragmentSignUpBinding

    private val validator by lazy { Validator() }

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
    }

    private fun initViews() {
        with(binding) {
            tvRegister.highlightTextMatches(
                getString(R.string.label_already_have_an_account),
                getString(R.string.label_login),
                Typeface.DEFAULT_BOLD
            ) {
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }
            btnSignUp.setOnClickListener {
                val userName = etName.text.toString()
                val userEmail = etEmail.text.toString()
                val userPhone = etPhoneNumber.text.toString()
                if (isValid(userName, userEmail, userPhone)) {
                    loginViewModel.doSignUp(
                        userName, userEmail, userPhone
                    )
                }
            }
        }
    }

    private fun isValid(name: String, email: String, phone: String): Boolean {
        // Check if name is valid
        if (validator.isValidName(name)) {
            binding.tilName.error = null
        } else {
            binding.tilName.error = validator.getNameError(name)
            return false
        }

        // Check if email is valid
        if (validator.isValidEmail(email)) {
            binding.tilEmail.error = null
        } else {
            binding.tilEmail.error = validator.getEmailError(email)
            return false
        }

        // Check if phone is valid
        if (validator.isValidPhoneNumber(phone)) {
            binding.tilPhoneNumber.error = null
        } else {
            binding.tilPhoneNumber.error = validator.getPhoneNumberError(email)
            return false
        }
        return true
    }

    private fun initObservers() {
        loginViewModel.signUpObserver.observe(viewLifecycleOwner) {
            it?.let {
                if (it.success) {
                    toast(it.message)
                    loginViewModel.clearSignUpObserver()
                    findNavController().navigate(R.id.action_signUpFragment_to_createPasswordFragment)
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
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }
}