package com.skydevices.marketcalc.model.database.produtoDAO

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.Produto
import com.skydevices.marketcalc.model.database.DatabaseHelper
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@ExperimentalBadgeUtils
class ProdutoDAO(context: Context) : iProdutoDAO {

    private val escrita = DatabaseHelper(context).writableDatabase
    private val leitura = DatabaseHelper(context).readableDatabase


    override fun salvarProduto(produto: Produto): Boolean {
        // val sql = "INSERT INTO produtos VALUES( null, '$[${produto.nome},'descricao..' )"

        val valores = ContentValues()
        valores.put(DatabaseHelper.ID_COMPRA, produto.id_compra)
        valores.put(DatabaseHelper.VALOR, produto.valor_produto)
        valores.put(DatabaseHelper.QUANTIDADE, produto.qtd_produto)
        valores.put(DatabaseHelper.DESCRICAO_PRODUTO, produto.descricao)



        try {
            //escrita.execSQL(sql)//whitableDatabase é utilizado para comandos de INSERT,UPDATE,DELETE
            escrita.insert(
                DatabaseHelper.TABELA_PRODUTOS,
                null,
                valores
            )

            Log.i("info_db", "Sucesso ao Inserir item na Tabela ") // log do sistema

        } catch (e: Exception) {
            Log.i("info_db", "Falha ao inserir item na Tabela ")

            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun iniciarCompra(): Int {
        val dataAtual = LocalDate.now()

        val compra = Compra(
            -1,
            0,
            dataAtual,
            0.0
        )


        val valores = ContentValues()
        valores.put(DatabaseHelper.STATUS_COMPRA, compra.status_compra)
        valores.put(DatabaseHelper.DATA_COMPRA, compra.data_compra.toString())
        valores.put(DatabaseHelper.VALOR_TOTAL, 0.0)

        var novoID: Long = -1


        try {

            //escrita.execSQL(sql)//whitableDatabase é utilizado para comandos de INSERT,UPDATE,DELETE
            val result = escrita.insert(
                DatabaseHelper.TABELA_COMPRAS,
                null,
                valores
            )

            if (result != -1L) {
                novoID = result
            }

            Log.i("info_db", "Sucesso ao Iniciar Nova Compra, ID gerado: $novoID")

            /* escrita.delete(DatabaseHelper.TABELA_PRODUTOS, null, null)
             listar()*/


        } catch (e: Exception) {
            Log.i("info_db", "Falha  ao Iniciar Nova Compra ")

            return 0
        }
        return novoID.toInt()

    }

    override fun atualizar(produto: Produto): Boolean {

        val valores = ContentValues()
        valores.put(DatabaseHelper.ID_COMPRA, produto.id_compra)
        valores.put(DatabaseHelper.DESCRICAO_PRODUTO, produto.descricao)
        valores.put(DatabaseHelper.VALOR, produto.valor_produto)
        valores.put(DatabaseHelper.QUANTIDADE, produto.qtd_produto)


        val args = arrayOf(produto.id_produto.toString())

        Log.i("info_atualizar", "valores : ${valores} ")
        Log.i("info_atualizar", "valores : ${args[0]} ")

        try {
            escrita.update(
                DatabaseHelper.TABELA_PRODUTOS,
                valores,
                "id_produto = ?",
                args
            )

            Log.i("info_db", "Sucesso ao Atualizar item da Tabela ") // log do sistema

        } catch (e: Exception) {
            Log.i("info_db", "Falha ao Atualizar item da Tabela ")

            return false
        }
        return true
    }

    override fun remover(id: Int): Boolean {


        val args = arrayOf(id.toString())

        try {
            escrita.delete(
                DatabaseHelper.TABELA_PRODUTOS,
                "${DatabaseHelper.ID_PRODUTO} = ?",
                args

            )

            atualizarTotal()

            Log.i("info_db", "Sucesso ao Remover Produto ") // log do sistema


        } catch (e: Exception) {
            Log.i("info_db", "Falha ao Remover Produto ")

            return false
        }
        return true
    }

    override fun listar(id: Int): MutableList<Produto> {
        val listaProdutos = mutableListOf<Produto>()

        val sql =
            "SELECT * FROM ${DatabaseHelper.TABELA_PRODUTOS} WHERE ${DatabaseHelper.ID_COMPRA} = ${id} ORDER BY ${DatabaseHelper.ID_PRODUTO} DESC" // linha de comando SQL para selecionar todos os itens da tabela Produtos

        @SuppressLint("Recycle")
        val cursor = leitura. // usado para selecionar(ler) dados
        rawQuery(sql, null)

        val indiceId = cursor.getColumnIndex(DatabaseHelper.ID_PRODUTO)
        val indiceCompra = cursor.getColumnIndex(DatabaseHelper.ID_COMPRA)
        val indiceValor = cursor.getColumnIndex(DatabaseHelper.VALOR)
        val indiceQtd = cursor.getColumnIndex(DatabaseHelper.QUANTIDADE)
        val indiceDescricao = cursor.getColumnIndex(DatabaseHelper.DESCRICAO_PRODUTO)


        while (cursor.moveToNext()) {

            val idCompra = cursor.getInt(indiceCompra)

            val idProduto =
                cursor.getInt(indiceId) // recupera o indice na coluna determinada, no caso coluna 1 no tipo INT
            val valor =
                cursor.getDouble(indiceValor) // recupera o indice na coluna determinada, no caso coluna 2 no tipo String
            val quantidade = cursor.getInt(indiceQtd)

            val descricao = cursor.getString(indiceDescricao)

            Log.i("info_db", "id: $idProduto valor: $valor, Descricao: $descricao ")
            listaProdutos.add(
                Produto(idProduto, idCompra, descricao, valor, quantidade)
            )

        }

        atualizarTotal()

        return listaProdutos

    }

    fun atualizarTotal() {
        var atualizaValorTotal = "UPDATE ${DatabaseHelper.TABELA_COMPRAS}\n" +
                "SET ${DatabaseHelper.VALOR_TOTAL} = (\n" +
                "    SELECT COALESCE(SUM(${DatabaseHelper.VALOR} * ${DatabaseHelper.QUANTIDADE}), 0)\n" +
                "    FROM produtos\n" +
                "    WHERE id_compra = compras.id_compra\n" +
                ")"

        escrita.execSQL(atualizaValorTotal)
    }

    fun calcularValorTotal(listaCompra: List<Produto>): Double {
        var resultado = 0.0
        for (produto in listaCompra) {
            val resultadoItem = produto.valor_produto * produto.qtd_produto
            resultado += resultadoItem
        }
        return resultado
    }

    @RequiresApi(Build.VERSION_CODES.O)


    override fun salvarCompra(compra: Compra): Boolean {

        val sql =
            "UPDATE ${DatabaseHelper.TABELA_COMPRAS} SET ${DatabaseHelper.STATUS_COMPRA} = 1 WHERE ${DatabaseHelper.ID_COMPRA} = ${compra.id_compra}"


        try {

            escrita.execSQL(sql)//whitableDatabase é utilizado para comandos de INSERT,UPDATE,DELETE
            /*escrita.insert(
                DatabaseHelper.TABELA_COMPRAS,
                null,
                valores
            )*/

            Log.i("info_db", "Sucesso ao Concluir compra ") // log do sistema

        } catch (e: Exception) {
            Log.i("info_db", "Falha ao Concluir compra ")

            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun listaHistorico(): List<Compra> {
        val listaHistorico = mutableListOf<Compra>()

        val sql =
            "SELECT * FROM ${DatabaseHelper.TABELA_COMPRAS} ORDER BY ${DatabaseHelper.ID_COMPRA} DESC" // linha de comando SQL para selecionar todos os itens da tabela Produtos

        @SuppressLint("Recycle")
        val cursor = leitura. // usado para selecionar(ler) dados
        rawQuery(sql, null)

        val indiceId = cursor.getColumnIndex(DatabaseHelper.ID_COMPRA)
        val indiceValor = cursor.getColumnIndex(DatabaseHelper.VALOR_TOTAL)
        val indiceData = cursor.getColumnIndex(DatabaseHelper.DATA_COMPRA)
        val indiceStatus = cursor.getColumnIndex(DatabaseHelper.STATUS_COMPRA)


        while (cursor.moveToNext()) {

            val idCompra =
                cursor.getInt(indiceId) // recupera o indice na coluna determinada, no caso coluna 1 no tipo INT
            val valorTotal =
                cursor.getDouble(indiceValor) // recupera o indice na coluna determinada, no caso coluna 2 no tipo String
            val data = cursor.getString(indiceData)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val localDate = LocalDate.parse(data, formatter)
            val status = cursor.getInt(indiceStatus)

            Log.i("info_db", "id: $idCompra valor: $valorTotal ")
            listaHistorico.add(
                Compra(idCompra, status, localDate, valorTotal)
            )


        }

        return listaHistorico

    }


}