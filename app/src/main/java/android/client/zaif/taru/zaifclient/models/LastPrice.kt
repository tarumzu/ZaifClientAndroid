package android.client.zaif.taru.zaifclient.models

import com.google.gson.annotations.Expose

/**
 * Created by mizukami on 2018/01/16.
 */
class LastPrice {
    @Expose
    var action: String? = null

    @Expose
    var price: Int = 0
}