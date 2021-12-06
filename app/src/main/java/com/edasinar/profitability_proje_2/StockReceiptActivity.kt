package com.edasinar.profitability_proje_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.edasinar.profitability_proje_2.databinding.ActivityStockReceiptBinding

class StockReceiptActivity : AppCompatActivity() {
    private  var index : ArrayList<Int> = arrayListOf()

    private lateinit var binding: ActivityStockReceiptBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockReceiptBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val database = this.openOrCreateDatabase("Stock_Receipt", MODE_PRIVATE,null)
        var cursor = database.rawQuery("SELECT * FROM stock_receipt" , null)
        var idIx = cursor.getColumnIndex("id")
        var islem = cursor.getColumnIndex("Işlem")
        println(idIx)
        while (cursor.moveToNext()){
            println(cursor.getString(idIx) +") " + cursor.getString(islem))

            index.add(cursor.getString(idIx).toInt())
        }
        binding.stockRecyclerView.layoutManager = LinearLayoutManager(this)
        val stockReceiptAdapter = StockReceiptAdapter(index,database)
        binding.stockRecyclerView.adapter = stockReceiptAdapter
    }
}