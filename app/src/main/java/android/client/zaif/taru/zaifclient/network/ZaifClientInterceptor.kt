package android.client.zaif.taru.zaifclient.network

import android.content.Context
import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by mizukami on 2018/01/14.
 */
class ZaifClientInterceptor(context: Context, credentialsStr: String?, consumerToken: String?) : Interceptor {
    private val mContext: Context
    private val mCredentialsStr: String?
    private val mTconsumerToken: String?
    lateinit private var mAccessToken: String

    init {
        mContext = context
        mCredentialsStr = credentialsStr
        mTconsumerToken = consumerToken
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val  builder: Request.Builder = request.newBuilder();

        builder.addHeader("Accept", "application/json");
        if (!TextUtils.isEmpty(mTconsumerToken)) builder.addHeader("X-CONSUMER-TOKEN", mTconsumerToken);
        // NOTE: dev, stgのみbasic対応
        //if (Utils.isStaging() || Utils.isDevelop()) {
        //    if (!TextUtils.isEmpty(mCredentialsStr)) builder.addHeader("Authorization", mCredentialsStr);
        //}
        //mAccessToken = SharedPref.getAccessToken(mContext);
        //if (!TextUtils.isEmpty(mAccessToken)) builder.addHeader("X-Access-Token", mAccessToken);

        var response: Response = chain.proceed(builder.build());

        return response;
    }
}