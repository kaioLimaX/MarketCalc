package com.skydevices.marketcalc.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.skydevices.marketcalc.databinding.ActivityAdicionarTarefaBinding
import com.skydevices.marketcalc.model.Produto
import com.skydevices.marketcalc.database.ProdutoDAO


class AdicionarTarefaActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAdicionarTarefaBinding.inflate(layoutInflater)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getSupportActionBar()?.setTitle("Novo Produto")
        val content = this

        with(binding){

           btnSalvar.setOnClickListener {
                val valor = editValor.text.toString().toDouble()
                val quantidade = editQtd.text.toString().toInt()
                val produtoDAO = ProdutoDAO(content)
                val produto = Produto(
                    -1,
                    valor,
                    quantidade
                )
                if (produtoDAO.salvar(produto)) {
                    Log.i("info_db", "Produto salvo com sucesso ")
                    Toast.makeText(content, "Produto salvo com sucesso", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Log.i("info_db", "Falha ao salvar produto ")
                    Toast.makeText(content, "Falha ao salvar produto", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}