package com.skydevices.marketcalc.presenter.compra

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.database.ProdutoDAO
import com.skydevices.marketcalc.model.Produto

@ExperimentalBadgeUtils
class CompraPresenter(
    private val compraHome: CompraHome,
    private val context: Context,
) {
    val produtoDAO = ProdutoDAO(context)

    fun exibirCompra(id: Int) {
        val listaCompra = produtoDAO.listar(id)
        compraHome.exibirCompra(listaCompra)


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
            Log.i("info_db", "Produto salvo com sucesso ")
            exibirCompra(produto.id_compra)
        } else {
            Log.i("info_db", "Erro ao salvar Produto ")
        }

    }

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

    fun exibirDialogFinalizar(compra:Compra){
        compraHome.exibirFinalizar(compra)
    }


}


