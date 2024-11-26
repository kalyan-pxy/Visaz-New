package com.pxy.visaz.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pxy.visaz.R
import com.pxy.visaz.core.BaseFragment
import com.pxy.visaz.core.extension.showDialog
import com.pxy.visaz.data.local.AppPreferenceHelper
import com.pxy.visaz.databinding.FragmentProfileBinding
import com.pxy.visaz.ui.authentication.PreAuthActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            optionLogout.onClick {
                showLogoutDialog()
            }

            optionAccountSettings.onClick {
                findNavController().navigate(R.id.action_profileFragment_to_profileSettingsFragment)
            }
        }
    }

    private fun showLogoutDialog() {
        showDialog(
            title = getString(R.string.app_name),
            message = getString(R.string.logout_message),
            positiveButtonText = getString(R.string.logout),
            negativeButtonText = getString(R.string.cancel),
            onPositiveClick = {
                profileViewModel.logout()
            },
        )
    }

    private fun initViews() {
        with(binding) {

            AppPreferenceHelper.user?.let {
                tvName.text = it.email
                tvEmail.text = it.email
                /*if (it.profilePicUrl.isNullOrEmpty().not()) {
                    ivProfilePic.loadImageFromFilePath(it.profilePicUrl!!)
                } else {
                    ivProfilePic.loadDefaultImage()
                }*/
            }
        }
    }

    private fun initObservers() {
        profileViewModel.logoutObserver.observe(viewLifecycleOwner) {
            if (it) {
                startActivity(
                    Intent(requireContext(), PreAuthActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}