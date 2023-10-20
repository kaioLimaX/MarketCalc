package com.skydevices.marketcalc.ui

import android.os.Build
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.textfield.TextInputLayout
import com.skydevices.marketcalc.Utils.Formatters
import com.skydevices.marketcalc.Utils.MaskMoney
import com.skydevices.marketcalc.Utils.dialogUtil.DialogData
import com.skydevices.marketcalc.Utils.dialogUtil.RoundedAlertDialog
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

    private lateinit var compraPresenter: CompraPresenter

    private var linearLayoutManager: LinearLayoutManager? = null

    val content = this

    private var produtoAdapter: produtoAdapter? = null

    private lateinit var badgeDrawable: BadgeDrawable

    private var idRecebido = 0

    lateinit var editCompra: Compra

    private var txtQntIncremento = 1

    var idProduto: Int = 0

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

            compraPresenter.processarModo(
                idProduto,
                idRecebido,
                valor,
                quantidade,
                descricao
            )

        }

        binding.rvLista.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


                val ultimoItemVisivel = linearLayoutManager?.findLastCompletelyVisibleItemPosition()
                val totalItens = recyclerView.adapter?.itemCount ?: 0

                // Verifique se a lista é grande o suficiente para permitir rolagem
                if (totalItens > linearLayoutManager!!.childCount) {
                    if (totalItens - 1 == ultimoItemVisivel) {
                        binding.fabAdicionar.hide()
                    } else {
                        binding.fabAdicionar.show()
                    }
                } else {
                    // Se a lista for pequena, você pode optar por mostrar o botão de qualquer maneira
                    binding.fabAdicionar.show()
                }
            }
        })

        binding.fabAdicionar.setOnClickListener {
            compraPresenter.exibirDialogFinalizar(editCompra)
        }

        with(binding) {
            txtInputQtd.setEndIconOnClickListener {
                compraPresenter.decrementCounter()
            }

            txtInputQtd.setStartIconOnClickListener {
                compraPresenter.incrementCounter()
            }
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
        produtoAdapter = produtoAdapter { editProduto ->
            compraPresenter.modoEdicao(editProduto)
            Log.i("info_teste", "editPrddsds: ${editProduto.id_produto}")

        }

        linearLayoutManager = LinearLayoutManager(this@CompraActivity)

        with(binding.rvLista) {
            adapter = produtoAdapter
            layoutManager = linearLayoutManager
        }

        val swipeCallback = SwipeCallback(this)

        // Anexe o SwipeCallback ao ItemTouchHelper
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvLista)
    }

    override fun finalizarActivity() {
        finish()
    }

    override fun modoEdicao(produto: Produto) {

        idProduto = produto.id_produto

        Log.i("info_teste", "idproduto: ${produto.id_produto}")
        with(binding) {
            val valor = String.format("%.2f", produto.valor_produto)
            val quantidade = produto.qtd_produto.toString()
            val descricao = produto.descricao
            val button = "atualizar"



            txtValor.setText(valor)
            txtQnd.setText(quantidade)
            txtDescricao.setText(descricao)
            btnSalvar.text = button
        }
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

    override fun adicionarProduto(
        idRecebido: Int,
        valor: Double,
        quantidade: Int,
        descricao: String
    ) {

        compraPresenter.processarModo(-1, idRecebido, valor, quantidade, descricao)
    }

    override fun atualizarProduto(idProduto: Int) {
        val formatador = Formatters()
        val valor = formatador.formatarStringToDouble(binding.txtValor.text.toString())
        val quantidade = binding.txtQnd.text.toString().toInt()
        val descricao = binding.txtDescricao.text.toString()

        compraPresenter.processarModo(
            idProduto,
            idRecebido,
            valor,
            quantidade,
            descricao
        )

    }

    override fun excluirProduto(position: Int) {
        compraPresenter.excluirProduto(position)

    }

    override fun exibirToast(mensagem: String) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
    }

    override fun limparCampos() {
        compraPresenter.editMode = false
        with(binding) {
            txtValor.setText("")
            txtQnd.setText("1")
            txtDescricao.setText("")
            btnSalvar.setText("INCLUIR")

        }
        compraPresenter.count = 1
    }

    override fun updateCounter(count: Int) {
        val counterTextView = binding.txtQnd
        counterTextView.setText("$count")
    }

    override fun onSwipeLeft(pos: Int) {
        val dialogFragment = RoundedAlertDialog(
            DialogData.dialogExcluir.title,
            DialogData.dialogExcluir.message,
            DialogData.dialogExcluir.buttonText,
            DialogData.dialogExcluir.iconResId,
            {//onPositive
                val idProduto = pos
                excluirProduto(idProduto)
                produtoAdapter?.removerItem(pos)

            },
            {//onNegative or Cancel
                binding.rvLista.adapter?.notifyItemChanged(pos)
            }
        )
        dialogFragment.show(supportFragmentManager, "ExibirExcluirDialog")
    }

    override fun scrollRecyclerViewToPosition(position: Int) {
        binding.rvLista.scrollToPosition(position)
    }

    override fun showErrorField(mensagem: String?) {
        with(binding){
            val inputValor = txtInputValor

            if(mensagem == null){
                binding.txtInputValor.error = mensagem
                binding.txtInputValor.isErrorEnabled = false



            }else{
                binding.txtInputValor.error = mensagem
            }
        }
    }

}











