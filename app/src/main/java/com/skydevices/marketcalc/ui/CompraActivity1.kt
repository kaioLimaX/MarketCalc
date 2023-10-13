/*
package com.skydevices.marketcalc.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.skydevices.marketcalc.R
import com.skydevices.marketcalc.Utils.MaskMoney
import com.skydevices.marketcalc.adapter.produtoAdapter
import com.skydevices.marketcalc.model.database.ProdutoDAO
import com.skydevices.marketcalc.databinding.ActivityCompraBinding
import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.Produto
import java.util.Locale


@ExperimentalBadgeUtils
class CompraActivity1 : AppCompatActivity() {

    private val binding by lazy {
        ActivityCompraBinding.inflate(layoutInflater)

    }


    val content = this

    private var itemAdapter: produtoAdapter? = null

    private var editMode = false

    private lateinit var badgeDrawable: BadgeDrawable

    private var idRecebido = 0

    lateinit var editCompra: Compra

    lateinit var editProduto: Produto

    private var txtQntIncremento = 1


    class RoundedAlertExcluir(
        private val produto: Produto,
        private val listarCallback: () -> Unit
    ) : DialogFragment() {
        //...

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return MaterialAlertDialogBuilder(
                requireActivity(),
                R.style.MaterialAlertDialog_rounded
            )
                .setMessage("Deseja excluir este produto?")
                .setTitle("Excluir produto")
                .setIcon(R.drawable.minus_circle_24)
                .setPositiveButton("EXCLUIR") { dialog, which ->
                    val produtoDAO = ProdutoDAO(requireContext())
                    if (produtoDAO.remover(produto.id_produto)) {
                        listarCallback.invoke()
                       // listar(produto.id_compra)
                        Toast.makeText(requireContext(), "Sucesso ao remover produto ", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Falha ao remover produto", Toast.LENGTH_SHORT).show()
                    }


                }
                .setNegativeButton("CANCELAR") { dialog, which ->

                    Toast.makeText(context, "Exclusão cancelada", Toast.LENGTH_SHORT).show()
                }
                .create()
        }

    }

    class RoundedAlertDialog(
        private val compra: Compra,
        private val listarCallback: () -> Unit
    ) : DialogFragment() {


        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return MaterialAlertDialogBuilder(
                requireActivity(),
                R.style.MaterialAlertDialog_rounded
            )
                .setMessage("Deseja concluir esta compra?")
                .setTitle("Concluir Compra")
                .setPositiveButton("CONCLUIR") { dialog, which ->
                    val produtoDAO = ProdutoDAO(requireContext())
                    Toast.makeText(context, "${compra.total_compra}", Toast.LENGTH_SHORT).show()

                    */
/*if(compra.total_compra != 0.0){
                        if (produtoDAO.salvarCompra(compra)) {
                            listarCallback.invoke()
                            Toast.makeText(requireContext(), "Sucesso ao Concluir compra ", Toast.LENGTH_SHORT).show()
                            requireActivity().finish()

                        } else {
                            Toast.makeText(requireContext(), "Falha ao Concluir compra", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(requireContext(), "o carrinho esta vazio", Toast.LENGTH_SHORT).show()
                    }*//*




                }
                .setNegativeButton("CANCELAR") { dialog, which ->

                    Toast.makeText(context, "Conclusão cancelada", Toast.LENGTH_SHORT).show()
                }
                .setIcon(R.drawable.shopping_cart_check_24)
                .create()
        }

    }




    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inicalizarToolbar()


        badgeDrawable = BadgeDrawable.create(this@CompraActivity1)

        getSupportActionBar()?.setTitle(R.string.app_name)
        setContentView(binding.root)
        with(binding) {
            fabAdicionar.setOnClickListener {



                val dialog = RoundedAlertDialog(editCompra){
                    //listar(produto.id_compra)
                }
                val fragmentManager: FragmentManager = supportFragmentManager
                dialog.show(fragmentManager, "RoundedAlertDialog")
            }

           */
/* include.imageButton.setOnClickListener {
                finish()
            }*//*


            btnSalvar.setOnClickListener {
                if (editMode != false) {
                    atualizar(editProduto)
                } else {
                    adicionarProduto()
                }
            }
            txtInputQtd.setEndIconOnClickListener {
                if (txtQntIncremento == 1){

                }else{
                    txtQntIncremento--
                    binding.txtQnd.setText(txtQntIncremento.toString())
                }
            }

            txtInputQtd.setStartIconOnClickListener {
                if(txtQntIncremento == 99){

                }else{
                    txtQntIncremento++
                    binding.txtQnd.setText(txtQntIncremento.toString())
                }
            }




        }



    }

    private fun inicalizarToolbar() {
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val mLocale = Locale("pt", "BR")
        val editValor  = binding.txtValor
        editValor.addTextChangedListener(MaskMoney(editValor, mLocale))

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        val extrasId = intent?.extras
        if (extrasId != null) {
            val compra: Compra? = intent.getParcelableExtra("compra")
            editCompra = compra!!
            idRecebido = compra!!.id_compra
            listar(idRecebido)



        } else {
            Log.i("info_teste", "onStart: $idRecebido")
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
     fun listar(id: Int) {
        var resultado = 0.00
        val produtoDAO = ProdutoDAO(this)
        val listaProdutos = produtoDAO.listar(id)
        itemAdapter = produtoAdapter(
            onClickExcluir = { produto ->
                confirmarExclusao(produto)


            },
            onClickEditar = { produto ->
                modoEdicao(produto)

            }
        )
        itemAdapter?.adicionarLista(listaProdutos)
        binding.rvLista.adapter = itemAdapter
        binding.rvLista.layoutManager = LinearLayoutManager(this)

        */
