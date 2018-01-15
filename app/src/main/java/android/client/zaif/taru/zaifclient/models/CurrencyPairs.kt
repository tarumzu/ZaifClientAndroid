package android.client.zaif.taru.zaifclient.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by mizukami on 2018/01/14.
 */
class CurrencyPairs {
    @Expose
    @SerializedName("currency_pairs")
    var currencyPairs: ArrayList<CurrencyPair>? = null

}