package android.client.zaif.taru.zaifclient.network

import android.client.zaif.taru.zaifclient.R
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by mizukami on 2018/01/14.
 */
class ServiceGenerator {

    companion object {
        open fun createRetrofit(context: Context): Retrofit {
            var credentialsStr: String? = null
            //if ((Utils.isStaging() || Utils.isDevelop())){
            //    credentialsStr = Credentials.basic(context.getString(R.string.basic_user), context.getString(R.string.basic_pass))
            //}

            //String consumerToken = context.getString(R.string.consumer_token)

            val baseUrl: String = context.getString(R.string.api_base_url)

            val gson: Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

            val builder: Retrofit.Builder = Retrofit.Builder()
                                                .baseUrl(baseUrl)
                                                .addConverterFactory(GsonConverterFactory.create(gson))

            var retrofit: Retrofit? = null;
            val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

            // log
            val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            if (!httpClient.interceptors().contains(logging)) {
                httpClient.addInterceptor(logging)
            }

            // staging環境でタイム・アウトしてしまうため伸ばす。
            //if (Utils.isStaging() || Utils.isDevelop()) {
            //    httpClient.connectTimeout(100, TimeUnit.SECONDS);
            //    httpClient.readTimeout(100, TimeUnit.SECONDS);
            //}else{
                httpClient.connectTimeout(20, TimeUnit.SECONDS)
                httpClient.readTimeout(20, TimeUnit.SECONDS)
            //}
            builder.client(httpClient.build());

            //val zaifClientInterceptor: ZaifClientInterceptor = ZaifClientInterceptor(context.getApplicationContext(), credentialsStr, consumerToken)
            val zaifClientInterceptor: ZaifClientInterceptor = ZaifClientInterceptor(context.getApplicationContext(), credentialsStr, null)
            if (!httpClient.interceptors().contains(zaifClientInterceptor)) {
                httpClient.addInterceptor(zaifClientInterceptor);
                builder.client(httpClient.build());
            }

            retrofit = builder.build();

            return retrofit;

        }
    }
}