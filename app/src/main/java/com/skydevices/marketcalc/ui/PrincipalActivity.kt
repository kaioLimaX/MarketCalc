package com.skydevices.marketcalc.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.adapter.compraAdapter
import com.skydevices.marketcalc.database.ProdutoDAO
import com.skydevices.marketcalc.databinding.ActivityPrincipalBinding
import com.skydevices.marketcalc.model.Compra
import java.time.LocalDate

@ExperimentalBadgeUtils
class PrincipalActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityPrincipalBinding.inflate(layoutInflater)
    }

    private var itemAdapter: compraAdapter? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            floatingActionButton.setOnClickListener {
                iniciarCompra()

            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        inicializarToolbar()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun iniciarCompra() {
        val produtoDAO = ProdutoDAO(this)
        val dataAtual = LocalDate.now()

        val compra = Compra(
            -1,
            0,
            dataAtual,
            0.0
        )

        val retornoDao = produtoDAO.iniciarCompra(compra)
        if (retornoDao != 0) {
            iniciarIntent(retornoDao)
        } else {
            Toast.makeText(this, "Falha ao iniciar compra", Toast.LENGTH_SHORT).show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun inicializarToolbar() {
        binding.includeToolbar.toolbarTitle.text = "Soma Mercado"

        listarCompras()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun listarCompras() {
        val produtoDAO = ProdutoDAO(this)
        val listaProdutos = produtoDAO.listaHistorico()
        itemAdapter = compraAdapter { id ->

            iniciarIntent(id.toInt())
        }
        itemAdapter?.adicionarLista(listaProdutos)
        binding.rvCompras.adapter = itemAdapter
        binding.rvCompras.layoutManager = LinearLayoutManager(this)

        /*binding.rvCompras.addItemDecoration(
            DividerItemDecoration(
                this,
                RecyclerView.VERTICAL
            )
        )*/


    }

    @SuppressLint("SuspiciousIndentation")
    fun iniciarIntent(idCompra: Int) {
        intent = Intent(this, CompraActivity::class.java)
        intent.putExtra("id", idCompra)
        startActivity(intent)
    }
}