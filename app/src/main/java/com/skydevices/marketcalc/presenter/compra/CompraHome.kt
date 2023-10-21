package com.skydevices.marketcalc.presenter.compra

import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.Produto

interface CompraHome {

    //exibições
    fun exibirCompra(produto: MutableList<Produto>)
    fun exibirTotal(valorTotal: Double)
    fun atualizarBadge(numeroItens: Int)
    fun exibirFinalizarCompra(compra: Compra)
    fun finalizarActivity()
    fun modoEdicao(produto: Produto)


    fun adicionarProduto(idRecebido: Int, valor: Double, quantidade: Int, descricao: String)
    fun atualizarProduto(idProduto: Int)
    fun excluirProduto(position: Int)
    fun exibirToast(mensagem: String)
    fun limparCampos()
    fun updateCounter(count: Int)
    fun onSwipeLeft(position: Int)
    fun scrollRecyclerViewToPosition(position: Int)
    fun showErrorField(message: String?)


}