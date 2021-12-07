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

class ProductsActivity : AppCompatActivity() {
    companion object {
        lateinit var imageArray: Array<Int>
    }
    private  var index : ArrayList<Int> = arrayListOf()
    private lateinit var binding: ActivityProductsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        productList()

    }
    fun productList(){
        imageArray = arrayOf(R.drawable.uclulugoldset,R.drawable.dortlugoldsetdort,
            R.drawable.dortlugoldsetuc,R.drawable.besligoldsetyedi,R.drawable.asimetrikcentik
            ,R.drawable.hasiryzkbir,R.drawable.yvrlkyzkbir,R.drawable.dgmyzkbir
            ,R.drawable.mrsvnsyzk,R.drawable.ynsblkyzkdort,R.drawable.incehltyzkuc
            ,R.drawable.gozlukyzkiki,R.drawable.yildizsekilliyzkbir,R.drawable.minimalkalp
            ,R.drawable.besligoldsetbes,R.drawable.ongoldsetiki,R.drawable.ongoldsetbir
            ,R.drawable.papatyayzk,R.drawable.onlusembolyuzukseti,R.drawable.dikdortgenyzk
            ,R.drawable.bubbleyzk,R.drawable.yinyangyzk,R.drawable.ovlamr,R.drawable.babygrlyzk
            ,R.drawable.laleyzk,R.drawable.beyazyuz,R.drawable.pembeyuz,R.drawable.aurrarisaintbir
            ,R.drawable.periyuzuk,R.drawable.ikilibobble,R.drawable.ovalcizgi,R.drawable.karekemer
            ,R.drawable.aurrarisrmlbir,R.drawable.kabartmayuz,R.drawable.kayayuzuk,R.drawable.aurraricyrkbrgbir
            ,R.drawable.dikbombe,R.drawable.aurrarifrekansbir,R.drawable.aurraritseklbir,R.drawable.aurrariyprbir,R.drawable.aurrarikmkyz,R.drawable.aurrarikmkyz)
        val database = this.openOrCreateDatabase("Product_Database", MODE_PRIVATE,null)
        var cursor = database.rawQuery("SELECT * FROM products" , null)
        var idIx = cursor.getColumnIndex("id")
        println(idIx)
        while (cursor.moveToNext()){
            println(cursor.getString(idIx))
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
        var kategori = binding.kategoriEditText.getText().toString()
        var cursor = database.rawQuery("SELECT * FROM products WHERE Barkod LIKE '%${barkod}%' OR ÜrünAdı LIKE '%${isim}%' OR Kategoriİsmi LIKE '%${kategori}%'" , null)
        //
        var idIx = cursor.getColumnIndex("id")
        var indexFilter = ArrayList<Int>()
        while (cursor.moveToNext()){
            indexFilter.add(cursor.getString(idIx).toInt())
        }
        binding.barkodEditText.setText(null)
        binding.kategoriEditText.setText(null)
        binding.isimEditText.setText(null)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        val productAdapter = ProductsAdapter(indexFilter,database)
        binding.recycleView.adapter = productAdapter

    }

    fun yeniUrunKaydet(view : View){
        val intent = Intent(applicationContext,AddNewProduct::class.java)
        startActivity(intent)
    }
}