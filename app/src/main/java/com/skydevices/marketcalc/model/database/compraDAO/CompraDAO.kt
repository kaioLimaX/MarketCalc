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
    override fun iniciarCompra() : Int{
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
        valores.put(DatabaseHelper.VALOR_TOTAL,0.0)

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
                Compra(idCompra, status,localDate,valorTotal)
            )


        }

        return listaHistorico

    }
}