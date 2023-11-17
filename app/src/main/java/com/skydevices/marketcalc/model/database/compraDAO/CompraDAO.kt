package com.skydevices.marketcalc.model.database.compraDAO

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.skydevices.marketcalc.model.Compra
import com.skydevices.marketcalc.model.database.DatabaseHelper
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CompraDAO(context: Context) : ICompraDAO {

    private val escrita = DatabaseHelper(context).writableDatabase
    private val leitura = DatabaseHelper(context).readableDatabase



    @RequiresApi(Build.VERSION_CODES.O)
    override fun listarCompra(): MutableList<Compra> {
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
                Compra(idCompra, status,localDate,valorTotal)
            )


        }

        return listaHistorico

    }

    override fun removerCompra(id: Int): Boolean {


        val args = arrayOf(id.toString())

        try {
            escrita.delete(
                DatabaseHelper.TABELA_PRODUTOS,
                "${DatabaseHelper.ID_COMPRA} = ?",
                args

            )

            escrita.delete(
                DatabaseHelper.TABELA_COMPRAS,
                "${DatabaseHelper.ID_COMPRA} = ?",
                args

            )


            Log.i("info_db", "Sucesso ao Remover Compra ") // log do sistema


        } catch (e: Exception) {
            Log.i("info_db", "Falha ao Remover Compra ")

            return false
        }
        return true
    }
}