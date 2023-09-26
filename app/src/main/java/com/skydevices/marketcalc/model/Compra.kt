package com.skydevices.marketcalc.model

import java.sql.Date
import java.time.LocalDate

class Compra(
    val id_compra: Int,
    val status_compra: Int,
    val data_compra: LocalDate,
    val total_compra: Double
)