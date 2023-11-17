package com.skydevices.marketcalc.model.database.compraDAO

import com.skydevices.marketcalc.model.Compra

interface ICompraDAO {


    fun listarCompra(): List<Compra>

    fun removerCompra(id: Int): Boolean
}