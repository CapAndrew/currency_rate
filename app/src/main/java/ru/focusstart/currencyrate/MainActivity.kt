package ru.focusstart.currencyrate

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList


const val currencyURL = "https://www.cbr-xml-daily.ru/daily_json.js"
const val updateDelay: Long = 30000
const val TAG_Parsing = "Debug_parsing"

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var currencyAdapter: CurrencyAdapter
    private var currencyList: MutableList<CurrencyItem> = ArrayList()
    private var requestQueue: RequestQueue? = null
    private val db = DataBaseHelper(this)
    private var timer = Timer()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        requestQueue = Volley.newRequestQueue(this)

        if (readCurrencyListFromDB().isEmpty()) {
            parseJSON()
        } else {
            fillCurrencyList(readCurrencyListFromDB())
        }

        currency_swipe_container.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                this,
                R.color.colorPrimary
            )
        )
        currency_swipe_container.setColorSchemeColors(Color.WHITE)
        currency_swipe_container.setOnRefreshListener {
            Log.i(TAG_Parsing, "Refresh currency list by swipe")
            restartTimer()
            reloadCurrencyList()
            currency_swipe_container.isRefreshing = false
        }
    }

    private fun reloadCurrencyList(){
        db.deleteData()
        currencyList.clear()
        parseJSON()
    }

    private fun stopTimer(){
        timer.cancel()
        timer.purge()
    }

    private fun restartTimer(){
        stopTimer()
        autoUpdateCurrencyList(updateDelay)
    }

    private fun autoUpdateCurrencyList(interval: Long) {
        timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                reloadCurrencyList()
            }
        }, interval, interval)
    }

    private fun readCurrencyListFromDB(): MutableList<CurrencyItem> {
        return db.readData()
    }

    private fun insertCurrencyListIntoDB(currencyList: MutableList<CurrencyItem>) {
        for (currency in currencyList) {
            db.insertData(currency)
        }
    }

    private fun parseJSON() {
        Log.i(TAG_Parsing, "Start download json from $currencyURL")
        val url = currencyURL
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val valuteObject = response.getJSONObject("Valute")
                    val valuteArray = valuteObject.names()

                    for (i in 0 until valuteArray.length()) {
                        val currencyObject = valuteObject.getJSONObject(valuteArray.getString(i))

                        val currencyName = currencyObject.getString("Name")
                        val currencyCode = currencyObject.getString("CharCode")
                        val currencyPrice = currencyObject.getString("Value")

                        currencyList.add(
                            CurrencyItem(
                                currencyCode,
                                currencyName,
                                currencyPrice
                            )
                        )
                    }
                    insertCurrencyListIntoDB(currencyList)
                    fillCurrencyList(currencyList)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error ->
                error.printStackTrace()

            })
        requestQueue?.add(request)
    }

    private fun fillCurrencyList(currencyList: MutableList<CurrencyItem>) {
        val prettyPrintCurrencyList: MutableList<CurrencyItem> = ArrayList()
        for (currency in currencyList) {
            prettyPrintCurrencyList.add(
                CurrencyItem(
                    currency.getCurrencyCode(),
                    currency.getCurrencyName(),
                    currency.getCurrencyPrice() + "₽"
                )
            )
        }

        currencyAdapter = CurrencyAdapter(this@MainActivity, prettyPrintCurrencyList)
        recyclerView.adapter = currencyAdapter
    }

    override fun onResume() {
        super.onResume()
        autoUpdateCurrencyList(updateDelay)
        Log.i(TAG_Parsing, "Activity onResume")
    }

    override fun onPause() {
        super.onPause()
        stopTimer()
        Log.i(TAG_Parsing, "Activity onPause")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        db.close()
        stopTimer()
        Log.i(TAG_Parsing, "Activity onDestroy")
    }
}