package com.skydevices.marketcalc.presenter.resumo

import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.Produto

interface ResumoHome {
    fun exibirCompra(produto: MutableList<Produto>)
    fun exibirTotal(valorTotal: Double)

}