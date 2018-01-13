package android.client.zaif.taru.zaifclient

import android.app.Application

/**
 * Created by mizukami on 2018/01/13.
 */
class ZaifClientApplication: Application() {
    companion object {
        lateinit var sComponent: BaseAppComponent
        lateinit var sApp: ZaifClientApplication
    }

    override fun onCreate() {
        super.onCreate()
        sApp = this

        sComponent = DaggerBaseAppComponent.builder()
                .baseAppModule(BaseAppModule(this))
                .build()
    }
}

