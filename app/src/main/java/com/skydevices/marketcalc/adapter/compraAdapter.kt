package com.skydevices.marketcalc.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.skydevices.marketcalc.R
import com.skydevices.marketcalc.Utils.Formatters
import com.skydevices.marketcalc.databinding.ItemCompraBinding
import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.Produto

class compraAdapter(

    private val recyclerView: RecyclerView,
    private val ContrainLayout : ConstraintLayout,
    private var listaCompra: MutableList<Compra> = mutableListOf(),
    private val clique: (Compra) -> Unit,


    ) : Adapter<compraAdapter.ProdutoViewHolder>() {

    fun getItem(position: Int): Compra {
        return this.listaCompra[position]
    }

    fun adicionarLista(lista: MutableList<Compra>) {
        this.listaCompra = lista
        verificarLista()
        notifyDataSetChanged()
    }

    fun removerItem(position: Int) {
    verificarLista()
        notifyItemRemoved(position)

    }

    fun verificarLista(){
        if (listaCompra.isEmpty()) {
            // Lista está vazia, atualize a visibilidade do RecyclerView e do layout alternativo
            recyclerView.visibility = View.GONE
            ContrainLayout.visibility = View.VISIBLE
        }else{
            recyclerView.visibility = View.VISIBLE
            ContrainLayout.visibility = View.INVISIBLE
        }
    }

    fun atualizarItem(compra: Compra){
        var produtoParaAtualizar = listaCompra.find { it.id_compra == compra.id_compra }
        val posicao = listaCompra.indexOfFirst { it.id_compra == compra.id_compra }

        if (produtoParaAtualizar != null) {
            // Realize as atualizações no produto
            produtoParaAtualizar.data_compra = compra.data_compra
            produtoParaAtualizar.total_compra = compra.total_compra
            produtoParaAtualizar.status_compra = compra.status_compra
            // Imprima a lista atualizada
            notifyItemChanged(posicao)

        } else {
            println("Produto com ID ${compra.id_compra} não encontrado na lista.")
        }
    }




    inner class ProdutoViewHolder(itemBinding: ItemCompraBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private val binding: ItemCompraBinding

        init {
            binding = itemBinding
        }

        @SuppressLint("SetTextI18n")
        fun binding(compra: Compra) {
            val dateFormatter = Formatters()
            val dataFormatada = dateFormatter.formatarData(compra.data_compra.toString())

            binding.txtIdCompra.text = "Compra nº ${compra.id_compra}"
            binding.txtValor.text = "R$ ${String.format("%.2f", compra.total_compra)}"
            binding.txtData.text = "${dataFormatada}"


            if(compra.status_compra == 0){
               // binding.txtStatus.text = "Status: "
               // binding.txtStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableAlert, null)
                binding.imgStatus.setImageResource(R.drawable.exclamation_24)
                binding.txtStatus.text = "em andamento"
            }else{
               // binding.txtStatus.text = "Status: "
               // binding.txtStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableCheck, null)
                binding.imgStatus.setImageResource(R.drawable.check_circle_24)
                binding.txtStatus.text = "concluida"
            }
            binding.ClItemCompra.setOnClickListener {
                clique(compra)
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val itemHistoricoBinding = ItemCompraBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProdutoViewHolder(itemHistoricoBinding)
    }

    override fun getItemCount(): Int {
        return listaCompra.size
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = listaCompra[position]
        holder.binding(produto)
    }
}