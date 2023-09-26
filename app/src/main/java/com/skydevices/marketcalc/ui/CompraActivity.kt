package com.skydevices.marketcalc.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.skydevices.marketcalc.R
import com.skydevices.marketcalc.adapter.itemAdapter
import com.skydevices.marketcalc.database.ProdutoDAO
import com.skydevices.marketcalc.databinding.ActivityCompraBinding
import com.skydevices.marketcalc.model.Produto


@ExperimentalBadgeUtils
class CompraActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCompraBinding.inflate(layoutInflater)

    }


    val content = this

    private var itemAdapter: itemAdapter? = null

    private var editMode = false

    private lateinit var badgeDrawable: BadgeDrawable

    private var idRecebido = 0

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

    class RoundedAlertDialog : DialogFragment() {
        //...

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return MaterialAlertDialogBuilder(
                requireActivity(),
                R.style.MaterialAlertDialog_rounded
            )
                .setMessage("Deseja concluir esta compra?")
                .setTitle("Concluir Compra")
                .setPositiveButton("SIM", null)
                .setNegativeButton("NAO", null)
                .setIcon(R.drawable.shopping_cart_check_24)
                .create()
        }

    }

    private fun showAlertDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        // Configurar o título e a mensagem
        builder.setTitle("Título do Alerta")
        builder.setMessage("Mensagem do Alerta")

        // Configurar os botões do alerta
        builder.setPositiveButton("Botão Positivo",
            DialogInterface.OnClickListener { dialog, which ->
                // Código a ser executado quando o botão positivo é clicado
            })
        builder.setNegativeButton("Botão Negativo",
            DialogInterface.OnClickListener { dialog, which ->
                // Código a ser executado quando o botão negativo é clicado
            })

        // Criar e exibir o AlertDialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inicalizarToolbar()


        badgeDrawable = BadgeDrawable.create(this@CompraActivity)

        getSupportActionBar()?.setTitle(R.string.app_name)
        setContentView(binding.root)
        with(binding) {
            fabAdicionar.setOnClickListener {
                val dialog = RoundedAlertDialog()
                val fragmentManager: FragmentManager = supportFragmentManager
                dialog.show(fragmentManager, "RoundedAlertDialog")
            }

            include.imageButton.setOnClickListener {
                finish()
            }

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

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        val extrasId = intent?.extras
        if (extrasId != null) {
            idRecebido =
                extrasId?.getInt("id")!! // Substitua "chave" pelo nome da chave que você usou
            // Faça algo com o valor recebido
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
        itemAdapter = itemAdapter(
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

        /*binding.rvLista.addItemDecoration(
            DividerItemDecoration(
                this,
                RecyclerView.VERTICAL
            )
        )*/


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

           /* badgeDrawable.setHorizontalOffset(65)
            badgeDrawable.setVerticalOffset(85)*/
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

    /*@RequiresApi(Build.VERSION_CODES.O)
    private fun confirmarExclusao(produto: Produto) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar exclusão")
        builder.setMessage(
            "Deseja realmente excluir o produto?"
        )
        builder.setPositiveButton(
            "Sim"
        ) { dialog, which ->
            val produtoDAO = ProdutoDAO(this)
            if (produtoDAO.remover(produto.id_produto)) {
                listar(produto.id_compra)
                Toast.makeText(content, "Sucesso ao remover produto ", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(content, "Falha ao remover produto", Toast.LENGTH_SHORT).show()
            }


        }
        builder.setNegativeButton(
            "Não"
        ) { dialog, which ->
            //empty
        }
        val dialog = builder.create()
        dialog.show()

    }*/

    @RequiresApi(Build.VERSION_CODES.O)
    private fun atualizar(produto: Produto) {
        val produtoDAO = ProdutoDAO(this)
        val valor = binding.txtValor.text.toString().toDouble()
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

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun adicionarProduto() {
        val idCompra = idRecebido
        val valor = binding.txtValor.text.toString().toDouble()
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

    }

    fun limparCampos(){
        with(binding){
            binding.txtValor.setText("")
            binding.txtQnd.setText("1")
            binding.txtDescricao.setText("")
        }
    }


}