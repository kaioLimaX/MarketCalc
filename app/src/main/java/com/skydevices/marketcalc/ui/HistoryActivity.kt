package com.skydevices.marketcalc.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skydevices.marketcalc.adapter.compraAdapter
import com.skydevices.marketcalc.adapter.itemAdapter
import com.skydevices.marketcalc.database.ProdutoDAO
import com.skydevices.marketcalc.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private var itemAdapter: compraAdapter? = null

    val binding by lazy {
        ActivityHistoryBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getSupportActionBar()?.setTitle("Historico")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        listarCompras()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun listarCompras() {
            val produtoDAO = ProdutoDAO(this)
            val listaProdutos = produtoDAO.listaHistorico()
            itemAdapter = compraAdapter()
            itemAdapter?.adicionarLista(listaProdutos)
            binding.rvHistorico.adapter = itemAdapter
            binding.rvHistorico.layoutManager = LinearLayoutManager(this)

            binding.rvHistorico.addItemDecoration(
                DividerItemDecoration(
                    this,
                    RecyclerView.VERTICAL
                )
            )


    }
}