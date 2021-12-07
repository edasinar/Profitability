package com.edasinar.profitability_proje_2

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
    private lateinit var expensesDatabase : SQLiteDatabase


    private lateinit var binding: ActivityMainMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.setTitle("            WELCOME TO MAIN MENU")
        /*expensesDatabase = this.openOrCreateDatabase("Receipt_Of_Expenses", MODE_PRIVATE,null)
        stockReceiptDatabase()
        orderingPeopleListDatabase()
        ordersDatabase()
        productDatabase()*/
    }


    fun stockReceiptDatabase(){
        val stockReceipt = InputStreamReader(assets.open("stock_receipt.csv"))
        var reader = BufferedReader(stockReceipt)
        val stockReceiptCol : ArrayList<String> = reader.readLine().split(",") as ArrayList<String>
        stockReceiptCol[0] = stockReceiptCol[0].split(stockReceiptCol[0][0])[1]

        try
        {
            stockReceiptDatabase = this.openOrCreateDatabase("Stock_Receipt" , MODE_PRIVATE, null)
            stockReceiptDatabase.execSQL("CREATE TABLE IF NOT EXISTS stock_receipt (id INTEGER PRIMARY KEY , ${stockReceiptCol[0]} VARCHAR , ${stockReceiptCol[1]} VARCHAR , ${stockReceiptCol[2]} VARCHAR , ${stockReceiptCol[3]} INTEGER , ${stockReceiptCol[4]} DOUBLE , ${stockReceiptCol[5]} DOUBLE, ${stockReceiptCol[6]} DOUBLE)")
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val temp = line?.split(",")
                stockReceiptDatabase.execSQL("INSERT INTO stock_receipt (${stockReceiptCol[0]},${stockReceiptCol[1]},${stockReceiptCol[2]},${stockReceiptCol[3]},${stockReceiptCol[4]},${stockReceiptCol[5]},${stockReceiptCol[6]}) VALUES ('${temp?.get(0)}' , '${temp?.get(1)}' ,'${temp?.get(2)}',${temp?.get(3)?.toInt()},${temp?.get(4)?.toDouble()},${temp?.get(5)?.toDouble()},${temp?.get(6)?.toDouble()})")
            }
            reader.close()
        }

        catch (e : Exception){
            e.printStackTrace()
            println("Products Database Okunurken Hata Oldu")
        }
    }

    fun orderingPeopleListDatabase(){
        val peoplecsv = InputStreamReader(assets.open("ordering_people_list.csv"))
        var reader = BufferedReader(peoplecsv)
        reader.readLine()
        try
        {
            orderingPeopleListDatabase = this.openOrCreateDatabase("ORDERS" , MODE_PRIVATE, null)
            orderingPeopleListDatabase.execSQL("CREATE TABLE IF NOT EXISTS ordering_people_list (id INTEGER PRIMARY KEY , isimSoyisim VARCHAR)")
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val temp = line?.toString()
                orderingPeopleListDatabase.execSQL("INSERT INTO ordering_people_list (isimSoyisim) VALUES ('${temp}')")
            }
            reader.close()
        }

        catch (e : Exception){
            e.printStackTrace()
            println("Products Database Okunurken Hata Oldu")
        }
    }

    fun ordersDatabase(){
        val orderscsv = InputStreamReader(assets.open("orders.csv"))
        var reader = BufferedReader(orderscsv)
        val orderCol : ArrayList<String> = reader.readLine().split(",") as ArrayList<String>
        orderCol[0] = orderCol[0].split(orderCol[0][0])[1]
        try
        {
            ordersDatabase = this.openOrCreateDatabase("ORDERS" , MODE_PRIVATE, null)
            ordersDatabase.execSQL("CREATE TABLE IF NOT EXISTS orders (id INTEGER PRIMARY KEY , ${orderCol[0]} VARCHAR , ${orderCol[1]} VARCHAR , ${orderCol[2]} VARCHAR , ${orderCol[3]} INTEGER , ${orderCol[4]} VARCHAR , ${orderCol[5]} VARCHAR , ${orderCol[6]} DOUBLE , ${orderCol[7]} INTEGER , ${orderCol[8]} DOUBLE , ${orderCol[9]} DOUBLE)")
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val temp = line?.split(",")
                ordersDatabase.execSQL("INSERT INTO orders (${orderCol[0]},${orderCol[1]},${orderCol[2]},${orderCol[3]},${orderCol[4]},${orderCol[5]},${orderCol[6]},${orderCol[7]},${orderCol[8]},${orderCol[9]}) VALUES ('${temp?.get(0)}' , '${temp?.get(1)}' ,'${temp?.get(2)}',${temp?.get(3)?.toDouble()},'${temp?.get(4)}','${temp?.get(5)}' ,${temp?.get(6)?.toDouble()},${temp?.get(7)?.toInt()},${temp?.get(8)?.toDouble()},${temp?.get(9)?.toDouble()})")
            }
            reader.close()
        }

        catch (e : Exception){
            e.printStackTrace()
            println("Products Database Okunurken Hata Oldu")
        }

    }

    fun productDatabase(){
        val productcsv = InputStreamReader(assets.open("products.csv"))
        val reader = BufferedReader(productcsv)
        val productCol : ArrayList<String> = reader.readLine().split(",") as ArrayList<String>
        productCol[0] = productCol[0].split(productCol[0][0])[1]
        try
        {
            productDatabase = this.openOrCreateDatabase("Product_Database" , MODE_PRIVATE, null)
            productDatabase.execSQL("CREATE TABLE IF NOT EXISTS products (id INTEGER PRIMARY KEY , ${productCol[0]} VARCHAR , ${productCol[1]} DOUBLE , ${productCol[2]} VARCHAR , ${productCol[3]} VARCHAR , ${productCol[4]} VARCHAR , ${productCol[5]} VARCHAR, ${productCol[6]} DOUBLE, ${productCol[7]} INT)")
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val temp = line?.split(",")
                productDatabase.execSQL("INSERT INTO products (${productCol[0]},${productCol[1]},${productCol[2]},${productCol[3]},${productCol[4]},${productCol[5]},${productCol[6]},${productCol[7]}) VALUES ('${temp?.get(0)}' , ${temp?.get(1)?.toDouble()} ,'${temp?.get(2)}','${temp?.get(3)}','${temp?.get(4)}','${temp?.get(5)}','${temp?.get(6)?.toDouble()}','${temp?.get(7)?.toInt()}')")
            }
            reader.close()
        }catch (e : Exception){
            e.printStackTrace()
            println("Products Database Okunurken Hata Oldu")
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