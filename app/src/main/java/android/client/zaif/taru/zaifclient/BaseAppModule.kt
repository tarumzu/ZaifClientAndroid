package android.client.zaif.taru.zaifclient

import android.app.Application
import android.client.zaif.taru.zaifclient.network.ServiceGenerator
import android.client.zaif.taru.zaifclient.network.ZaifClientService
import android.content.Context
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by mizukami on 2018/01/13.
 */
@Module
class BaseAppModule(private val application: Application) {

    @Provides
    fun provideAppContext(): Context {
        return application;
    }

    @Provides
    @Singleton
    open fun provideZaifClientService(): ZaifClientService {
        return ServiceGenerator.createRetrofit(provideAppContext()).create(ZaifClientService::class.java)
    }
}