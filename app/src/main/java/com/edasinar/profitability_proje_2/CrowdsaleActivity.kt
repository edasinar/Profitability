package com.edasinar.profitability_proje_2

import android.content.res.AssetManager
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.edasinar.profitability_proje_2.databinding.ActivityCrowdsaleBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.security.cert.CertPath

class CrowdsaleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrowdsaleBinding
    private lateinit var listOfOrders : ArrayList<Order>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrowdsaleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.title = "         TOPLU SATIŞ GİRİŞİ"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.mesajText.isVisible = false
    }

    fun dosyaOku(fileName: String){
        listOfOrders = ArrayList()
        var temp = fileName.split(".")
        if(!temp[1].equals("csv")){
            val alert = AlertDialog.Builder(this)
            alert.setTitle("HATALI DOSYA TÜRÜ UYARISI!")
            alert.setMessage(
                "Eklemek istediğiniz dosya csv uzantılı değildir. Lütfen başka " +
                        "dosya ekleyiniz"
            )
            alert.setNegativeButton("OK") { dialog, which ->
                Toast.makeText(applicationContext, "Tekrar Deneyiniz", Toast.LENGTH_LONG)
                    .show()
            }
            binding.mesajText.text = "dosya seçilemedi"
            alert.show()
        }
        else {
            val inputStreamReader = InputStreamReader(assets.open(fileName))
            val reader = BufferedReader(inputStreamReader)
            val col: ArrayList<String> = reader.readLine().split(",") as ArrayList<String>
            col[0] = col[0].split(col[0][0])[1]
            try {
                var line: String?
                var nullTutucu = arrayListOf<Int>()
                var index = 0
                while (reader.readLine().also { line = it } != null) {
                    var temp = line?.split(",")
                    var i = 0
                    while (i < 6) {
                        if (temp?.get(i).isNullOrBlank()) {
                            nullTutucu.add(index)
                        }
                        i += 1
                    }
                    listOfOrders.add(Order(temp!![0],temp!![1],temp!![2].toInt(),temp!![3],temp!![4].toInt(),temp!![5].toDouble()))
                    index += 1
                }

                if (!nullTutucu.isEmpty()) {
                    val alert = AlertDialog.Builder(this)
                    alert.setTitle("EKSİK BİLGİ UYARISI!")
                    alert.setMessage(
                        "Eklemek istediğiniz dosyalarda boşluklar vardır lütfen tüm boşlukları" +
                                " doldurup tekrar yüklemeyi deneyiniz."
                    )
                    alert.setNegativeButton("OK") { dialog, which ->
                        Toast.makeText(applicationContext, "Tekrar Deneyiniz", Toast.LENGTH_LONG)
                            .show()
                    }
                    binding.mesajText.text = "dosya seçilemedi"
                    alert.show()
                } else {
                    Toast.makeText(applicationContext,"Dosya Seçme Başarılı",Toast.LENGTH_LONG).show()
                    binding.mesajText.text = "dosya seçildi"
                }
                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
                println("Products Database Okunurken Hata Oldu")
            }
        }
    }

    fun dosyaSec(view: View){
        var fileName : String = ""
        val alert = AlertDialog.Builder(this)
        alert.setTitle("SELECTION")
        alert.setMessage("Aşağıdaki dosyalardan birini seçiniz")
        alert.setNeutralButton("Temmuz.txt"){ dialog, which ->
            fileName = "crowdsaleTemmuz.txt"
            dosyaOku(fileName)
        }
        alert.setPositiveButton("Mart.csv") { dialog, which ->
            fileName = "crowdsaleMart.csv"
            dosyaOku(fileName)
        }
        alert.setNegativeButton("Mayıs.csv") { dialog, which ->
            fileName = "crowdsaleMayis.csv"
            dosyaOku(fileName)
        }

        alert.show()

    }

    fun entegreEt(view: View){

        if(binding.mesajText.text.equals("dosya seçildi")){
            var database = this.openOrCreateDatabase("ORDERS", MODE_PRIVATE,null)
            for(i in listOfOrders){
                database.execSQL("INSERT INTO orders (Barkod,SiparişTarihi,SiparişNumarası,Alıcı,Adet,FaturalanacakTutar) VALUES ('${i.barkod}','${i.tarih}',${i.siparisNo},'${i.alici}',${i.adet},${i.faturaTutar})")
            }
            var cursor = database.rawQuery("SELECT * FROM orders",null)
            var tarihIx = cursor.getColumnIndex("SiparişTarihi")
            var isimIx = cursor.getColumnIndex("Alıcı")
            while (cursor.moveToNext()){
                println(cursor.getString(tarihIx) + "   " + cursor.getString(isimIx))
            }
            val alert = AlertDialog.Builder(this)
            alert.setTitle("DOSYA SEÇİLDİ")
            alert.setMessage("Seçtiğiniz dosya başarılı bir şekilde sisteme entegre edildi")
            alert.setNeutralButton("OK"){ dialog, which ->

            }

            alert.show()

        }
        else{
            val alert = AlertDialog.Builder(this)
            alert.setTitle("DOSYA SEÇİLEMEDİ")
            alert.setMessage("Dosya sisteme entegre edilemedi. Lütfen tekrar dosya seçin")
            alert.setNegativeButton("OK"){ dialog, which ->

            }

            alert.show()
        }
    }
}