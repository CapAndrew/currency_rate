package ru.focusstart.currencyrate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.currency_exchange.*
import java.math.RoundingMode

class CurrencyExchange : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.currency_exchange)

        title = "Обмен валюты"

        val currencyCode = intent.getStringExtra("currencyCode")
        val currencyPrice = intent.getStringExtra("currencyPrice")

        selectedCurrency.text = "Выбрана валюта: $currencyCode"

        calculateButton.setOnClickListener {
            val result =
                (amount.text.toString().toDouble() / currencyPrice.toDouble()).toBigDecimal()
                    .setScale(2, RoundingMode.UP)
            calculationResult.text = ("${amount.text} ₽ = $result $currencyCode")
        }
    }

}