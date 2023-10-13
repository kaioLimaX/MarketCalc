package com.skydevices.marketcalc.presenter.principal

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.model.database.ProdutoDAO

@ExperimentalBadgeUtils class PrincipalPresenter(
    private val principalHome: PrincipalHome,
    private val context: Context,


    ) {

    val produtoDAO = ProdutoDAO(context)
        @RequiresApi(Build.VERSION_CODES.O)
        fun recuperarCompras(){
            val listaProdutos = produtoDAO.listaHistorico()
            principalHome.exibirCompras(listaProdutos)


        }



        }
