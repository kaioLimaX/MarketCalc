package com.skydevices.marketcalc.presenter.compra

import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.Produto

interface CompraHome {

    fun exibirCompra(produto : List<Produto>)
    fun exibirTotal(valorTotal: Double)
    fun atualizarBadge(numeroItens: Int)
    fun exibirFinalizar(compra: Compra)
    fun adicionarProduto(idRecebido : Int,valor: Double, quantidade: Int, descricao: String)
    fun atualizarProduto(produto: Produto)
    fun excluirProduto(produto : Produto)



}