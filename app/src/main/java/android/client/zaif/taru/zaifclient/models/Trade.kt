package android.client.zaif.taru.zaifclient.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by mizukami on 2018/01/16.
 */
class Trade {
    @Expose
    @SerializedName("currency_pair")
    var currencyPair: String? = null

    @Expose
    @SerializedName("trade_type")
    var tradeType: String? = null

    @Expose
    var price: Int = 0

    @Expose
    var tid: Int = 0

    @Expose
    var amount: Double = 0.0

    @Expose
    var date: Int = 0
}