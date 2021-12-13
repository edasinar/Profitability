package com.edasinar.profitability_proje_2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.edasinar.profitability_proje_2.databinding.ActivityMainMenuBinding
import java.io.BufferedReader
import java.io.InputStreamReader

class MainMenuActivity : AppCompatActivity() {
    private lateinit var orderingPeopleListDatabase : SQLiteDatabase
    private lateinit var ordersDatabase: SQLiteDatabase
    private lateinit var productDatabase: SQLiteDatabase
    private lateinit var stockReceiptDatabase: SQLiteDatabase
    private lateinit var otherExpensesDatabase : SQLiteDatabase
    private lateinit var displayNetProfitDatabase: SQLiteDatabase

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: ActivityMainMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.title = "            KARLILIK HESAPLAMA"

        sharedPreferences = this.getSharedPreferences("prof_pref", Context.MODE_PRIVATE)
        if(sharedPreferences.getInt("isOpenDatabase",0) == 0){
            sharedPreference()
        }
        else{
            println("asdfghj")
        }
    }

    private fun sharedPreference(){
        //sharedPreferences.edit().putInt("isOpenDatabase",0).apply()
        if (sharedPreferences.getInt("isOpenDatabase",0) == 0){
            stockReceiptDatabase()
            orderingPeopleListDatabase()
            ordersDatabase()
            productDatabase()
            otherExpensesDatabase()
            netProfitDisplayDatabase()
            sharedPreferences.edit().putInt("isOpenDatabase", 1).apply()
        }
    }

    private fun stockReceiptDatabase(){
        val stockReceipt = InputStreamReader(assets.open("product_cost_and_stock.csv"))
        val reader = BufferedReader(stockReceipt)
        val stockReceiptCol : ArrayList<String> = reader.readLine().split(",") as ArrayList<String>
        stockReceiptCol[0] = stockReceiptCol[0].split(stockReceiptCol[0][0])[1]

        try
        {
            stockReceiptDatabase = this.openOrCreateDatabase("Stock_Receipt" , MODE_PRIVATE, null)
            stockReceiptDatabase.execSQL("CREATE TABLE IF NOT EXISTS stock_receipt (id INTEGER PRIMARY KEY , ${stockReceiptCol[0]} VARCHAR , ${stockReceiptCol[1]} VARCHAR , ${stockReceiptCol[2]} INTEGER , ${stockReceiptCol[3]} DOUBLE , ${stockReceiptCol[4]} DOUBLE)")
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val temp = line?.split(",")
                stockReceiptDatabase.execSQL("INSERT INTO stock_receipt (${stockReceiptCol[0]},${stockReceiptCol[1]},${stockReceiptCol[2]},${stockReceiptCol[3]},${stockReceiptCol[4]}) VALUES ('${temp?.get(0)}' , '${temp?.get(1)}' ,${temp?.get(2)?.toInt()},${temp?.get(3)?.toDouble()},${temp?.get(4)?.toDouble()})")
            }
            reader.close()
        }

        catch (e : Exception){
            e.printStackTrace()
            println("Products Database Okunurken Hata Oldu")
        }
    }

    private fun orderingPeopleListDatabase(){
        val peoplecsv = InputStreamReader(assets.open("ordering_people_list.csv"))
        val reader = BufferedReader(peoplecsv)
        reader.readLine()
        try
        {
            orderingPeopleListDatabase = this.openOrCreateDatabase("ORDERS" , MODE_PRIVATE, null)
            orderingPeopleListDatabase.execSQL("CREATE TABLE IF NOT EXISTS ordering_people_list (id INTEGER PRIMARY KEY , isimSoyisim VARCHAR)")
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val temp = line
                orderingPeopleListDatabase.execSQL("INSERT INTO ordering_people_list (isimSoyisim) VALUES ('${temp}')")
            }
            reader.close()
        }

        catch (e : Exception){
            e.printStackTrace()
            println("Products Database Okunurken Hata Oldu")
        }
    }

    private fun ordersDatabase(){
        val orderscsv = InputStreamReader(assets.open("orders.csv"))
        val reader = BufferedReader(orderscsv)
        val orderCol : ArrayList<String> = reader.readLine().split(",") as ArrayList<String>
        orderCol[0] = orderCol[0].split(orderCol[0][0])[1]
        try
        {
            ordersDatabase = this.openOrCreateDatabase("ORDERS" , MODE_PRIVATE, null)
            ordersDatabase.execSQL("CREATE TABLE IF NOT EXISTS orders (id INTEGER PRIMARY KEY , ${orderCol[0]} VARCHAR , ${orderCol[1]} VARCHAR , ${orderCol[2]} INTEGER , ${orderCol[3]} VARCHAR , ${orderCol[4]} INTEGER , ${orderCol[5]} DOUBLE)")
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val temp = line?.split(",")
                ordersDatabase.execSQL("INSERT INTO orders (${orderCol[0]},${orderCol[1]},${orderCol[2]},${orderCol[3]},${orderCol[4]},${orderCol[5]}) VALUES ('${temp?.get(0)}' , '${temp?.get(1)}' ,${temp?.get(2)?.toInt()},'${temp?.get(3)}',${temp?.get(4)?.toInt()},${temp?.get(5)?.toDouble()})")
            }
            reader.close()
        }

        catch (e : Exception){
            e.printStackTrace()
            println("Products Database Okunurken Hata Oldu")
        }

    }

    private fun productDatabase(){
        val productcsv = InputStreamReader(assets.open("products.csv"))
        val reader = BufferedReader(productcsv)
        val productCol : ArrayList<String> = reader.readLine().split(",") as ArrayList<String>
        productCol[0] = productCol[0].split(productCol[0][0])[1]
        try
        {
            productDatabase = this.openOrCreateDatabase("Product_Database" , MODE_PRIVATE, null)
            productDatabase.execSQL("CREATE TABLE IF NOT EXISTS products (id INTEGER PRIMARY KEY , ${productCol[0]} VARCHAR , ${productCol[1]} DOUBLE , ${productCol[2]} VARCHAR , ${productCol[3]} VARCHAR , ${productCol[4]} VARCHAR , ${productCol[5]} DOUBLE)")
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val temp = line?.split(",")
                productDatabase.execSQL("INSERT INTO products (${productCol[0]},${productCol[1]},${productCol[2]},${productCol[3]},${productCol[4]},${productCol[5]}) VALUES ('${temp?.get(0)}' , ${temp?.get(1)?.toDouble()} ,'${temp?.get(2)}','${temp?.get(3)}','${temp?.get(4)}',${temp?.get(5)?.toDouble()})")
            }
            reader.close()
        }catch (e : Exception){
            e.printStackTrace()
            println("Products Database Okunurken Hata Oldu")
        }
    }

    private fun otherExpensesDatabase(){
        val otherExpensescsv = InputStreamReader(assets.open("other_expenses.csv"))
        val reader = BufferedReader(otherExpensescsv)
        val otherExpensesCol : ArrayList<String> = reader.readLine().split(",") as ArrayList<String>
        //otherExpensesCol[0] = otherExpensesCol[0].split(otherExpensesCol[0][0])[1]
        try{
            otherExpensesDatabase = this.openOrCreateDatabase("Receipt_Of_Expenses", MODE_PRIVATE,null)
            otherExpensesDatabase.execSQL("CREATE TABLE IF NOT EXISTS other_expenses (id INTEGER PRIMARY KEY,Yıl VARCHAR,Ay VARCHAR,Gün VARCHAR,Tutar DOUBLE,Açıklama VARCHAR)")
            var line: String?
            while(reader.readLine().also { line = it } != null){
                val temp = line?.split(",")
                otherExpensesDatabase.execSQL("INSERT INTO other_expenses (Yıl,Ay,Gün,Tutar,Açıklama) VALUES ('${temp?.get(0)}','${temp?.get(1)}','${temp?.get(2)}',${temp?.get(3)?.toDouble()},'${temp?.get(4)}')")
            }
            reader.close()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun netProfitDisplayDatabase(){
        try {
            displayNetProfitDatabase = this.openOrCreateDatabase("Display_Net_Profit", MODE_PRIVATE,null)
            displayNetProfitDatabase.execSQL("CREATE TABLE IF NOT EXISTS net_profits (id INTEGER PRIMARY KEY, Aylar VARCHAR, BrütKar DOUBLE, DiğerGiderler DOUBLE, NetKar DOUBLE)")
            var aylar = arrayListOf<String>("OCAK","ŞUBAT","MART","NİSAN","MAYIS","HAZİRAN","TEMMUZ","AĞUSTOS",
            "EYLÜL","EKİM","KASIM","ARALIK")
            var i = 0
            while(i < aylar.size){
                displayNetProfitDatabase.execSQL("INSERT INTO net_profits(Aylar,BrütKar,DiğerGiderler,NetKar) VALUES ('${aylar[i]}',${0.0},${0.0},${0.0}) ")
                i+= 1
            }
        }catch (e :Exception){
            e.printStackTrace()
        }
    }

    fun clickProducts(view: View) {
        val intent = Intent(applicationContext, ProductsActivity::class.java)
        startActivity(intent)
    }

    fun clickOrders(view: View){
        val intent = Intent(applicationContext,OrdersActivity::class.java)
        startActivity(intent)
    }

    fun clickStockReceipt(view: View){
        val intent = Intent(applicationContext,StockReceiptActivity::class.java)
        startActivity(intent)
    }

    fun clickReceiptOfExpenses(view: View){
        val intent = Intent(applicationContext,ReceiptOfExpensesActivity::class.java)
        startActivity(intent)
    }

    fun clickDisplayOfNetProfit(view : View){
        val intent = Intent(applicationContext,DisplayOfNetProfitActivity::class.java)
        startActivity(intent)
    }
}