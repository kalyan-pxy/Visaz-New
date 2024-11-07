package com.pxy.visaz.core


import android.annotation.SuppressLint
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pxy.visaz.ui.common.ProgressDialogFragment


open class BaseFragment : Fragment() {

    private var progressDialog: DialogFragment? = null

    // Function to show progress dialog
    fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialogFragment.newInstance()
        }
        progressDialog?.show(childFragmentManager, ProgressDialogFragment.TAG)
    }

    // Function to hide progress dialog
    fun hideProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    fun toastSuccess(msg: String) {
        toast(msg)
    }

    fun toastError(msg: String) {
        toast(msg)
    }

    fun toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), msg, length).show()
    }

    fun goBack() {
        findNavController().popBackStack()
    }

    @SuppressLint("NewApi")
    fun hideSystemUI() {
        requireActivity().window?.insetsController?.let { controller ->
            controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    @SuppressLint("NewApi")
    fun showSystemUI() {
        requireActivity().window?.insetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
    }
}