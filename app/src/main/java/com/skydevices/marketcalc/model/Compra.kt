package com.skydevices.marketcalc.model

import java.sql.Date
import java.time.LocalDate

class Compra(
    val id_compra: Int,
    val valor_compra: Double,
    val data_compra: LocalDate
)