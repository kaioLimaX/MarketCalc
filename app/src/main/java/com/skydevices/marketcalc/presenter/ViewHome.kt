package com.skydevices.marketcalc.presenter

import com.skydevices.marketcalc.model.Produto

interface ViewHome {
    interface View {
        fun showProgressBar()
        fun showFailule(message: String)
        fun hideProgressBar()
        fun showProducts(article: List<Produto>)
        fun showPurchases(article: List<Produto>)


    }




}