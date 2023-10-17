package com.skydevices.marketcalc.presenter.compra

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.adapter.produtoAdapter
import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.database.produtoDAO.ProdutoDAO
import com.skydevices.marketcalc.model.Produto

@ExperimentalBadgeUtils
class CompraPresenter(
    private val compraHome: CompraHome,
    private val context: Context,
    private val adapter: produtoAdapter
) {
    val produtoDAO = ProdutoDAO(context)

    var listaProdutos = mutableListOf<Produto>()

    fun exibirCompra(id: Int) {
        listaProdutos = produtoDAO.listar(id)
        compraHome.exibirCompra(listaProdutos)


    }

    fun excluirProduto(position: Int) {
        val produto = listaProdutos.get(position)
        produtoDAO.remover(produto.id_produto)
        listaProdutos.removeAt(position)

        val total = produtoDAO.calcularValorTotal(listaProdutos)

        compraHome.atualizarBadge(listaProdutos.size)
        compraHome.exibirTotal(total)

    }


    fun atualizarBadgeETotal(listaCompra: List<Produto>) {
        if (listaCompra.isNotEmpty()) {
            val resultado = produtoDAO.calcularValorTotal(listaCompra)
            compraHome.exibirTotal(resultado)
            compraHome.atualizarBadge(listaCompra.size)
        } else {
            compraHome.exibirTotal(0.0)
            compraHome.atualizarBadge(0)
        }
    }

    fun adicionarProduto(idRecebido: Int, valor: Double, quantidade: Int, descricao: String) {
        val produto = Produto(
            -1,
            idRecebido,
            descricao,
            valor,
            quantidade
        )




        if (produtoDAO.salvarProduto(produto)) {
            listaProdutos.add(produto)
            compraHome.atualizarBadge(listaProdutos.size)

            val resultado = produtoDAO.calcularValorTotal(listaProdutos)
            produtoDAO.atualizarTotal()
            compraHome.exibirTotal(resultado)

            adapter.adicionarItem(produto)


        } else {
            Log.i("info_db", "Erro ao salvar Produto ")
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun finalizarCompra(compra: Compra) {

        if (compra.total_compra != 0.0) {
            if (produtoDAO.salvarCompra(compra)) {
                Toast.makeText(context, "Sucesso ao Concluir compra ", Toast.LENGTH_SHORT).show()
                compraHome.finalizarActivity()


            } else {
                Toast.makeText(context, "Falha ao Concluir compra", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "o carrinho esta vazio", Toast.LENGTH_SHORT).show()
        }

    }

    fun exibirDialogFinalizar(compra: Compra) {
        compraHome.exibirFinalizarCompra(compra)
    }


}


