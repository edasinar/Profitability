package com.edasinar.profitability_proje_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.edasinar.profitability_proje_2.databinding.ActivityStockReceiptBinding
import java.io.BufferedWriter
import java.util.regex.Matcher
import java.util.regex.Pattern

class StockReceiptActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStockReceiptBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockReceiptBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.title = "            STOK GİRİŞİ"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


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

    fun stokKaydet(view : View){
        val database = this.openOrCreateDatabase("Stock_Receipt", MODE_PRIVATE,null)

        val yil = binding.stokYilEditText.getText().toString()
        val ay = binding.stokAyEditText.getText().toString()
        val gun = binding.stokGunEditText.getText().toString()
        val barkod = binding.stokBarkodEditText.getText().toString()
        val birimTutar = binding.stokBirimEditText.getText().toString().toDouble()
        val adet = binding.stokAdetEditText.getText().toString().toInt()

        if(isSpace(barkod)){
            println("barkodda boşluk vardır lütfen boşluk bırakmadan girin")
            val alert = AlertDialog.Builder(this)
            alert.setTitle("UYARI!")
            alert.setMessage("Barkod boşluk karakterini içeremez. Başında ortasında ya da sonunda boşluk bırakmayınız.")
            alert.setNegativeButton("OK"){dialog, which->
                Toast.makeText(applicationContext,"Try Again", Toast.LENGTH_LONG).show()}

            alert.show()
            binding.stokBarkodEditText.setText(null)
        }else {
            val tarih = yil+"-"+ay+"-"+gun
            val toplamTutar = adet.toDouble()*birimTutar
            database.execSQL("INSERT INTO products (AlınmaTarihi,ÜrünBarkodu,AlınanAdet,ÜrünAdetTutarı,ToplamTutar) VALUES('${tarih}','${barkod}',${adet},${birimTutar},${toplamTutar})")

            binding.stokAdetEditText.setText(null)
            binding.stokAyEditText.setText(null)
            binding.stokBarkodEditText.setText(null)
            binding.stokBirimEditText.setText(null)
            binding.stokGunEditText.setText(null)
            binding.stokYilEditText.setText(null)

        }

    }
}