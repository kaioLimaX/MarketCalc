package com.skydevices.marketcalc.model.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.skydevices.marketcalc.presenter.principal.PrincipalPresenter
import java.time.LocalDate

class DatabaseHelper(context: Context): SQLiteOpenHelper(// databaseHelper herda de SqLiteOpenHelper

    context, "loja.db", null, 2//vc tem que passar o conteudo, Nome do banco de dados, CursosFactory e a versao do banco

) {

    companion object{
        const val TABELA_PRODUTOS = "produtos"
        const val TABELA_COMPRAS = "compras"
        const val ID_PRODUTO = "id_produto"
        const val ID_COMPRA = "id_compra"
        const val DESCRICAO_PRODUTO = "descricao"
        const val VALOR = "valor"
        const val VALOR_TOTAL = "valor_total"
        const val STATUS_COMPRA = "status"
        const val QUANTIDADE = "quantidade"
        const val DATA_COMPRA = "data_compra"


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(db: SQLiteDatabase?) {

        val sql = "CREATE TABLE IF NOT EXISTS $TABELA_COMPRAS(" +
                "$ID_COMPRA INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "$STATUS_COMPRA INTEGER," +
                "$VALOR_TOTAL DOUBLE(8,2)," +
                "$DATA_COMPRA DATE" +
                ");"

        val sql2 = "CREATE TABLE IF NOT EXISTS $TABELA_PRODUTOS(" +
                "$ID_PRODUTO INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "$DESCRICAO_PRODUTO TEXT," +
                "$ID_COMPRA INTEGER, " +
                "$VALOR DOUBLE(5,2)," +
                "$QUANTIDADE INTEGER" +
                ");"


        try { // try é um comando necessario para tratamento de erros de retorno de funções
            db?.apply {
                execSQL(sql)
                execSQL(sql2)
            }
            Log.i("info_db", "Sucesso ao criar Tabela ")
        }catch (e : Exception){ // catch captura a excessao(erro) caso o comando execSQL retornar
            e.printStackTrace()
            Log.i("info_db", "Erro ao criar Tabela ")
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i("info_db", "nova versão inserida ")
    }

}