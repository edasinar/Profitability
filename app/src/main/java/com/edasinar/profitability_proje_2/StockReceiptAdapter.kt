package com.edasinar.profitability_proje_2

import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edasinar.profitability_proje_2.databinding.StockReceiptListItemBinding

class StockReceiptAdapter(val index : ArrayList<Int>,val stockDatabase : SQLiteDatabase): RecyclerView.Adapter<StockReceiptAdapter.StockReceiptHolder>() {

    class StockReceiptHolder(val binding: StockReceiptListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val tarih = binding.tarihListItem
        val islem = binding.islemListItem
        val urun = binding.urunListItem
        val adet = binding.adetListItem
        val tutar = binding.tutarListItem
        val borc = binding.borcListItem
        val alacak = binding.alacakListItem
    }

    private lateinit var binding : StockReceiptListItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockReceiptHolder {
        binding = StockReceiptListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return StockReceiptAdapter.StockReceiptHolder(binding)
    }

    override fun onBindViewHolder(holder: StockReceiptHolder, position: Int) {
        val currentItem = index[position]
        var cursor = stockDatabase.rawQuery("SELECT * FROM stock_receipt WHERE id = ${index?.get(position)}" , null)
        var tarihIx = cursor.getColumnIndex("Tarih")
        var islemIx = cursor.getColumnIndex("Işlem")
        var urun = cursor.getColumnIndex("Urün")
        var adet = cursor.getColumnIndex("Adet")
        var tutar = cursor.getColumnIndex("Tutar")
        var borc = cursor.getColumnIndex("Borç")
        var alacak = cursor.getColumnIndex("Alacak")

        while (cursor.moveToNext()){
            holder.tarih.text = cursor.getString(tarihIx)
            holder.islem.text = cursor.getString(islemIx)
            holder.urun.text = cursor.getString(urun)
            holder.adet.text = cursor.getString(adet)
            holder.tutar.text = cursor.getString(tutar)
            holder.borc.text = cursor.getString(borc)
            holder.alacak.text = cursor.getString(alacak)
        }
    }

    override fun getItemCount(): Int {
        return index.size
    }
}