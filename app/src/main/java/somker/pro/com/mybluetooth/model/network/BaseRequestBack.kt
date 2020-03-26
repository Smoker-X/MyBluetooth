package somker.pro.com.mybluetooth.model.network

import somker.pro.com.mybluetooth.entity.ResultBean

/**
 * Created by Smoker on 2018/11/9.
 * 说明：RxJava + Retrofit2 封装一层请求回调
 */
interface BaseRequestBack<T> {
    /**
     * @description 请求之前的操作
     */
    fun requestBefore()

    /**
     * @param throwable 异常类型
     * @description 请求异常
     */
    fun requestError(throwable: Throwable)

    /**
     * @description 请求完成
     */
    fun requestComplete()

    /**
     * @param result 根据业务返回相应的数据
     * @description 请求成功
     */
    fun requestSuccess(result: ResultBean<T>)

}
