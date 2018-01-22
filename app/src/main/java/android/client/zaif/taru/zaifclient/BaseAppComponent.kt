package android.client.zaif.taru.zaifclient

import android.client.zaif.taru.zaifclient.activities.BaseActivity
import android.client.zaif.taru.zaifclient.activities.DetailActivity
import android.support.v4.app.Fragment
import dagger.Component
import javax.inject.Singleton

/**
 * Created by mizukami on 2018/01/13.
 */
@Singleton
@Component(modules = arrayOf(BaseAppModule::class))
interface BaseAppComponent {
    fun inject(baseActivity: BaseActivity)

    fun inject(fragment: DetailActivity.PlaceholderFragment)
}