package com.skydevices.marketcalc.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.sql.Date
import java.time.LocalDate

@Parcelize
class Compra(
    var id_compra: Int,
    val status_compra: Int,
    val data_compra: LocalDate,
    val total_compra: Double
) : Parcelable