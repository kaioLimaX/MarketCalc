package com.skydevices.marketcalc.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.ContactsContract.Data
import android.util.Log
import androidx.annotation.RequiresApi
import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.Produto
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ProdutoDAO(context: Context) : iProdutoDAO {

    private val escrita = DatabaseHelper(context).writableDatabase
    private val leitura = DatabaseHelper(context).readableDatabase

    override fun salvar(produto: Produto): Boolean {
        // val sql = "INSERT INTO produtos VALUES( null, '$[${produto.nome},'descricao..' )"

        val valores = ContentValues()
        valores.put(DatabaseHelper.VALOR, produto.valor_produto)
        valores.put(DatabaseHelper.QUANTIDADE, produto.qtd_produto)

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

    override fun atualizar(produto: Produto): Boolean {
        /*val titulo = binding.editProduto.text.toString()
        val item = binding.spinnerLista.selectedItem
        val posicao = binding.spinnerLista.selectedItemPosition

        val sql =
            "UPDATE ${DatabaseHelper.TABELA_PRODUTOS} SET ${DatabaseHelper.TITULO} = '$titulo' WHERE ${DatabaseHelper.ID_PRODUTO} = '${item}'" // COMANDO UPDATE para atualizar itens
*/

        val valores = ContentValues()
        valores.put(DatabaseHelper.VALOR, produto.valor_produto)
        valores.put(DatabaseHelper.QUANTIDADE, produto.qtd_produto)
        val args = arrayOf(produto.id_produto.toString())
        try {
            //bancoDados.writableDatabase.execSQL(sql)//whitableDatabase é utilizado para comandos de INSERT,UPDATE,DELETE
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
        /*
                val item = binding.spinnerLista.selectedItem
                val posicao = binding.spinnerLista.selectedItemPosition

                val sql =
                    "DELETE FROM ${DatabaseHelper.TABELA_PRODUTOS} WHERE ${DatabaseHelper.ID_PRODUTO} = '${item}'" // COMANDO UPDATE para atualizar itens
        */
        val args = arrayOf(id.toString())

        try {
            escrita.delete(
                DatabaseHelper.TABELA_PRODUTOS,
                "${DatabaseHelper.ID_PRODUTO} = ?",
                args

            )

            Log.i("info_db", "Sucesso ao Remover Produto ") // log do sistema

        } catch (e: Exception) {
            Log.i("info_db", "Falha ao Remover Produto ")

            return false
        }
        return true
    }

    override fun listar(): List<Produto> {
        val listaProdutos = mutableListOf<Produto>()

        val sql =
            "SELECT * FROM ${DatabaseHelper.TABELA_PRODUTOS} ORDER BY ${DatabaseHelper.ID_PRODUTO} DESC" // linha de comando SQL para selecionar todos os itens da tabela Produtos

        @SuppressLint("Recycle")
        val cursor = leitura. // usado para selecionar(ler) dados
        rawQuery(sql, null)

        val indiceId = cursor.getColumnIndex(DatabaseHelper.ID_PRODUTO)
        val indiceValor = cursor.getColumnIndex(DatabaseHelper.VALOR)
        val indiceQtd = cursor.getColumnIndex(DatabaseHelper.QUANTIDADE)


        while (cursor.moveToNext()) {

            val idProduto =
                cursor.getInt(indiceId) // recupera o indice na coluna determinada, no caso coluna 1 no tipo INT
            val valor =
                cursor.getDouble(indiceValor) // recupera o indice na coluna determinada, no caso coluna 2 no tipo String
            val quantidade = cursor.getInt(indiceQtd)

            Log.i("info_db", "id: $idProduto valor: $valor ")
            listaProdutos.add(
                Produto(idProduto, valor, quantidade)
            )


        }

        return listaProdutos

    }

    override fun salvarCompra(compra: Compra): Boolean {
        val valores = ContentValues()
        valores.put(DatabaseHelper.VALOR_TOTAL, compra.valor_compra)
        valores.put(DatabaseHelper.DATA_COMPRA, compra.data_compra.toString())


        try {

            //escrita.execSQL(sql)//whitableDatabase é utilizado para comandos de INSERT,UPDATE,DELETE
            escrita.insert(
                DatabaseHelper.TABELA_HISTORICO,
                null,
                valores
            )

            escrita.delete(DatabaseHelper.TABELA_PRODUTOS, null, null)
            listar()
            Log.i("info_db", "Sucesso ao Inserir item na Tabela ") // log do sistema

        } catch (e: Exception) {
            Log.i("info_db", "Falha ao inserir item na Tabela ")

            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun listaHistorico(): List<Compra> {
        val listaHistorico = mutableListOf<Compra>()

        val sql =
            "SELECT * FROM ${DatabaseHelper.TABELA_HISTORICO} ORDER BY ${DatabaseHelper.ID_HISTORICO} DESC" // linha de comando SQL para selecionar todos os itens da tabela Produtos

        @SuppressLint("Recycle")
        val cursor = leitura. // usado para selecionar(ler) dados
        rawQuery(sql, null)

        val indiceId = cursor.getColumnIndex(DatabaseHelper.ID_HISTORICO)
        val indiceValor = cursor.getColumnIndex(DatabaseHelper.VALOR_TOTAL)
        val indiceData = cursor.getColumnIndex(DatabaseHelper.DATA_COMPRA)


        while (cursor.moveToNext()) {

            val idCompra =
                cursor.getInt(indiceId) // recupera o indice na coluna determinada, no caso coluna 1 no tipo INT
            val valorTotal =
                cursor.getDouble(indiceValor) // recupera o indice na coluna determinada, no caso coluna 2 no tipo String
            val data = cursor.getString(indiceData)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val localDate = LocalDate.parse(data, formatter)

            Log.i("info_db", "id: $idCompra valor: $valorTotal ")
            listaHistorico.add(
                Compra(idCompra, valorTotal, localDate)
            )


        }

        return listaHistorico

    }




}