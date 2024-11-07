package com.pxy.visaz.ui.common

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ProgressDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Loading...")
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false) // Optional: set true if you want it to be cancelable
        return progressDialog
    }

    companion object {
        const val TAG = "ProgressDialogFragment"
        fun newInstance(): ProgressDialogFragment {
            return ProgressDialogFragment()
        }
    }
}
