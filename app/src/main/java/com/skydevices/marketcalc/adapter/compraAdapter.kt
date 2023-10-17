package com.skydevices.marketcalc.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.skydevices.marketcalc.R
import com.skydevices.marketcalc.Utils.Formatters
import com.skydevices.marketcalc.databinding.ItemCompraBinding
import com.skydevices.marketcalc.model.Compra

class compraAdapter(
    private val context: Context,
    private var listaProduto: List<Compra> = listOf(),
    private val clique: (Compra) -> Unit,


    ) : Adapter<compraAdapter.ProdutoViewHolder>() {

    fun getItem(position: Int): Compra {
        return this.listaProduto[position]
    }

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
        return listaProduto.size
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = listaProduto[position]
        holder.binding(produto)
    }
}