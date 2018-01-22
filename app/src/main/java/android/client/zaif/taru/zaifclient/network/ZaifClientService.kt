package android.client.zaif.taru.zaifclient.network

import android.client.zaif.taru.zaifclient.models.CurrencyPair
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.ArrayList

/**
 * Created by mizukami on 2018/01/14.
 */
interface ZaifClientService {

    /**
     * リアルタイム取得
     */
    @GET("/api/1/currency_pairs/all")
    fun getCurrencyPairs(): Call<ArrayList<CurrencyPair>>

    @GET("/api/1/currency_pairs/{pair}")
    fun getCurrencyPair(@Path(value = "pair") pair: String): CurrencyPair
}