package ru.focusstart.currencyrate

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

const val DBNAME = "CURRENCY_LIST"
const val TABLE = "Currency"
const val CUR_CODE = "CharCode"
const val CUR_NAME = "Name"
const val CUR_PRICE = "Value"
const val TAG = "DBHelper"

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DBNAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " +
                TABLE + " (" + CUR_CODE + " VARCHAR(256) PRIMARY KEY, " +
                CUR_NAME + " VARCHAR(256), " +
                CUR_PRICE + " VARCHAR(256))"
        db?.execSQL(createTable)
        Log.i(TAG, "Create table $DBNAME")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE")
        Log.i(TAG, "Drop table $DBNAME")
        onCreate(db)
    }

    fun insertData(currency: CurrencyItem) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(CUR_CODE, currency.getCurrencyCode())
        contentValues.put(CUR_NAME, currency.getCurrencyName())
        contentValues.put(CUR_PRICE, currency.getCurrencyPrice())
        Log.i(TAG, "Insert new data into $DBNAME")
        db.insert(TABLE, null, contentValues)
    }

    fun readData(): MutableList<CurrencyItem> {
        val list: MutableList<CurrencyItem> = ArrayList()
        val db = this.readableDatabase
        val result = db.query(
            TABLE,   // The table to query
            null,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null
        )

        if (result.moveToFirst()) {
            do {
                val currencyName = result.getString(result.getColumnIndex(CUR_NAME))
                val currencyCode = result.getString(result.getColumnIndex(CUR_CODE))
                val currencyPrice = result.getString(result.getColumnIndex(CUR_PRICE))

                list.add(
                    CurrencyItem(
                        currencyCode,
                        currencyName,
                        currencyPrice
                    )
                )
            } while (result.moveToNext())
        }
        Log.i(TAG, "Read data from $DBNAME")
        return list
    }

    fun deleteData() {
        val db = this.writableDatabase
        val selection = "delete from $TABLE"
        db.execSQL(selection)
        Log.i(TAG, "Delete all from table $DBNAME")
    }
}