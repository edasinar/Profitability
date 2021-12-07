package com.edasinar.profitability_proje_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.edasinar.profitability_proje_2.databinding.ActivityAddNewProductBinding

class AddNewProduct : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun addDatabase(view: View){
        val barkod = binding.newBarkodEditText.getText().toString()
        val komisyon = binding.newKomisyonEditText.getText().toString()
        val renk = binding.newRenkEditText.getText().toString()
        val marka = binding.newMarkaEditText.getText().toString()
        val kategori = binding.newKategoriEditText.getText().toString()
        val urun = binding.newUrunEditText.getText().toString()
        val fiyat = binding.newFiyatEditText.getText().toString()
        val stok = binding.newStokAdetEditText.getText().toString()
        val database = this.openOrCreateDatabase("Product_Database", MODE_PRIVATE,null)
        database.execSQL("INSERT INTO products (Barkod,KomisyonOranı,ÜrünRengi,Marka,Kategoriİsmi,ÜrünAdı,SatisFiyat,ÜrünStokAdedi) VALUES('${barkod}',${komisyon.toDouble()},'${renk}','${marka}','${kategori}','${urun}',${fiyat.toDouble()},${stok.toInt()})")

        binding.newBarkodEditText.setText(null)
        binding.newKomisyonEditText.setText(null)
        binding.newRenkEditText.setText(null)
        binding.newMarkaEditText.setText(null)
        binding.newKategoriEditText.setText(null)
        binding.newUrunEditText.setText(null)
        binding.newFiyatEditText.setText(null)
        binding.newStokAdetEditText.setText(null)

        val intent = Intent(applicationContext,ProductsActivity::class.java)
        startActivity(intent)

    }
}