package com.skydevices.marketcalc.presenter.compra

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.Utils.Contants
import com.skydevices.marketcalc.adapter.produtoAdapter
import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.Produto
import com.skydevices.marketcalc.model.database.produtoDAO.ProdutoDAO

@ExperimentalBadgeUtils
class CompraPresenter(
    private val compraHome: CompraHome,
    private val context: Context,
    private val adapter: produtoAdapter
) {
    val produtoDAO = ProdutoDAO(context)

    var editMode = false

    var count = 1

    var listaProdutos = mutableListOf<Produto>()


    fun exibirCompra(id: Int) {
        listaProdutos = produtoDAO.listar(id)
        compraHome.exibirCompra(listaProdutos)

    }

    fun excluirProduto(position: Int) {
        val produto = listaProdutos.get(position)
        produtoDAO.remover(produto.id_produto)
        listaProdutos.removeAt(position)

        val total = calcularValorTotal(listaProdutos)

        compraHome.atualizarBadge(listaProdutos.size)
        compraHome.exibirTotal(total)
        compraHome.limparCampos()

    }


    fun atualizarBadgeETotal(listaCompra: List<Produto>) {
        if (listaCompra.isNotEmpty()) {
            val resultado = calcularValorTotal(listaCompra)
            compraHome.exibirTotal(resultado)
            compraHome.atualizarBadge(listaCompra.size)
        } else {
            compraHome.exibirTotal(0.0)
            compraHome.atualizarBadge(0)
        }
    }

    fun processarModo(
        idProduto: Int,
        idCompra: Int,
        valor: Double,
        quantidade: Int,
        descricao: String
    ) {
        compraHome.showErrorField(null)


        if(validarCampos(valor)){
            val produto = Produto(
                if (idProduto != -1) idProduto else -1,
                idCompra,
                descricao,
                valor,
                quantidade
            )

            if (editMode) {
                atualizarProduto(produto)
            } else {
                adicionarProduto(produto)
            }
        }

    }

    fun adicionarProduto(produto: Produto) {


        if (produtoDAO.salvarProduto(produto)) {
            editMode = false
            adapter.adicionarItem(produto)

            val lista = adapter.recuperarLista()
            compraHome.atualizarBadge(lista.size)

            val resultado = calcularValorTotal(lista)

            produtoDAO.atualizarTotal()
            compraHome.exibirTotal(resultado)
            compraHome.limparCampos()
            compraHome.scrollRecyclerViewToPosition(0)

        } else {
            Log.i("info_db", "Erro ao salvar Produto")
        }

    }

    fun atualizarProduto(produto: Produto) {
        if (produtoDAO.atualizar(produto)) {
            editMode = false

            adapter.atualizarItem(produto)

            val lista = adapter.recuperarLista()
            compraHome.atualizarBadge(lista.size)

            val resultado = calcularValorTotal(lista)
            produtoDAO.atualizarTotal()
            compraHome.exibirTotal(resultado)
            compraHome.limparCampos()

        } else {
            Log.i("info_db", "Erro ao salvar Produto")
        }

    }

    fun calcularValorTotal(listaCompra: List<Produto>): Double {
        var resultado = 0.0
        for (produto in listaCompra) {
            val resultadoItem = produto.valor_produto * produto.qtd_produto
            resultado += resultadoItem
        }
        return resultado
    }

    fun modoEdicao(produto: Produto) {

        compraHome.modoEdicao(produto)
        editMode = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun finalizarCompra(compra: Compra) {

        if (!listaProdutos.isEmpty()) {
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

    fun validarCampos(campo: Double): Boolean {
        if (campo <= 0.00) {
            compraHome.showErrorField("insira um valor valido")
            return false
        }  else {
            return true
        }
    }

    fun incrementCounter() {
        if (count < Contants.maxValue) {
            count++
            compraHome.updateCounter(count)
        }

    }

    fun decrementCounter() {
        if (count > Contants.minValue) {
            count--
            compraHome.updateCounter(count)
        }
    }


}


