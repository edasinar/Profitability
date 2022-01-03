package com.edasinar.profitability_proje_2

data class Order(
    var barkod: String, val tarih: String, var siparisNo: Int,
    val alici : String, val adet: Int, val faturaTutar : Double)
