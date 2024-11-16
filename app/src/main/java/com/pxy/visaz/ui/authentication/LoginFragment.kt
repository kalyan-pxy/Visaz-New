package com.pxy.visaz.ui.authentication

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.credentials.CredentialManager
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
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

    private val RC_SIGN_IN = 100
    private lateinit var credentialManager: CredentialManager
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private val clientID =
        "349434490447-ku367vc3q3dke8oblj04ntovsduv0o1c.apps.googleusercontent.com" // Replace with your actual client ID

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize Credential Manager
        credentialManager = CredentialManager.create(requireContext())

        // Configure Google Sign-In options
        oneTapClient = Identity.getSignInClient(requireContext())
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(clientID) // Use Web client ID here
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
        initObservers()
        initViews()
    }

    private fun signIn() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender,
                        RC_SIGN_IN,
                        null, 0, 0, 0, null
                    )
                } catch (e: Exception) {
                    Log.e("SignIn", "Failed to start sign-in intent", e)
                }
            }
            .addOnFailureListener { e ->
                Log.e("SignIn", "Google Sign-In failed: ${e.message}")
            }
    }

    fun signOut() {
        oneTapClient.signOut().addOnCompleteListener {
            Log.d("SignOut", "User signed out successfully")
        }
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

            btnGoogleLogin.setOnClickListener {
                signIn()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken
                val displayName = credential.displayName

                if (idToken != null) {
                    // Use ID Token to authenticate with your backend
                    Log.d("SignIn", "ID Token: $idToken, User: $displayName")
                } else {
                    Log.e("SignIn", "Google ID Token is null")
                }
            } catch (e: ApiException) {
                Log.e("SignIn", "Sign-in failed", e)
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