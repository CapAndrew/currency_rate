package ru.focusstart.currencyrate

class CurrencyItem(
    code: String,
    name: String,
    price: String
) {
    private var currencyCode: String = code
    private var currencyName: String = name
    private var currencyPrice: String = price

    fun getCurrencyCode(): String {
        return currencyCode
    }

    fun getCurrencyName(): String {
        return currencyName
    }

    fun getCurrencyPrice(): String {
        return currencyPrice
    }
}