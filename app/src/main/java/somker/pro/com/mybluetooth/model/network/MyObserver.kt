package somker.pro.com.mybluetooth.model.network

import somker.pro.com.mybluetooth.base.BaseRxApiManager
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import somker.pro.com.mybluetooth.entity.ResultBean

/**
 * Created by Smoker on 2018/11/9.
 * 说明：自定义观察者
 */
class MyObserver<T>(private val tag: Any, private val requestBack: BaseRequestBack<T>) : Observer<ResultBean<T>> {

    override fun onSubscribe(d: Disposable) {
        requestBack.requestBefore()
        BaseRxApiManager.instance.add(tag ,d)
    }

    override fun onNext(t: ResultBean<T>) {
        requestBack.requestSuccess(t)
    }

    override fun onError(e: Throwable) {
        requestBack.requestError(e)
    }

    override fun onComplete() {
        requestBack.requestComplete()
    }
}
