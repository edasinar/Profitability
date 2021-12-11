package com.edasinar.profitability_proje_2


import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edasinar.profitability_proje_2.databinding.ActivityProductsBinding
import java.lang.NullPointerException

class ProductsActivity : AppCompatActivity() {
    companion object {
        lateinit var imageArray: ArrayList<Int>
        var addImageArray : ArrayList<String> = arrayListOf()
    }
    private  var index : ArrayList<Int> = arrayListOf()
    private lateinit var binding: ActivityProductsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        productList()
        supportActionBar?.title = "            ÜRÜNLER"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    fun productList(){
        imageArray = arrayListOf(R.drawable.uclulugoldset,R.drawable.dortlugoldsetdort,
            R.drawable.dortlugoldsetuc,R.drawable.besligoldsetyedi,R.drawable.asimetrikcentik
            ,R.drawable.hasiryzkbir,R.drawable.yvrlkyzkbir,R.drawable.dgmyzkbir
            ,R.drawable.incezincirbir,R.drawable.mrsvnsyzk,R.drawable.ynsblkyzkdort
            ,R.drawable.incehltyzkuc,R.drawable.gozlukyzkiki,R.drawable.minimalkalp
        )
        val database = this.openOrCreateDatabase("Product_Database", MODE_PRIVATE,null)
        var cursor = database.rawQuery("SELECT * FROM products" , null)
        var idIx = cursor.getColumnIndex("id")
        while (cursor.moveToNext()){
            index.add(cursor.getString(idIx).toInt())
        }
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        val productAdapter = ProductsAdapter(index,database)
        binding.recycleView.adapter = productAdapter
    }

    fun filterProducts(view : View){
        val database = this.openOrCreateDatabase("Product_Database", MODE_PRIVATE,null)

        var barkod = binding.barkodEditText.getText().toString()
        var isim = binding.isimEditText.getText().toString()
        if(barkod.isNullOrBlank() && isim.isNullOrBlank()){
            println("barkod ve isim boş değer almıştır")
            binding.recycleView.layoutManager = LinearLayoutManager(this)
            val productAdapter = ProductsAdapter(index, database)
            binding.recycleView.adapter = productAdapter
        }
        else if(barkod.isNullOrBlank() && !isim.isNullOrBlank()){
            var cursor = database.rawQuery(
                "SELECT * FROM products WHERE İsim LIKE '%${isim}%'",
                null
            )

            var idIx = cursor.getColumnIndex("id")
            var indexFilter = ArrayList<Int>()
            while (cursor.moveToNext()) {
                indexFilter.add(cursor.getString(idIx).toInt())
            }
            binding.barkodEditText.setText(null)
            binding.isimEditText.setText(null)
            binding.recycleView.layoutManager = LinearLayoutManager(this)
            val productAdapter = ProductsAdapter(indexFilter, database)
            binding.recycleView.adapter = productAdapter
        }
        else if(!barkod.isNullOrBlank() && isim.isNullOrBlank()){
            var cursor = database.rawQuery(
                "SELECT * FROM products WHERE ÜrünBarkodu LIKE '%${barkod}%'",
                null
            )

            var idIx = cursor.getColumnIndex("id")
            var indexFilter = ArrayList<Int>()
            while (cursor.moveToNext()) {
                indexFilter.add(cursor.getString(idIx).toInt())
            }
            binding.barkodEditText.setText(null)
            binding.isimEditText.setText(null)
            binding.recycleView.layoutManager = LinearLayoutManager(this)
            val productAdapter = ProductsAdapter(indexFilter, database)
            binding.recycleView.adapter = productAdapter

        }
        else {
            var cursor = database.rawQuery(
                "SELECT * FROM products WHERE ÜrünBarkodu LIKE '%${barkod}%' OR İsim LIKE '%${isim}%'",
                null
            )

            var idIx = cursor.getColumnIndex("id")
            var indexFilter = ArrayList<Int>()
            while (cursor.moveToNext()) {
                indexFilter.add(cursor.getString(idIx).toInt())
            }
            binding.barkodEditText.setText(null)
            binding.isimEditText.setText(null)
            binding.recycleView.layoutManager = LinearLayoutManager(this)
            val productAdapter = ProductsAdapter(indexFilter, database)
            binding.recycleView.adapter = productAdapter
        }


    }
    fun yeniUrunKaydet(view : View){
        val intent = Intent(applicationContext,AddNewProduct::class.java)
        startActivity(intent)
    }
}