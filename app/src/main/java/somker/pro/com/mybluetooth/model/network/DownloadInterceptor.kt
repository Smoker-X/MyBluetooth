package somker.pro.com.mybluetooth.model.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Smoker on 2019/11/25.
 * 说明：下载的拦截器
 *
 * https://blog.csdn.net/shusheng0007/article/details/82428733
 */
class DownloadInterceptor<T>(private var listener: MyUploadDownloadBack<T>) :Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        return originalResponse.newBuilder()
            .body(originalResponse.body()?.let { DownloadResponseBody(it, listener) })
            .build()
    }
}