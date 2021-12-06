package com.edasinar.profitability_proje_2

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.edasinar.profitability_proje_2.databinding.ActivityReceiptOfExpensesBinding
import java.time.LocalDate

class ReceiptOfExpensesActivity : AppCompatActivity() {

    private lateinit var database : SQLiteDatabase

    private lateinit var binding: ActivityReceiptOfExpensesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiptOfExpensesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        database  = this.openOrCreateDatabase("Receipt_Of_Expenses", MODE_PRIVATE,null)
        database.execSQL("CREATE TABLE IF NOT EXISTS expenses (id INTEGER PRIMARY KEY, Tarih VARCHAR, Gider DOUBLE, Aciklama VARCHAR)")
    }

    fun kaydet(view: View){
        var yil = binding.yilEditText.getText().toString()
        var ay = binding.ayEditText.getText().toString()
        var gun = binding.gunEditText.getText().toString()
        var gider = binding.toplamGiderEditText.getText().toString().toDouble()
        var aciklama = binding.aciklamaEditText.getText().toString()

        var tarih: LocalDate = LocalDate.parse(yil+"-"+ay+"-"+gun)
        database.execSQL("INSERT INTO expenses (Tarih,Gider,Aciklama) VALUES ('${tarih}',${gider},'${aciklama}')")
        var cursor = database.rawQuery("SELECT * FROM expenses",null)
        var tari = cursor.getColumnIndex("Tarih")
        var gide = cursor.getColumnIndex("Gider")
        var aciklam = cursor.getColumnIndex("Aciklama")
        while (cursor.moveToNext()){
            println(cursor.getString(tari) + "  " + cursor.getString(gide) + "  " + cursor.getString(aciklam))
        }
        println("veritabanına başarıyla kaydedildi")
        binding.yilEditText.setText(null)
        binding.ayEditText.setText(null)
        binding.gunEditText.setText(null)
        binding.toplamGiderEditText.setText(null)
        binding.aciklamaEditText.setText(null)

    }


}