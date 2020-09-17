package ru.focusstart.currencyrate

class CurrencyItem(
    code: String?,
    name: String?,
    price: String?
) {
    private var currencyCode: String? = null
    private var currencyName: String? = null
    private var currencyPrice: String? = null


    init {
        currencyCode = code
        currencyName = name
        currencyPrice = price
    }

    fun getCurrencyCode(): String? {
        return currencyCode
    }

    fun getCurrencyName(): String? {
        return currencyName
    }

    fun getCurrencyPrice(): String? {
        return currencyPrice
    }
}