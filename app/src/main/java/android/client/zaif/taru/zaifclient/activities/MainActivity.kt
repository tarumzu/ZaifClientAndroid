package android.client.zaif.taru.zaifclient.activities

import android.client.zaif.taru.zaifclient.R
import android.client.zaif.taru.zaifclient.adapters.CurrencyPairsAdapter
import android.client.zaif.taru.zaifclient.models.CurrencyPair
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import android.R.string.cancel
import android.client.zaif.taru.zaifclient.utils.Constants
import android.content.Intent
import android.os.Handler
import java.sql.Time
import java.util.*


class MainActivity : BaseActivity() {

    var mCurrencyPairsAdapter: CurrencyPairsAdapter<CurrencyPair>? = null

    val handler = Handler()
    var t: Timer? = null
    var task: TimerTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swiperefresh.setColorSchemeResources(
                R.color.blue, R.color.blue, R.color.blue,
                R.color.blue);
        swiperefresh.setOnRefreshListener({
            Timber.d("onRefresh called from SwipeRefreshLayout");
            initiateRefresh();
        });


        // アニメーション
        list.setItemAnimator(DefaultItemAnimator())
        list.setLayoutManager(LinearLayoutManager(getApplicationContext()))
        var dividerItemDecoration = DividerItemDecoration(list.getContext(), LinearLayoutManager(this).getOrientation())
        list.addItemDecoration(dividerItemDecoration)

        // アイテムリストを読み込む
        if (mCurrencyPairsAdapter == null) {
            mCurrencyPairsAdapter = CurrencyPairsAdapter(list, ArrayList<CurrencyPair>(), this)
            list.setAdapter(mCurrencyPairsAdapter)
            swiperefresh?.setRefreshing(true)
            initiateRefresh()
        }
        list.setAdapter(mCurrencyPairsAdapter)

    }

    private fun initiateRefresh() {
        Timber.d("initiateRefresh")

        // box一覧取得
        t?.cancel()
        val currencyPairsCall = mZaifClientService.getCurrencyPairs()
        currencyPairsCall.enqueue(object : Callback<MutableList<CurrencyPair>> {
            override fun onResponse(call: Call<MutableList<CurrencyPair>>, response: Response<MutableList<CurrencyPair>>) {
                if (isDestroyed()) return

                swiperefresh.setRefreshing(false)

                if (!response.isSuccessful) {
                    //ErrorUtils.showErrorDialog(mConnectlyAppRetrofit, response, this)
                    return
                }

                if (mCurrencyPairsAdapter == null) return
                val currencyPairs = response.body()
                currencyPairs?.sortBy { c -> c.seq }
                mCurrencyPairsAdapter?.resetItems(currencyPairs!!)
                mCurrencyPairsAdapter?.finish()

                // ZaifAPIの制限のため、10秒間隔で価格更新する
                startTimer()
            }

            override fun onFailure(call: Call<MutableList<CurrencyPair>>, t: Throwable) {
                if (isDestroyed()) return
                swiperefresh.setRefreshing(false)

                //ErrorUtils.showNetworkErrorDialog(t, getActivity())
            }
        })
    }

    override fun onStop() {
        super.onStop()
        t?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        t?.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.REQUEST) {
            startTimer()
        }
    }

    // ZaifAPIの制限のため、10秒間隔で価格更新する
    fun startTimer() {
        t?.cancel()
        t = Timer()
        task = object : TimerTask() {
            override fun run() {
                // Timerスレッド
                handler.post({
                    Timber.d("timer loop")
                    if (mCurrencyPairsAdapter?.getDataSet()?.size!! > 0) {
                        var currencyPairs = mCurrencyPairsAdapter!!.getDataSet()!!
                        for (currencyPair in currencyPairs) {
                            val lastPriceCall = mZaifClientService.getLastPrice(currencyPair.currencyPair.toString())
                            lastPriceCall.enqueue(object : Callback<CurrencyPair> {
                                override fun onResponse(call: Call<CurrencyPair>, response: Response<CurrencyPair>) {
                                    if (isDestroyed()) return

                                    if (!response.isSuccessful) {
                                        //ErrorUtils.showErrorDialog(mConnectlyAppRetrofit, response, this)
                                        return
                                    }

                                    if (mCurrencyPairsAdapter == null) return
                                    val currencyPairResult = response.body()
                                    currencyPair.lastPrice = currencyPairResult!!.lastPrice
                                    //mCurrencyPairsAdapter?.resetItems(currencyPairs!!)
                                    runOnUiThread({
                                        mCurrencyPairsAdapter?.notifyDataSetChanged()
                                    })
                                }

                                override fun onFailure(call: Call<CurrencyPair>, t: Throwable) {
                                    if (isDestroyed()) return
                                    //ErrorUtils.showNetworkErrorDialog(t, getActivity())
                                }
                            })
                        }
                    }
                })
            }
        }
        t!!.scheduleAtFixedRate(task, 500, 10000);
    }
}
