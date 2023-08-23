package com.skydevices.marketcalc.database

import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.Produto

interface iProdutoDAO {

    fun salvar (produto: Produto): Boolean
    fun atualizar (produto: Produto): Boolean
    fun remover (int: Int): Boolean
    fun listar(): List<Produto>
    fun salvarCompra (compra: Compra): Boolean
    fun listaHistorico(): List<Compra>

}