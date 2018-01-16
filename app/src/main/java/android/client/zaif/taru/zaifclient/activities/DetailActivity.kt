package android.client.zaif.taru.zaifclient.activities

import android.client.zaif.taru.zaifclient.BaseAppComponent
import android.client.zaif.taru.zaifclient.R
import android.client.zaif.taru.zaifclient.ZaifClientApplication
import android.client.zaif.taru.zaifclient.models.CurrencyPairStream
import android.client.zaif.taru.zaifclient.network.ZaifClientService
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_detail.view.*
import okhttp3.*
import javax.inject.Inject
import okio.ByteString
import timber.log.Timber


class DetailActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    @Inject lateinit protected var mZaifClientService: ZaifClientService
    @Inject lateinit protected var mZaifClientWSOkHttpClient: OkHttpClient
    protected var mWebSocket: WebSocket? = null
    private var mGson: Gson = Gson()


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        inject(ZaifClientApplication.getAppComponent())

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1)
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

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_detail, container, false)
            rootView.section_label.text = getString(R.string.section_format, arguments.getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val request: Request = Request.Builder().url(getString(R.string.api_ws_base_url) + "btc_jpy").build()
        mWebSocket = mZaifClientWSOkHttpClient.newWebSocket(request, EchoWebSocketListener())
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
