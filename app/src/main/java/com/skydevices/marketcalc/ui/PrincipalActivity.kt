package com.skydevices.marketcalc.ui

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.Utils.swipeExcluir.SwipeCallback
import com.skydevices.marketcalc.adapter.compraAdapter
import com.skydevices.marketcalc.databinding.ActivityPrincipalBinding
import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.presenter.principal.PrincipalHome
import com.skydevices.marketcalc.presenter.principal.PrincipalPresenter

@ExperimentalBadgeUtils
class PrincipalActivity : AbstractActivity(), PrincipalHome {

    private lateinit var binding: ActivityPrincipalBinding

    private lateinit var principalPresenter: PrincipalPresenter

    private var compraAdapter: compraAdapter? = null


    override fun getLayout(): ViewBinding {
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        return binding
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onInject() {
        inicializarView()
        configRecycler()




        with(binding) {
            floatingActionButton.setOnClickListener {
                iniciarCompra()
            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartActivity() {
        principalPresenter = PrincipalPresenter(this, applicationContext)
        principalPresenter.recuperarCompras()
    }

    private fun configRecycler() {
        compraAdapter = compraAdapter(this) { compra ->
            if (compra.status_compra == 1) {
                iniciarIntent(compra, ResumoActivity::class.java)
            } else {
                iniciarIntent(compra, CompraActivity::class.java)
            }
        }
        with(binding.rvCompras) {
            adapter = compraAdapter
            layoutManager = LinearLayoutManager(this@PrincipalActivity)
        }

        /* swipeCallback = SwipeCallback(this,this)

         val itemTouchHelper = ItemTouchHelper(swipeCallback)
         itemTouchHelper.attachToRecyclerView(binding.rvCompras)*/

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


}