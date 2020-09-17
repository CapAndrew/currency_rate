package ru.focusstart.currencyrate

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException


class MainActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var currencyAdapter: CurrencyAdapter? = null
    private var currencyList: ArrayList<CurrencyItem> = ArrayList()
    private var requestQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        requestQueue = Volley.newRequestQueue(this)
        parseJSON()
    }

    private fun parseJSON() {
        Log.i("Debug_Parsing", "Start download json from www.cbr-xml-daily.ru")
        val url = "https://www.cbr-xml-daily.ru/daily_json.js"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val valuteObject = response.getJSONObject("Valute")
                    val valuteArray = valuteObject.names()

                    for (i in 0 until valuteArray.length()) {
                        val jsonObject = valuteObject.getJSONObject(valuteArray.getString(i))

                        val currencyName = jsonObject.getString("Name")
                        val currencyCode = jsonObject.getString("CharCode")
                        val currencyPrice = jsonObject.getString("Value")

                        currencyList.add(
                            CurrencyItem(
                                currencyCode,
                                currencyName,
                                "$currencyPrice₽"
                            )
                        )
                    }

                    currencyAdapter = CurrencyAdapter(this@MainActivity, currencyList)
                    recyclerView!!.adapter = currencyAdapter
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error ->
                error.printStackTrace()

            })
        requestQueue?.add(request)
    }
}