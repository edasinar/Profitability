package com.edasinar.profitability_proje_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.edasinar.profitability_proje_2.databinding.ActivityOrdersBinding

class OrdersActivity : AppCompatActivity() {
    private  var index : ArrayList<Int> = arrayListOf()

    private lateinit var binding: ActivityOrdersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val database = this.openOrCreateDatabase("ORDERS", MODE_PRIVATE,null)
        var cursor = database.rawQuery("SELECT * FROM orders" , null)
        var idIx = cursor.getColumnIndex("id")
        println(idIx)
        while (cursor.moveToNext()){
            println(cursor.getString(idIx))
            index.add(cursor.getString(idIx).toInt())
        }
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        val ordersAdapter = OrdersAdapter(index,database)
        binding.recycleView.adapter = ordersAdapter
    }
}
