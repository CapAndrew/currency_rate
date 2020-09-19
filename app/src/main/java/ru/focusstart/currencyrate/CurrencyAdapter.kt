package ru.focusstart.currencyrate


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


class CurrencyAdapter(context: Context, currencyList: MutableList<CurrencyItem>) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {
    private val context: Context = context
    private val currencyList: MutableList<CurrencyItem> = currencyList
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrencyViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.currency_item, parent, false)
        return CurrencyViewHolder(v)
    }

    override fun onBindViewHolder(
        holder: CurrencyViewHolder,
        position: Int
    ) {
        val currentItem: CurrencyItem = currencyList[position]
        val currencyName: String? = currentItem.getCurrencyName()
        val currencyCode: String? = currentItem.getCurrencyCode()
        val currencyPrice: String? = currentItem.getCurrencyPrice()

        holder.currencyName.text = currencyName
        holder.currencyCode.text = currencyCode
        holder.currencyPrice.text = currencyPrice
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    inner class CurrencyViewHolder(itemView: View) : ViewHolder(itemView) {
        var currencyName: TextView = itemView.findViewById(R.id.currency_name)
        var currencyCode: TextView = itemView.findViewById(R.id.currency_code)
        var currencyPrice: TextView = itemView.findViewById(R.id.currency_price)

    }

}