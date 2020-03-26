package somker.pro.com.mybluetooth.model.network

import com.blankj.utilcode.util.LogUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import somker.pro.com.mybluetooth.BuildConfig
import somker.pro.com.mybluetooth.constant.IPConstant
import java.util.concurrent.TimeUnit

/**
 * Created by Smoker on 2019/7/22.
 * 说明：创建Retrofit(内部类单例模式)
 */
class APIDownload private constructor() {

    /**
     * 项目主地址
     */
    private lateinit var baseUrl: String


    companion object {
        val instance = APIDownloadHolder.mApi
    }

    private object APIDownloadHolder {
        val mApi = APIDownload()
    }

    /**
     * 获取API文档
     */
    fun <T> getAPIServer(back : MyUploadDownloadBack<T>): APIDownloadServer {
        resetBaseUrl()
        return initAPIServer(back)
    }


    /**
     * 重置请求地址
     */
    private fun resetBaseUrl() {
        baseUrl = IPConstant.getInstance().getRequestPath()
    }


    /**
     * 初始化Api服务
     */
    private fun <T>initAPIServer(back : MyUploadDownloadBack<T>): APIDownloadServer {
        val mLogg = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            if (message.startsWith("{") || message.startsWith("[")) {
                LogUtils.json(message)
            } else {
                LogUtils.i(message)
            }
        })

        if (BuildConfig.DEBUG) {
            mLogg.level = HttpLoggingInterceptor.Level.BODY
        } else {
            mLogg.level = HttpLoggingInterceptor.Level.NONE
        }

        val mClient = OkHttpClient.Builder()
        mClient.addInterceptor(mLogg)
        mClient.addInterceptor(DownloadInterceptor(back))
        mClient.connectTimeout(30 ,TimeUnit.SECONDS)
        mClient.readTimeout(30 ,TimeUnit.SECONDS)


        val mFit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(mClient.build())
            .baseUrl(baseUrl)
            .build()

        return mFit.create(APIDownloadServer::class.java)

    }

}