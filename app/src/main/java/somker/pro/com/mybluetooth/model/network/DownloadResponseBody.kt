package somker.pro.com.mybluetooth.model.network

import com.blankj.utilcode.util.LogUtils

import java.io.IOException

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Okio
import okio.Source
import somker.pro.com.mybluetooth.model.network.MyUploadDownloadBack

/**
 * Created by Smoker on 2018/11/26.
 * 说明：下载的请求体
 */
class DownloadResponseBody(
    private val responseBody: ResponseBody,
    private val downloadListener: MyUploadDownloadBack<*>?
) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            private var totalBytesRead = 0L

            private var totalLength = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                if (totalLength == 0L) {
                    totalLength = responseBody.contentLength()
                }

                if (null != downloadListener) {
                    totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                    LogUtils.iTag("DownloadUtil","已经下载的： $totalBytesRead  共有： $totalLength")
                    Observable.just(totalBytesRead).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<Long> {
                            override fun onNext(aLong: Long) {
                                downloadListener.onProgressChange(aLong, totalLength)
                            }
                            override fun onSubscribe(d: Disposable) {

                            }

                            override fun onError(e: Throwable) {}

                            override fun onComplete() {}
                        })

                }
                return bytesRead
            }
        }
    }

}