package com.skydevices.marketcalc.ui

import android.content.Intent
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.Utils.dialogUtil.DialogData
import com.skydevices.marketcalc.Utils.dialogUtil.RoundedAlertDialog
import com.skydevices.marketcalc.Utils.swipeExcluir.SwipeActionListener
import com.skydevices.marketcalc.Utils.swipeExcluir.SwipeCallback
import com.skydevices.marketcalc.adapter.compraAdapter
import com.skydevices.marketcalc.databinding.ActivityPrincipalBinding
import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.presenter.principal.PrincipalHome
import com.skydevices.marketcalc.presenter.principal.PrincipalPresenter

@ExperimentalBadgeUtils
class PrincipalActivity : AbstractActivity(), PrincipalHome, SwipeActionListener {

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
            fabNovaCompra.setOnClickListener {
                iniciarCompra()
            }
            btnIniciarCompra.setOnClickListener {
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
        val recycler = binding.rvCompras
        val constraintLayout = binding.cLemptyList
        compraAdapter = compraAdapter(recycler,constraintLayout) { compra ->
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

         val swipeCallback = SwipeCallback(this)

         val itemTouchHelper = ItemTouchHelper(swipeCallback)
         itemTouchHelper.attachToRecyclerView(binding.rvCompras)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun exibirCompras(compra: MutableList<Compra>) {
        compraAdapter?.adicionarLista(compra)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun iniciarCompra() {
        principalPresenter.novaCompra()


    }

    override fun excluirCompra(position: Int) {
        principalPresenter.excluirCompra(position)
    }


    override fun inicializarView() {
        setContentView(binding.root)
        binding.includeToolbar.toolbarTitle.text = "Soma do Mercado"


    }

    override fun iniciarIntent(compra: Compra?, activity: Class<*>) {
        val intent = Intent(this, activity)
        intent.putExtra("compra", compra)
        startActivity(intent)
    }

    override fun verificarTermosdeUso() {
        if (!principalPresenter.verificarAceitacaoTermosDeUso()){
            val dialogFragment = RoundedAlertDialog(
                DialogData.dialogTermos.title,
                DialogData.dialogTermos.message,
                DialogData.dialogTermos.buttonText,
                DialogData.dialogTermos.iconResId,
                {//onPositive
                principalPresenter.salvarAceitacaoTermosDeUso()

                },
                {//onNegative or Cancel
                   finish()
                }
            )
            dialogFragment.show(supportFragmentManager, "ExibirTermosDialog")
        }
    }

    override fun onSwipeLeft(pos: Int) {
        val dialogFragment = RoundedAlertDialog(
            DialogData.dialogExcluir.title,
            DialogData.dialogExcluir.message,
            DialogData.dialogExcluir.buttonText,
            DialogData.dialogExcluir.iconResId,
            {//onPositive
                val idProduto = pos
                excluirCompra(idProduto)
                compraAdapter?.removerItem(pos)

            },
            {//onNegative or Cancel
                binding.rvCompras.adapter?.notifyItemChanged(pos)
            }
        )
        dialogFragment.show(supportFragmentManager, "ExibirFinalizarDialog")

    }


}