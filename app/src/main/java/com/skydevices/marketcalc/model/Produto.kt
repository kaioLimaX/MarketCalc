package com.skydevices.marketcalc.model

class Produto(
    val id_produto: Int,
    val id_compra: Int,
    val descricao: String?,
    val valor_produto: Double,
    val qtd_produto: Int
)