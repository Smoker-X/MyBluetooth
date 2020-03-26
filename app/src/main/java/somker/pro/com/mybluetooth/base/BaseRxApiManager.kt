package somker.pro.com.mybluetooth.base

import android.util.ArrayMap
import io.reactivex.disposables.Disposable


/**
 * Created by Smoker on 2019/7/22.
 * 说明：API 请求管理工具类 单例模式
 */

class BaseRxApiManager private constructor() {

    companion object {
        val instance = RxApiManagerHolder.apiManager
    }


    private object RxApiManagerHolder {
        val apiManager = BaseRxApiManager()
    }


    /**
     * 保存请求的Disposable
     */
    private var requestTag = ArrayMap<Any, Disposable>()

    init {
        requestTag.clear()
    }


    /**
     * 添加一个请求记录
     */
    fun add(tag: Any, sub: Disposable) {
        requestTag[tag] = sub
    }


    /**
     * 移除一个请求记录
     */
    fun remove(tag: Any) {
        if (!requestTag.isEmpty()) {
            requestTag.remove(tag)
        }
    }

    /**
     * 移除所有请求记录
     */
    fun removeAll() {
        if (!requestTag.isEmpty()) {
            requestTag.clear()
        }
    }


    /**
     * 取消一个请求
     */
    fun cancel(tag: Any) {
        if (requestTag.isEmpty() || requestTag[tag] == null) {
            return
        }

        if (!requestTag[tag]!!.isDisposed) {
            requestTag[tag]!!.dispose()
            requestTag.remove(tag)
        }
    }

    /**
     * 取消全部请求
     */
    fun cancelAll() {
        if (requestTag.isEmpty()) {
            return
        }

        val keys = requestTag.keys
        for (apiKey in keys) {
            cancel(apiKey)
        }
    }

}
