package com.edasinar.profitability_proje_2

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.edasinar.profitability_proje_2.ProductsActivity.Companion.addImageArray
import com.edasinar.profitability_proje_2.ProductsActivity.Companion.imageArray
import com.edasinar.profitability_proje_2.databinding.ActivityProductsBinding
import com.edasinar.profitability_proje_2.databinding.ProductListItemBinding


class ProductsAdapter(val index : ArrayList<Int>,val productsDatabase : SQLiteDatabase) : RecyclerView.Adapter<ProductsAdapter.ProductHolder>() {
    class ProductHolder(val binding: ProductListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val gorsel = binding.gorselImageView
        val barkod = binding.barkodTextViewR
        val komisyon = binding.komisyonTextViewR
        val satisFiyat = binding.satisTextViewR
        val silButton = binding.silButton
    }

    private lateinit var binding : ProductListItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        binding = ProductListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        val currentItem = index[position]
        var cursor = productsDatabase.rawQuery("SELECT * FROM products WHERE id = ${index?.get(position)}" , null)
        val barkodIx = cursor.getColumnIndex("ÜrünBarkodu")
        val komisyonIx = cursor.getColumnIndex("Komisyon")
        val satisFiyatiIx = cursor.getColumnIndex("SatışTutarı")
        val urunAd = cursor.getColumnIndex("İsim")
        var barkod =""
        var komisyon = ""
        var satisFiyat = ""
        var urunAdi = ""
        while(cursor.moveToNext()){
            barkod = cursor.getString(barkodIx)
            komisyon = cursor.getString(komisyonIx)
            satisFiyat = cursor.getString(satisFiyatiIx)
            urunAdi = cursor.getString(urunAd)
        }
        if(position>= imageArray.size){
            holder.gorsel.setImageResource(R.drawable.bos)
        }
        else {
            holder.gorsel.setImageResource(imageArray[position])
        }
        holder.barkod.text = barkod
        holder.komisyon.text = komisyon
        holder.satisFiyat.text = satisFiyat
        holder.silButton.setOnClickListener {
            //geri gidip tekrar girince siliyor!!
            productsDatabase.execSQL("DELETE FROM products WHERE id = ${index?.get(position)}")

        }
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ProductDetailActivity::class.java)
            val deger : Int = position - 1
            intent.putExtra("position",deger)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return index.size
    }


}