package com.skydevices.marketcalc.model

class Produto(
    val id_produto: Int,
    val id_compra: Int,
    var descricao: String?,
    var valor_produto: Double,
    var qtd_produto: Int
)