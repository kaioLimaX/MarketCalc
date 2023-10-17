package com.skydevices.marketcalc.presenter.principal

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.database.produtoDAO.ProdutoDAO
import com.skydevices.marketcalc.ui.CompraActivity
import java.time.LocalDate

@ExperimentalBadgeUtils class PrincipalPresenter(
    private val principalHome: PrincipalHome,
    private val context: Context,


    ) {

    val compraDAO = ProdutoDAO(context)
        @RequiresApi(Build.VERSION_CODES.O)
        fun recuperarCompras(){
            val listaProdutos = compraDAO.listaHistorico()
            principalHome.exibirCompras(listaProdutos)


        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun novaCompra(){
            val retornoDao = compraDAO.iniciarCompra()

            if (retornoDao != 0) {
                val compra = Compra(-1, 0, LocalDate.now(), 0.0)
                compra.id_compra = retornoDao
                principalHome.iniciarIntent(compra,CompraActivity::class.java)
            } else {
                Toast.makeText(context, "erro ao iniciar compra", Toast.LENGTH_SHORT).show()
            }

        }

        }
