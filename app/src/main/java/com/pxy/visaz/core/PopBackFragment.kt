package com.pxy.visaz.core


import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.pxy.visaz.core.extension.initBackNavigationHandler


open class PopBackFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().initBackNavigationHandler {
            findNavController().popBackStack()
        }
    }
}