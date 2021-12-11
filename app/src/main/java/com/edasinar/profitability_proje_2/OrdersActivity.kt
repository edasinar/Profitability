package com.edasinar.profitability_proje_2

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.edasinar.profitability_proje_2.databinding.ActivityOrdersBinding
import android.text.Editable

import android.text.TextWatcher
import java.util.*
import kotlin.collections.ArrayList


class OrdersActivity : AppCompatActivity() {
    private  var index : ArrayList<Int> = arrayListOf()

    //VERİLER GİRDİ OLARAK ALINACAK DATABASEE EKLENECEK
    //BARKODLAR İLE EŞLEŞMESİ GEREK DEĞERLER BUTONA TIKLANDIKTAN SONRA TEMİZLENECEK
    private lateinit var binding: ActivityOrdersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val database = this.openOrCreateDatabase("ORDERS", MODE_PRIVATE,null)
        var cursor = database.rawQuery("SELECT * FROM orders" , null)
        var idIx = cursor.getColumnIndex("id")
        println(idIx)
        while (cursor.moveToNext()){
            println(cursor.getString(idIx))
            index.add(cursor.getString(idIx).toInt())
        }
        var date = binding.tarihSiparis
        date.addTextChangedListener(object : TextWatcher {
            private var current = ""
            private val ddmmyyyy = "YYYYMMDD"
            private val cal: Calendar = Calendar.getInstance()
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != current) {
                    var clean = s.toString().replace("[^\\d.]".toRegex(), "")
                    val cleanC = current.replace("[^\\d.]".toRegex(), "")
                    val cl = clean.length
                    var sel = cl
                    var i = 2
                    while (i <= cl && i < 6) {
                        sel++
                        i += 2
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean == cleanC) sel--
                    if (clean.length < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length)
                    } else {
                        var year = clean.substring(0, 4).toInt()
                        var mon = clean.substring(4, 6).toInt()
                        var day = clean.substring(6, 8).toInt()
                        if (mon > 12) mon = 12
                        cal.set(Calendar.MONTH, mon - 1)
                        year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                        cal.set(Calendar.YEAR, year)
                        day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(
                            Calendar.DATE
                        ) else day
                        clean = String.format("%02d%02d%02d", year, mon, day)
                    }
                    clean = String.format(
                        "%s/%s/%s", clean.substring(0, 4),
                        clean.substring(4, 6),
                        clean.substring(6, 8)
                    )
                    sel = if (sel < 0) 0 else sel
                    current = clean
                    date.setText(current)
                    date.setSelection(if (sel < current.length) sel else current.length)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
    }

    fun barkodKiyasla(barkod:String): Boolean{
        val database : SQLiteDatabase = this.openOrCreateDatabase("Product_Database", MODE_PRIVATE,null)
        val cursor = database.rawQuery("SELECT * FROM products WHERE ÜrünBarkodu = '${barkod}'",null)
        var retVal = 0
        var idIx = cursor.getColumnIndex("id")
        while (cursor.moveToNext()){
            cursor.getString(idIx)
            retVal += 1
        }
        if(retVal != 0){
            return true
        }
        return false
    }

    fun kaydet(view: View){
        val barkod = binding.barkodSiparis.getText().toString()
        val numara = binding.numaraSiparis.getText().toString()
        val alici = binding.aliciSiparis.getText().toString()
        val adet = binding.adetSiparis.getText().toString()
        val tutar = binding.tutarSiparis.getText().toString()
        var tarih = binding.tarihSiparis.getText().toString()
        if(barkod.isNullOrBlank()||tarih.isNullOrBlank()||numara.isNullOrBlank()||alici.isNullOrBlank()||adet.isNullOrBlank()||tutar.isNullOrBlank()){
            val alert = AlertDialog.Builder(this)
            alert.setTitle("UYARI!")
            alert.setMessage("Boş değer girişi yaptınız lütfen eksiksiz doldurunuz.")
            alert.setNegativeButton("OK"){dialog, which->
                Toast.makeText(applicationContext,"Try Again", Toast.LENGTH_LONG).show()}
            alert.show()

            binding.barkodSiparis.setText(null)
            binding.tarihSiparis.setText(null)
            binding.numaraSiparis.setText(null)
            binding.aliciSiparis.setText(null)
            binding.adetSiparis.setText(null)
            binding.tutarSiparis.setText(null)
        }else {
            val num = binding.numaraSiparis.getText().toString().toInt()
            val adt = binding.adetSiparis.getText().toString().toInt()
            val ttr = binding.tutarSiparis.getText().toString().toDouble()
            println(tarih.replace("/","-"))
            if (!barkodKiyasla(barkod)) {
                val alert = AlertDialog.Builder(this)
                alert.setTitle("UYARI!")
                alert.setMessage("Kayıtlı olmayan barkod değeri giridiniz.")
                alert.setNegativeButton("OK") { dialog, which ->
                    Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()
                }

                alert.show()
                binding.barkodSiparis.setText(null)
            } else {
                println("ürün başarıyla kaydedildi!")
                //DATABASE'E EKLENECEK KISIM!!
            }
        }
    }
}
