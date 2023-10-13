package com.skydevices.marketcalc.Utils

import java.text.SimpleDateFormat

class Formatters {
    fun formatarData(data: String): String {
        val formatoEntrada = SimpleDateFormat("yyyy-MM-dd")
        val formatoSaida = SimpleDateFormat("dd/MM/yyyy")
        val dataObj = formatoEntrada.parse(data)
        return formatoSaida.format(dataObj)
    }

    fun formatarStringToDouble(texto : String) : Double{
        val texto = texto.replace("[^\\d,]".toRegex(), "").replace(".", "").replace(",", ".")
        return texto.toDouble()
    }
}