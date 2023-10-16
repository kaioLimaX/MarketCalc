package com.skydevices.marketcalc.ui

import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.Utils.swipe.SwipeActionListener
import com.skydevices.marketcalc.Utils.swipe.SwipeCallback
import com.skydevices.marketcalc.adapter.compraAdapter
import com.skydevices.marketcalc.databinding.ActivityPrincipalBinding
import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.presenter.principal.PrincipalHome
import com.skydevices.marketcalc.presenter.principal.PrincipalPresenter
import java.time.LocalDate

@ExperimentalBadgeUtils
class PrincipalActivity : AbstractActivity(), PrincipalHome, SwipeActionListener {

    private lateinit var binding: ActivityPrincipalBinding

    private lateinit var principalPresenter: PrincipalPresenter

    private var compraAdapter: compraAdapter? = null

    private lateinit var swipeCallback: SwipeCallback


    override fun getLayout(): ViewBinding {
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        return binding
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onInject() {
        inicializarView()
        configRecycler()


        principalPresenter = PrincipalPresenter(this, applicationContext)
        principalPresenter.recuperarCompras()

        with(binding){
            floatingActionButton.setOnClickListener {
                iniciarCompra()
            }
        }


    }

    private fun configRecycler() {
        compraAdapter = compraAdapter(this) { compra ->
            if (compra.status_compra == 1) {
               iniciarIntent(compra, ResumoActivity::class.java)
            } else {
                iniciarIntent(compra, CompraActivity::class.java)
            }
        }
        with(binding.rvCompras){
            adapter = compraAdapter
            layoutManager = LinearLayoutManager(this@PrincipalActivity)
        }

        swipeCallback = SwipeCallback(this,this)

        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvCompras)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun exibirCompras(compra: List<Compra>) {

        compraAdapter?.adicionarLista(compra)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun iniciarCompra() {
        principalPresenter.novaCompra()


    }


    override fun inicializarView() {
        setContentView(binding.root)

        binding.includeToolbar.toolbarTitle.text = "Soma Mercado"



    }

    override fun iniciarIntent(compra: Compra, activity: Class<*>) {
        val intent = Intent(this, activity)
        intent.putExtra("compra", compra)
        startActivity(intent)
    }

    override fun onSwipeLeft(position: Int) {
        Toast.makeText(this, "$position", Toast.LENGTH_SHORT).show()
    }
}