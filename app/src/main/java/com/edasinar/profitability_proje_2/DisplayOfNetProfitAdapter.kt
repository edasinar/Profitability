package com.edasinar.profitability_proje_2

import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edasinar.profitability_proje_2.databinding.DisplayOfNetProfitListItemBinding

class DisplayOfNetProfitAdapter(val index : ArrayList<Int>,val profitDatabase : SQLiteDatabase):
    RecyclerView.Adapter<DisplayOfNetProfitAdapter.DisplayOfNetProfitHolder>() {

    class DisplayOfNetProfitHolder(val binding: DisplayOfNetProfitListItemBinding):
        RecyclerView.ViewHolder(binding.root){
        val ay = binding.aylarListItem
        val gider = binding.digerGiderlerListItem
        val brüt = binding.brutKarListItem
        val netKar = binding.netKarListItem
    }


    private lateinit var binding: DisplayOfNetProfitListItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisplayOfNetProfitHolder {
        binding = DisplayOfNetProfitListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DisplayOfNetProfitHolder(binding)
    }

    override fun onBindViewHolder(holder: DisplayOfNetProfitHolder, position: Int) {
        var cursor = profitDatabase.rawQuery("SELECT * FROM net_profits WHERE id = ${index?.get(position)}" , null)
        var tarihIx = cursor.getColumnIndex("Aylar")
        var brutIx = cursor.getColumnIndex("BrütKar")
        var giderIx = cursor.getColumnIndex("DiğerGiderler")
        var netIx = cursor.getColumnIndex("NetKar")
        while (cursor.moveToNext()){
            holder.ay.text = cursor.getString(tarihIx)
            holder.gider.text = cursor.getString(giderIx)
            holder.brüt.text = cursor.getString(brutIx)
            holder.netKar.text = cursor.getString(netIx)
        }
    }

    override fun getItemCount(): Int {
        return index.size
    }
}