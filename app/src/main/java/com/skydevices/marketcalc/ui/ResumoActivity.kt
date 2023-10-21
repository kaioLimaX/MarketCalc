package com.skydevices.marketcalc.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.adapter.produtoAdapter
import com.skydevices.marketcalc.databinding.ActivityResumoBinding
import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.Produto
import com.skydevices.marketcalc.presenter.resumo.ResumoHome
import com.skydevices.marketcalc.presenter.resumo.ResumoPresenter

@ExperimentalBadgeUtils
class ResumoActivity : AbstractActivity() , ResumoHome {

    private lateinit var binding: ActivityResumoBinding

    private lateinit var resumoPresenter: ResumoPresenter

    private var produtoAdapter: produtoAdapter? = null

    private var linearLayoutManager: LinearLayoutManager? = null

    private var idRecebido = 0

    override fun getLayout(): ViewBinding {
        binding = ActivityResumoBinding.inflate(layoutInflater)

        return binding
    }

    override fun onInject() {
        iniciarView()
        recuperarDadosActivity()
        configRecycler()

        resumoPresenter = ResumoPresenter(this, applicationContext, produtoAdapter!!)
        resumoPresenter.exibirCompra(idRecebido)

    }

    private fun iniciarView() {
        setSupportActionBar(binding.includeToolbar.toolbar)

        binding.includeToolbar.toolbarTitle.text = "Resumo da Compra"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onStartActivity() {

    }

    private fun recuperarDadosActivity() {
        val extrasId = intent?.extras
        if (extrasId != null) {
            val compra: Compra? = intent.getParcelableExtra("compra")
            idRecebido = compra!!.id_compra
        } else {
            Log.i("info_teste", "onStart: $idRecebido")
        }
    }

    private fun configRecycler() {
        val recycler = binding.rvResumo
        val constraintLayout = null
        produtoAdapter = produtoAdapter(recycler,constraintLayout) { editProduto ->

        }

        linearLayoutManager = LinearLayoutManager(this@ResumoActivity)

        with(binding.rvResumo) {
            adapter = produtoAdapter
            layoutManager = linearLayoutManager
        }

    }

    override fun exibirCompra(compra: MutableList<Produto>) {
        produtoAdapter?.adicionarLista(compra)
    }

    @SuppressLint("SetTextI18n")
    override fun exibirTotal(valorTotal: Double) {
        Log.i("TAG", "exibirTotal: $valorTotal")
        binding.textView4.text = "R$ ${String.format("%.2f", valorTotal)}"
    }
}