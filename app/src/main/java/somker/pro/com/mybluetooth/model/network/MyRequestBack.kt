package somker.pro.com.mybluetooth.model.network


/**
 * Created by Smoker on 2019/7/22.
 * 说明：统一请求回调
 */
abstract class MyRequestBack<T> : BaseRequestBack<T> {
    /**
     * @description 请求之前的操作
     */
    override fun requestBefore() {
    }

    /**
     * @param throwable 异常类型
     * @description 请求异常
     */
    override fun requestError(throwable: Throwable) {
    }

    /**
     * @description 请求完成
     */
    override fun requestComplete() {
    }

}