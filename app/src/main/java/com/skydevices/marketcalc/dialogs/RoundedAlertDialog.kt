

package com.skydevices.marketcalc.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.skydevices.marketcalc.R
import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.Produto

class RoundedAlertDialog(
    private val title : String,
    private val message : String,
    private val textButton :  String,
    private val icon : Int,
    private val onClickPositive: () -> Unit
) : DialogFragment() {
    //...

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
                //empty
            }
            .create()
    }
}