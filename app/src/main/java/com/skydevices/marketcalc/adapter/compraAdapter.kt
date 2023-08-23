package com.skydevices.marketcalc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.skydevices.marketcalc.databinding.ItemHistoricoBinding
import com.skydevices.marketcalc.databinding.ItemProdutoBinding
import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.Produto

class compraAdapter : Adapter<compraAdapter.ProdutoViewHolder>() {

    private var listaProduto: List<Compra> = listOf()

    fun adicionarLista(lista : List<Compra>){
        this.listaProduto = lista
        notifyDataSetChanged()
    }

    inner class ProdutoViewHolder(itemBinding: ItemHistoricoBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private val binding: ItemHistoricoBinding

        init {
            binding = itemBinding
        }

        fun binding(compra: Compra) {
            binding.txtIdCompra.text = "compra nº ${compra.id_compra}"
            binding.txtDataCompra.text = "Data da compra: ${compra.data_compra}"
            binding.txtValor.text = "R$ ${ String.format("%.2f", compra.valor_compra)}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val itemHistoricoBinding = ItemHistoricoBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
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