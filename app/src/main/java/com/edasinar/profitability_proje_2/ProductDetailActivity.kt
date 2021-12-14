package com.edasinar.profitability_proje_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.edasinar.profitability_proje_2.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.title = "         ÜRÜN DETAYLARI"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        var position = intent.getIntExtra("position",0)
        val productsDatabase  = this.openOrCreateDatabase("Product_Database", MODE_PRIVATE,null)
        var cursor = productsDatabase.rawQuery("SELECT * FROM products WHERE id = ${(position+2)}" , null)
        if((position+1)>=ProductsActivity.imageArray.size){
            binding.imageView.setImageResource(R.drawable.bos)
        }else{
            binding.imageView.setImageResource(ProductsActivity.imageArray[position+1])
        }

        val adIx = cursor.getColumnIndex("İsim")
        val barkodIx = cursor.getColumnIndex("ÜrünBarkodu")
        val komisyonIx = cursor.getColumnIndex("Komisyon")
        val fiyatIx = cursor.getColumnIndex("SatışTutarı")
        val renkIx = cursor.getColumnIndex("Renk")
        val markaIx = cursor.getColumnIndex("Marka")

        var ad = ""
        var barkod =""
        var komisyon = ""
        var satisFiyat = ""
        var renk = ""
        var marka = ""
        while(cursor.moveToNext()){
            ad = cursor.getString(adIx)
            barkod = cursor.getString(barkodIx)
            komisyon = cursor.getString(komisyonIx)
            satisFiyat = cursor.getString(fiyatIx)
            renk = cursor.getString(renkIx)
            marka = cursor.getString(markaIx)
        }
        binding.isimD.text = ad
        binding.barkodD.setText(barkod)
        binding.komisyonD.setText(komisyon)
        binding.fiyatD.setText(satisFiyat)
        binding.renkD.setText(renk)
        binding.markaD.setText(marka)
    }
}
