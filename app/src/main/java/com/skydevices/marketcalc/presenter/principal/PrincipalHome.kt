package com.skydevices.marketcalc.presenter.principal

import com.skydevices.marketcalc.model.Compra

interface PrincipalHome {
    fun exibirCompras(compra: List<Compra>)

    fun iniciarCompra()
    fun inicializarView()

    fun iniciarIntent(compra: Compra, activity: Class<*>)
}