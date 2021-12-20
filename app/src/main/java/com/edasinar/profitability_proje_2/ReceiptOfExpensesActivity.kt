package com.edasinar.profitability_proje_2

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.edasinar.profitability_proje_2.databinding.ActivityReceiptOfExpensesBinding
import java.time.LocalDate
import java.util.regex.Matcher
import java.util.regex.Pattern

class ReceiptOfExpensesActivity : AppCompatActivity() {

    private lateinit var database : SQLiteDatabase

    private lateinit var binding: ActivityReceiptOfExpensesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiptOfExpensesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        supportActionBar?.title = "        HARCAMA GİRİŞİ"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        database  = this.openOrCreateDatabase("Receipt_Of_Expenses", MODE_PRIVATE,null)
        database.execSQL("CREATE TABLE IF NOT EXISTS expenses (id INTEGER PRIMARY KEY, Tarih VARCHAR, Gider DOUBLE, Aciklama VARCHAR)")
    }

    private fun isSpace(barkod: String): Boolean{
        val space: Pattern = Pattern.compile("\\s+")
        val matcherSpace: Matcher = space.matcher(barkod)
        val containsSpace: Boolean = matcherSpace.find()

        if (containsSpace === true) {
            return true
        }

        return false
    }

    fun kaydet(view: View){
        var yil = binding.yilEditText.getText().toString()
        var ay = binding.ayEditText.getText().toString()
        var gun = binding.gunEditText.getText().toString()
        var gider = binding.toplamGiderEditText.getText().toString()
        var aciklama = binding.aciklamaEditText.getText().toString()
        /*if(yil.isNullOrBlank() && ay.isNullOrBlank() && gun.isNullOrBlank() && gider.toString().isNullOrBlank()){
            val alert = AlertDialog.Builder(this)
            alert.setTitle("UYARI!")
            alert.setMessage("Tüm alanları boş bıraktınız. Sadece açıklamayı boş bırakabilirsiniz")
            alert.setNegativeButton("OK"){dialog, which->
                Toast.makeText(applicationContext,"Try Again", Toast.LENGTH_LONG).show()}

            alert.show()
            binding.yilEditText.setText(null)
            binding.ayEditText.setText(null)
            binding.gunEditText.setText(null)
            binding.toplamGiderEditText.setText(null)
        }*/
            if (yil.isNullOrBlank() || ay.isNullOrBlank() || gun.isNullOrBlank() || gider.toString()
                    .isNullOrBlank()) {
                val alert = AlertDialog.Builder(this)
                alert.setTitle("UYARI!")
                alert.setMessage("Sadece açıklamayı boş bırakabilirsiniz")
                alert.setNegativeButton("OK") { dialog, which ->
                    Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()
                }

                alert.show()
                binding.yilEditText.setText(null)
                binding.ayEditText.setText(null)
                binding.gunEditText.setText(null)
                binding.toplamGiderEditText.setText(null)
                binding.aciklamaEditText.setText(null)
            }
            else {
                if (aciklama.isNullOrBlank()) {
                    aciklama = "-"
                }
                var giderr = binding.toplamGiderEditText.getText().toString().toDouble()
                database.execSQL("INSERT INTO other_expenses (Yıl,Ay,Gün,Tutar,Açıklama) VALUES ('${yil}','${ay}','${gun}',${giderr},'${aciklama}')")
                var cursor = database.rawQuery("SELECT * FROM other_expenses", null)
                var tari = cursor.getColumnIndex("Yıl")
                var gide = cursor.getColumnIndex("Tutar")
                var aciklam = cursor.getColumnIndex("Açıklama")
                while (cursor.moveToNext()) {
                    println(
                        cursor.getString(tari) + "  " + cursor.getString(gide) + "  " + cursor.getString(
                            aciklam
                        )
                    )
                }
                println("veritabanına başarıyla kaydedildi")
                binding.yilEditText.setText(null)
                binding.ayEditText.setText(null)
                binding.gunEditText.setText(null)
                binding.toplamGiderEditText.setText(null)
                binding.aciklamaEditText.setText(null)
            }


    }


}