package android.client.zaif.taru.zaifclient.adapters

import android.app.Activity
import android.client.zaif.taru.zaifclient.R
import android.client.zaif.taru.zaifclient.activities.BaseActivity
import android.client.zaif.taru.zaifclient.activities.DetailActivity
import android.client.zaif.taru.zaifclient.models.CurrencyPair
import android.client.zaif.taru.zaifclient.utils.Constants
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import java.util.ArrayList

/**
 * Created by mizukami on 2018/01/22.
 */
class CurrencyPairsAdapter<T>(recyclerView: RecyclerView, boxes: ArrayList<T>, activity: Activity) : AbstractRecyclerViewFooterAdapter<T>(recyclerView, boxes, null) {
    private var mLayoutInflater: LayoutInflater? = null
    private var mActivity: Activity? = null
    private var mDataList: ArrayList<T>? = null

    init {
        mActivity = activity
        mDataList = boxes
        mLayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getItemViewType(position: Int): Int {
        val type: Int
        if (mDataList == null) {
            type = LIST_TYPE_FOOTER
        } else if (mDataList!![position] is CurrencyPair) {
            type = LIST_TYPE_ITEM
        } else {
            type = LIST_TYPE_HEADER
        }
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        val v: View
        when (viewType) {
            LIST_TYPE_ITEM -> {
                v = mLayoutInflater!!.inflate(R.layout.list_currency_pair_layout, parent, false)
                return CurrencyPairsAdapter.ViewHolder(v)
            }
            LIST_TYPE_FOOTER -> {
                v = mLayoutInflater!!.inflate(R.layout.list_progress_bar, parent, false)
                return ProgressViewHolder(v)
            }
            else -> return null
        }
    }

    /**
     * type毎のViewHolder生成
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            LIST_TYPE_FOOTER -> onBindFooterView(holder, position)
            else -> onBindBasicItemView(holder, position)
        }
    }

    // ヘッダー
    override fun onBindBasicItemView(holder: RecyclerView.ViewHolder, position: Int) {
        val mainholder = holder as ViewHolder
        if (mDataList!![position] == null || mDataList!![position] !is CurrencyPair) return
        val currencyPair = mDataList!![position] as CurrencyPair ?: return

        mainholder.nameTextView!!.setText(currencyPair.name)

        mainholder.priceTextView!!.setText(currencyPair.lastPrice.toString())

        mainholder.frameLayout!!.setOnClickListener { v ->
            if (mActivity == null) return@setOnClickListener
            val intent = Intent(mActivity!!.getApplicationContext(), DetailActivity::class.java)
            //intent.putExtra(Constants.ARG_TAG, MainActivity.TAG)
            intent.putExtra("currencyPair", currencyPair.currencyPair)
            mActivity!!.startActivityForResult(intent, Constants.REQUEST)
        }
    }

    fun addItems(newDataSetItems: ArrayList<T>) {
        mDataList!!.addAll(newDataSetItems)
        notifyDataSetChanged()
    }

    fun destroy() {
        mActivity = null
        mLayoutInflater = null
        mDataList = null
    }

    /**
     * viewHolder
     */
    internal class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var nameTextView: TextView? = v.findViewById(R.id.name) as TextView
        var priceTextView: TextView? = v.findViewById(R.id.price) as TextView
        var frameLayout: RelativeLayout? = v.findViewById(R.id.frame) as RelativeLayout
    }
}
