package com.skydevices.marketcalc.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.R
import com.skydevices.marketcalc.adapter.itemAdapter
import com.skydevices.marketcalc.database.ProdutoDAO
import com.skydevices.marketcalc.databinding.ActivityMainBinding
import com.skydevices.marketcalc.model.Compra
import java.security.AccessController.getContext
import java.time.LocalDate


@ExperimentalBadgeUtils class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)

    }
    val content = this

    private var itemAdapter: itemAdapter? = null

    private lateinit var badgeDrawable: BadgeDrawable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        badgeDrawable = BadgeDrawable.create(this@MainActivity)

        getSupportActionBar()?.setTitle(R.string.app_name)
        setContentView(binding.root)
        with(binding) {
            fabAdicionar.setOnClickListener {
                adicionar()
            }

        }


    }

    override fun onStart() {
        super.onStart()
        listar()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_compras, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_close -> {
                finish()
                return true
            }
            R.id.action_done ->{
                exibirFinalizar(this)

            }
            R.id.action_history -> {
                historico()
                return true
            }

        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun exibirFinalizar(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Concluir compra")
        builder.setMessage("Deseja concluir sua compra e encerrar o carrinho?"
        )
        builder.setPositiveButton(
            "Concluir"
        ) { dialog, which ->

            salvarCompra()
            dialog.dismiss()
        }
        builder.setNegativeButton(
            "Continuar"
        ) { dialog, which ->
            dialog.dismiss()

        }
        val dialog = builder.create()
        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun salvarCompra() {

        val valorTotal = binding.txtTotal.text.toString()
        val valorSemPrefixo = valorTotal.replace("R$", "").trim().toDouble()
        val dataAtual = LocalDate.now()
        val produtoDAO = ProdutoDAO(content)
        val compra = Compra(
            -1,
            valorSemPrefixo,
            dataAtual
        )
        if (produtoDAO.salvarCompra(compra)) {
            listar()
            badgeDrawable.isVisible = false

            Toast.makeText(content, "Compra salva com sucesso", Toast.LENGTH_SHORT).show()

        } else {

            Toast.makeText(content, "Falha ao salvar compra", Toast.LENGTH_SHORT).show()
        }
    }

    private fun listar() {
        var resultado = 0.00
        val produtoDAO = ProdutoDAO(this)
        val listaProdutos = produtoDAO.listar()
        itemAdapter = itemAdapter { id -> confirmarExclusao(id) }
        itemAdapter?.adicionarLista(listaProdutos)
        binding.rvLista.adapter = itemAdapter
        binding.rvLista.layoutManager = LinearLayoutManager(this)

        binding.rvLista.addItemDecoration(
            DividerItemDecoration(
                this,
                RecyclerView.VERTICAL
            )
        )


        if (listaProdutos.isNotEmpty()) {
            listaProdutos.forEach { produto ->
                Log.i("info_db", "${produto.valor_produto} + ${produto.qtd_produto} ")

                val resultadoItem = produto.valor_produto * produto.qtd_produto
                resultado += resultadoItem
                Log.i("info_db", "$resultado ")

                binding.txtTotal.text = "R$ ${String.format("%.2f", resultado)}"

            }
        }

        binding.fabAdicionar.doOnPreDraw {

            badgeDrawable.setHorizontalOffset(55)
            badgeDrawable.setVerticalOffset(40)
            badgeDrawable.number = listaProdutos.size
            badgeDrawable.isVisible = listaProdutos.size != 0



            BadgeUtils.attachBadgeDrawable(badgeDrawable, binding.fabAdicionar, null)
        }



    }

    private fun confirmarExclusao(id: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar exclusão")
        builder.setMessage("Deseja realmente excluir o produto?"
        )
        builder.setPositiveButton(
            "Sim"
        ) { dialog, which ->
            val produtoDAO = ProdutoDAO(this)
            if(produtoDAO.remover(id)){
                listar()
                Toast.makeText(content, "Sucesso ao remover produto", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(content, "Falha ao remover produto", Toast.LENGTH_SHORT).show()
            }


        }
        builder.setNegativeButton(
            "Não"
        ) { dialog, which ->


        }
        val dialog = builder.create()
        dialog.show()

    }

    private fun adicionar() {
        intent = Intent(this, AdicionarTarefaActivity::class.java)
        startActivity(intent)
    }

    private fun historico() {
        intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }


}