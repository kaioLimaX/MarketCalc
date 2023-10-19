

package com.skydevices.marketcalc.Utils.dialogUtil

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.skydevices.marketcalc.R

class RoundedAlertDialog(
    private val title : String,
    private val message : String,
    private val textButton :  String,
    private val icon : Int,
    private val onClickPositive: () -> Unit,
    private val onClickNegative: () -> Unit

) : DialogFragment() {
    //...

    @SuppressLint("DialogFragmentCallbacksDetector")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(
            requireActivity(),
            R.style.MaterialAlertDialog_rounded
        )
            .setMessage(message)
            .setTitle(title)
            .setIcon(icon)
            .setPositiveButton(textButton) { dialog, which ->
                onClickPositive.invoke()
            }
            .setNegativeButton("CANCELAR") { dialog, which ->
                onClickNegative.invoke()
            }
            .setOnCancelListener(){
                onClickNegative.invoke()
            }
            .create()
    }
}