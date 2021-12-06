package com.edasinar.profitability_proje_2

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.edasinar.profitability_proje_2.databinding.ActivityDisplayOfNetProfitBinding

class DisplayOfNetProfitActivity : AppCompatActivity() {
    private lateinit var netProfitDatabase : SQLiteDatabase
    private lateinit var stockDatabase: SQLiteDatabase
    private lateinit var orderDatabase : SQLiteDatabase
    private lateinit var peopleDatabase: SQLiteDatabase
    private lateinit var expensesDatabase: SQLiteDatabase

    lateinit var aylikBorcStok : ArrayList<Double> //gider
    lateinit var aylikKazancStok: ArrayList<Double> //netkar
    lateinit var aylikKargoTutarlariSiparis : ArrayList<Double> //kargo ücreti gider
    lateinit var aylikKomisyonTutarlariSiparis : ArrayList<Double> //gider
    lateinit var aylikFaturaTutarlariSiparis : ArrayList<Double> //brütgelir
    lateinit var aylikGiderExpenses : ArrayList<Double> //gider

    private lateinit var binding : ActivityDisplayOfNetProfitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayOfNetProfitBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)
        try {
            //stokGelirHesapla()
            //siparisGelirHesapla()
            //giderlerGelirHesapla()
            //createNetProfitDatabase()
            netProfitDatabase = this.openOrCreateDatabase("PROFITS" , MODE_PRIVATE, null)
            var index = arrayListOf<Int>(1,2,3,4,5,6,7,8,9,10,11,12)
            binding.displayRecycler.layoutManager = LinearLayoutManager(this)
            val displayOfNetProfitAdapter = DisplayOfNetProfitAdapter(index,netProfitDatabase)
            binding.displayRecycler.adapter = displayOfNetProfitAdapter
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun stokGelirHesapla(){
        try {
            stockDatabase = this.openOrCreateDatabase("Stock_Receipt", MODE_PRIVATE,null)
            var cursor: Cursor = stockDatabase.rawQuery("SELECT * FROM stock_receipt",null)
            var borcIx = cursor.getColumnIndex("Borç")
            var alacakIx = cursor.getColumnIndex("Alacak")
            var borclar : ArrayList<Double> = arrayListOf()
            var alacaklar : ArrayList<Double> = arrayListOf()
            while (cursor.moveToNext()){
                borclar.add(cursor.getString(borcIx).toDouble())
                alacaklar.add(cursor.getString(alacakIx).toDouble())
            }
            aylikBorcStok  = arrayListOf()
            aylikKazancStok = arrayListOf()
            var i = 0
            var toplamBorc = 0.0
            var toplamAlacak = 0.0
            while(i < borclar.size){
                toplamBorc+=borclar[i]
                toplamAlacak+=alacaklar[i]
                if((i+1)%3 ==0&&i!=0){
                    aylikBorcStok.add(toplamBorc)
                    aylikKazancStok.add(toplamAlacak)
                    toplamBorc = 0.0
                    toplamAlacak = 0.0
                }
                i+=1
            }
        }catch (e : java.lang.Exception){
            e.printStackTrace()
        }
    }

    fun siparisGelirHesapla(){
        try {
            orderDatabase = this.openOrCreateDatabase("ORDERS", MODE_PRIVATE,null)
            var cursorOrder: Cursor = orderDatabase.rawQuery("SELECT * FROM orders",null)
            peopleDatabase = this.openOrCreateDatabase("ORDERS", MODE_PRIVATE,null)
            var cursorPeople : Cursor = peopleDatabase.rawQuery("SELECT * FROM ordering_people_list",null)
            var isimSoyisimIx = cursorPeople.getColumnIndex("isimSoyisim")

            var aliciIx = cursorOrder.getColumnIndex("Alıcı")
            var tarihIx = cursorOrder.getColumnIndex("SiparişTarihi")
            var kargoTutarIx = cursorOrder.getColumnIndex("FaturalananKargoTutarı")
            var komisyonIx = cursorOrder.getColumnIndex("KomisyonOranı")
            var faturaUcretiIx = cursorOrder.getColumnIndex("FaturalanacakTutar")

            var tarihler : ArrayList<String> = arrayListOf()
            var kargoTutarlari : ArrayList<Double> = arrayListOf()
            var komisyonGiderleri : ArrayList<Double> = arrayListOf()
            var faturaUcretleri : ArrayList<Double> = arrayListOf()
            while(cursorPeople.moveToNext()){
                var k = 0
                while (cursorOrder.moveToNext()){
                    if(cursorPeople.getString(isimSoyisimIx) == cursorOrder.getString(aliciIx) && k == 0){
                        tarihler.add(cursorOrder.getString(tarihIx).split("-")[1])
                        kargoTutarlari.add(cursorOrder.getString(kargoTutarIx).toDouble())
                        var komisyonUcreti = (cursorOrder.getString(faturaUcretiIx).toDouble()*
                                cursorOrder.getString(komisyonIx).toDouble())/100
                        komisyonGiderleri.add(String.format("%.2f",komisyonUcreti).toDouble())
                        faturaUcretleri.add(String.format("%.2f",(cursorOrder.getString(faturaUcretiIx).toDouble()-komisyonUcreti)).toDouble())

                        k += 1
                    }
                }
                cursorOrder = orderDatabase.rawQuery("SELECT * FROM orders",null)
            }
            var i = 0
            aylikKargoTutarlariSiparis  = arrayListOf()
            aylikKomisyonTutarlariSiparis = arrayListOf()
            aylikFaturaTutarlariSiparis = arrayListOf()
            while(i < tarihler.distinct().size){
                var toplam = 0.0
                var toplamKomisyon = 0.0
                var toplamFatura = 0.0
                var j = 0
                while(j < tarihler.size){
                  if(tarihler.distinct()[i] == tarihler[j]){
                      toplam += kargoTutarlari[j]
                      toplamKomisyon += komisyonGiderleri[j]
                      toplamFatura += faturaUcretleri[j]
                  }
                    j+=1
                }
                aylikKargoTutarlariSiparis.add(String.format("%.2f",toplam).toDouble())
                aylikKomisyonTutarlariSiparis.add(String.format("%.2f",toplamKomisyon).toDouble())
                aylikFaturaTutarlariSiparis.add(String.format("%.2f",toplamFatura).toDouble())
                i+=1
            }
        }catch (e : Exception){
            e.printStackTrace()
        }


    }

    fun giderlerGelirHesapla(){
        try {
            expensesDatabase = this.openOrCreateDatabase("Receipt_Of_Expenses", MODE_PRIVATE,null)
            var cursor : Cursor = expensesDatabase.rawQuery("SELECT * FROM expenses",null)
            var tarihIx = cursor.getColumnIndex("Tarih")
            var giderIx = cursor.getColumnIndex("Gider")

            var giderler : ArrayList<Double> = arrayListOf()
            var tarihler : ArrayList<String> = arrayListOf()
            while (cursor.moveToNext()){
                giderler.add(cursor.getString(giderIx).toDouble())
                tarihler.add(cursor.getString(tarihIx).split("-")[1])
            }

            var i = 0
            var trh : ArrayList<String> = arrayListOf("01","02","03","04","05","06","07","08","09","10","11","12")
            aylikGiderExpenses = arrayListOf()
            while(i < trh.size){
                var toplam = 0.0
                var j = 0
                while(j < tarihler.size){
                    if(trh[i] == tarihler[j]){
                        toplam += giderler[j]
                    }
                    j+=1
                }
                aylikGiderExpenses.add(String.format("%.2f",toplam).toDouble())
                i+=1
            }
        }catch (e : Exception){
            e.printStackTrace()
        }

    }

    fun createNetProfitDatabase(){
        try {

            netProfitDatabase.execSQL("CREATE TABLE IF NOT EXISTS profits (id INTEGER PRIMARY KEY , Ay VARCHAR, Gider VARCHAR, Brüt VARCHAR,Net VARCHAR)")
            val aylar : ArrayList<String> = arrayListOf("OCAK","ŞUBAT","MART","NİSAN","MAYIS","HAZİRAN",
            "TEMMUZ","AĞUSTOS","EYLÜL","EKİM","KASIM","ARALIK")

            var i = 0
            var gider : ArrayList<Double> = arrayListOf()
            var brut : ArrayList<Double> = arrayListOf()
            var net : ArrayList<Double> = arrayListOf()
            while(i < aylar.size){
                var giderT = aylikBorcStok[i]+aylikKargoTutarlariSiparis[i]+aylikKomisyonTutarlariSiparis[i]+aylikGiderExpenses[i]
                var brutT = aylikFaturaTutarlariSiparis[i]-(aylikKargoTutarlariSiparis[i]+aylikKomisyonTutarlariSiparis[i])
                var netT = aylikKazancStok[i]+brutT-(aylikBorcStok[i]+aylikGiderExpenses[i])
                gider.add(String.format("%.2f",giderT).toDouble())
                brut.add(String.format("%.2f",brutT).toDouble())
                net.add(String.format("%.2f",netT).toDouble())
                i += 1
            }


            /*i = 0
            while(i < aylar.size){
                netProfitDatabase.execSQL("INSERT INTO profits (Ay,Gider,Brüt,Net) VALUES ('${aylar[i]}',${gider[i]},${brut[i]},${net[i]})")
                i += 1
            }*/

            var cursor = netProfitDatabase.rawQuery("SELECT * FROM profits",null)
            var ayIx = cursor.getColumnIndex("Ay")
            while(cursor.moveToNext()){
                println(cursor.getString(ayIx))
            }

        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}