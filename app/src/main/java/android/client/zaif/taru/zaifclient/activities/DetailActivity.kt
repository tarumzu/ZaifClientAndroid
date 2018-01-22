package android.client.zaif.taru.zaifclient.activities

import android.client.zaif.taru.zaifclient.BaseAppComponent
import android.client.zaif.taru.zaifclient.R
import android.client.zaif.taru.zaifclient.ZaifClientApplication
import android.client.zaif.taru.zaifclient.models.CurrencyPairStream
import android.client.zaif.taru.zaifclient.utils.Constants

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail.view.*
import okhttp3.*
import okio.ByteString
import timber.log.Timber
import javax.inject.Inject


class DetailActivity : BaseActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var mCurrencyPair: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        val intent = intent
        val tag = intent.getStringExtra(Constants.ARG_TAG)
        mCurrencyPair = intent.getStringExtra("currencyPair")

        if (mCurrencyPair == null) this.finish()
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1, mCurrencyPair)
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {
        protected var mGson = Gson()
        protected var mWebSocket: WebSocket? = null
        private var mCurrencyPair: String? = null
        @Inject lateinit protected var mZaifClientWSOkHttpClient: OkHttpClient

        protected fun inject(component: BaseAppComponent) {
            component.inject(this);
        }

        private inner class EchoWebSocketListener : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                Timber.d("open")
            }

            override fun onMessage(webSocket: WebSocket?, text: String?) {
                Timber.d("Receiving : " + text!!)
                var currencyPairStream = mGson.fromJson(text, CurrencyPairStream::class.java)

                activity.runOnUiThread({
                    price.text = if (currencyPairStream.lastPrice != null) {
                        currencyPairStream.lastPrice!!.price.toString()
                    } else {
                        currencyPairStream.price.toString()
                    }
                })
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Timber.d("Receiving bytes : " + bytes.hex())
            }

            override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
                webSocket!!.close(1000, null)
                Timber.d("Closing : $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
                Timber.d("Error : " + t?.message)
            }

        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {

            inject(ZaifClientApplication.getAppComponent())

            val rootView = inflater.inflate(R.layout.fragment_detail, container, false)
            // TODO: 仮で前ページ固定とする
            mCurrencyPair = arguments.getString(Constants.ARG_CURRENCY_PAIR)
            rootView.title.text = getString(R.string.title_format, mCurrencyPair)

            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int, currencyPair: String?): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(Constants.ARG_SECTION_NUMBER, sectionNumber)
                args.putString(Constants.ARG_CURRENCY_PAIR, currencyPair)
                fragment.arguments = args
                return fragment
            }
        }

        override fun onStart() {
            super.onStart()

            val request: Request = Request.Builder().url(getString(R.string.api_ws_base_url, mCurrencyPair)).build()
            mWebSocket = mZaifClientWSOkHttpClient.newWebSocket(request, EchoWebSocketListener())

            // TODO: 不要な気がする。調べる
            //mZaifClientWSOkHttpClient.dispatcher().executorService().shutdown()
        }

        override fun onStop() {
            super.onStop()
            mWebSocket?.close(1000, null)
        }
        override fun onDestroy() {
            super.onDestroy()
            mWebSocket?.close(1000, null)
        }
    }

}
