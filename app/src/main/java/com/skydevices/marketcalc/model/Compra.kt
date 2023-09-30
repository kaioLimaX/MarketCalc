package com.skydevices.marketcalc.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Date
import java.time.LocalDate

@Parcelize
class Compra(
    val id_compra: Int,
    val status_compra: Int,
    val data_compra: LocalDate,
    val total_compra: Double
) : Parcelable