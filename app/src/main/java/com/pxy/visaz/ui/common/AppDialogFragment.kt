package com.pxy.esubject.ui.common

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AppDialogFragment(
    private val dialogModel: DialogModel,
    private val onPositiveClick: () -> Unit,
    private val onNegativeClick: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(dialogModel.title)
            .setMessage(dialogModel.message)
            .setPositiveButton(dialogModel.positiveButtonText) { _, _ ->
                onPositiveClick()
            }
            .setNegativeButton(dialogModel.negativeButtonText) { _, _ ->
                onNegativeClick()
            }

        return builder.create()
    }

    companion object {
        fun newInstance(
            dialogModel: DialogModel,
            onPositiveClick: () -> Unit,
            onNegativeClick: () -> Unit
        ): AppDialogFragment {
            return AppDialogFragment(
                dialogModel,
                onPositiveClick,
                onNegativeClick
            )
        }
    }
}
