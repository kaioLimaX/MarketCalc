package com.skydevices.marketcalc.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.adapter.produtoAdapter
import com.skydevices.marketcalc.model.database.produtoDAO.ProdutoDAO
import com.skydevices.marketcalc.databinding.ActivityResumoBinding
import com.skydevices.marketcalc.model.Compra

@ExperimentalBadgeUtils class ResumoActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityResumoBinding.inflate(layoutInflater)
    }
    private var itemAdapter: produtoAdapter? = null

    private var idRecebido = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        inicializarToolbar()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        val extrasId = intent?.extras
        if (extrasId != null) {
            val compra: Compra? = intent.getParcelableExtra("compra")

            idRecebido = compra!!.id_compra
            listar(idRecebido)



        } else {
            Log.i("info_teste", "onStart: $idRecebido")
        }

    }

    private fun inicializarToolbar() {
        binding.includeToolbar.toolbarTitle.text = "Resumo da compra"
        setSupportActionBar(binding.includeToolbar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //listarCompras()
    }


    fun listar(id: Int) {
        /*var resultado = 0.00
        val produtoDAO = ProdutoDAO(this)
        val listaProdutos = produtoDAO.listar(id)
        itemAdapter = produtoAdapter(
            onClickExcluir = { produto ->

            }
        ) { produto ->

        }
        itemAdapter?.adicionarLista(listaProdutos)
        binding.rvResumo.adapter = itemAdapter
        binding.rvResumo.layoutManager = LinearLayoutManager(this)

        *//*binding.rvLista.addItemDecoration(
            DividerItemDecoration(
                this,
                RecyclerView.VERTICAL
            )
        )*//*


        if (listaProdutos.isNotEmpty()) {
            listaProdutos.forEach { produto ->
                Log.i("info_db", "${produto.valor_produto} + ${produto.qtd_produto} ")

                val resultadoItem = produto.valor_produto * produto.qtd_produto
                resultado += resultadoItem
                Log.i("info_db", "$resultado ")
                binding.textView4.text = " R$ ${String.format("%.2f", resultado)}"

            }
        }else{
            binding.textView4.text = " R$ 00.00"
        }*/




    }
}