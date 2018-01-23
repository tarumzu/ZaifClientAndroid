package android.client.zaif.taru.zaifclient.adapters

import android.client.zaif.taru.zaifclient.R
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import java.util.ArrayList

/**
 * Created by mizukami on 2018/01/22.
 */
abstract class AbstractRecyclerViewFooterAdapter<T>(recyclerView: RecyclerView, dataSet: MutableList<T>, onLoadMoreListener: EndlessScrollListener? ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VISIBLE_THRESHOLD = 7

    private val ITEM_VIEW_TYPE_BASIC = 0
    private val ITEM_VIEW_TYPE_FOOTER = 1

    protected val LIST_TYPE_ITEM = 1
    protected val LIST_TYPE_FOOTER = 9
    protected val LIST_TYPE_HEADER = 10

    private val dataSet: MutableList<T>?

    private var firstVisibleItem: Int = 0
    private var visibleItemCount:Int = 0
    private var totalItemCount:Int = 0
    private var previousTotal = 0

    private var loading = true
    private var mFinish = true

    init {
        this.dataSet = dataSet;

        if (recyclerView.getLayoutManager() is LinearLayoutManager) {
            val linearLayoutManager: LinearLayoutManager  = recyclerView.getLayoutManager() as LinearLayoutManager;
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    totalItemCount = linearLayoutManager.itemCount
                    visibleItemCount = linearLayoutManager.childCount
                    firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false
                            previousTotal = totalItemCount
                        }
                    }
                    if (!loading && mFinish && totalItemCount - visibleItemCount <= firstVisibleItem + VISIBLE_THRESHOLD) {
                        // End has been reached

                        // TODO: フッターは一旦コメントアウト
                        //addItem(null);
                        onLoadMoreListener?.onLoadMore()
                        loading = true
                        mFinish = false
                    }
                }
            })
        }
    }

    fun getFirstVisibleItem(): Int {
        return firstVisibleItem
    }

    fun resetItems(newDataSet: MutableList<T>) {
        loading = true
        mFinish = true
        firstVisibleItem = 0
        visibleItemCount = 0
        totalItemCount = 0
        previousTotal = 0

        dataSet?.clear()
        addItems(newDataSet)
    }

    fun resetItems() {
        loading = true
        mFinish = true
        firstVisibleItem = 0
        visibleItemCount = 0
        totalItemCount = 0
        previousTotal = 0

        dataSet?.clear()
    }

    fun addItems(newDataSetItems: List<T>) {
        dataSet?.addAll(newDataSetItems)
        notifyDataSetChanged()
    }

    fun addItem(item: T) {
        if (!dataSet?.contains(item)!!) {
            dataSet.add(item)
            notifyItemInserted(dataSet.size - 1)
        }
    }

    fun insertItem(item: T, index: Int) {
        dataSet?.add(index, item)
        notifyItemInserted(index)
    }

    fun removeItem(item: T) {
        val indexOfItem = dataSet?.indexOf(item)
        if (indexOfItem != null && indexOfItem != -1) {
            this.dataSet?.removeAt(indexOfItem)
            notifyItemRemoved(indexOfItem)
        }
    }

    fun getItem(index: Int): T? {
        return if (dataSet != null && dataSet[index] != null) {
            dataSet[index]
        } else if (dataSet != null && dataSet[index] == null) {
            null
        } else {
            throw IllegalArgumentException("Item with index $index doesn't exist, dataSet is $dataSet")
        }
    }

    fun getDataSet(): MutableList<T>? {
        return dataSet
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataSet!!.get(position) != null) ITEM_VIEW_TYPE_BASIC else ITEM_VIEW_TYPE_FOOTER
    }

    override fun getItemCount(): Int {
        if (dataSet != null) {
            return dataSet.size
        }else {
            return 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        return if (viewType == ITEM_VIEW_TYPE_BASIC) {
            //return onCreateBasicItemViewHolder(parent, viewType);
            null
        } else if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            onCreateFooterViewHolder(parent, viewType)
        } else {
            throw IllegalStateException("Invalid type, this type ot items $viewType can't be handled")
        }
    }

    override fun onBindViewHolder(genericHolder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM_VIEW_TYPE_BASIC) {
            onBindBasicItemView(genericHolder, position)
        } else {
            onBindFooterView(genericHolder, position)
        }
    }

    abstract fun onBindBasicItemView(genericHolder: RecyclerView.ViewHolder, position: Int)

    fun onCreateFooterViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_progress_bar, parent, false)
        return ProgressViewHolder(v)
    }

    fun onBindFooterView(genericHolder: RecyclerView.ViewHolder, position: Int) {
        (genericHolder as ProgressViewHolder).progressBar!!.isIndeterminate = true
    }

    class ProgressViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var progressBar: ProgressBar? = null

        init {
        }
    }

    fun finish() {
        mFinish = true
    }

}