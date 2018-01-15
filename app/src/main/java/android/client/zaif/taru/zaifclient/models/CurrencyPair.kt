package android.client.zaif.taru.zaifclient.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by mizukami on 2018/01/14.
 */
class CurrencyPair {
    @Expose
    var name: String? = null

    @Expose
    var title: String? = null

    @Expose
    @SerializedName("currency_pair")
    var currencyPair: String? = null

    @Expose
    var description: String? = null

    @Expose
    @SerializedName("is_token")
    var isToken: Boolean = false

    @Expose
    @SerializedName("event_number")
    var eventNumber: Int = 0

    @Expose
    var seq: Int = 0

    @Expose
    @SerializedName("item_unit_min")
    var itemUnitMin: Float = 0f

    @Expose
    @SerializedName("item_unit_step")
    var itemUnitStep: Float = 0f

    @Expose
    @SerializedName("item_japanese")
    var itemJapanese: String? = null

    @Expose
    @SerializedName("aux_unit_min")
    var auxUnitMin: Float = 0f

    @Expose
    @SerializedName("aux_unit_step")
    var auxUnitStep: Float = 0f

    @Expose
    @SerializedName("aux_unit_point")
    var auxUnitPoint: Int = 0

    @Expose
    @SerializedName("aux_japanese")
    var auxJapanese: String? = null
}