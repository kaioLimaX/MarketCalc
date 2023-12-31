package com.skydevices.marketcalc.Utils.dialogUtil

import com.skydevices.marketcalc.R
import com.skydevices.marketcalc.Utils.Contants

class DialogData {
    companion object {
        val dialogExcluir = DialogInfo(
            title = "Excluir item",
            message = "você tem certeza que deseja excluir este item ?",
            buttonText = "Excluir",
            iconResId = R.drawable.ic_trash_24
        )

        val dialogConcluir = DialogInfo(
            title = "Concluir compra",
            message = "Deseja concluir esta compra ?",
            buttonText = "concluir",
            iconResId = R.drawable.ic_shopping_cart_check_24
        )

        val dialogTermos = DialogInfo(
            title = "Termos de uso",
            message = Contants.mensagemTermos,
            buttonText = "aceitar",
            iconResId = R.drawable.ic_exclamation_24
        )
    }
}