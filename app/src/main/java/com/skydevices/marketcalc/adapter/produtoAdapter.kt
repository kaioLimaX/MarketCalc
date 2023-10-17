package com.skydevices.marketcalc.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.skydevices.marketcalc.databinding.ItemProdutoBinding
import com.skydevices.marketcalc.model.Produto


class produtoAdapter(

    val onClickEditar : (Produto) -> Unit

) : Adapter<produtoAdapter.ProdutoViewHolder>() {

    private var listaProduto: MutableList<Produto> = mutableListOf()




    fun adicionarLista(lista : MutableList<Produto>){
        this.listaProduto = lista
        notifyDataSetChanged()
    }

    fun adicionarItem(produto: Produto){
        listaProduto.add(0,produto)
        notifyItemInserted(0)
    }




    inner class ProdutoViewHolder(itemBinding: ItemProdutoBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private val binding: ItemProdutoBinding

        init {
            binding = itemBinding

        }


        @SuppressLint("ClickableViewAccessibility")
        fun binding(produto: Produto) {
            val titulo = if(produto.descricao!!.isNotEmpty()){
                "${produto.descricao}"
            }else{
                "Produto ${listaProduto.size - adapterPosition}"
            }

            binding.txtProduto.text = titulo
            binding.txtQuantidade.text = "${produto.qtd_produto} itens a R$ ${produto.valor_produto} cada"
            binding.txtTotalItem.text = "R$ ${ String.format("%.2f", produto.qtd_produto * produto.valor_produto)}"

            binding.clProduto.setOnClickListener {
                onClickEditar(produto)
            }




        }




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val itemProdutoBinding = ItemProdutoBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ProdutoViewHolder(itemProdutoBinding)
    }

    override fun getItemCount(): Int {
        return listaProduto.size
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = listaProduto[position]
        holder.binding(produto)
    }
}