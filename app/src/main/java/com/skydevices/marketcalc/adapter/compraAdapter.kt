package com.skydevices.marketcalc.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.skydevices.marketcalc.databinding.ItemCompraBinding
import com.skydevices.marketcalc.model.Compra

class compraAdapter(
    private var listaProduto: List<Compra> = listOf(),
    private val clique: (String) -> Unit

) : Adapter<compraAdapter.ProdutoViewHolder>() {

    fun adicionarLista(lista: List<Compra>) {
        this.listaProduto = lista
        notifyDataSetChanged()
    }

    inner class ProdutoViewHolder(itemBinding: ItemCompraBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private val binding: ItemCompraBinding

        init {
            binding = itemBinding
        }

        @SuppressLint("SetTextI18n")
        fun binding(compra: Compra) {
            binding.txtIdCompra.text = "compra nº ${compra.id_compra}"
            binding.txtDataCompra.text = "Data da compra: ${compra.data_compra}"
            binding.txtValor.text = "R$ ${String.format("%.2f", compra.total_compra)}"
            binding.txtStatus.text = if (compra.status_compra == 0) "em Andamento" else "Concluido"
            binding.ClItemCompra.setOnClickListener {
                clique("${compra.id_compra}")
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
        return listaProduto.size
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = listaProduto[position]
        holder.binding(produto)
    }
}