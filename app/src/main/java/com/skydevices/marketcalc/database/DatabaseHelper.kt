package com.skydevices.marketcalc.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.skydevices.marketcalc.databinding.ActivityAdicionarTarefaBinding
import java.sql.Date
import java.time.LocalDate

class DatabaseHelper(context:Context): SQLiteOpenHelper(// databaseHelper herda de SqLiteOpenHelper

    context, "loja.db", null, 2//vc tem que passar o conteudo, Nome do banco de dados, CursosFactory e a versao do banco

) {

    companion object{
        const val TABELA_PRODUTOS = "produtos"
        const val TABELA_HISTORICO = "historico"
        const val ID_PRODUTO = "id_produto"
        const val ID_HISTORICO = "id_produto"
        const val VALOR = "valor"
        const val VALOR_TOTAL = "valor_total"
        const val QUANTIDADE = "quantidade"
        const val DATA_COMPRA = "data_compra"


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(db: SQLiteDatabase?) {


        val sql = "CREATE TABLE IF NOT EXISTS $TABELA_PRODUTOS(" +
                "$ID_PRODUTO INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "$VALOR DOUBLE(5,2)," +
                "$QUANTIDADE INTEGER" +
                ");"

        val sql2 = "CREATE TABLE IF NOT EXISTS $TABELA_HISTORICO(" +
                "$ID_HISTORICO INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "$VALOR_TOTAL DOUBLE(10,2)," +
                "$DATA_COMPRA DATE" +
                ");"

        try { // try é um comando necessario para tratamento de erros de retorno de funções
            db?.execSQL(sql) // comando para executar linha de codigo mysql
            db?.execSQL(sql2)
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