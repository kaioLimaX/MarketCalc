package com.skydevices.marketcalc.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.Utils.Formatters
import com.skydevices.marketcalc.Utils.MaskMoney
import com.skydevices.marketcalc.Utils.dialogs.DialogData
import com.skydevices.marketcalc.Utils.dialogs.RoundedAlertDialog
import com.skydevices.marketcalc.Utils.swipeExcluir.SwipeActionListener

import com.skydevices.marketcalc.Utils.swipeExcluir.SwipeCallback
import com.skydevices.marketcalc.adapter.produtoAdapter
import com.skydevices.marketcalc.databinding.ActivityCompraBinding
import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.Produto
import com.skydevices.marketcalc.presenter.compra.CompraHome
import com.skydevices.marketcalc.presenter.compra.CompraPresenter
import java.util.Locale

@ExperimentalBadgeUtils
class CompraActivity : AbstractActivity(), CompraHome, SwipeActionListener {

    private lateinit var binding: ActivityCompraBinding
    override fun getLayout(): ViewBinding {
        binding = ActivityCompraBinding.inflate(layoutInflater)
        return binding
    }

    private lateinit var swipeCallback: SwipeActionListener

    private lateinit var compraPresenter: CompraPresenter

    val content = this

    private var produtoAdapter: produtoAdapter? = null

    private var editMode = false

    private lateinit var badgeDrawable: BadgeDrawable

    private var idRecebido = 0

    lateinit var editCompra: Compra

    lateinit var editProduto: Produto

    private var txtQntIncremento = 1

    override fun onInject() {
        setContentView(binding.root)
        inicializarView()
        configRecycler()
        recuperarDadosActivity()

        badgeDrawable = BadgeDrawable.create(this)
        compraPresenter = CompraPresenter(this, applicationContext, produtoAdapter!!)
        compraPresenter.exibirCompra(idRecebido)

        binding.btnSalvar.setOnClickListener {
            val formatador = Formatters()
            val valor = formatador.formatarStringToDouble(binding.txtValor.text.toString())
            val quantidade = binding.txtQnd.text.toString().toInt()
            val descricao = binding.txtDescricao.text.toString()

            compraPresenter.adicionarProduto(
                idRecebido,
                valor,
                quantidade,
                descricao
            )
            binding.rvLista.scrollToPosition(0)
        }

        binding.fabAdicionar.setOnClickListener {
            compraPresenter.exibirDialogFinalizar(editCompra)
        }
    }

    override fun onStartActivity() {

    }

    private fun inicializarView() {
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val mLocale = Locale("pt", "BR")
        val editValor = binding.txtValor
        editValor.addTextChangedListener(MaskMoney(editValor, mLocale))

    }


    private fun recuperarDadosActivity() {
        val extrasId = intent?.extras
        if (extrasId != null) {
            val compra: Compra? = intent.getParcelableExtra("compra")
            editCompra = compra!!
            idRecebido = compra!!.id_compra
        } else {
            Log.i("info_teste", "onStart: $idRecebido")
        }
    }

    private fun configRecycler() {
        produtoAdapter = produtoAdapter{editProduto->

        }

        with(binding.rvLista){
            adapter = produtoAdapter
            layoutManager = LinearLayoutManager(this@CompraActivity)
        }

        val swipeCallback = SwipeCallback(this, this)

        // Anexe o SwipeCallback ao ItemTouchHelper
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvLista)
    }

    override fun finalizarActivity(){
        finish()
    }

    override fun exibirCompra(listaCompra: MutableList<Produto>) {

        produtoAdapter?.adicionarLista(listaCompra)
        compraPresenter.atualizarBadgeETotal(listaCompra)


    }

    override fun exibirTotal(valorTotal: Double) {
        binding.include.toolbarTitle.text = " R$ ${String.format("%.2f", valorTotal)}"
    }

    override fun atualizarBadge(numeroItens: Int) {
        binding.fabAdicionar.doOnPreDraw {

            badgeDrawable.number = numeroItens
            badgeDrawable.isVisible = numeroItens != 0

            BadgeUtils.attachBadgeDrawable(badgeDrawable, binding.fabAdicionar, null)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun exibirFinalizarCompra(compra: Compra) {
        val dialogFragment = RoundedAlertDialog(
            DialogData.dialogConcluir.title,
            DialogData.dialogConcluir.message,
            DialogData.dialogConcluir.buttonText,
            DialogData.dialogConcluir.iconResId,
            {//onPositive
                compraPresenter.finalizarCompra(compra)
            },
            {//onNegative

            }
        )
        dialogFragment.show(supportFragmentManager, "ExibirFinalizarDialog")
    }

    override fun adicionarProduto(idRecebido: Int,valor: Double,quantidade: Int, descricao: String) {

        compraPresenter.adicionarProduto(idRecebido, valor, quantidade, descricao)
    }

    override fun atualizarProduto(produto: Produto) {
        TODO("Not yet implemented")
    }

    override fun excluirProduto(position : Int) {
        compraPresenter.excluirProduto(position)

    }

    override fun onSwipeLeft(position: Int) {
        val dialogFragment = RoundedAlertDialog(
            DialogData.dialogExcluir.title,
            DialogData.dialogExcluir.message,
            DialogData.dialogExcluir.buttonText,
            DialogData.dialogExcluir.iconResId,
            {//onPositive
               val idProduto = position
                excluirProduto(idProduto)
                binding.rvLista.adapter?.notifyItemRemoved(position)

            },
            {//onNegative or Cancel
                binding.rvLista.adapter?.notifyItemChanged(position)
            }
        )
        dialogFragment.show(supportFragmentManager, "ExibirFinalizarDialog")
    }

}











