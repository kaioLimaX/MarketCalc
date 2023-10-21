package com.skydevices.marketcalc.presenter.principal

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.Produto
import com.skydevices.marketcalc.model.database.compraDAO.CompraDAO
import com.skydevices.marketcalc.model.database.produtoDAO.ProdutoDAO
import com.skydevices.marketcalc.ui.CompraActivity
import java.time.LocalDate

@ExperimentalBadgeUtils
class PrincipalPresenter(
    private val principalHome: PrincipalHome,
    private val context: Context,


    ) {


    var listaCompras = mutableListOf<Compra>()

    val compraDAO = CompraDAO(context)

    @RequiresApi(Build.VERSION_CODES.O)
    fun recuperarCompras() {
        principalHome.verificarTermosdeUso()
        val listaCompra = compraDAO.listarCompra()
        principalHome.exibirCompras(listaCompra)
        this.listaCompras = listaCompra



    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun novaCompra() {
        principalHome.iniciarIntent(null, CompraActivity::class.java)
        /*val retornoDao = compraDAO.iniciarCompra()

        if (retornoDao != 0) {
            val compra = Compra(-1, 0, LocalDate.now(), 0.0)
            compra.id_compra = retornoDao
            principalHome.iniciarIntent(compra, CompraActivity::class.java)
        } else {
            Toast.makeText(context, "erro ao iniciar compra", Toast.LENGTH_SHORT).show()
        }*/

    }

    fun excluirCompra(position: Int) {
        val produto = listaCompras.get(position)
        compraDAO.removerCompra(produto.id_compra)
        listaCompras.removeAt(position)

    }

     fun salvarAceitacaoTermosDeUso() {

        val sharedPreferences = context.getSharedPreferences("Preferencias", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("termos_aceitos", true)
        editor.apply()
    }

     fun verificarAceitacaoTermosDeUso(): Boolean {
        val sharedPreferences = context.getSharedPreferences("Preferencias", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("termos_aceitos", false)
    }

}
