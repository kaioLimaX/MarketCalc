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
    var status_compra: Int,
    var data_compra: LocalDate,
    var total_compra: Double
) : Parcelable