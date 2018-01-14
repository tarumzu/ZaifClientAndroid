package android.client.zaif.taru.zaifclient.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by mizukami on 2018/01/14.
 */
interface ZaifClientService {

    /**
     * リアルタイム取得
     */
    @GET("/stream?currency_pair={pair}")
    fun getItems(@Path(value = "pair") pair: String): Call<Void>;

}