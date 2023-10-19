package com.skydevices.marketcalc.presenter.principal

import com.skydevices.marketcalc.model.Compra

interface PrincipalHome {
    fun exibirCompras(compra: MutableList<Compra>)

    fun iniciarCompra()

    fun excluirCompra(position: Int)
    fun inicializarView()

    fun iniciarIntent(compra: Compra, activity: Class<*>)
}