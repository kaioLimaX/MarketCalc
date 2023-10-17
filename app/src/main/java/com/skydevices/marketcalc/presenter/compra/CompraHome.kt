package com.skydevices.marketcalc.presenter.compra

import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.Produto

interface CompraHome {

    fun exibirCompra(produto : MutableList<Produto>)
    fun exibirTotal(valorTotal: Double)
    fun atualizarBadge(numeroItens: Int)
    fun exibirFinalizarCompra(compra: Compra)
    fun finalizarActivity()
    fun adicionarProduto(idRecebido : Int,valor: Double, quantidade: Int, descricao: String)
    fun atualizarProduto(produto: Produto)
    fun excluirProduto(position: Int)
    fun onSwipeLeft(position: Int)



}