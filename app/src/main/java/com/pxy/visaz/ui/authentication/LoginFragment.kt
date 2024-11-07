package com.pxy.visaz.ui.authentication

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.pxy.visaz.R
import com.pxy.visaz.core.AppConstants
import com.pxy.visaz.core.BaseFragment
import com.pxy.visaz.core.extension.highlightTextMatches
import com.pxy.visaz.core.utils.Validator
import com.pxy.visaz.databinding.FragmentLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment() {


    private lateinit var binding: FragmentLoginBinding

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
                getString(R.string.label_do_not_have_an_account),
                getString(R.string.label_sign_up),
                Typeface.DEFAULT_BOLD
            ) {
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }
            btnLogin.setOnClickListener {
                val userName = etUsername.text.toString()
                val password = etPassword.text.toString()
                if (isValid(userName, password)) {
                    loginViewModel.doLogin(
                        userName, password
                    )
                }
            }
        }
    }

    private fun isValid(userName: String, password: String): Boolean {
        // Check if username is valid
        if (validator.isValidUsername(userName)) {
            binding.tilUsername.error = null
        } else {
            binding.tilUsername.error = validator.getUsernameError(userName)
            return false
        }

        // Check if password is valid
        /*if (validator.isValidPassword(password)) {
            binding.tilPassword.error = null
        } else {
            binding.tilPassword.error = validator.getPasswordError(password)
            return false
        }*/
        if (password.isEmpty().not()) {
            binding.tilPassword.error = null
        } else {
            binding.tilPassword.error = getString(R.string.error_password_empty)
            return false
        }
        return true
    }

    private fun initObservers() {
        loginViewModel.loginObserver.observe(viewLifecycleOwner) {
            it?.let {
                if (it.success) {
                    toast(it.message)
                    findNavController().navigate(R.id.action_to_homeFragment, Bundle().apply {
                        putString(
                            AppConstants.EXTRA_USER_NAME,
                            binding.etUsername.text.toString()
                        )
                    })
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
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
}