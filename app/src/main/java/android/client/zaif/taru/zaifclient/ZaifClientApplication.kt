package android.client.zaif.taru.zaifclient

import android.app.Application
import timber.log.Timber

/**
 * Created by mizukami on 2018/01/13.
 */
class ZaifClientApplication: Application() {
    companion object {
        lateinit var sComponent: BaseAppComponent
        lateinit var sApp: ZaifClientApplication

        open fun getAppComponent(): BaseAppComponent {
            return sComponent
        }
    }

    override fun onCreate() {
        super.onCreate()
        sApp = this

        sComponent = DaggerBaseAppComponent.builder()
                .baseAppModule(BaseAppModule(this))
                .build()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

