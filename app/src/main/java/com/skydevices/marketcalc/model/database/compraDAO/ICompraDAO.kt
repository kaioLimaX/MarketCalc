package com.skydevices.marketcalc.model.database.compraDAO

import com.skydevices.marketcalc.model.Compra

interface ICompraDAO {

    fun iniciarCompra() : Int
    fun listarCompra(): List<Compra>

    fun removerCompra(id: Int): Boolean
}