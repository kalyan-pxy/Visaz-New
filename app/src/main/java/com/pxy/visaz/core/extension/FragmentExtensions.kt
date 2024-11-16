package com.pxy.visaz.core.extension

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.pxy.esubject.ui.common.AppDialogFragment
import com.pxy.esubject.ui.common.DialogModel
import com.pxy.visaz.core.utils.DateUtils
import java.io.File

fun FragmentActivity.initBackNavigationHandler(function: () -> Unit) {
    onBackPressedDispatcher
        .addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                function.invoke()
            }
        })
}

fun Fragment.showKeyboard() {
    view?.let { v ->
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Fragment.hideKeyboard() {
    view?.let { v ->
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}

fun Fragment.uriToFile(uri: Uri): File? {
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = requireActivity().contentResolver.query(uri, filePathColumn, null, null, null)
    cursor?.moveToFirst()
    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
    val filePath = cursor?.getString(columnIndex!!)
    cursor?.close()
    return if (filePath != null) File(filePath) else null
}

fun Fragment.uriToFilePath(uri: Uri): String? {
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = requireActivity().contentResolver.query(uri, filePathColumn, null, null, null)
    cursor?.moveToFirst()
    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
    val filePath = cursor?.getString(columnIndex!!)
    cursor?.close()
    return filePath
}

fun Fragment.showDialog(
    title: String,
    message: String,
    positiveButtonText: String = "Yes",
    negativeButtonText: String = "No",
    onPositiveClick: (() -> Unit)? = null,
    onNegativeClick: (() -> Unit)? = null
) {
    AppDialogFragment.newInstance(
        dialogModel = DialogModel(
            title = title,
            message = message,
            positiveButtonText = positiveButtonText,
            negativeButtonText = negativeButtonText
        ),
        onPositiveClick = {
            onPositiveClick?.invoke()
        },
        onNegativeClick = {
            onNegativeClick?.invoke()
        }
    ).show(this.childFragmentManager, "TwoButtonDialog")
}


fun Fragment.showDatePicker(onDateSelected: (String) -> Unit) {

    // Set constraints to disable past dates
    val constraintsBuilder = CalendarConstraints.Builder()
        .setValidator(DateValidatorPointForward.now())

    // Create a MaterialDatePicker with constraints
    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select a date")
        .setCalendarConstraints(constraintsBuilder.build())
        .build()

    // Show the date picker
    datePicker.show(childFragmentManager, "DATE_PICKER")

    // Handle the date selection
    datePicker.addOnPositiveButtonClickListener { selection ->
        val selectedDate = DateUtils.convertToDisplayDate(selection)
        onDateSelected(selectedDate)
    }
}