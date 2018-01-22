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
import java.util.ArrayList

class MainActivity : BaseActivity() {

    var mCurrencyPairsAdapter: CurrencyPairsAdapter<CurrencyPair>? = null

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
        val currencyPairsCall = mZaifClientService.getCurrencyPairs()
        currencyPairsCall.enqueue(object : Callback<ArrayList<CurrencyPair>> {
            override fun onResponse(call: Call<ArrayList<CurrencyPair>>, response: Response<ArrayList<CurrencyPair>>) {
                if (isDestroyed()) return

                swiperefresh?.setRefreshing(false)

                if (!response.isSuccessful) {
                    //ErrorUtils.showErrorDialog(mConnectlyAppRetrofit, response, this)
                    return
                }

                if (mCurrencyPairsAdapter == null) return
                val currencyPairs = response.body()
                mCurrencyPairsAdapter?.resetItems(currencyPairs!!)
                mCurrencyPairsAdapter?.finish()
            }

            override fun onFailure(call: Call<ArrayList<CurrencyPair>>, t: Throwable) {
                if (isDestroyed()) return
                swiperefresh?.setRefreshing(false)

                //ErrorUtils.showNetworkErrorDialog(t, getActivity())
            }
        })
    }
}
