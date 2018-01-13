package android.client.zaif.taru.zaifclient

import android.client.zaif.taru.zaifclient.activities.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by mizukami on 2018/01/13.
 */
@Singleton
@Component(modules = arrayOf(BaseAppModule::class))
interface BaseAppComponent {
    fun inject(mainActivity: MainActivity)
}