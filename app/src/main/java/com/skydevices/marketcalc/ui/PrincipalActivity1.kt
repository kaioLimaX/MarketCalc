/*
package com.skydevices.marketcalc.ui


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.R
import com.skydevices.marketcalc.R.*
import com.skydevices.marketcalc.adapter.compraAdapter
import com.skydevices.marketcalc.databinding.ActivityPrincipalBinding
import com.skydevices.marketcalc.model.Compra
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class SwipeCallback(
    private val context: Context,
    private val onSwipeLeftListener: (position: Int, ) -> Unit,
    private val recyclerView: RecyclerView,
    private val canSwipeLeftFunction: (position: Int) -> Boolean
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val itemToDelete = (recyclerView.adapter as? compraAdapter)?.getItem(position)

        if (canSwipeLeftFunction(position)) {
            // Mostrar o AlertDialog de confirmação
            val alertDialogBuilder = AlertDialog.Builder(viewHolder.itemView.context)
            alertDialogBuilder.setTitle("Confirmar exclusão")
            alertDialogBuilder.setMessage("Você tem certeza de que deseja excluir este item?")
            alertDialogBuilder.setPositiveButton("Sim") { _, _ ->
                // Executar a exclusão do item aqui
                onSwipeLeftListener(position, itemToDelete!!)
            }
            alertDialogBuilder.setNegativeButton("Cancelar") { _, _ ->
                // Restaurar o item se o usuário cancelar
                recyclerView.adapter?.notifyItemChanged(position)
            }
            alertDialogBuilder.setOnCancelListener {
                // Restaurar o item se o usuário pressionar o botão de voltar
                recyclerView.adapter?.notifyItemChanged(position)
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        } else {
            // A função canSwipeLeftFunction retornou false, portanto, restaure o item
            recyclerView.adapter?.notifyItemChanged(position)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftActionIcon(R.drawable.trash_24)
            .addSwipeLeftLabel("Excluir")
            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }


}
@ExperimentalBadgeUtils
class PrincipalActivity1 : AppCompatActivity() {

    val binding by lazy {
        ActivityPrincipalBinding.inflate(layoutInflater)
    }

    private var itemAdapter: compraAdapter? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            floatingActionButton.setOnClickListener {
                //iniciarCompra()

            }
        }

        val swipeCallback = SwipeCallback(
            this,
            onSwipeLeftListener = { position, compra ->
                // Implemente aqui a lógica para excluir o item quando deslizar para a esquerda
                // por exemplo: compraAdapter.removeItem(position)
            },
            recyclerView = binding.rvCompras,
            canSwipeLeftFunction = { position ->

                true
            }
        )

        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvCompras)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        inicializarToolbar()

        binding.rvCompras.adapter = itemAdapter
        binding.rvCompras.layoutManager = LinearLayoutManager(this)
    }

   */
/* @RequiresApi(Build.VERSION_CODES.O)
    private fun iniciarCompra() {
        val produtoDAO = ProdutoDAO(this)
        val dataAtual = LocalDate.now()

        val compra = Compra(
            -1,
            0,
            dataAtual,
            0.0
        )

        val retornoDao = produtoDAO.iniciarCompra(compra)

        if (retornoDao != 0) {
            compra.id_compra = retornoDao

            iniciarIntent(compra,CompraActivity1::class.java)
        } else {
            Toast.makeText(this, "Falha ao iniciar compra", Toast.LENGTH_SHORT).show()
        }

    }*//*


    @RequiresApi(Build.VERSION_CODES.O)
    private fun inicializarToolbar() {
        binding.includeToolbar.toolbarTitle.text = "Soma Mercado"

      //  listarCompras()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    */
/* fun listarCompras() {

        val produtoDAO = ProdutoDAO(this)
        val listaProdutos = produtoDAO.listaHistorico()
        itemAdapter = compraAdapter(this) { compra ->
            if(compra.status_compra == 1){
                iniciarIntent(compra,ResumoActivity::class.java)
            }else{
                iniciarIntent(compra,CompraActivity1::class.java)
            }


        }
        itemAdapter?.adicionarLista(listaProdutos)

    }*//*


    @SuppressLint("SuspiciousIndentation")
    fun iniciarIntent(compra: Compra, activity: Class<*>) {
        intent = Intent(this, activity)
        intent.putExtra("compra", compra)
        startActivity(intent)
    }
}*/
