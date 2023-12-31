package com.skydevices.marketcalc.model.database.produtoDAO

import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.Produto

interface iProdutoDAO {

    fun salvarProduto (produto: Produto): Boolean
    fun atualizar (produto: Produto): Boolean
    fun remover (int: Int): Boolean
    fun listar(id: Int): List<Produto>
    fun iniciarCompra() : Int
    fun salvarCompra (compra: Compra): Boolean
    fun listaHistorico(): List<Compra>

}