package com.edasinar.profitability_proje_2

import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edasinar.profitability_proje_2.databinding.OrderListItemBinding


class OrdersAdapter (val index : ArrayList<Int>,val ordersDatabase : SQLiteDatabase) : RecyclerView.Adapter<OrdersAdapter.OrderHolder>() {
    class OrderHolder(val binding: OrderListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val barkod = binding.barkodTextView
        val kargoFirma = binding.kargoFirmasiTextView
        val tarih = binding.siparisTarihiTextView
        val siparisNumara = binding.siparisNumarasiTextView
        val alici = binding.aliciTextView
        val urunAd = binding.urunAdiTextView
        val komisyon = binding.komisyonTextView
        val adet = binding.adetTextView
        val tutar = binding.tutarTextView
        val kargoTutar = binding.kargoTutarTextView

    }
    private lateinit var binding : OrderListItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        binding = OrderListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        var cursor = ordersDatabase.rawQuery("SELECT * FROM orders WHERE id = ${index?.get(position)+1}" , null)
        var barkodIx = cursor.getColumnIndex("Barkod")
        var firmaIx = cursor.getColumnIndex("KargoFirması")
        var tarihIx = cursor.getColumnIndex("SiparişTarihi")
        var numaraIx = cursor.getColumnIndex("SiparişNumarası")
        var aliciIx = cursor.getColumnIndex("Alıcı")
        var isimIx = cursor.getColumnIndex("ÜrünAdı")
        var komisyonIx = cursor.getColumnIndex("KomisyonOranı")
        var adetIx = cursor.getColumnIndex("Adet")
        var tutarIx = cursor.getColumnIndex("FaturalanacakTutar")
        var kargoTutarIx = cursor.getColumnIndex("FaturalananKargoTutarı")
        while(cursor.moveToNext()){
            holder.barkod.setText(cursor.getString(barkodIx))
            holder.kargoFirma.setText(cursor.getString(firmaIx))
            holder.tarih.setText(cursor.getString(tarihIx))
            holder.siparisNumara.setText(cursor.getString(numaraIx))
            holder.alici.setText(cursor.getString(aliciIx))
            holder.urunAd.setText(cursor.getString(isimIx))
            holder.komisyon.setText(cursor.getString(komisyonIx))
            holder.adet.setText(cursor.getString(adetIx))
            holder.tutar.setText(cursor.getString(tutarIx))
            holder.kargoTutar.setText(cursor.getString(kargoTutarIx))
        }

    }

    override fun getItemCount(): Int {
        return index.size
    }

}