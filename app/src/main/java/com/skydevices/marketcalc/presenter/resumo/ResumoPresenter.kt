package com.skydevices.marketcalc.presenter.resumo

import android.content.Context
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.adapter.produtoAdapter
import com.skydevices.marketcalc.model.Produto
import com.skydevices.marketcalc.model.database.produtoDAO.ProdutoDAO
import com.skydevices.marketcalc.presenter.compra.CompraHome

@ExperimentalBadgeUtils
class ResumoPresenter(
    private val resumoHome: ResumoHome,
    private val context: Context,
    private val adapter: produtoAdapter
) {

    val produtoDAO = ProdutoDAO(context)

    var listaProdutos = mutableListOf<Produto>()

    fun exibirCompra(id: Int) {
        listaProdutos = produtoDAO.listar(id)
        resumoHome.exibirCompra(listaProdutos)

        val resultado = calcularValorTotal(listaProdutos)
        resumoHome.exibirTotal(resultado)

    }

    fun calcularValorTotal(listaCompra: List<Produto>): Double {
        var resultado = 0.0
        for (produto in listaCompra) {
            val resultadoItem = produto.valor_produto * produto.qtd_produto
            resultado += resultadoItem
        }
        return resultado
    }


}