/*binding.rvLista.addItemDecoration(
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
                binding.include.toolbarTitle.text = " R$ ${String.format("%.2f", resultado)}"

            }
        }else{
            binding.include.toolbarTitle.text = " R$ 00.00"
        }

        binding.fabAdicionar.doOnPreDraw {

           */
/* badgeDrawable.setHorizontalOffset(65)
            badgeDrawable.setVerticalOffset(85)*//*

            badgeDrawable.number = listaProdutos.size
            badgeDrawable.isVisible = listaProdutos.size != 0



            BadgeUtils.attachBadgeDrawable(badgeDrawable, binding.fabAdicionar, null)
        }


    }

    private fun modoEdicao(produto: Produto) {
        editMode = true
        val valor = produto.valor_produto.toString()
        val qtd = produto.qtd_produto.toString()
        val descricao = produto.descricao.toString()

        editProduto = Produto(
            produto.id_produto,
            produto.id_compra,
            produto.descricao,
            produto.valor_produto,
            produto.qtd_produto
        )


        binding.txtValor.setText(valor)
        binding.txtQnd.setText(qtd)
        binding.txtDescricao.setText(descricao)
        binding.btnSalvar.text = "ATUALIZAR"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun confirmarExclusao(produto: Produto) {
        val dialog = RoundedAlertExcluir(produto){
            listar(produto.id_compra)
        }
        val fragmentManager: FragmentManager = supportFragmentManager
        dialog.show(fragmentManager, "RoundedAlertDialog")

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun atualizar(produto: Produto) {
        val produtoDAO = ProdutoDAO(this)

        if(binding.txtValor.text?.isNotEmpty() == true){
            val valorNumericoString: String =
                binding.txtValor.text!!.replace("[^\\d,]".toRegex(), "").replace(".", "").replace(",", ".")
            val valor = valorNumericoString.toDouble()
            val quantidade = binding.txtQnd.text.toString().toInt()
            val descricao = binding.txtDescricao.text.toString()

            val produtoAtualizado = Produto(
                produto.id_produto,
                produto.id_compra,
                descricao,
                valor,
                quantidade
            )
            if (produtoDAO.atualizar(produtoAtualizado)) {
                listar(produto.id_compra)
                Toast.makeText(content, "Sucesso ao atualizar produto ", Toast.LENGTH_SHORT).show()
                editMode = false
                binding.txtValor.requestFocus()
                binding.btnSalvar.text = "INCLUIR"
                limparCampos()
            } else {
                Toast.makeText(content, "Falha ao atualizar produto", Toast.LENGTH_SHORT).show()
            }
            }else{
            Toast.makeText(this, "insira algo diferente de 0", Toast.LENGTH_SHORT).show()
        }





    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun adicionarProduto() {
        val idCompra = idRecebido
        if(binding.txtValor.text?.isNotEmpty() == true){
            val valorNumericoString: String =
                binding.txtValor.text!!.replace("[^\\d,]".toRegex(), "").replace(".", "").replace(",", ".")
            val valor = valorNumericoString.toDouble()
            val quantidade = binding.txtQnd.text.toString().toInt()
            val descricao = binding.txtDescricao.text.toString()
            val produtoDAO = ProdutoDAO(content)

            val produto = Produto(
                -1,
                idCompra,
                descricao,
                valor,
                quantidade
            )
            if (produtoDAO.salvarProduto(produto)) {
                Log.i("info_db", "Produto salvo com sucesso ")
                Toast.makeText(content, "Produto salvo com sucesso", Toast.LENGTH_SHORT).show()
                listar(idCompra)
                limparCampos()
                binding.txtValor.requestFocus()
            } else {
                Log.i("info_db", "Falha ao salvar produto ")
                Toast.makeText(content, "Falha ao salvar produto", Toast.LENGTH_SHORT).show()
            }
            }else{
            binding.txtInputValor.error = "Insira um valor diferente de 0"
        }



    }

    fun limparCampos(){
        with(binding){
            binding.txtValor.setText("")
            binding.txtQnd.setText("1")
            binding.txtDescricao.setText("")
            txtQntIncremento = 1
        }
    }


}*/
