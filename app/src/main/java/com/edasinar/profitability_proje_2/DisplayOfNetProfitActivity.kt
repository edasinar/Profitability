package com.edasinar.profitability_proje_2

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.edasinar.profitability_proje_2.databinding.ActivityDisplayOfNetProfitBinding

class DisplayOfNetProfitActivity : AppCompatActivity() {
    private lateinit var ordersDatabase: SQLiteDatabase //siparişlerin olduğu ve hesaplanması gereken verilerin olduğu database
    private lateinit var productDatabase: SQLiteDatabase //ürünlerin olduğu database
    private lateinit var stockReceiptDatabase: SQLiteDatabase //stokta kaydedilen ürünlerin olduğu database
    private lateinit var otherExpensesDatabase : SQLiteDatabase //diğer harcamaların gözüktüğü database
    private lateinit var displayNetProfitDatabase: SQLiteDatabase //her şeyin gözükeceği database

    private lateinit var binding: ActivityDisplayOfNetProfitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayOfNetProfitBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        supportActionBar?.title = "  AYLIK NET KAR GÖSTERİMİ"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        databaseGuncelleme()

    }

    fun databaseGuncelleme(){
        displayNetProfitDatabase = this.openOrCreateDatabase("Display_Net_Profit", MODE_PRIVATE,null)

        var fatura = aylikFaturaTutarHesaplama()
        var kargo = aylikKargoUcretiHesaplama()
        var komisyon = aylikKomisyonUcretiHesaplama()
        var birimAdet = aylikBirimAdetHesaplama()
        var digerHarcamalar = aylikDigerHarcamalarHesaplama()
        println(fatura)
        println(kargo)
        println(komisyon)
        println(birimAdet)
        println(digerHarcamalar)


        var ay : ArrayList<String> = arrayListOf("01","02","03","04","05","06","07","08","09","10","11","12")
        var aylar = arrayListOf<String>("OCAK","ŞUBAT","MART","NİSAN","MAYIS","HAZİRAN","TEMMUZ","AĞUSTOS",
            "EYLÜL","EKİM","KASIM","ARALIK")
        var aylarIslem = HashMap<String,String>()
        var k = 0
        while(k < ay.size){
            aylarIslem.put(ay[k],aylar[k])
            k+=1
        }
        var aylikBrutKazanc = HashMap<String,Double>()

        var i = 0
        while(i < ay.size){
            var brut = fatura[ay[i]]!! - (kargo[ay[i]]!! + komisyon[ay[i]]!! + birimAdet[ay[i]]!!)
            aylikBrutKazanc.put(ay[i],brut)
            i += 1
        }
        println(aylikBrutKazanc)
        i = 0
        while(i < ay.size){
            displayNetProfitDatabase.execSQL("UPDATE net_profits SET BrütKar = ${aylikBrutKazanc[ay[i]]},DiğerGiderler = ${digerHarcamalar[ay[i]]}, NetKar = ${(aylikBrutKazanc[ay[i]]!!-digerHarcamalar[ay[i]]!!)} WHERE Aylar = '${aylarIslem[ay[i]]}'")
            i+=1
        }
        var indexFilter = arrayListOf<Int>(1,2,3,4,5,6,7,8,9,10,11,12)
        binding.displayRecycler.layoutManager = LinearLayoutManager(this)
        val displayAdapter = DisplayOfNetProfitAdapter(indexFilter, displayNetProfitDatabase)
        binding.displayRecycler.adapter = displayAdapter
    }

    fun aylikFaturaTutarHesaplama() : HashMap<String, Double>{
        ordersDatabase = this.openOrCreateDatabase("ORDERS", MODE_PRIVATE,null)
        var cursorSiparis = ordersDatabase.rawQuery("SELECT * FROM orders ",null)

        var tarih = cursorSiparis.getColumnIndex("SiparişTarihi")
        val kisiKargo = cursorSiparis.getColumnIndex("FaturalanacakTutar")

        var ay : ArrayList<String> = arrayListOf("01","02","03","04","05","06","07","08","09","10","11","12")
        var aylikFaturalanacakTutar = HashMap<String,Double>()
        var i = 0
        while(i < ay.size){
            aylikFaturalanacakTutar.put(ay[i],0.0)
            i+=1
        }
        i = 0
        while(i < ay.size){
            var aylikFaturaTutar = 0.0
            while(cursorSiparis.moveToNext()){
                if(cursorSiparis.getString(tarih).split(".")[1].equals(ay[i])){
                    aylikFaturaTutar += cursorSiparis.getString(kisiKargo).toDouble()
                }
            }
            aylikFaturalanacakTutar[ay[i]] = aylikFaturaTutar
            cursorSiparis = ordersDatabase.rawQuery("SELECT * FROM orders ",null)
            i+=1
        }

        return aylikFaturalanacakTutar
    }

    fun aylikKargoUcretiHesaplama() : HashMap<String,Double>{
        ordersDatabase = this.openOrCreateDatabase("ORDERS", MODE_PRIVATE,null)
        var cursorSiparis = ordersDatabase.rawQuery("SELECT * FROM orders ",null)
        var cursorKisiler = ordersDatabase.rawQuery("SELECT * FROM ordering_people_list",null)

        var kisiKisiler = cursorKisiler.getColumnIndex("isimSoyisim")
        var kisiSiparisler = cursorSiparis.getColumnIndex("Alıcı")
        var tarihSiparisler = cursorSiparis.getColumnIndex("SiparişTarihi")
        var faturaSiparisler = cursorSiparis.getColumnIndex("FaturalanacakTutar")

        var ay : ArrayList<String> = arrayListOf("01","02","03","04","05","06","07","08","09","10","11","12")
        var kisilerKargoTutari = HashMap<String,Double>()

        var k = 0
        var kisiTarih = HashMap<String,String>()
        while(cursorKisiler.moveToNext()){
            var kisiFaturaTutar = 0.0
            var ayDeger = ""
            while(cursorSiparis.moveToNext()){
                if(cursorKisiler.getString(kisiKisiler).equals(cursorSiparis.getString(kisiSiparisler))){
                    kisiFaturaTutar += cursorSiparis.getString(faturaSiparisler).toDouble()
                    ayDeger = cursorSiparis.getString(tarihSiparisler).split(".")[1]
                }
            }
            var kisiKargoTutar : Double?
            if(kisiFaturaTutar > 0.0 && kisiFaturaTutar <= 19.99)
                kisiKargoTutar = 2.99
            else if(kisiFaturaTutar >= 20.0 && kisiFaturaTutar <= 29.99)
                kisiKargoTutar = 4.98
            else if(kisiFaturaTutar >= 30.0 && kisiFaturaTutar <= 49.99)
                kisiKargoTutar = 8.5
            else
                kisiKargoTutar = 11.5
            ayDeger += ".${k}"
            kisilerKargoTutari.put(ayDeger,kisiKargoTutar)
            cursorSiparis = ordersDatabase.rawQuery("SELECT * FROM orders ",null)
            k += 1
        }

        var aylikKargoTutar = HashMap<String,Double>()
        var i = 0
        while(i < ay.size){
            aylikKargoTutar.put(ay[i],0.0)
            i+=1
        }

        i = 0
        while(i < ay.size){
            val values = kisilerKargoTutari.filterKeys { it.split(".")[0].equals(ay[i]) }.values
            var kargotutar = 0.0
            if(!values.isEmpty()) {
                for (k in values) {
                    kargotutar += k
                }
                aylikKargoTutar[ay[i]] = kargotutar
            }
            i += 1
        }
        println(aylikKargoTutar)
        return aylikKargoTutar
    }

    fun aylikKomisyonUcretiHesaplama() : HashMap<String,Double>{
        ordersDatabase = this.openOrCreateDatabase("ORDERS", MODE_PRIVATE,null)
        productDatabase = this.openOrCreateDatabase("Product_Database", MODE_PRIVATE,null)

        var cursorSiparis = ordersDatabase.rawQuery("SELECT * FROM orders ",null)
        var cursorProduct = productDatabase.rawQuery("SELECT * FROM products",null)

        var barkodUrun = cursorProduct.getColumnIndex("ÜrünBarkodu")
        var komisyonUrun  = cursorProduct.getColumnIndex("Komisyon")

        var barkodSiparis = cursorSiparis.getColumnIndex("Barkod")
        var faturaUcretSiparis = cursorSiparis.getColumnIndex("FaturalanacakTutar")
        var tarih = cursorSiparis.getColumnIndex("SiparişTarihi")

        var barkodKomisyon = HashMap<String,Double>()
        var tarihKomisyonOrani = HashMap<String, Double>()
        var ay : ArrayList<String> = arrayListOf("01","02","03","04","05","06","07","08","09","10","11","12")

        while(cursorProduct.moveToNext()){
            barkodKomisyon.put(cursorProduct.getString(barkodUrun),cursorProduct.getDouble(komisyonUrun))
        }
        var i = 1
        while(cursorSiparis.moveToNext()){
            var komisyon = (cursorSiparis.getString(faturaUcretSiparis).toDouble() * barkodKomisyon[cursorSiparis.getString(barkodSiparis)]!!)/100
            var trh = cursorSiparis.getString(tarih) + ".${i}"
            tarihKomisyonOrani.put(trh,komisyon)
            i+=1
        }

        var aylikKomisyonTutar = HashMap<String,Double>()
        i = 0
        while(i < ay.size){
            aylikKomisyonTutar.put(ay[i],0.0)
            i+=1
        }

        i = 0
        while(i < ay.size){
            val values = tarihKomisyonOrani.filterKeys { it.split(".")[1].equals(ay[i]) }.values
            var kargotutar = 0.0
            if(!values.isEmpty()) {
                for (k in values) {
                    kargotutar += k
                }
                aylikKomisyonTutar[ay[i]] = kargotutar
            }
            i += 1
        }
        return aylikKomisyonTutar
    }

    fun aylikBirimAdetHesaplama() : HashMap<String,Double>{
        ordersDatabase = this.openOrCreateDatabase("ORDERS", MODE_PRIVATE,null)
        stockReceiptDatabase = this.openOrCreateDatabase("Stock_Receipt", MODE_PRIVATE,null)

        var cursorSiparis = ordersDatabase.rawQuery("SELECT * FROM orders ",null)
        var cursorStok = stockReceiptDatabase.rawQuery("SELECT * FROM stock_receipt",null)

        var barkodStok = cursorStok.getColumnIndex("ÜrünBarkodu")
        var birimFiyatStok = cursorStok.getColumnIndex("ÜrünAdetTutarı")

        var barkodSiparis = cursorSiparis.getColumnIndex("Barkod")
        var alinanAdetSiparis = cursorSiparis.getColumnIndex("Adet")
        var tarih = cursorSiparis.getColumnIndex("SiparişTarihi")

        var stokHashMap = HashMap<String,Double>()
        var tarihAdetFyt = HashMap<String,Double>()
        var ay : ArrayList<String> = arrayListOf("01","02","03","04","05","06","07","08","09","10","11","12")

        var i = 1

        while(cursorStok.moveToNext()){
            stokHashMap.put(cursorStok.getString(barkodStok),cursorStok.getDouble(birimFiyatStok))
        }
        while(cursorSiparis.moveToNext()){
            var adet = stokHashMap[cursorSiparis.getString(barkodSiparis)]!! * cursorSiparis.getString(alinanAdetSiparis).toDouble()!!
            var trh = cursorSiparis.getString(tarih)+".${i}"
            tarihAdetFyt.put(trh, adet)
            i+=1
        }

        var aylikToplamAdetFytTutar = HashMap<String,Double>()
        i = 0
        while(i < ay.size){
            aylikToplamAdetFytTutar.put(ay[i],0.0)
            i+=1
        }

        i = 0
        while(i < ay.size){
            val values = tarihAdetFyt.filterKeys { it.split(".")[1].equals(ay[i]) }.values
            var kargotutar = 0.0
            if(!values.isEmpty()) {
                for (k in values) {
                    kargotutar += k
                }
                aylikToplamAdetFytTutar[ay[i]] = kargotutar
            }
            i += 1
        }
        return aylikToplamAdetFytTutar
    }

    fun aylikDigerHarcamalarHesaplama() : HashMap<String,Double>{
        otherExpensesDatabase = this.openOrCreateDatabase("Receipt_Of_Expenses", MODE_PRIVATE,null)
        var cursor = otherExpensesDatabase.rawQuery("SELECT * FROM other_expenses ORDER BY Ay",null)
        var ay : ArrayList<String> = arrayListOf("01","02","03","04","05","06","07","08","09","10","11","12")
        val tutarIx = cursor.getColumnIndex("Tutar")
        val ayIx = cursor.getColumnIndex("Ay")
        var aylikToplam = HashMap<String,Double>()
        var i = 0
        while(i < ay.size){
            aylikToplam.put(ay[i],0.0)
            i+=1
        }
        var indisKontrol = 0
        while(indisKontrol < ay.size){
            var aylikTplm = 0.0
            while (cursor.moveToNext()){
                if(cursor.getString(ayIx).toString().equals(ay[indisKontrol])){
                    aylikTplm += cursor.getString(tutarIx).toDouble()
                }
            }
            aylikToplam[ay[indisKontrol]] = aylikTplm
            cursor = otherExpensesDatabase.rawQuery("SELECT * FROM other_expenses ORDER BY Ay",null)
            indisKontrol += 1
        }
        return aylikToplam
    }
}