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
class API private constructor() {

    /**
     * 项目主地址
     */
    private lateinit var baseUrl: String
    /**
     * api接口文档
     */

    private var apiServer: APIServer


    init {
        resetBaseUrl()
        apiServer = initAPIServer()
    }

    companion object {
        val instance = APIHolder.mApi
    }

    private object APIHolder {
        val mApi = API()
    }

    /**
     * 获取API文档
     */
    fun getAPIServer(): APIServer {
        return apiServer
    }


    /**
     * 重置请求地址
     */
    private fun resetBaseUrl() {
        baseUrl = IPConstant.getInstance().getRequestPath()
    }


    /**
     * 当服务器地址更改了，要刷新服务器地址，重新实例化Retrofit
     */
    fun refresh() {
        baseUrl = IPConstant.getInstance().getRequestPath()
        apiServer = initAPIServer()
    }


    /**
     * 初始化Api服务
     */
    private fun initAPIServer(): APIServer {
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
        mClient.connectTimeout(30 ,TimeUnit.SECONDS)
        mClient.readTimeout(30 ,TimeUnit.SECONDS)


        val mFit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(mClient.build())
            .baseUrl(baseUrl)
            .build()

        return mFit.create(APIServer::class.java)

    }

}