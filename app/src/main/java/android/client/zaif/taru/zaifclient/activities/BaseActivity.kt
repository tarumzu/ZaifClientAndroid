package android.client.zaif.taru.zaifclient.activities

import android.client.zaif.taru.zaifclient.BaseAppComponent
import android.client.zaif.taru.zaifclient.R
import android.client.zaif.taru.zaifclient.ZaifClientApplication
import android.client.zaif.taru.zaifclient.network.ZaifClientService
import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_detail.*
import okhttp3.*
import javax.inject.Inject

open class BaseActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    @Inject lateinit protected var mZaifClientService: ZaifClientService
    @Inject lateinit protected var mZaifClientWSOkHttpClient: OkHttpClient


    protected fun inject(component: BaseAppComponent) {
        component.inject(this);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: 時間あればFragmentでやるようにする
        inject(ZaifClientApplication.getAppComponent())
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
}
