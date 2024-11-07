package com.pxy.visaz.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.pxy.visaz.R
import com.pxy.visaz.core.BaseFragment
import com.pxy.visaz.data.local.AppPreferenceHelper
import com.pxy.visaz.databinding.FragmentLoadingBinding


class LoadingFragment : BaseFragment() {

    private lateinit var binding: FragmentLoadingBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (AppPreferenceHelper.user != null) {
            goToHomeFlow()
        } else {
            goToPreAuthFlow()
        }
    }

    private fun goToPreAuthFlow() {
        findNavController().navigate(R.id.action_loading_to_loginFragment, arguments)
    }

    private fun goToHomeFlow() {
        findNavController().navigate(R.id.action_loading_to_homeFragment, arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }
}