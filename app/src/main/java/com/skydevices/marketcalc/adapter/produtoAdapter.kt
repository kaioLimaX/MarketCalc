package com.skydevices.marketcalc.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.skydevices.marketcalc.databinding.ItemProdutoBinding
import com.skydevices.marketcalc.model.Produto


class produtoAdapter(

    private val recyclerView: RecyclerView,
    private val ContrainLayout: ConstraintLayout?,
    val onClickEditar: (Produto) -> Unit

) : Adapter<produtoAdapter.ProdutoViewHolder>() {

    private var listaProduto: MutableList<Produto> = mutableListOf()


    @SuppressLint("NotifyDataSetChanged")
    fun adicionarLista(lista: MutableList<Produto>) {
        this.listaProduto = lista
        verificarLista()
        notifyDataSetChanged()
    }

    fun getPosition(item: Produto): Int {
        val posicao = listaProduto.indexOfFirst { it.id_produto == item.id_produto }
        Log.i("teste_produto", "O produto  ID ${item.id_produto} ")
        return posicao
    }

    fun recuperarLista(): MutableList<Produto> {
        return listaProduto
    }

    fun adicionarItem(produto: Produto) {
        listaProduto.add(0, produto)
        verificarLista()
        notifyItemInserted(0)
    }

    fun removerItem(position: Int) {
        verificarLista()
        notifyItemRemoved(position)
    }

    fun verificarLista(){
        if (listaProduto.isEmpty()) {
            // Lista está vazia, atualize a visibilidade do RecyclerView e do layout alternativo
            recyclerView.visibility = View.GONE
            ContrainLayout?.visibility = View.VISIBLE
        }else{
            recyclerView.visibility = View.VISIBLE
            ContrainLayout?.visibility = View.INVISIBLE
        }
    }



    fun atualizarItem(produto: Produto){
        var produtoParaAtualizar = listaProduto.find { it.id_produto == produto.id_produto }
        val posicao = listaProduto.indexOfFirst { it.id_produto == produto.id_produto }

        if (produtoParaAtualizar != null) {
            // Realize as atualizações no produto
            produtoParaAtualizar.valor_produto = produto.valor_produto
            produtoParaAtualizar.qtd_produto = produto.qtd_produto
            produtoParaAtualizar.descricao = produto.descricao
            // Imprima a lista atualizada
            notifyItemChanged(posicao)

        } else {
            println("Produto com ID ${produto.id_produto} não encontrado na lista.")
        }
    }


    inner class ProdutoViewHolder(itemBinding: ItemProdutoBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private val binding: ItemProdutoBinding

        init {
            binding = itemBinding

        }


        @SuppressLint("ClickableViewAccessibility")
        fun binding(produto: Produto) {
            val titulo = if (produto.descricao!!.isNotEmpty()) {
                "${produto.descricao}"
            } else {
                "Produto ${listaProduto.size - adapterPosition}"
            }

            binding.txtProduto.text = titulo
            binding.txtQuantidade.text =
                "${produto.qtd_produto} itens a R$ ${produto.valor_produto} cada"
            binding.txtTotalItem.text =
                "R$ ${String.format("%.2f", produto.qtd_produto * produto.valor_produto)}"

            binding.clProduto.setOnClickListener {
                onClickEditar(produto)
            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val itemProdutoBinding = ItemProdutoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
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