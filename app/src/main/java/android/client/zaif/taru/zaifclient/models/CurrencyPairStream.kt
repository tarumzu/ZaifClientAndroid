package android.client.zaif.taru.zaifclient.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by mizukami on 2018/01/16.
 */
class CurrencyPairStream {
    @Expose
    var asks: ArrayList<ArrayList<String>>? = null

    @Expose
    var bids: ArrayList<ArrayList<String>>? = null

    @Expose
    var trades: ArrayList<Trade>? = null

    @Expose
    var timestamp: String? = null

    @Expose
    @SerializedName("last_price")
    var lastPrice: LastPrice? = null

    @Expose
    @SerializedName("currency_pair")
    var currencyPair: String? = null

    @Expose
    var price: Double = 0.0
}