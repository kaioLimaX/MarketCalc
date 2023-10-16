package com.skydevices.marketcalc.Utils.dialogs

import com.skydevices.marketcalc.R

class DialogData {
    companion object {
        val dialogExcluir = DialogInfo(
            title = "Excluir item",
            message = "você tem certeza que deseja excluir este item ?",
            buttonText = "Excluir",
            iconResId = R.drawable.trash_24
        )

        val dialogConcluir = DialogInfo(
            title = "Concluir compra",
            message = "Deseja concluir esta compra ?",
            buttonText = "concluir",
            iconResId = R.drawable.shopping_cart_check_24
        )
    }
}