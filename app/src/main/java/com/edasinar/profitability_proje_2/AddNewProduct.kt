package com.edasinar.profitability_proje_2

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.edasinar.profitability_proje_2.ProductsActivity.Companion.addImageArray
import com.edasinar.profitability_proje_2.ProductsActivity.Companion.imageArray
import com.edasinar.profitability_proje_2.databinding.ActivityAddNewProductBinding
import java.util.regex.Matcher
import java.util.regex.Pattern

/*
*       BU SAYFADA ÜRÜN EKLENİRKEN FOTOĞRAF GALARİDEN ÇEKİLECEK
*       EĞER SEÇİLMEMİŞSE BOŞ FOTOĞRAFI ATANACAK!!
*
*       EKLEDİKTEN SONRA ÜRÜNLER EKRANINA GERİ DÖN
*
* */
class AddNewProduct : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewProductBinding

    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "      YENİ ÜRÜN EKLE"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            binding.secilenResim.setImageURI(imageUri)
            println(imageUri)
            if (imageUri.toString().isNullOrBlank()) {
                addImageArray.add("-")
            } else
                addImageArray.add(imageUri.toString())
        }
    }
    fun resimSec(view: View){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
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

    fun addDatabase(view: View){
        val barkod = binding.newBarkodEditText.getText().toString()
        val komisyon = binding.newKomisyonEditText.getText().toString()
        val renk = binding.newRenkEditText.getText().toString()
        val marka = binding.newMarkaEditText.getText().toString()
        val urun = binding.newUrunEditText.getText().toString()
        val fiyat = binding.newFiyatEditText.getText().toString()
        if(isSpace(barkod)){
            println("barkodda boşluk vardır lütfen boşluk bırakmadan girin")
            val alert = AlertDialog.Builder(this)
            alert.setTitle("UYARI!")
            alert.setMessage("Barkod boşluk karakterini içeremez. Başında ortasında ya da sonunda boşluk bırakmayınız.")
            alert.setNegativeButton("OK"){dialog, which->
                Toast.makeText(applicationContext,"Try Again", Toast.LENGTH_LONG).show()}

            alert.show()
            binding.newBarkodEditText.setText(null)
        }else {
            val database = this.openOrCreateDatabase("Product_Database", MODE_PRIVATE, null)
            database.execSQL("INSERT INTO products (ÜrünBarkodu,Komisyon,Renk,Marka,İsim,SatışTutarı) VALUES('${barkod}',${komisyon.toDouble()},'${renk}','${marka}','${urun}',${fiyat.toDouble()})")

            binding.newBarkodEditText.setText(null)
            binding.newKomisyonEditText.setText(null)
            binding.newRenkEditText.setText(null)
            binding.newMarkaEditText.setText(null)
            binding.newUrunEditText.setText(null)
            binding.newFiyatEditText.setText(null)

            val intent = Intent(applicationContext, ProductsActivity::class.java)
            startActivity(intent)
        }

    }
}